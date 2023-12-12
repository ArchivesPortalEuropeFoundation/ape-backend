package eu.apenet.scripts;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
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

    public static void main (String[] args) throws IOException {
        System.out.println("Hello from CLI world!");

        EadXmlFileStatistics eadXmlFileStatistics = new EadXmlFileStatistics();
        eadXmlFileStatistics.readValidFilePaths();
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

    private void doTheJob() throws IOException {
        System.out.println("time start: " + (new Date()));
        String repoPath = ((ScriptsConfig) APEnetUtilities.getConfig()).getRepoDirPath();

        int totalFileCounter = 0;
        int totalNotFoundFiles = 0;
        double maxFileSize = 0;
        String maxFileCountry = "";
        String maxFileName = "";
        double minFileSize = 100000000;
        String minFileCountry = "";
        String minFileName = "";
        Map<String, Map<String, Double>> perCountryInfo = new HashMap<>();

        Map<String, Integer> archdescDidElementsMap = new HashMap<>();
        Map<String, Integer> archdescElementsMap = new HashMap<>();
        Map<String, Integer> cDidElementsMap = new HashMap<>();
        Map<String, Integer> cElementsMap = new HashMap<>();

        Map<String, Integer> cLeavesDidElementsMap = new HashMap<>();
        Map<String, Integer> cLeavesElementsMap = new HashMap<>();

        Map<String, Integer> otherUnitIdTypes = new HashMap<>();
        Map<String, Integer> unitIdInfoMap = new HashMap<>();
        Map<String, Integer> otherUnitIdTypesForLeaves = new HashMap<>();
        Map<String, Integer> unitIdInfoMapForLeaves = new HashMap<>();

        Map<String, Integer> schemaLocations = new HashMap<>();
        Map<String, List<String>> schemaLocationsMap = new HashMap<>();
        int cCounter = 0;
        int cCounter2 = 0;
        int cCounterLeaves = 0;

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

//                if (!countryDir.getName().equals("AL")) continue;

                int totalCountryFiles=0;
                double totalCountrySize=0;
                double maxCountrySize = 0;
                double minCountrySize = 100000000;

                int totalFileCounterPerCountry = 0;

                for (File institutionDir : countryDir.listFiles()){
                    if (institutionDir.isDirectory()){
//                        System.out.println("\tInstitution: " + institutionDir.getName());
                        for (File typeDir : institutionDir.listFiles()){
                            if (typeDir.isDirectory() && (typeDir.getName().equals("FA") || typeDir.getName().equals("SG") || typeDir.getName().equals("HG"))){
                                for (File xmlFile : typeDir.listFiles()){
                                    if (isValidFile(xmlFile)/* || true*//*xmlFile.getName().endsWith(".xml")*/){
//                                        System.out.println("\t\tXML file ("+typeDir.getName()+"): " + xmlFile.getName());
                                        totalFileCounter++;
                                        totalFileCounterPerCountry++;

                                        printWriter.println(xmlFile.getAbsolutePath().replace("/ape/data/repo", ""));

                                        long fileSize = xmlFile.length();
                                        if (fileSize > maxFileSize){
                                            maxFileSize = fileSize;
                                            maxFileCountry = countryDir.getName();
                                            maxFileName = xmlFile.getAbsolutePath();
                                        }

                                        if (fileSize < minFileSize){
                                            minFileSize = fileSize;
                                            minFileCountry = countryDir.getName();
                                            minFileName = xmlFile.getAbsolutePath();
                                        }

                                        totalCountryFiles++;
                                        totalCountrySize += fileSize;

                                        if (fileSize > maxCountrySize){
                                            maxCountrySize = fileSize;
                                        }

                                        if (fileSize < minCountrySize){
                                            minCountrySize = fileSize;
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
                                                    if (startElement.getName().getLocalPart().equals("ead")) {
                                                        Iterator iterator = startElement.getAttributes();
                                                        Attribute attribute2 = startElement.getAttributeByName(new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation"));
                                                        if (attribute2!=null) {
                                                            if (!schemaLocations.containsKey(attribute2.getValue())) {
                                                                schemaLocations.put(attribute2.getValue(),1);
                                                                schemaLocationsMap.put(attribute2.getValue(), new ArrayList<>());
                                                                schemaLocationsMap.get(attribute2.getValue()).add(xmlFile.getAbsolutePath());
                                                            }
                                                            else {
                                                                schemaLocations.put(attribute2.getValue(), schemaLocations.get(attribute2.getValue())+1);
                                                                schemaLocationsMap.get(attribute2.getValue()).add(xmlFile.getAbsolutePath());
                                                            }
                                                        }
                                                        else {
                                                            System.out.println("Error: " + xmlFile.getAbsolutePath());
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
                                                        }
                                                    }
                                                    else if (startElement.getName().getLocalPart().equals("c")) {
                                                        cCounter++;

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
                                                            if (currentCInfo != null){
                                                                currentCInfo.handleUnitidElement(startElement);
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
                                                else if (nextEvent.isEndElement()) {
                                                    currentDepth--;

                                                    EndElement endElement = nextEvent.asEndElement();
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
                                                    else {

                                                    }
                                                }
                                            }

                                            for (String s : archDescElementSet){
//                                                System.out.println(s);
                                                if (!archdescElementsMap.containsKey(s)){
                                                    archdescElementsMap.put(s, 1);
                                                }
                                                else {
                                                    archdescElementsMap.put(s, archdescElementsMap.get(s)+1);
                                                }
                                            }

                                            for (String s : archDescDidElementSet){
                                                if (!archdescDidElementsMap.containsKey(s)){
                                                    archdescDidElementsMap.put(s, 1);
                                                }
                                                else {
                                                    archdescDidElementsMap.put(s, archdescDidElementsMap.get(s)+1);
                                                }
                                            }

                                            cCounter2 += allCInfos.size();
                                            cCounterLeaves += allCLeavesInfos.size();
                                            for (CInfo cInfo : allCInfos){
                                                for (String s : cInfo.otheUnitIdTypes){
                                                    if (!otherUnitIdTypes.containsKey(s)){
                                                        otherUnitIdTypes.put(s, 1);
                                                    }
                                                    else {
                                                        otherUnitIdTypes.put(s, otherUnitIdTypes.get(s)+1);
                                                    }
                                                }

                                                for (String s : cInfo.cDidSubElements){
                                                    if (!cDidElementsMap.containsKey(s)){
                                                        cDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        cDidElementsMap.put(s, cDidElementsMap.get(s)+1);
                                                    }
                                                }

                                                for (String s : cInfo.cSubElements){
                                                    if (!cElementsMap.containsKey(s)){
                                                        cElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        cElementsMap.put(s, cElementsMap.get(s)+1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitId>0){
                                                    increase("metric1", unitIdInfoMap);

                                                    if (cInfo.id != null){
                                                        increase("metric2", unitIdInfoMap);
                                                    }
                                                }
                                                else {
                                                    increase("metric3", unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric4", unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                    increase("metric5", unitIdInfoMap);
                                                    if (cInfo.id != null){
                                                        increase("metric6", unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>1){
                                                    increase("metric7", unitIdInfoMap);
                                                }
                                                if (cInfo.noOfUnitIdsWithOtherType>0){
                                                    increase("metric8", unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric9", unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric10", unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithNoType>0){
                                                    increase("metric11", unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric12", unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric13", unitIdInfoMap);
                                                    }
                                                }
                                                if (cInfo.id != null){
                                                    increase("metric14", unitIdInfoMap);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric15", unitIdInfoMap);
                                                    }
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric16", unitIdInfoMap);
                                                    }
                                                }
                                                else {
                                                    increase("metric17", unitIdInfoMap);
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric18", unitIdInfoMap);
                                                    }
                                                    else {
                                                        increase("metric19", unitIdInfoMap);
                                                    }
                                                }



                                                String myKey = "with_id";
                                                if (cInfo.id == null){
                                                    myKey = "with_no_id";
                                                }
                                                increase(myKey, unitIdInfoMap);

                                                myKey = "with_no_unitid";
                                                if (cInfo.noOfUnitId > 0){
                                                    myKey = "with_unitid";
                                                }
                                                if (!unitIdInfoMap.containsKey(myKey)){
                                                    unitIdInfoMap.put(myKey, 1);
                                                }
                                                else {
                                                    unitIdInfoMap.put(myKey, unitIdInfoMap.get(myKey)+1);
                                                }

                                                if (cInfo.noOfUnitId > 0) {
                                                    myKey = "with_no_type_call_number";
                                                    if (cInfo.noOfUnitIdsWithCallNumber > 0) {
                                                        myKey = "with_at_least_one_type_call_number";
                                                    }
                                                    if (!unitIdInfoMap.containsKey(myKey)) {
                                                        unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMap.put(myKey, unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber > 1) {
                                                    myKey = "with_more_than_one_call_number";
                                                    if (!unitIdInfoMap.containsKey(myKey)) {
                                                        unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMap.put(myKey, unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithNoType > 0) {
                                                    myKey = "with_no_type";
                                                    if (!unitIdInfoMap.containsKey(myKey)) {
                                                        unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMap.put(myKey, unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                    myKey = "with_other_type";
                                                    if (!unitIdInfoMap.containsKey(myKey)) {
                                                        unitIdInfoMap.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMap.put(myKey, unitIdInfoMap.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber==0) {
                                                    if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                        myKey = "with_no_call_number_but_other_type";
                                                        if (!unitIdInfoMap.containsKey(myKey)) {
                                                            unitIdInfoMap.put(myKey, 1);
                                                        } else {
                                                            unitIdInfoMap.put(myKey, unitIdInfoMap.get(myKey) + 1);
                                                        }
                                                    }
                                                }
                                            }

                                            //Leaves
                                            for (CInfo cInfo : allCLeavesInfos){
                                                for (String s : cInfo.otheUnitIdTypes){
                                                    if (!otherUnitIdTypesForLeaves.containsKey(s)){
                                                        otherUnitIdTypesForLeaves.put(s, 1);
                                                    }
                                                    else {
                                                        otherUnitIdTypesForLeaves.put(s, otherUnitIdTypesForLeaves.get(s)+1);
                                                    }
                                                }

                                                for (String s : cInfo.cDidSubElements){
                                                    if (!cLeavesDidElementsMap.containsKey(s)){
                                                        cLeavesDidElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        cLeavesDidElementsMap.put(s, cLeavesDidElementsMap.get(s)+1);
                                                    }
                                                }

                                                for (String s : cInfo.cSubElements){
                                                    if (!cLeavesElementsMap.containsKey(s)){
                                                        cLeavesElementsMap.put(s, 1);
                                                    }
                                                    else {
                                                        cLeavesElementsMap.put(s, cLeavesElementsMap.get(s)+1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitId>0){
                                                    increase("metric1", unitIdInfoMapForLeaves);

                                                    if (cInfo.id != null){
                                                        increase("metric2", unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                else {
                                                    increase("metric3", unitIdInfoMapForLeaves);
                                                    if (cInfo.id != null){
                                                        increase("metric4", unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                    increase("metric5", unitIdInfoMapForLeaves);
                                                    if (cInfo.id != null){
                                                        increase("metric6", unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithCallNumber>1){
                                                    increase("metric7", unitIdInfoMapForLeaves);
                                                }
                                                if (cInfo.noOfUnitIdsWithOtherType>0){
                                                    increase("metric8", unitIdInfoMapForLeaves);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric9", unitIdInfoMapForLeaves);
                                                    }
                                                    else {
                                                        increase("metric10", unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.noOfUnitIdsWithNoType>0){
                                                    increase("metric11", unitIdInfoMapForLeaves);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric12", unitIdInfoMapForLeaves);
                                                    }
                                                    else {
                                                        increase("metric13", unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                if (cInfo.id != null){
                                                    increase("metric14", unitIdInfoMapForLeaves);
                                                    if (cInfo.noOfUnitIdsWithCallNumber>0){
                                                        increase("metric15", unitIdInfoMapForLeaves);
                                                    }
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric16", unitIdInfoMapForLeaves);
                                                    }
                                                }
                                                else {
                                                    increase("metric17", unitIdInfoMapForLeaves);
                                                    if (cInfo.noOfUnitId>0){
                                                        increase("metric18", unitIdInfoMapForLeaves);
                                                    }
                                                    else {
                                                        increase("metric19", unitIdInfoMapForLeaves);
                                                    }
                                                }



                                                String myKey = "with_id";
                                                if (cInfo.id == null){
                                                    myKey = "with_no_id";
                                                }
                                                increase(myKey, unitIdInfoMapForLeaves);

                                                myKey = "with_no_unitid";
                                                if (cInfo.noOfUnitId > 0){
                                                    myKey = "with_unitid";
                                                }
                                                if (!unitIdInfoMapForLeaves.containsKey(myKey)){
                                                    unitIdInfoMapForLeaves.put(myKey, 1);
                                                }
                                                else {
                                                    unitIdInfoMapForLeaves.put(myKey, unitIdInfoMapForLeaves.get(myKey)+1);
                                                }

                                                if (cInfo.noOfUnitId > 0) {
                                                    myKey = "with_no_type_call_number";
                                                    if (cInfo.noOfUnitIdsWithCallNumber > 0) {
                                                        myKey = "with_at_least_one_type_call_number";
                                                    }
                                                    if (!unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMapForLeaves.put(myKey, unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber > 1) {
                                                    myKey = "with_more_than_one_call_number";
                                                    if (!unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMapForLeaves.put(myKey, unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithNoType > 0) {
                                                    myKey = "with_no_type";
                                                    if (!unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMapForLeaves.put(myKey, unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                    myKey = "with_other_type";
                                                    if (!unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                        unitIdInfoMapForLeaves.put(myKey, 1);
                                                    } else {
                                                        unitIdInfoMapForLeaves.put(myKey, unitIdInfoMapForLeaves.get(myKey) + 1);
                                                    }
                                                }

                                                if (cInfo.noOfUnitIdsWithCallNumber==0) {
                                                    if (cInfo.noOfUnitIdsWithOtherType > 0) {
                                                        myKey = "with_no_call_number_but_other_type";
                                                        if (!unitIdInfoMapForLeaves.containsKey(myKey)) {
                                                            unitIdInfoMapForLeaves.put(myKey, 1);
                                                        } else {
                                                            unitIdInfoMapForLeaves.put(myKey, unitIdInfoMapForLeaves.get(myKey) + 1);
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
                                        totalNotFoundFiles++;
                                        printWriter2.println(xmlFile.getAbsolutePath().replace("/ape/data/repo", ""));
                                    }
                                }
                            }
                        }
                    }
                }
                System.out.println("\tFiles: " + (int)totalFileCounterPerCountry);
                Map<String, Double> temp = new HashMap<>();
                temp.put("no_of_files", new Double(totalCountryFiles));
                temp.put("size_of_files", totalCountrySize);
                perCountryInfo.put(countryDir.getName(),temp);
            }
        }

        System.out.println("totalFileCounter: " + totalFileCounter);
        System.out.println("totalNotFoundFiles: " + totalNotFoundFiles);
        System.out.println("MaxFileSize: " + gettFileSize(maxFileSize));
        System.out.println("MaxCountry: " + maxFileCountry);
        System.out.println("MaxFileName: " + maxFileName);

        System.out.println("MinFileSize: " + gettFileSize(minFileSize));
        System.out.println("MinCountry: " + minFileCountry);
        System.out.println("MinFileName: " + minFileName);

        System.out.println("c counter: " + cCounter);
        System.out.println("c counter 2: " + cCounter2);

        System.out.println("c counter (leaves): " + cCounterLeaves);

        System.out.println("schemaLocations: " + schemaLocations.size());
        for (String s : schemaLocations.keySet()){
            System.out.println("\t["+schemaLocations.get(s)+"] " + s);
        }

        FileWriter fileWriter3 = new FileWriter("schemalocations.txt");
        PrintWriter printWriter3 = new PrintWriter(fileWriter3);
        for (String s : schemaLocationsMap.keySet()){
            for (String s1 : schemaLocationsMap.get(s)) {
                printWriter3.println(s.replaceAll("[\\t\\n\\r]+", " ") + ";" + s1);
            }
        }
        printWriter3.close();

        System.out.println("arch elements");
        for (String s : archdescElementsMap.keySet()){
            System.out.println("\t["+archdescElementsMap.get(s)+"] " + s);
        }

        System.out.println("arch did elements");
        for (String s : archdescDidElementsMap.keySet()){
            System.out.println("\t["+archdescDidElementsMap.get(s)+"] " + s);
        }

        System.out.println("other unitid types");
        for (String s : otherUnitIdTypes.keySet()){
            System.out.println("\t["+otherUnitIdTypes.get(s)+"] " + s);
        }

        System.out.println("other unitid types (for leaves)");
        for (String s : otherUnitIdTypesForLeaves.keySet()){
            System.out.println("\t["+otherUnitIdTypesForLeaves.get(s)+"] " + s);
        }

        System.out.println("unitid info");
        for (String s : unitIdInfoMap.keySet()){
            System.out.println("\t["+unitIdInfoMap.get(s)+"] " + s);
        }

        System.out.println("unitid info (leaves");
        for (String s : unitIdInfoMapForLeaves.keySet()){
            System.out.println("\t["+unitIdInfoMapForLeaves.get(s)+"] " + s);
        }

        System.out.println("c did elements");
        for (String s : cDidElementsMap.keySet()){
            System.out.println("\t["+cDidElementsMap.get(s)+"] " + s);
        }

        System.out.println("c (leaves) did elements");
        for (String s : cLeavesDidElementsMap.keySet()){
            System.out.println("\t["+cLeavesDidElementsMap.get(s)+"] " + s);
        }

        System.out.println("c elements");
        for (String s : cElementsMap.keySet()){
            System.out.println("\t["+cElementsMap.get(s)+"] " + s);
        }

        System.out.println("c leaves elements");
        for (String s : cLeavesElementsMap.keySet()){
            System.out.println("\t["+cLeavesElementsMap.get(s)+"] " + s);
        }

        System.out.println("Per country info:");
        System.out.println("------------------------------");
        for (String country : perCountryInfo.keySet()){
            Map<String, Double> temp = perCountryInfo.get(country);
//            System.out.println("country: " + country);
//            System.out.println("\tNo of files: " + temp.get("no_of_files").intValue());
//            System.out.println("\tSize of files: " + gettFileSize(temp.get("size_of_files")));
//            System.out.println("\tMean size of files: " + (temp.get("no_of_files")>0?gettFileSize(temp.get("size_of_files")/temp.get("no_of_files")):0));

            System.out.println(country + ";" + temp.get("no_of_files").intValue() + ";" + gettFileSizeInMB(temp.get("size_of_files")) + ";" + (temp.get("no_of_files")>0?gettFileSizeInMB(temp.get("size_of_files")/temp.get("no_of_files")):0));
        }

        printWriter.close();
        printWriter2.close();
        printWriter4.close();

        System.out.println("time stendart: " + (new Date()));
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
