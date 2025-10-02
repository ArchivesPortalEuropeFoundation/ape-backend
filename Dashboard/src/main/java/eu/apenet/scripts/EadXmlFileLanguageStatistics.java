package eu.apenet.scripts;

import com.google.gson.JsonObject;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.scripts.utils.Statistics;
import eu.archivesportaleurope.commons.config.ScriptsConfig;
import org.apache.log4j.Logger;
import org.apache.solr.common.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class EadXmlFileLanguageStatistics {

    private final Logger log = Logger.getLogger(getClass());

    private Set<String> validPaths = new HashSet<>();
    private Map<String, String> allAIs = new HashMap<>();

    public static void main (String[] args) throws IOException, XMLStreamException {
        System.out.println("Hello from CLI world!");

        EadXmlFileLanguageStatistics eadXmlFileStatistics = new EadXmlFileLanguageStatistics();
        eadXmlFileStatistics.readValidFilePaths();
        eadXmlFileStatistics.readArchInsts();
        eadXmlFileStatistics.doTheJob();

    }

    private String removeNewLine(String s){

        return s.replaceAll("\\R", " ");
    }

    private void readValidFilePaths(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/valid_paths.txt"));
            String line = reader.readLine();

            while (line != null) {
                // read next line
                if (line.trim().length()>0) {
//                    System.out.println("Adding:SSS" + line.trim()+"SSS --> " + line.trim().length());
                    validPaths.add(line.trim());
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private void readArchInsts(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/arch_inst.txt"));
            String line = reader.readLine();

            while (line != null) {
                // read next line
                if (line.trim().length()>0) {
//                    System.out.println("Adding:SSS" + line.trim()+"SSS --> " + line.trim().length());
                    String[] parts = line.split(";");
                    if (parts.length==2) {
                        String name = parts[1];
                        if (name.startsWith("\"") && name.endsWith("\"")){
                            name = name.substring(1, name.length()-1);
                        }
                        allAIs.put(parts[0], name);
                    }
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private void doTheJob() throws IOException, XMLStreamException {
        System.out.println("time start: " + (new Date()));
        String repoPath = ((ScriptsConfig) APEnetUtilities.getConfig()).getRepoDirPath();

        String dqstatsPath = ((ScriptsConfig) APEnetUtilities.getConfig()).getScriptProperties().getProperty("DATA_QUALITY_DIR_PATH") + "/" + System.currentTimeMillis();
        System.out.println("dqstatsPath: " + dqstatsPath);
        Statistics totalStatistics = new Statistics();
        totalStatistics.type = "total";
        totalStatistics.allAIs = allAIs;

        FileWriter fileWriter = new FileWriter("found.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        FileWriter fileWriter2 = new FileWriter("notfound.txt");
        PrintWriter printWriter2 = new PrintWriter(fileWriter2);
        FileWriter fileWriter4 = new FileWriter("c_id.txt");
        PrintWriter printWriter4 = new PrintWriter(fileWriter4);

        File repoFile = new File(repoPath);
        for (File countryDir : repoFile.listFiles()){
            if (countryDir.isDirectory() && countryDir.getName().length()==2){
                System.out.println("Country: " + countryDir.getName());

                Statistics countryStatistics = new Statistics();
                countryStatistics.type = "country";
                countryStatistics.allAIs = allAIs;

                for (File institutionDir : countryDir.listFiles()){
                    if (institutionDir.isDirectory()){
                        System.out.println("\tInstitution: " + institutionDir.getName());

                        Statistics institutionStatistics = new Statistics();
                        institutionStatistics.type = "institution";
                        institutionStatistics.allAIs = allAIs;

                        for (File typeDir : institutionDir.listFiles()){
                            if (typeDir.isDirectory() && (typeDir.getName().equals("FA") || typeDir.getName().equals("SG") || typeDir.getName().equals("HG"))){
                                for (File xmlFile : typeDir.listFiles()){
                                    System.out.println("\t\t\tfile: " + xmlFile.getName());
                                    if (isValidFile(xmlFile)/* || true*//*xmlFile.getName().endsWith(".xml")*/){
                                        printWriter.println(xmlFile.getAbsolutePath().replace("/ape/data/repo", ""));

                                        totalStatistics.totalFiles++;

                                        countryStatistics.totalFiles++;

                                        institutionStatistics.totalFiles++;

                                        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
                                        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlFile.getAbsoluteFile()));

                                        int langusageOK = 0;
                                        int langmaterialOK = 0;
                                        boolean inProfileDescLangUsage = false;
                                        boolean inLangMaterial = false;
                                        boolean inLanguage = false;
                                        StartElement currentStartElement = null;
                                        String currentLangText = null;

                                        while (reader.hasNext()) {
                                            XMLEvent nextEvent = null;
                                            try {
                                                nextEvent = reader.nextEvent();
                                            } catch (XMLStreamException e) {
                                                e.printStackTrace();
                                            }
                                            if (nextEvent.isStartElement()) {
                                                StartElement startElement = nextEvent.asStartElement();
                                                currentStartElement = startElement;

                                                if (startElement.getName().getLocalPart().equals("profiledesclangusage")) {
                                                    inProfileDescLangUsage = true;
                                                }
                                                else if (startElement.getName().getLocalPart().equals("langmaterial")) {
                                                    inLangMaterial = true;
                                                }
                                                else if (startElement.getName().getLocalPart().equals("language")) {
                                                    inLanguage = true;
                                                }
                                                else if (startElement.getName().getLocalPart().equals("c")) {
                                                    totalStatistics.cCounter++;
                                                    countryStatistics.cCounter++;
                                                    institutionStatistics.cCounter++;
                                                }
                                            }
                                            else if (nextEvent.isEndElement()) {

                                                EndElement endElement = nextEvent.asEndElement();

                                                if (endElement.getName().getLocalPart().equals("profiledesclangusage")) {
                                                    if (inProfileDescLangUsage) {
                                                        inProfileDescLangUsage = false;
                                                    }
                                                } else if (endElement.getName().getLocalPart().equals("langmaterial")) {
                                                    if (inLangMaterial) {
                                                        inLangMaterial = false;
                                                    }
                                                } else if (endElement.getName().getLocalPart().equals("language")) {
                                                    if (inLanguage) {
                                                        inLanguage = false;

                                                        Attribute attribute = currentStartElement.getAttributeByName(new QName("langcode"));
                                                        String currentLangCode = null;
                                                        if (attribute != null){
                                                            currentLangCode = attribute.getValue();
                                                        }

                                                        boolean isValidLanguage = !StringUtils.isEmpty(currentLangText) || !StringUtils.isEmpty(currentLangCode);
                                                        String concatLang = (currentLangCode!=null?currentLangCode:"null") +"_"+(currentLangText!=null?currentLangText:"null");

                                                        if (isValidLanguage){
                                                            if (inProfileDescLangUsage) {
                                                                langusageOK++;
                                                            }
                                                            else if (inLangMaterial){
                                                                langmaterialOK++;
                                                            }
                                                        }

                                                        if (langusageOK==2){
                                                            institutionStatistics.langusageMultipleInt++;
                                                            countryStatistics.langusageMultipleInt++;
                                                            totalStatistics.langusageMultipleInt++;
                                                        }

                                                        if (langmaterialOK==2){
                                                            institutionStatistics.langmaterialMultipleInt++;
                                                            countryStatistics.langmaterialMultipleInt++;
                                                            totalStatistics.langmaterialMultipleInt++;
                                                        }

                                                        if (!StringUtils.isEmpty(currentLangText)){
                                                            if (inProfileDescLangUsage) {
                                                                institutionStatistics.langusageInt++;
                                                                countryStatistics.langusageInt++;
                                                                totalStatistics.langusageInt++;
                                                            }
                                                            else if (inLangMaterial){
                                                                institutionStatistics.langmaterialInt++;
                                                                countryStatistics.langmaterialInt++;
                                                                totalStatistics.langmaterialInt++;
                                                            }
                                                        }

                                                        if (!StringUtils.isEmpty(currentLangCode)){
                                                            if (inProfileDescLangUsage) {
                                                                institutionStatistics.langusageCodeInt++;
                                                                countryStatistics.langusageCodeInt++;
                                                                totalStatistics.langusageCodeInt++;
                                                            }
                                                            else if (inLangMaterial){
                                                                institutionStatistics.langmaterialCodeInt++;
                                                                countryStatistics.langmaterialCodeInt++;
                                                                totalStatistics.langmaterialCodeInt++;
                                                            }
                                                        }
                                                        if (isValidLanguage){
                                                            institutionStatistics.langAndLangCodes.add(concatLang);
                                                            countryStatistics.langAndLangCodes.add(concatLang);
                                                            totalStatistics.langAndLangCodes.add(concatLang);

                                                            increase(concatLang, institutionStatistics.langAndLangCodesMap);
                                                            increase(concatLang, countryStatistics.langAndLangCodesMap);
                                                            increase(concatLang, totalStatistics.langAndLangCodesMap);
                                                        }
                                                        if (!StringUtils.isEmpty(currentLangCode)){
                                                            institutionStatistics.langcodes.add(currentLangCode);
                                                            countryStatistics.langcodes.add(currentLangCode);
                                                            totalStatistics.langcodes.add(currentLangCode);

                                                            increase(currentLangCode, institutionStatistics.langcodesMap);
                                                            increase(currentLangCode, countryStatistics.langcodesMap);
                                                            increase(currentLangCode, totalStatistics.langcodesMap);
                                                        }
                                                        if (!StringUtils.isEmpty(currentLangText)){
                                                            institutionStatistics.languages.add(currentLangText);
                                                            countryStatistics.languages.add(currentLangText);
                                                            totalStatistics.languages.add(currentLangText);

                                                            increase(currentLangText, institutionStatistics.languagesMap);
                                                            increase(currentLangText, countryStatistics.languagesMap);
                                                            increase(currentLangText, totalStatistics.languagesMap);
                                                        }
                                                    }
                                                }
                                            }
                                            else if (nextEvent.isCharacters()) {

                                                if (inLanguage) {
                                                    currentLangText = nextEvent.asCharacters().getData();
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        totalStatistics.totalNotFoundFiles++;
                                        countryStatistics.totalNotFoundFiles++;
                                        institutionStatistics.totalNotFoundFiles++;
                                        printWriter2.println(xmlFile.getAbsolutePath().replace("/ape/data/repo", ""));
                                    }
                                }
                            }
                        }
                        //End of institution
                        if (institutionStatistics.totalFiles > 0) {
//                            institutionStatistics.writeCSV(dqstatsPath+"/" + countryDir.getName() + "/" + institutionDir.getName());

//                            JsonObject jsonObject = institutionStatistics.writeJson(dqstatsPath + "/" + countryDir.getName() + "/" + institutionDir.getName());
                            institutionStatistics.writeLanguageExcel(dqstatsPath + "/" + countryDir.getName() + "/" + institutionDir.getName(), "institution", countryDir.getName(), institutionDir.getName());

                        }
                        countryStatistics.perInsitutionInfoStatistics.put(institutionDir.getName(), institutionStatistics);
                    }
                }
                //End of country
                System.out.println("\tFiles: " + (int)countryStatistics.totalFiles);
//                Map<String, Double> temp = new HashMap<>();
//                temp.put("no_of_files", new Double(countryStatistics.totalFiles));
//                temp.put("size_of_files", countryStatistics.totalSize);
//                totalStatistics.perCountryInfo.put(countryDir.getName(),temp);

                totalStatistics.perCountryInfoStatistics.put(countryDir.getName(), countryStatistics);

                if (countryStatistics.totalFiles > 0) {
//                    countryStatistics.writeCSV(dqstatsPath+"/" + countryDir.getName());

//                    JsonObject jsonObject = countryStatistics.writeJson(dqstatsPath + "/" + countryDir.getName());
                    countryStatistics.writeLanguageExcel(dqstatsPath + "/" + countryDir.getName(), "country", countryDir.getName(), null);

                }
            }
        }

//        JsonObject jsonObject = totalStatistics.writeJson(dqstatsPath + "/");
        totalStatistics.writeLanguageExcel(dqstatsPath + "/", "total", null, null);


        printWriter.close();
        printWriter2.close();

        System.out.println("time standard: " + (new Date()));
    }

    private boolean isValidFile(File xmlFile){
        String fullPath = xmlFile.getAbsolutePath();
        fullPath = fullPath.replace("/ape/data/repo", "");

        boolean temp = validPaths.contains(fullPath);
        return temp;
    }

    public static void printAttributesInfo(Node root)
    {
        NamedNodeMap attributes = root.getAttributes();
        if (attributes != null)
        {
            for (int i = 0; i < attributes.getLength(); i++)
            {
                Node node = attributes.item(i);
                if (node.getNodeType() == Node.ATTRIBUTE_NODE)
                {
                    String name = node.getNodeName();
                    if (name.equals("xsi:schemaLocation")) {
                        //System.out.println("\t\t"+node.getNodeValue());
                    }
                }
            }
        }
    }

    private void increase(String myKey, Map<String, Integer> unitIdInfoMap){
        if (!unitIdInfoMap.containsKey(myKey)){
            unitIdInfoMap.put(myKey, 1);
        }
        else {
            unitIdInfoMap.put(myKey, unitIdInfoMap.get(myKey)+1);
        }
    }
}
