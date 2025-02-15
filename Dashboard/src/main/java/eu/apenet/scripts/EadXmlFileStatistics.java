package eu.apenet.scripts;

import com.google.gson.JsonObject;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.scripts.utils.Statistics;
import eu.archivesportaleurope.commons.config.ScriptsConfig;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class EadXmlFileStatistics {

    private final Logger log = Logger.getLogger(getClass());

    private Set<String> validPaths = new HashSet<>();
    private Map<String, String> allAIs = new HashMap<>();

    public static void main (String[] args) throws IOException {
        System.out.println("Hello from CLI world!");

        EadXmlFileStatistics eadXmlFileStatistics = new EadXmlFileStatistics();
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

    private void doTheJob() throws IOException {
        System.out.println("time start: " + (new Date()));
        String repoPath = ((ScriptsConfig) APEnetUtilities.getConfig()).getRepoDirPath();

        String dqstatsPath = ((ScriptsConfig) APEnetUtilities.getConfig()).getScriptProperties().getProperty("DATA_QUALITY_DIR_PATH") + "/" + System.currentTimeMillis();


//        int totalFileCounter = 0;
//        int totalNotFoundFiles = 0;
//        double maxFileSize = 0;
//        String maxFileCountry = "";
//        String maxFileName = "";
//        double minFileSize = 100000000;
//        String minFileCountry = "";
//        String minFileName = "";
//        Map<String, Map<String, Double>> perCountryInfo = new HashMap<>();
//
//        Map<String, Integer> archdescDidElementsMap = new HashMap<>();
//        Map<String, Integer> archdescElementsMap = new HashMap<>();
//        Map<String, Integer> cDidElementsMap = new HashMap<>();
//        Map<String, Integer> cElementsMap = new HashMap<>();
//
//        Map<String, Integer> cLeavesDidElementsMap = new HashMap<>();
//        Map<String, Integer> cLeavesElementsMap = new HashMap<>();
//
//        Map<String, Integer> otherUnitIdTypes = new HashMap<>();
//        Map<String, Integer> unitIdInfoMap = new HashMap<>();
//        Map<String, Integer> otherUnitIdTypesForLeaves = new HashMap<>();
//        Map<String, Integer> unitIdInfoMapForLeaves = new HashMap<>();
//
//        Map<String, Integer> schemaLocations = new HashMap<>();
//        Map<String, List<String>> schemaLocationsMap = new HashMap<>();
//        int cCounter = 0;
//        int cCounter2 = 0;
//        int cCounterLeaves = 0;

        Statistics totalStatistics = new Statistics();
        totalStatistics.type = "total";
        totalStatistics.allAIs = allAIs;

        FileWriter fileWriter = new FileWriter("found.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        FileWriter fileWriter2 = new FileWriter("notfound.txt");
        PrintWriter printWriter2 = new PrintWriter(fileWriter2);
        FileWriter fileWriter4 = new FileWriter("c_id.txt");
        PrintWriter printWriter4 = new PrintWriter(fileWriter4);

        Map<String, Integer> materialSpecMap = new HashMap<>();
        Map<String, Integer> physLocMap = new HashMap<>();
        boolean doMaterialAndPhysLoc = false; //Literal stats for two fields
        boolean doOther = true; //The original stats

        File repoFile = new File(repoPath);
        for (File countryDir : repoFile.listFiles()){
            if (countryDir.isDirectory() && countryDir.getName().length()==2){
                System.out.println("Country: " + countryDir.getName());

//                if (!countryDir.getName().equals("AL")) continue;

//                int totalCountryFiles=0;
//                double totalCountrySize=0;
//                double maxCountrySize = 0;
//                double minCountrySize = 100000000;

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
//                            System.out.println("\t\ttype: " + typeDir.getName());
                            if (typeDir.isDirectory() && (typeDir.getName().equals("FA") || typeDir.getName().equals("SG") || typeDir.getName().equals("HG"))){
                                for (File xmlFile : typeDir.listFiles()){
                                    System.out.println("\t\t\tfile: " + xmlFile.getName());
                                    if (isValidFile(xmlFile)/* || true*//*xmlFile.getName().endsWith(".xml")*/){
//                                        System.out.println("\t\tXML file ("+typeDir.getName()+"): " + xmlFile.getName());


                                        printWriter.println(xmlFile.getAbsolutePath().replace("/ape/data/repo", ""));

                                        long fileSize = xmlFile.length();

                                        totalStatistics.totalFiles++;
                                        totalStatistics.totalSize += fileSize;

                                        countryStatistics.totalFiles++;
                                        countryStatistics.totalSize += fileSize;

                                        institutionStatistics.totalFiles++;
                                        institutionStatistics.totalSize += fileSize;

                                        if (fileSize > totalStatistics.maxSize){
                                            totalStatistics.maxSize = fileSize;
                                            totalStatistics.maxFileCountry = countryDir.getName();
                                            totalStatistics.maxFileInstitution = institutionDir.getName();
                                            totalStatistics.maxFileName = xmlFile.getAbsolutePath();
                                        }

                                        if (fileSize < totalStatistics.minSize){
                                            totalStatistics.minSize = fileSize;
                                            totalStatistics.minFileCountry = countryDir.getName();
                                            totalStatistics.minFileInsitution = institutionDir.getName();
                                            totalStatistics.minFileName = xmlFile.getAbsolutePath();
                                        }

                                        if (fileSize > countryStatistics.maxSize){
                                            countryStatistics.maxSize = fileSize;
                                            countryStatistics.maxFileCountry = countryDir.getName();
                                            countryStatistics.maxFileInstitution = institutionDir.getName();
                                            countryStatistics.maxFileName = xmlFile.getAbsolutePath();
                                        }

                                        if (fileSize < countryStatistics.minSize){
                                            countryStatistics.minSize = fileSize;
                                            countryStatistics.minFileCountry = countryDir.getName();
                                            countryStatistics.minFileInsitution = institutionDir.getName();
                                            countryStatistics.minFileName = xmlFile.getAbsolutePath();
                                        }

                                        if (fileSize > institutionStatistics.maxSize){
                                            institutionStatistics.maxSize = fileSize;
                                            institutionStatistics.maxFileCountry = countryDir.getName();
                                            institutionStatistics.maxFileInstitution = institutionDir.getName();
                                            institutionStatistics.maxFileName = xmlFile.getAbsolutePath();
                                        }

                                        if (fileSize < institutionStatistics.minSize){
                                            institutionStatistics.minSize = fileSize;
                                            institutionStatistics.minFileCountry = countryDir.getName();
                                            institutionStatistics.minFileInsitution = institutionDir.getName();
                                            institutionStatistics.minFileName = xmlFile.getAbsolutePath();
                                        }

                                        Stack<CInfo> stack = new Stack<>();
                                        CInfo currentCInfo = null;
                                        List<CInfo> allCInfos = new ArrayList<>();
                                        List<CInfo> allCLeavesInfos = new ArrayList<>();

                                        try {
                                            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
                                            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlFile.getAbsoluteFile()));

                                            boolean inArchDesc = false;
                                            boolean inArchDescDid = false;
                                            boolean inCDid = false;

                                            boolean inPhysLoc = false;
                                            boolean inMaterialSpec = false;

                                            boolean inUnitId = false;

                                            int currentDepth = 0;

//                                            System.out.println("File: " + xmlFile.getAbsoluteFile());
                                            Set<String> archDescElementSet = new HashSet<>();
                                            Set<String> archDescDidElementSet = new HashSet<>();

                                            while (reader.hasNext()) {
                                                XMLEvent nextEvent = reader.nextEvent();
                                                if (nextEvent.isStartElement()) {
                                                    currentDepth++;

                                                    StartElement startElement = nextEvent.asStartElement();
//                                                    System.out.println("startElement: " + startElement.getName().getLocalPart() + " --> depth: " + currentDepth);

                                                    if (doMaterialAndPhysLoc) {
                                                        if (startElement.getName().getLocalPart().equals("materialspec")) {
                                                            inMaterialSpec = true;
                                                        } else if (startElement.getName().getLocalPart().equals("physloc")) {
                                                            inPhysLoc = true;
                                                        }
                                                    }

                                                    if (doOther) {
                                                        if (startElement.getName().getLocalPart().equals("ead")) {
                                                            Iterator iterator = startElement.getAttributes();
                                                            Attribute attribute2 = startElement.getAttributeByName(new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation"));
                                                            if (attribute2!=null) {
                                                                String tmp = attribute2.getValue();
                                                                tmp = getCorrectSchemaLocation(tmp);
                                                                if (!totalStatistics.schemaLocations.containsKey(tmp)) {
                                                                    totalStatistics.schemaLocations.put(tmp,1);
//                                                                totalStatistics.schemaLocationsMap.put(tmp, new ArrayList<>());
//                                                                totalStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                else {
                                                                    totalStatistics.schemaLocations.put(tmp, totalStatistics.schemaLocations.get(tmp)+1);
//                                                                totalStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                if (!countryStatistics.schemaLocations.containsKey(tmp)) {
                                                                    countryStatistics.schemaLocations.put(tmp,1);
//                                                                countryStatistics.schemaLocationsMap.put(tmp, new ArrayList<>());
//                                                                countryStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                else {
                                                                    countryStatistics.schemaLocations.put(tmp, countryStatistics.schemaLocations.get(tmp)+1);
//                                                                countryStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                if (!institutionStatistics.schemaLocations.containsKey(tmp)) {
                                                                    institutionStatistics.schemaLocations.put(tmp,1);
//                                                                institutionStatistics.schemaLocationsMap.put(tmp, new ArrayList<>());
//                                                                institutionStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                else {
                                                                    institutionStatistics.schemaLocations.put(tmp, institutionStatistics.schemaLocations.get(tmp)+1);
//                                                                institutionStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                            }
                                                            else {
                                                                System.out.println("Error: " + xmlFile.getAbsolutePath());

                                                                String tmp = "no_schema_location";

                                                                if (!totalStatistics.schemaLocations.containsKey(tmp)) {
                                                                    totalStatistics.schemaLocations.put(tmp,1);
//                                                                totalStatistics.schemaLocationsMap.put(tmp, new ArrayList<>());
//                                                                totalStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                else {
                                                                    totalStatistics.schemaLocations.put(tmp, totalStatistics.schemaLocations.get(tmp)+1);
//                                                                totalStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                if (!countryStatistics.schemaLocations.containsKey(tmp)) {
                                                                    countryStatistics.schemaLocations.put(tmp,1);
//                                                                countryStatistics.schemaLocationsMap.put(tmp, new ArrayList<>());
//                                                                countryStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                else {
                                                                    countryStatistics.schemaLocations.put(tmp, countryStatistics.schemaLocations.get(tmp)+1);
//                                                                countryStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                if (!institutionStatistics.schemaLocations.containsKey(tmp)) {
                                                                    institutionStatistics.schemaLocations.put(tmp,1);
//                                                                institutionStatistics.schemaLocationsMap.put(tmp, new ArrayList<>());
//                                                                institutionStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                                else {
                                                                    institutionStatistics.schemaLocations.put(tmp, institutionStatistics.schemaLocations.get(tmp)+1);
//                                                                institutionStatistics.schemaLocationsMap.get(tmp).add(xmlFile.getAbsolutePath());
                                                                }
                                                            }

                                                            if (!institutionStatistics.schemaLocations.containsKey("no_schema_location")){
                                                                institutionStatistics.schemaLocations.put("no_schema_location",0);
                                                            }
                                                            if (!institutionStatistics.schemaLocations.containsKey("other_schema_location")){
                                                                institutionStatistics.schemaLocations.put("other_schema_location",0);
                                                            }

                                                            if (xmlFile.getName().equals("EDM_conversionTest_Umlaut.xml")){
                                                                System.out.println();
                                                            }

//                                                        break;
                                                        }
                                                        else if (startElement.getName().getLocalPart().equals("archdesc")) {
                                                            inArchDesc = true;
                                                        }
                                                        else if (startElement.getName().getLocalPart().equals("did")) {
                                                            if (inArchDesc && currentDepth==3){
                                                                inArchDescDid = true;
                                                                archDescElementSet.add(startElement.getName().getLocalPart());
                                                            }
                                                            else if (currentCInfo != null){
                                                                inCDid = true;
                                                                currentCInfo.handleCSubElement(startElement);
                                                            }
                                                        }
                                                        else if (startElement.getName().getLocalPart().equals("c")) {
                                                            totalStatistics.cCounter++;
                                                            countryStatistics.cCounter++;
                                                            institutionStatistics.cCounter++;

                                                            if (!stack.empty()){
                                                                CInfo tempInfo = stack.peek();
                                                                tempInfo.hasChildCs = true;
                                                            }

                                                            CInfo cInfo = new CInfo();
                                                            cInfo.handleStartElement(startElement);
                                                            cInfo.depth = currentDepth;
                                                            currentCInfo = stack.push(cInfo);
                                                            if (cInfo.id != null){
                                                                printWriter4.println(cInfo.id);
                                                            }
                                                        }
                                                        else if (startElement.getName().getLocalPart().equals("materialspec")) {
                                                            inMaterialSpec = true;
                                                        }
                                                        else if (startElement.getName().getLocalPart().equals("physloc")) {
                                                            inPhysLoc = true;
                                                        }
                                                        else {
                                                            if (inArchDescDid){
                                                                if (currentDepth==4){
                                                                    archDescDidElementSet.add(startElement.getName().getLocalPart());
                                                                }
                                                            }
                                                            if (inArchDesc){
                                                                if (currentDepth==3){
                                                                    archDescElementSet.add(startElement.getName().getLocalPart());
                                                                }
                                                            }
                                                            if (startElement.getName().getLocalPart().equals("unitid")) {
                                                                inUnitId = true;
                                                                if (currentCInfo != null){
                                                                    currentCInfo.handleUnitidElement(startElement);
                                                                }
                                                            }
                                                            if (startElement.getName().getLocalPart().equals("extptr")) {
                                                                if (inUnitId) {
                                                                    if (currentCInfo != null) {
                                                                        currentCInfo.handleExtptrElement(startElement);
                                                                    }
                                                                }

                                                            }
                                                            if (inCDid){
                                                                if (currentCInfo.depth+2 == currentDepth){
                                                                    currentCInfo.handleCDidSubElement(startElement);
                                                                }
                                                            }
                                                            if (currentCInfo!=null){
                                                                if (currentCInfo.depth+1 == currentDepth){
                                                                    currentCInfo.handleCSubElement(startElement);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                else if (nextEvent.isEndElement()) {
                                                    currentDepth--;

                                                    EndElement endElement = nextEvent.asEndElement();

                                                    if (doMaterialAndPhysLoc) {
                                                        if (endElement.getName().getLocalPart().equals("materialspec")) {
                                                            if (inMaterialSpec) {
                                                                inMaterialSpec = false;
                                                            }
                                                        } else if (endElement.getName().getLocalPart().equals("physloc")) {
                                                            if (inPhysLoc) {
                                                                inPhysLoc = false;
                                                            }
                                                        }
                                                    }

                                                    if (doOther) {
                                                        if (endElement.getName().getLocalPart().equals("archdesc")) {
                                                            if (inArchDesc){
                                                                inArchDesc = false;
                                                            }
                                                        }
                                                        else if (endElement.getName().getLocalPart().equals("did")) {
                                                            if (inArchDescDid && currentDepth==2){
                                                                inArchDescDid = false;
                                                                //break;
                                                            }
                                                            else if (inCDid) {
                                                                inCDid = false;
                                                            }
                                                        }
                                                        else if (endElement.getName().getLocalPart().equals("c")) {
                                                            currentCInfo = stack.pop();
                                                            allCInfos.add(currentCInfo);
                                                            if (!currentCInfo.hasChildCs){
                                                                allCLeavesInfos.add(currentCInfo);
                                                            }
                                                        }
                                                        else if (endElement.getName().getLocalPart().equals("materialspec")) {
                                                            if (inMaterialSpec){
                                                                inMaterialSpec = false;
                                                            }
                                                        }
                                                        else if (endElement.getName().getLocalPart().equals("physloc")) {
                                                            if (inPhysLoc){
                                                                inPhysLoc = false;
                                                            }
                                                        }
                                                        else if (endElement.getName().getLocalPart().equals("unitid")) {
                                                            inUnitId = false;
                                                        }
                                                        else {

                                                        }
                                                    }
                                                }
                                                else if (nextEvent.isCharacters()) {
                                                    if (doMaterialAndPhysLoc) {
                                                        if (inMaterialSpec) {
                                                            if (doMaterialAndPhysLoc) {
                                                                String materialSpec = nextEvent.asCharacters().getData();
                                                                //System.out.println("material: " + materialSpec);
                                                                if (!materialSpecMap.containsKey(materialSpec)) {
                                                                    materialSpecMap.put(materialSpec, 0);
                                                                }
                                                                materialSpecMap.put(materialSpec, materialSpecMap.get(materialSpec) + 1);
                                                            }
                                                        } else if (inPhysLoc) {
                                                            if (doMaterialAndPhysLoc) {
                                                                String physLoc = nextEvent.asCharacters().getData();
                                                                //System.out.println("physloc: " + physLoc);
                                                                if (!physLocMap.containsKey(physLoc)) {
                                                                    physLocMap.put(physLoc, 0);
                                                                }
                                                                physLocMap.put(physLoc, physLocMap.get(physLoc) + 1);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            for (String s : archDescElementSet){
                                                s = getCorrectArchDescElement(s);
                                                if (!totalStatistics.archdescElementsMap.containsKey(s)){
                                                    totalStatistics.archdescElementsMap.put(s, 1);
                                                }
                                                else {
                                                    totalStatistics.archdescElementsMap.put(s, totalStatistics.archdescElementsMap.get(s)+1);
                                                }
                                                if (!countryStatistics.archdescElementsMap.containsKey(s)){
                                                    countryStatistics.archdescElementsMap.put(s, 1);
                                                }
                                                else {
                                                    countryStatistics.archdescElementsMap.put(s, countryStatistics.archdescElementsMap.get(s)+1);
                                                }
                                                if (!institutionStatistics.archdescElementsMap.containsKey(s)){
                                                    institutionStatistics.archdescElementsMap.put(s, 1);
                                                }
                                                else {
                                                    institutionStatistics.archdescElementsMap.put(s, institutionStatistics.archdescElementsMap.get(s)+1);
                                                }
                                            }
                                            if (!institutionStatistics.archdescElementsMap.containsKey("other_archdesc_element")){
                                                institutionStatistics.archdescElementsMap.put("other_archdesc_element",0);
                                            }

                                            for (String s : archDescDidElementSet){
                                                s = getCorrectArchDescDidElement(s);
                                                if (!totalStatistics.archdescDidElementsMap.containsKey(s)){
                                                    totalStatistics.archdescDidElementsMap.put(s, 1);
                                                }
                                                else {
                                                    totalStatistics.archdescDidElementsMap.put(s, totalStatistics.archdescDidElementsMap.get(s)+1);
                                                }
                                                if (!countryStatistics.archdescDidElementsMap.containsKey(s)){
                                                    countryStatistics.archdescDidElementsMap.put(s, 1);
                                                }
                                                else {
                                                    countryStatistics.archdescDidElementsMap.put(s, countryStatistics.archdescDidElementsMap.get(s)+1);
                                                }
                                                if (!institutionStatistics.archdescDidElementsMap.containsKey(s)){
                                                    institutionStatistics.archdescDidElementsMap.put(s, 1);
                                                }
                                                else {
                                                    institutionStatistics.archdescDidElementsMap.put(s, institutionStatistics.archdescDidElementsMap.get(s)+1);
                                                }
                                            }
                                            if (!institutionStatistics.archdescDidElementsMap.containsKey("other_archdesc_did_element")){
                                                institutionStatistics.archdescDidElementsMap.put("other_archdesc_did_element",0);
                                            }

                                            totalStatistics.cCounter2 += allCInfos.size();
                                            totalStatistics.cCounterLeaves += allCLeavesInfos.size();
                                            countryStatistics.cCounter2 += allCInfos.size();
                                            countryStatistics.cCounterLeaves += allCLeavesInfos.size();
                                            institutionStatistics.cCounter2 += allCInfos.size();
                                            institutionStatistics.cCounterLeaves += allCLeavesInfos.size();

                                            for (CInfo cInfo : allCInfos){
                                                for (String s : cInfo.otheUnitIdTypes){
                                                    s = getCorrectOtherUnitIdType(s);

                                                    if (!totalStatistics.otherUnitIdTypes.containsKey(s)){
                                                        totalStatistics.otherUnitIdTypes.put(s, 1);
                                                    }
                                                    else {
                                                        totalStatistics.otherUnitIdTypes.put(s, totalStatistics.otherUnitIdTypes.get(s)+1);
                                                    }
                                                    if (!countryStatistics.otherUnitIdTypes.containsKey(s)){
                                                        countryStatistics.otherUnitIdTypes.put(s, 1);
                                                    }
                                                    else {
                                                        countryStatistics.otherUnitIdTypes.put(s, countryStatistics.otherUnitIdTypes.get(s)+1);
                                                    }
                                                    if (!institutionStatistics.otherUnitIdTypes.containsKey(s)){
                                                        institutionStatistics.otherUnitIdTypes.put(s, 1);
                                                    }
                                                    else {
                                                        institutionStatistics.otherUnitIdTypes.put(s, institutionStatistics.otherUnitIdTypes.get(s)+1);
                                                    }
                                                }
                                                if (!institutionStatistics.otherUnitIdTypes.containsKey("other_unitid_type")){
                                                    institutionStatistics.otherUnitIdTypes.put("other_unitid_type",0);
                                                }

                                                for (String s : cInfo.cDidSubElements){
                                                    s = getCorrectCDidElement(s);

                                                    if (!totalStatistics.cDidElementsMap.containsKey(s)){
                                                        totalStatistics.cDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        totalStatistics.cDidElementsMap.put(s, totalStatistics.cDidElementsMap.get(s)+1);
                                                    }
                                                    if (!countryStatistics.cDidElementsMap.containsKey(s)){
                                                        countryStatistics.cDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        countryStatistics.cDidElementsMap.put(s, countryStatistics.cDidElementsMap.get(s)+1);
                                                    }
                                                    if (!institutionStatistics.cDidElementsMap.containsKey(s)){
                                                        institutionStatistics.cDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        institutionStatistics.cDidElementsMap.put(s, institutionStatistics.cDidElementsMap.get(s)+1);
                                                    }
                                                }
                                                if (!institutionStatistics.cDidElementsMap.containsKey("other_c_did_element")){
                                                    institutionStatistics.cDidElementsMap.put("other_c_did_element",0);
                                                }

                                                for (String s : cInfo.cSubElements){
                                                    s = getCorrectCElement(s);

                                                    if (!totalStatistics.cElementsMap.containsKey(s)){
                                                        totalStatistics.cElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        totalStatistics.cElementsMap.put(s, totalStatistics.cElementsMap.get(s)+1);
                                                    }
                                                    if (!countryStatistics.cElementsMap.containsKey(s)){
                                                        countryStatistics.cElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        countryStatistics.cElementsMap.put(s, countryStatistics.cElementsMap.get(s)+1);
                                                    }
                                                    if (!institutionStatistics.cElementsMap.containsKey(s)){
                                                        institutionStatistics.cElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        institutionStatistics.cElementsMap.put(s, institutionStatistics.cElementsMap.get(s)+1);
                                                    }
                                                }
                                                if (!institutionStatistics.cElementsMap.containsKey("other_c_element")){
                                                    institutionStatistics.cElementsMap.put("other_c_element",0);
                                                }

                                                if (cInfo.noOfUnitId>0){
                                                    increase("metric01", totalStatistics.unitIdInfoMap);

                                                    if (cInfo.id != null){
                                                        increase("metric02", totalStatistics.unitIdInfoMap);
                                                    }

                                                    increase("metric01", countryStatistics.unitIdInfoMap);

                                                    if (cInfo.id != null){
                                                        increase("metric02", countryStatistics.unitIdInfoMap);
                                                    }

                                                    increase("metric01", institutionStatistics.unitIdInfoMap);

                                                    if (cInfo.id != null){
                                                        increase("metric02", institutionStatistics.unitIdInfoMap);
                                                    }

                                                    if (cInfo.isExtptrPID ){
                                                        increase("metric21", totalStatistics.unitIdInfoMap);
                                                        increase("metric21", countryStatistics.unitIdInfoMap);
                                                        increase("metric21", institutionStatistics.unitIdInfoMap);
                                                    }
                                                }
                                                else {
                                                    increase("metric03", totalStatistics.unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric04", totalStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric03", countryStatistics.unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric04", countryStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric03", institutionStatistics.unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric04", institutionStatistics.unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                    increase("metric05", totalStatistics.unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric06", totalStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric05", countryStatistics.unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric06", countryStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric05", institutionStatistics.unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric06", institutionStatistics.unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>1){
                                                    increase("metric07", totalStatistics.unitIdInfoMap);
                                                    increase("metric07", countryStatistics.unitIdInfoMap);
                                                    increase("metric07", institutionStatistics.unitIdInfoMap);
                                                }
                                                if (cInfo.noOfUnitIdsWithOtherType>0){
                                                    increase("metric08", totalStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric09", totalStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric10", totalStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric08", countryStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric09", countryStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric10", countryStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric08", institutionStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric09", institutionStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric10", institutionStatistics.unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithNoType>0){
                                                    increase("metric11", totalStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric12", totalStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric13", totalStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric11", countryStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric12", countryStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric13", countryStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric11", institutionStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric12", institutionStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric13", institutionStatistics.unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.id != null){
                                                    increase("metric14", totalStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric15", totalStatistics.unitIdInfoMap);
                                                    }
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric16", totalStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric14", countryStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric15", countryStatistics.unitIdInfoMap);
                                                    }
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric16", countryStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric14", institutionStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric15", institutionStatistics.unitIdInfoMap);
                                                    }
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric16", institutionStatistics.unitIdInfoMap);
                                                    }

                                                    if (cInfo.idIsPID) {
                                                        increase("metric20", totalStatistics.unitIdInfoMap);
                                                        increase("metric20", countryStatistics.unitIdInfoMap);
                                                        increase("metric20", institutionStatistics.unitIdInfoMap);
                                                    }
                                                }
                                                else {
                                                    increase("metric17", totalStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric18", totalStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric19", totalStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric17", countryStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric18", countryStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric19", countryStatistics.unitIdInfoMap);
                                                    }
                                                    increase("metric17", institutionStatistics.unitIdInfoMap);
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric18", institutionStatistics.unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric19", institutionStatistics.unitIdInfoMap);
                                                    }
                                                }



                                                String myKey = "with_id";
                                                if (cInfo.id == null){
                                                    myKey = "with_no_id";
                                                }
                                                increase(myKey, totalStatistics.unitIdInfoMap);
                                                increase(myKey, countryStatistics.unitIdInfoMap);
                                                increase(myKey, institutionStatistics.unitIdInfoMap);

                                                myKey = "with_no_unitid";
                                                if (cInfo.noOfUnitId > 0){
                                                    myKey = "with_unitid";
                                                }
                                                if (!totalStatistics.unitIdInfoMap.containsKey(myKey)){
                                                    totalStatistics.unitIdInfoMap.put(myKey, 1);
                                                }
                                                else {
                                                    totalStatistics.unitIdInfoMap.put(myKey, totalStatistics.unitIdInfoMap.get(myKey)+1);
                                                }
                                                if (!countryStatistics.unitIdInfoMap.containsKey(myKey)){
                                                    countryStatistics.unitIdInfoMap.put(myKey, 1);
                                                }
                                                else {
                                                    countryStatistics.unitIdInfoMap.put(myKey, countryStatistics.unitIdInfoMap.get(myKey)+1);
                                                }
                                                if (!institutionStatistics.unitIdInfoMap.containsKey(myKey)){
                                                    institutionStatistics.unitIdInfoMap.put(myKey, 1);
                                                }
                                                else {
                                                    institutionStatistics.unitIdInfoMap.put(myKey, institutionStatistics.unitIdInfoMap.get(myKey)+1);
                                                }

                                                if (cInfo.noOfUnitId > 0) {
                                                    myKey = "with_no_type_call_number";
                                                    if (cInfo.noOfUnitIdsWithCallNumber > 0) {
                                                        myKey = "with_at_least_one_type_call_number";
                                                    }
                                                    if (!totalStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMap.put(myKey, totalStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMap.put(myKey, countryStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, institutionStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber > 1) {
                                                    myKey = "with_more_than_one_call_number";
                                                    if (!totalStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMap.put(myKey, totalStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMap.put(myKey, countryStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, institutionStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithNoType > 0) {
                                                    myKey = "with_no_type";
                                                    if (!totalStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMap.put(myKey, totalStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMap.put(myKey, countryStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, institutionStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                    myKey = "with_other_type";
                                                    if (!totalStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMap.put(myKey, totalStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMap.put(myKey, countryStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMap.put(myKey, institutionStatistics.unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber==0) {
                                                    if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                        myKey = "with_no_call_number_but_other_type";
                                                        if (!totalStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                            totalStatistics.unitIdInfoMap.put(myKey, 1);
                                                        } else {
                                                            totalStatistics.unitIdInfoMap.put(myKey, totalStatistics.unitIdInfoMap.get(myKey) + 1);
                                                        }
                                                        if (!countryStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                            countryStatistics.unitIdInfoMap.put(myKey, 1);
                                                        } else {
                                                            countryStatistics.unitIdInfoMap.put(myKey, countryStatistics.unitIdInfoMap.get(myKey) + 1);
                                                        }
                                                        if (!institutionStatistics.unitIdInfoMap.containsKey(myKey)) {
                                                            institutionStatistics.unitIdInfoMap.put(myKey, 1);
                                                        } else {
                                                            institutionStatistics.unitIdInfoMap.put(myKey, institutionStatistics.unitIdInfoMap.get(myKey) + 1);
                                                        }
                                                    }
                                                }
                                            }

                                            //Leaves
                                            for (CInfo cInfo : allCLeavesInfos){
                                                for (String s : cInfo.otheUnitIdTypes){
                                                    if (!totalStatistics.otherUnitIdTypesForLeaves.containsKey(s)){
                                                        totalStatistics.otherUnitIdTypesForLeaves.put(s, 1);
                                                    }
                                                    else {
                                                        totalStatistics.otherUnitIdTypesForLeaves.put(s, totalStatistics.otherUnitIdTypesForLeaves.get(s)+1);
                                                    }
                                                    if (!countryStatistics.otherUnitIdTypesForLeaves.containsKey(s)){
                                                        countryStatistics.otherUnitIdTypesForLeaves.put(s, 1);
                                                    }
                                                    else {
                                                        countryStatistics.otherUnitIdTypesForLeaves.put(s, countryStatistics.otherUnitIdTypesForLeaves.get(s)+1);
                                                    }
                                                    if (!institutionStatistics.otherUnitIdTypesForLeaves.containsKey(s)){
                                                        institutionStatistics.otherUnitIdTypesForLeaves.put(s, 1);
                                                    }
                                                    else {
                                                        institutionStatistics.otherUnitIdTypesForLeaves.put(s, institutionStatistics.otherUnitIdTypesForLeaves.get(s)+1);
                                                    }
                                                }

                                                for (String s : cInfo.cDidSubElements){
                                                    if (!totalStatistics.cLeavesDidElementsMap.containsKey(s)){
                                                        totalStatistics.cLeavesDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        totalStatistics.cLeavesDidElementsMap.put(s, totalStatistics.cLeavesDidElementsMap.get(s)+1);
                                                    }
                                                    if (!countryStatistics.cLeavesDidElementsMap.containsKey(s)){
                                                        countryStatistics.cLeavesDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        countryStatistics.cLeavesDidElementsMap.put(s, countryStatistics.cLeavesDidElementsMap.get(s)+1);
                                                    }
                                                    if (!institutionStatistics.cLeavesDidElementsMap.containsKey(s)){
                                                        institutionStatistics.cLeavesDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        institutionStatistics.cLeavesDidElementsMap.put(s, institutionStatistics.cLeavesDidElementsMap.get(s)+1);
                                                    }
                                                }

                                                for (String s : cInfo.cSubElements){
                                                    if (!totalStatistics.cLeavesElementsMap.containsKey(s)){
                                                        totalStatistics.cLeavesElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        totalStatistics.cLeavesElementsMap.put(s, totalStatistics.cLeavesElementsMap.get(s)+1);
                                                    }
                                                    if (!countryStatistics.cLeavesElementsMap.containsKey(s)){
                                                        countryStatistics.cLeavesElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        countryStatistics.cLeavesElementsMap.put(s, countryStatistics.cLeavesElementsMap.get(s)+1);
                                                    }
                                                    if (!institutionStatistics.cLeavesElementsMap.containsKey(s)){
                                                        institutionStatistics.cLeavesElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        institutionStatistics.cLeavesElementsMap.put(s, institutionStatistics.cLeavesElementsMap.get(s)+1);
                                                    }
                                                }

//                                                if (cInfo.idIsPID) {
//                                                    increase("metric20", totalStatistics.unitIdInfoMapForLeaves);
//                                                    increase("metric20", countryStatistics.unitIdInfoMapForLeaves);
//                                                    increase("metric20", institutionStatistics.unitIdInfoMapForLeaves);
//                                                }

                                                if (cInfo.noOfUnitId>0){
                                                    increase("metric01", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric01", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric01", institutionStatistics.unitIdInfoMapForLeaves);

                                                    if (cInfo.isExtptrPID ){
                                                        increase("metric21", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric21", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric21", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }

                                                    if (cInfo.id != null){
                                                        increase("metric02", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric02", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric02", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                else {
                                                    increase("metric03", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric03", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric03", institutionStatistics.unitIdInfoMapForLeaves);
                                                    if (cInfo.id != null){
                                                        increase("metric04", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric04", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric04", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                    increase("metric05", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric05", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric05", institutionStatistics.unitIdInfoMapForLeaves);
                                                    if (cInfo.id != null){
                                                        increase("metric06", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric06", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric06", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>1){
                                                    increase("metric07", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric07", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric07", institutionStatistics.unitIdInfoMapForLeaves);
                                                }
                                                if (cInfo.noOfUnitIdsWithOtherType>0){
                                                    increase("metric08", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric08", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric08", institutionStatistics.unitIdInfoMapForLeaves);

                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric09", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric09", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric09", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                    else {
                                                        increase("metric10", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric10", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric10", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithNoType>0){
                                                    increase("metric11", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric11", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric11", institutionStatistics.unitIdInfoMapForLeaves);

                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric12", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric12", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric12", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                    else {
                                                        increase("metric13", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric13", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric13", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.id != null){
                                                    increase("metric14", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric14", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric14", institutionStatistics.unitIdInfoMapForLeaves);

                                                    if (cInfo.idIsPID) {
                                                        increase("metric20", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric20", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric20", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }

                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric15", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric15", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric15", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric16", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric16", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric16", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                else {
                                                    increase("metric17", totalStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric17", countryStatistics.unitIdInfoMapForLeaves);
                                                    increase("metric17", institutionStatistics.unitIdInfoMapForLeaves);
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric18", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric18", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric18", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                    else {
                                                        increase("metric19", totalStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric19", countryStatistics.unitIdInfoMapForLeaves);
                                                        increase("metric19", institutionStatistics.unitIdInfoMapForLeaves);
                                                    }
                                                }



                                                String myKey = "with_id";
                                                if (cInfo.id == null){
                                                    myKey = "with_no_id";
                                                }
                                                increase(myKey, totalStatistics.unitIdInfoMapForLeaves);
                                                increase(myKey, countryStatistics.unitIdInfoMapForLeaves);
                                                increase(myKey, institutionStatistics.unitIdInfoMapForLeaves);

                                                myKey = "with_no_unitid";
                                                if (cInfo.noOfUnitId > 0){
                                                    myKey = "with_unitid";
                                                }
                                                if (!totalStatistics.unitIdInfoMapForLeaves.containsKey(myKey)){
                                                    totalStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                }
                                                else {
                                                    totalStatistics.unitIdInfoMapForLeaves.put(myKey, totalStatistics.unitIdInfoMapForLeaves.get(myKey)+1);
                                                }
                                                if (!countryStatistics.unitIdInfoMapForLeaves.containsKey(myKey)){
                                                    countryStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                }
                                                else {
                                                    countryStatistics.unitIdInfoMapForLeaves.put(myKey, countryStatistics.unitIdInfoMapForLeaves.get(myKey)+1);
                                                }
                                                if (!institutionStatistics.unitIdInfoMapForLeaves.containsKey(myKey)){
                                                    institutionStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                }
                                                else {
                                                    institutionStatistics.unitIdInfoMapForLeaves.put(myKey, institutionStatistics.unitIdInfoMapForLeaves.get(myKey)+1);
                                                }

                                                if (cInfo.noOfUnitId > 0) {
                                                    myKey = "with_no_type_call_number";
                                                    if (cInfo.noOfUnitIdsWithCallNumber > 0) {
                                                        myKey = "with_at_least_one_type_call_number";
                                                    }
                                                    if (!totalStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, totalStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, countryStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, institutionStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber > 1) {
                                                    myKey = "with_more_than_one_call_number";
                                                    if (!totalStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, totalStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, countryStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, institutionStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithNoType > 0) {
                                                    myKey = "with_no_type";
                                                    if (!totalStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, totalStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, countryStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, institutionStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                    myKey = "with_other_type";
                                                    if (!totalStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        totalStatistics.unitIdInfoMapForLeaves.put(myKey, totalStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!countryStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        countryStatistics.unitIdInfoMapForLeaves.put(myKey, countryStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                    if (!institutionStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        institutionStatistics.unitIdInfoMapForLeaves.put(myKey, institutionStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber==0) {
                                                    if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                        myKey = "with_no_call_number_but_other_type";
                                                        if (!totalStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                            totalStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                        } else {
                                                            totalStatistics.unitIdInfoMapForLeaves.put(myKey, totalStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                        }
                                                        if (!countryStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                            countryStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                        } else {
                                                            countryStatistics.unitIdInfoMapForLeaves.put(myKey, countryStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                        }
                                                        if (!institutionStatistics.unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                            institutionStatistics.unitIdInfoMapForLeaves.put(myKey, 1);
                                                        } else {
                                                            institutionStatistics.unitIdInfoMapForLeaves.put(myKey, institutionStatistics.unitIdInfoMapForLeaves.get(myKey) + 1);
                                                        }
                                                    }
                                                }
                                            }

                                        } catch (XMLStreamException e) {
                                            //e.printStackTrace();
                                            System.out.println("Error2: " + xmlFile.getAbsolutePath() + " --> ("+e.getMessage()+")");
                                        } catch (FileNotFoundException e) {
                                            //e.printStackTrace();
                                            System.out.println("Error3: " + xmlFile.getAbsolutePath() + " --> ("+e.getMessage()+")");
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
                            if (doOther) {
                                JsonObject jsonObject = institutionStatistics.writeJson(dqstatsPath + "/" + countryDir.getName() + "/" + institutionDir.getName());
                                institutionStatistics.writeExcel(dqstatsPath + "/" + countryDir.getName() + "/" + institutionDir.getName(), jsonObject, "institution", countryDir.getName(), institutionDir.getName());
                            }
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
                    if (doOther) {
                        JsonObject jsonObject = countryStatistics.writeJson(dqstatsPath + "/" + countryDir.getName());
                        countryStatistics.writeExcel(dqstatsPath + "/" + countryDir.getName(), jsonObject, "country", countryDir.getName(), null);
                    }
                }
            }
        }

//        totalStatistics.writeCSV(dqstatsPath+"/");
        if (doOther) {
            JsonObject jsonObject = totalStatistics.writeJson(dqstatsPath + "/");
            totalStatistics.writeExcel(dqstatsPath + "/", jsonObject, "total", null, null);
        }

        System.out.println("totalFileCounter: " + totalStatistics.totalFiles);
        System.out.println("totalNotFoundFiles: " + totalStatistics.totalNotFoundFiles);
        System.out.println("MaxFileSize: " + gettFileSize(totalStatistics.maxSize));
        System.out.println("MaxCountry: " + totalStatistics.maxFileCountry);
        System.out.println("MaxFileName: " + totalStatistics.maxFileName);

        System.out.println("MinFileSize: " + gettFileSize(totalStatistics.minSize));
        System.out.println("MinCountry: " + totalStatistics.minFileCountry);
        System.out.println("MinFileName: " + totalStatistics.maxFileName);

        System.out.println("c counter: " + totalStatistics.cCounter);
        System.out.println("c counter 2: " + totalStatistics.cCounter2);

        System.out.println("c counter (leaves): " + totalStatistics.cCounterLeaves);

        System.out.println("schemaLocations: " + totalStatistics.schemaLocations.size());
        for (String s : totalStatistics.schemaLocations.keySet()){
            System.out.println("\t["+totalStatistics.schemaLocations.get(s)+"] " + s);
        }

//        FileWriter fileWriter3 = new FileWriter("schemalocations.txt");
//        PrintWriter printWriter3 = new PrintWriter(fileWriter3);
//        for (String s : totalStatistics.schemaLocationsMap.keySet()){
//            for (String s1 : totalStatistics.schemaLocationsMap.get(s)) {
//                printWriter3.println(s.replaceAll("[\\t\\n\\r]+", " ") + ";" + s1);
//            }
//        }
//        printWriter3.close();

        System.out.println("arch elements");
        for (String s : totalStatistics.archdescElementsMap.keySet()){
            System.out.println("\t["+totalStatistics.archdescElementsMap.get(s)+"] " + s);
        }

        System.out.println("arch did elements");
        for (String s : totalStatistics.archdescDidElementsMap.keySet()){
            System.out.println("\t["+totalStatistics.archdescDidElementsMap.get(s)+"] " + s);
        }

        System.out.println("other unitid types");
        for (String s : totalStatistics.otherUnitIdTypes.keySet()){
            System.out.println("\t["+totalStatistics.otherUnitIdTypes.get(s)+"] " + s);
        }

        System.out.println("other unitid types (for leaves)");
        for (String s : totalStatistics.otherUnitIdTypesForLeaves.keySet()){
            System.out.println("\t["+totalStatistics.otherUnitIdTypesForLeaves.get(s)+"] " + s);
        }

        System.out.println("unitid info");
        for (String s : totalStatistics.unitIdInfoMap.keySet()){
            System.out.println("\t["+totalStatistics.unitIdInfoMap.get(s)+"] " + s);
        }

        System.out.println("unitid info (leaves");
        for (String s : totalStatistics.unitIdInfoMapForLeaves.keySet()){
            System.out.println("\t["+totalStatistics.unitIdInfoMapForLeaves.get(s)+"] " + s);
        }

        System.out.println("c did elements");
        for (String s : totalStatistics.cDidElementsMap.keySet()){
            System.out.println("\t["+totalStatistics.cDidElementsMap.get(s)+"] " + s);
        }

        System.out.println("c (leaves) did elements");
        for (String s : totalStatistics.cLeavesDidElementsMap.keySet()){
            System.out.println("\t["+totalStatistics.cLeavesDidElementsMap.get(s)+"] " + s);
        }

        System.out.println("c elements");
        for (String s : totalStatistics.cElementsMap.keySet()){
            System.out.println("\t["+totalStatistics.cElementsMap.get(s)+"] " + s);
        }

        System.out.println("c leaves elements");
        for (String s : totalStatistics.cLeavesElementsMap.keySet()){
            System.out.println("\t["+totalStatistics.cLeavesElementsMap.get(s)+"] " + s);
        }

//        System.out.println("Per country info:");
//        System.out.println("------------------------------");
//        for (String country : totalStatistics.perCountryInfo.keySet()){
//            Map<String, Double> temp = totalStatistics.perCountryInfo.get(country);
//            System.out.println(country + ";" + temp.get("no_of_files").intValue() + ";" + gettFileSizeInMB(temp.get("size_of_files")) + ";" + (temp.get("no_of_files")>0?gettFileSizeInMB(temp.get("size_of_files")/temp.get("no_of_files")):0));
//        }

        printWriter.close();
        printWriter2.close();
        printWriter4.close();


        FileWriter fileWriterMaterialSpec = new FileWriter("materialSpecs.txt");
        PrintWriter prinWritertMaterialSpec = new PrintWriter(fileWriterMaterialSpec);
        for (String s : materialSpecMap.keySet()){
            prinWritertMaterialSpec.println(s+"|"+materialSpecMap.get(s));
        }
        fileWriterMaterialSpec.close();
        prinWritertMaterialSpec.close();

        FileWriter fileWriterPhysLoc = new FileWriter("physLocs.txt");
        PrintWriter printWriterPhysLoc = new PrintWriter(fileWriterPhysLoc);
        for (String s : physLocMap.keySet()){
            printWriterPhysLoc.println(s+"|"+physLocMap.get(s));
        }
        fileWriterPhysLoc.close();
        printWriterPhysLoc.close();

        System.out.println("time standart: " + (new Date()));
    }

    private String getCorrectSchemaLocation(String s){
        if (s.equals("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/schemas/ead/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd")
                || s.equals("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd")
                || s.equals("urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd")
                || s.equals("urn:isbn:1-931666-22-9 https://schemas.archivesportaleurope.net/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd")
                || s.equals("urn:isbn:1-931666-22-9 http://schemas.archivesportaleurope.net/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd")
                || s.equals("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/schemas/ead/apeEAD.xsd")
                || s.equals("urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd")
                || s.equals("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd")
                || s.equals("urn:isbn:1-931666-22-9 https://schemas.archivesportaleurope.net/profiles/apeEAD.xsd")
                || s.equals("urn:isbn:1-931666-22-9 http://schemas.archivesportaleurope.net/profiles/apeEAD.xsd")
                || s.equals("urn:isbn:1-931666-22-9 apeEAD.xsd")
        ){
            return s;
        }
        else {
//            System.out.println("Other SCHEMA LOCATION: " + s);
            return "other_schema_location";
        }
    }

    private String getCorrectOtherUnitIdType(String s){
        if (s.equals("former call number")
                || s.equals("file reference")
        ){
            return s;
        }
        else {
//            System.out.println("Other UNITIDTYPE: " + s);
            return "other_unitid_type";
        }
    }

    private String getCorrectCDidElement(String s){
        if (s.equals("unittitle")
                || s.equals("unitdate")
                || s.equals("physdesc")
                || s.equals("dao")
                || s.equals("origination")
                || s.equals("repository")
                || s.equals("langmaterial")
                || s.equals("container")
                || s.equals("physloc")
                || s.equals("materialspec")
                || s.equals("note")
                || s.equals("head")
        ){
            return s;
        }
        else {
//            System.out.println("Other UNITIDTYPE: " + s);
            return "other_c_element";
        }
    }

    private String getCorrectCElement(String s){
        if (s.equals("scopecontent")
                || s.equals("userestrict")
                || s.equals("prefercite")
                || s.equals("accessrestrict")
                || s.equals("controlaccess")
                || s.equals("bioghist")
                || s.equals("custodhist")
                || s.equals("otherfindaid")
                || s.equals("bibliography")
                || s.equals("acqinfo")
                || s.equals("appraisal")
                || s.equals("accruals")
                || s.equals("arrangement")
                || s.equals("processinfo")
                || s.equals("fileplan")
                || s.equals("phystech")
                || s.equals("odd")
                || s.equals("fileplan")
                || s.equals("did")
        ) {
            return s;
        }
        else if (s.equals("altformavail") || s.equals("originalsloc")){
            return "altformavail";
        }
        else if (s.equals("relatedmaterial") || s.equals("separatedmaterial")){
            return "relatedmaterial";
        }
        else {
//            System.out.println("Other UNITIDTYPE: " + s);
            return "other_c_did_element";
        }
    }

    private String getCorrectArchDescDidElement(String s){
        if (s.equals("unittitle")
                || s.equals("unitdate")
                || s.equals("physdesc")
                || s.equals("dao")
                || s.equals("origination")
                || s.equals("repository")
                || s.equals("langmaterial")
                || s.equals("physloc")
                || s.equals("container")
                || s.equals("materialspec")
                || s.equals("note")
                || s.equals("head")
                || s.equals("unitid")
        ){
            return s;
        }
        else {
//            System.out.println("Other UNITIDTYPE: " + s);
            return "other_archdesc_did_element";
        }
    }

    private String getCorrectArchDescElement(String s){
        if (s.equals("scopecontent")
                || s.equals("userestrict")
                || s.equals("prefercite")
                || s.equals("accessrestrict")
                || s.equals("controlaccess")
                || s.equals("bioghist")
                || s.equals("custodhist")
                || s.equals("otherfindaid")
                || s.equals("bibliography")
                || s.equals("acqinfo")
                || s.equals("appraisal")
                || s.equals("accruals")
                || s.equals("arrangement")
                || s.equals("processinfo")
                || s.equals("fileplan")
                || s.equals("phystech")
                || s.equals("odd")
                || s.equals("dsc")
                || s.equals("did")
        ) {
            return s;
        }
        else if (s.equals("altformavail") || s.equals("originalsloc")){
            return "altformavail";
        }
        else if (s.equals("relatedmaterial") || s.equals("separatedmaterial")){
            return "relatedmaterial";
        }
        else {
//            System.out.println("Other UNITIDTYPE: " + s);
            return "other_archdesc_element";
        }
    }

    private boolean isValidFile(File xmlFile){
        String fullPath = xmlFile.getAbsolutePath();
        fullPath = fullPath.replace("/ape/data/repo", "");

//        System.out.println("checking: " + fullPath + " --> " + validPaths.size());
        boolean temp = validPaths.contains(fullPath);
//        System.out.println("check: " + temp);
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

    private static String gettFileSize(Double fileSize){
        if (fileSize  / (1024 * 1024 * 1024) > 1){
            return fileSize  / (1024 * 1024 * 1024)+ " GB";
        }
        else if (fileSize  / (1024 * 1024) > 1){
            return fileSize  / (1024 * 1024)+ " MB";
        }
        else {
            return fileSize  / (1024)+ " KB";
        }
    }

    private static Double gettFileSizeInMB(Double fileSize){
        return fileSize  / (1024 * 1024);
    }

    private static String getFileSizeGigaBytes(File file) {
        return (double) file.length() / (1024 * 1024 * 1024) + " GB";
    }

    private static String getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " MB";
    }

    private static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 + "  KB";
    }

    private static String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }

    private class CInfo {

        public String id = null;
        public boolean idIsPID = false;
        public boolean isExtptrPID = false;
        public int depth;
        public int noOfUnitIdsWithCallNumber = 0;
        public int noOfUnitIdsWithNoType = 0;
        public int noOfUnitIdsWithOtherType = 0;
        public int noOfUnitId = 0;
        public Set<String> otheUnitIdTypes = new HashSet<>();

        public Set<String> cDidSubElements = new HashSet<>();
        public Set<String> cSubElements = new HashSet<>();

        public boolean hasChildCs = false;

        public void handleStartElement(StartElement startElement){
            Attribute attribute = startElement.getAttributeByName(new QName("id"));
            if (attribute!=null){
                this.id = attribute.getValue();
                if (this.id != null) {
                    if (this.id.contains("ark:") || this.id.contains("purl") || this.id.contains("doi.") || this.id.contains("hdl.handle") || this.id.contains("urn")) {
                        this.idIsPID = true;
                    }
                }
            }
        }

        public void handleExtptrElement(StartElement element){
            if (!isExtptrPID) {
                Iterator<Attribute> iterator = element.getAttributes();
                List<Attribute> actualList = new ArrayList<>();
                while (iterator.hasNext()) {
                    actualList.add(iterator.next());
                }

                for (Attribute attribute : actualList){
                    if (attribute.getName().getLocalPart().equals("href")){
                        String s = attribute.getValue();
                        if (s.contains("ark:") || s.contains("purl") || s.contains("doi.") || s.contains("hdl.handle") || s.contains("urn")) {
                            isExtptrPID = true;
                            break;
                        }
                    }
                }
//                Attribute attribute = element.getAttributeByName(new QName("href"));
//                if (attribute==null){
//                    attribute = element.getAttributeByName(new QName("http://www.w3.org/1999/xlink","href","xlink"));
//                }
//                if (attribute != null) {
//                    String s = attribute.getValue();
//                    if (s.contains("ark:") || s.contains("purl") || s.contains("doi.") || s.contains("hdl.handle") || s.contains("urn")) {
//                        isExtptrPID = true;
//                    }
//                }
            }
        }

        public void handleCDidSubElement(StartElement element){
            cDidSubElements.add(element.getName().getLocalPart());
        }

        public void handleCSubElement(StartElement element){
            cSubElements.add(element.getName().getLocalPart());
        }

        public void handleUnitidElement(StartElement unitidElement){
            noOfUnitId++;

            Attribute attribute = unitidElement.getAttributeByName(new QName("type"));
            if (attribute != null){
                if (attribute.getValue().equals("call number")){
                    noOfUnitIdsWithCallNumber++;
                }
                else {
                    noOfUnitIdsWithOtherType++;
                    otheUnitIdTypes.add(attribute.getValue());
                }
            }
            else {
                noOfUnitIdsWithNoType++;
            }
        }
    }
}
