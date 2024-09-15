package eu.apenet.scripts.utils;

import com.google.gson.*;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

import java.io.*;
import java.util.*;

public class Statistics {

    public String type;

    public int totalFiles = 0;
    public int totalNotFoundFiles = 0;
    public double totalSize = 0;
    public double maxSize = 0;
    public double minSize = 100000000;

    public String maxFileCountry = "";
    public String maxFileName = "";
    public String minFileCountry = "";
    public String minFileName = "";

    public String maxFileInstitution = "";
    public String minFileInsitution = "";

//    public Map<String, Map<String, Double>> perCountryInfo = new HashMap<>();
//    public Map<String, Map<String, Double>> perInsitutionInfo = new HashMap<>();

    public Map<String, Statistics> perCountryInfoStatistics = new HashMap<>();
    public Map<String, Statistics> perInsitutionInfoStatistics = new HashMap<>();

    public Map<String, Integer> archdescDidElementsMap = new HashMap<>();
    public Map<String, Integer> archdescElementsMap = new HashMap<>();
    public Map<String, Integer> cDidElementsMap = new HashMap<>();
    public Map<String, Integer> cElementsMap = new HashMap<>();

    public Map<String, Integer> cLeavesDidElementsMap = new HashMap<>();
    public Map<String, Integer> cLeavesElementsMap = new HashMap<>();

    public Map<String, Integer> otherUnitIdTypes = new HashMap<>();
    public Map<String, Integer> unitIdInfoMap = new TreeMap<>();
    public Map<String, Integer> otherUnitIdTypesForLeaves = new HashMap<>();
    public Map<String, Integer> unitIdInfoMapForLeaves = new TreeMap<>();

    public Map<String, Integer> schemaLocations = new HashMap<>();
//    public Map<String, List<String>> schemaLocationsMap = new HashMap<>();

    public int cCounter = 0;
    public int cCounter2 = 0;
    public int cCounterLeaves = 0;

    private Map<String, String> countryMap = new HashMap<>();
    public Map<String, String> allAIs = new HashMap<>();

    public Statistics(){
        unitIdInfoMap.put("metric01", 0);
        unitIdInfoMap.put("metric02", 0);
        unitIdInfoMap.put("metric03", 0);
        unitIdInfoMap.put("metric04", 0);
        unitIdInfoMap.put("metric05", 0);
        unitIdInfoMap.put("metric06", 0);
        unitIdInfoMap.put("metric07", 0);
        unitIdInfoMap.put("metric08", 0);
        unitIdInfoMap.put("metric09", 0);
        unitIdInfoMap.put("metric10", 0);
        unitIdInfoMap.put("metric11", 0);
        unitIdInfoMap.put("metric12", 0);
        unitIdInfoMap.put("metric13", 0);
        unitIdInfoMap.put("metric14", 0);
        unitIdInfoMap.put("metric15", 0);
        unitIdInfoMap.put("metric16", 0);
        unitIdInfoMap.put("metric17", 0);
        unitIdInfoMap.put("metric18", 0);
        unitIdInfoMap.put("metric19", 0);

        countryMap.put("ES","SPAIN");
        countryMap.put("FR","FRANCE");
        countryMap.put("DE","GERMANY");
        countryMap.put("GR","GREECE");
        countryMap.put("PL","POLAND");
        countryMap.put("SE","SWEDEN");
        countryMap.put("NL","NETHERLANDS");
        countryMap.put("BE","BELGIUM");
        countryMap.put("SI","SLOVENIA");
        countryMap.put("IE","IRELAND");
        countryMap.put("LV","LATVIA");
        countryMap.put("MT","MALTA");
        countryMap.put("PT","PORTUGAL");
        countryMap.put("FI","FINLAND");
        countryMap.put("BG","BULGARIA");
        countryMap.put("CZ","CZECH REPUBLIC");
        countryMap.put("EE","ESTONIA");
        countryMap.put("HU","HUNGARY");
        countryMap.put("IS","ICELAND");
        countryMap.put("LU","LUXEMBOURG");
        countryMap.put("CH","SWITZERLAND");
        countryMap.put("DK","DENMARK");
        countryMap.put("AT","AUSTRIA");
        countryMap.put("GB","UNITED KINGDOM");
        countryMap.put("LI","LIECHTENSTEIN");
        countryMap.put("SK","SLOVAKIA");
        countryMap.put("NO","NORWAY");
        countryMap.put("IT","ITALY");
        countryMap.put("LT","LITHUANIA");
        countryMap.put("RO","ROMANIA");
        countryMap.put("HR","CROATIA");
        countryMap.put("MK","MACEDONIA");
        countryMap.put("RS","SERBIA");
        countryMap.put("ME","MONTENEGRO");
        countryMap.put("GE","GEORGIA");
        countryMap.put("IM","ISLE OF MAN");
        countryMap.put("AL","ALBANIA");
        countryMap.put("IL","ISRAEL");
        countryMap.put("UA","UKRAINE");
        countryMap.put("XX","MULTINATIONAL INSTITUTIONS");
        countryMap.put("XK","KOSOVO");
        countryMap.put("TR","TURKEY");
    }

    public JsonObject writeJson(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }

        List<String> allPossibleSchemaLocations = new ArrayList<>();
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/schemas/ead/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 https://schemas.archivesportaleurope.net/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 http://schemas.archivesportaleurope.net/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/schemas/ead/apeEAD.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 https://schemas.archivesportaleurope.net/profiles/apeEAD.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 http://schemas.archivesportaleurope.net/profiles/apeEAD.xsd");
        allPossibleSchemaLocations.add("urn:isbn:1-931666-22-9 apeEAD.xsd");
        allPossibleSchemaLocations.add("no_schema_location");
        allPossibleSchemaLocations.add("other_schema_location");
        for (String s : allPossibleSchemaLocations){
            if (!schemaLocations.containsKey(s)){
                schemaLocations.put(s, 0);
            }
        }

        List<String> allPossibleOtherUnitIdTypes = new ArrayList<>();
        allPossibleOtherUnitIdTypes.add("former call number");
        allPossibleOtherUnitIdTypes.add("file reference");
        allPossibleOtherUnitIdTypes.add("other_unitid_type");
        for (String s : allPossibleOtherUnitIdTypes){
            if (!otherUnitIdTypes.containsKey(s)){
                otherUnitIdTypes.put(s, 0);
            }
        }

        List<String> allPossibleCDidElements = new ArrayList<>();
        allPossibleCDidElements.add("scopecontent");
        allPossibleCDidElements.add("userestrict");
        allPossibleCDidElements.add("prefercite");
        allPossibleCDidElements.add("accessrestrict");
        allPossibleCDidElements.add("controlaccess");
        allPossibleCDidElements.add("bioghist");
        allPossibleCDidElements.add("custodhist");
        allPossibleCDidElements.add("otherfindaid");
        allPossibleCDidElements.add("altformavail");
        allPossibleCDidElements.add("relatedmaterial");
        allPossibleCDidElements.add("bibliography");
        allPossibleCDidElements.add("acqinfo");
        allPossibleCDidElements.add("appraisal");
        allPossibleCDidElements.add("accruals");
        allPossibleCDidElements.add("arrangement");
        allPossibleCDidElements.add("processinfo");
        allPossibleCDidElements.add("fileplan");
        allPossibleCDidElements.add("phystech");
        allPossibleCDidElements.add("odd");
        allPossibleCDidElements.add("fileplan");
        allPossibleCDidElements.add("other_c_element");
        for (String s : allPossibleCDidElements){
            if (!cElementsMap.containsKey(s)){
                cElementsMap.put(s, 0);
            }
        }

        List<String> allPossibleCElements = new ArrayList<>();
        allPossibleCElements.add("unittitle");
        allPossibleCElements.add("unitdate");
        allPossibleCElements.add("physdesc");
        allPossibleCElements.add("dao");
        allPossibleCElements.add("origination");
        allPossibleCElements.add("repository");
        allPossibleCElements.add("langmaterial");
        allPossibleCElements.add("container");
        allPossibleCElements.add("physloc");
        allPossibleCElements.add("materialspec");
        allPossibleCElements.add("note");
        allPossibleCElements.add("head");
        allPossibleCElements.add("other_c_did_element");
        for (String s : allPossibleCElements){
            if (!cDidElementsMap.containsKey(s)){
                cDidElementsMap.put(s, 0);
            }
        }

        List<String> allPossibleArchDescElements = new ArrayList<>();
        allPossibleArchDescElements.add("scopecontent");
        allPossibleArchDescElements.add("userestrict");
        allPossibleArchDescElements.add("prefercite");
        allPossibleArchDescElements.add("accessrestrict");
        allPossibleArchDescElements.add("controlaccess");
        allPossibleArchDescElements.add("bioghist");
        allPossibleArchDescElements.add("custodhist");
        allPossibleArchDescElements.add("otherfindaid");
        allPossibleArchDescElements.add("altformavail");
        allPossibleArchDescElements.add("relatedmaterial");
        allPossibleArchDescElements.add("bibliography");
        allPossibleArchDescElements.add("acqinfo");
        allPossibleArchDescElements.add("appraisal");
        allPossibleArchDescElements.add("accruals");
        allPossibleArchDescElements.add("arrangement");
        allPossibleArchDescElements.add("processinfo");
        allPossibleArchDescElements.add("fileplan");
        allPossibleArchDescElements.add("phystech");
        allPossibleArchDescElements.add("odd");
        allPossibleArchDescElements.add("dsc");
        allPossibleArchDescElements.add("other_archdesc_element");
        for (String s : allPossibleArchDescElements){
            if (!archdescElementsMap.containsKey(s)){
                archdescElementsMap.put(s, 0);
            }
        }

        List<String> allPossibleArchDescDidElements = new ArrayList<>();
        allPossibleArchDescDidElements.add("unitid");
        allPossibleArchDescDidElements.add("unittitle");
        allPossibleArchDescDidElements.add("unitdate");
        allPossibleArchDescDidElements.add("physdesc");
        allPossibleArchDescDidElements.add("dao");
        allPossibleArchDescDidElements.add("origination");
        allPossibleArchDescDidElements.add("repository");
        allPossibleArchDescDidElements.add("langmaterial");
        allPossibleArchDescDidElements.add("container");
        allPossibleArchDescDidElements.add("physloc");
        allPossibleArchDescDidElements.add("materialspec");
        allPossibleArchDescDidElements.add("note");
        allPossibleArchDescDidElements.add("head");
        allPossibleArchDescDidElements.add("other_archdesc_did_element");
        for (String s : allPossibleArchDescDidElements){
            if (!archdescDidElementsMap.containsKey(s)){
                archdescDidElementsMap.put(s, 0);
            }
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("total_files", totalFiles);
        jsonObject.addProperty("total_size", getFileSize(totalSize));
        jsonObject.addProperty("max_size", getFileSize(maxSize));
        jsonObject.addProperty("min_size", getFileSize(minSize));

        if (type.equals("total")) {
            jsonObject.addProperty("max_country", maxFileCountry);
            jsonObject.addProperty("max_institution", maxFileInstitution);
            jsonObject.addProperty("max_filename", maxFileName);

            jsonObject.addProperty("min_country", minFileCountry);
            jsonObject.addProperty("min_institution", minFileInsitution);
            jsonObject.addProperty("min_filename", minFileName);
        }
        else if (type.equals("country")) {
            jsonObject.addProperty("max_institution", maxFileInstitution);
            jsonObject.addProperty("max_filename", maxFileName);

            jsonObject.addProperty("min_institution", minFileInsitution);
            jsonObject.addProperty("min_filename", minFileName);
        }
        else if (type.equals("institution")) {
            jsonObject.addProperty("max_filename", maxFileName);
            jsonObject.addProperty("min_filename", minFileName);
        }

        jsonObject.addProperty("c_counter", cCounter);
        jsonObject.addProperty("c_counter_leaves", cCounterLeaves);

        JsonArray localJsonArray = null;
        JsonObject localJsonObject = null;

        localJsonObject = new JsonObject();
        for (String s : schemaLocations.keySet()){
            localJsonArray = new JsonArray();
            localJsonArray.add(schemaLocations.get(s));
            localJsonArray.add(getPrettyPercentage(new Double(schemaLocations.get(s))/new Double(totalFiles)));
            localJsonObject.add(s, localJsonArray);
        }
        jsonObject.add("schema_locations", localJsonObject);

        localJsonObject = new JsonObject();
        for (String s : archdescElementsMap.keySet()){
            localJsonArray = new JsonArray();
            localJsonArray.add(archdescElementsMap.get(s));
            localJsonArray.add(getPrettyPercentage(new Double(archdescElementsMap.get(s))/new Double(totalFiles)));
            localJsonObject.add(s, localJsonArray);
        }
        jsonObject.add("arch_elements", localJsonObject);

        localJsonObject = new JsonObject();
        for (String s : archdescDidElementsMap.keySet()){
            localJsonArray = new JsonArray();
            localJsonArray.add(archdescDidElementsMap.get(s));
            try {
                localJsonArray.add(getPrettyPercentage(new Double(archdescDidElementsMap.get(s))/new Double(archdescElementsMap.get("did"))));
            } catch (Exception e) {
//                e.printStackTrace();
                localJsonArray.add("--%");
            }
            localJsonObject.add(s, localJsonArray);
        }
        jsonObject.add("arch_did_elements", localJsonObject);


        localJsonObject = new JsonObject();
        for (String s : cElementsMap.keySet()){
            localJsonArray = new JsonArray();
            localJsonArray.add(cElementsMap.get(s));
            localJsonArray.add(getPrettyPercentage(new Double(cElementsMap.get(s))/new Double(cCounter)));
            localJsonObject.add(s, localJsonArray);
        }
        jsonObject.add("c_elements", localJsonObject);

        localJsonObject = new JsonObject();
        for (String s : cDidElementsMap.keySet()){
            localJsonArray = new JsonArray();
            localJsonArray.add(cDidElementsMap.get(s));
            try {
                localJsonArray.add(getPrettyPercentage(new Double(cDidElementsMap.get(s))/new Double(cElementsMap.get("did"))));
            } catch (Exception e) {
                //e.printStackTrace();
                localJsonArray.add("--%");
            }
            localJsonObject.add(s, localJsonArray);
        }
        jsonObject.add("c_did_elements", localJsonObject);

        JsonObject localJsonObject22 = new JsonObject();
        for (String s : otherUnitIdTypes.keySet()){
            localJsonArray = new JsonArray();
            localJsonArray.add(otherUnitIdTypes.get(s));
            localJsonArray.add(getPrettyPercentage(new Double(otherUnitIdTypes.get(s))/new Double(cCounter)));
            localJsonObject22.add(s, localJsonArray);
        }
        jsonObject.add("other_unitid_types", localJsonObject22);


        JsonObject countryLocalJsonObject2 = new JsonObject();
        for (String s : unitIdInfoMap.keySet()){
            localJsonArray = new JsonArray();
            localJsonArray.add(unitIdInfoMap.get(s));
            localJsonArray.add(getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
            countryLocalJsonObject2.add(s, localJsonArray);
        }
        jsonObject.add("unitid_info", countryLocalJsonObject2);



        Map<String, Statistics> mapToCheckInfo = null;
        String lektiko = null;
        if (type.equals("total")) {
            mapToCheckInfo = perCountryInfoStatistics;
            lektiko = "country";
        }
        else if (type.equals("country")) {
            mapToCheckInfo = perInsitutionInfoStatistics;
            lektiko = "institution";
        }

        if (mapToCheckInfo != null) {
            localJsonObject = new JsonObject();
            JsonObject schemaLocationsLocalJsonObject = new JsonObject();
            JsonObject otherUnitIdsLocalJsonObject = new JsonObject();
            JsonObject archElementsLocalJsonObject = new JsonObject();
            JsonObject archDidElementsLocalJsonObject = new JsonObject();
            JsonObject cElementsLocalJsonObject = new JsonObject();
            JsonObject cDidElementsLocalJsonObject = new JsonObject();

            for (String ccode : mapToCheckInfo.keySet()) {
                Statistics statistics = mapToCheckInfo.get(ccode);
                if (statistics.totalFiles > 0) {


                    JsonObject countryLocalJsonObject3 = new JsonObject();
                    List<String> allSchemas2 = new ArrayList<>();
                    for (String s : statistics.otherUnitIdTypes.keySet()) {
                        localJsonArray = new JsonArray();
                        localJsonArray.add(statistics.otherUnitIdTypes.get(s));
                        localJsonArray.add(getPrettyPercentage(new Double(statistics.otherUnitIdTypes.get(s)/new Double(cCounter))));
                        localJsonArray.add(getPrettyPercentage(new Double(statistics.otherUnitIdTypes.get(s)/new Double(statistics.cCounter))));
                        countryLocalJsonObject3.add(s, localJsonArray);
                        allSchemas2.add(s);
                    }
                    for (String s : otherUnitIdTypes.keySet()){
                        if (!allSchemas2.contains(s)){
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(0);
                            localJsonArray2.add("0.00%");
                            localJsonArray2.add("0.00%");
                            countryLocalJsonObject3.add(s, localJsonArray2);
                        }
                    }
                    otherUnitIdsLocalJsonObject.add(ccode, countryLocalJsonObject3);


                    JsonObject countryLocalJsonObject = new JsonObject();
                    for (String s : statistics.unitIdInfoMap.keySet()) {
                        localJsonArray = new JsonArray();
                        localJsonArray.add(statistics.unitIdInfoMap.get(s));
                        localJsonArray.add(getPrettyPercentage(new Double(statistics.unitIdInfoMap.get(s)/new Double(cCounter))));
                        localJsonArray.add(getPrettyPercentage(new Double(statistics.unitIdInfoMap.get(s)/new Double(statistics.cCounter))));
                        countryLocalJsonObject.add(s, localJsonArray);
                    }
                    localJsonObject.add(ccode, countryLocalJsonObject);


                    JsonObject localJsonObject2 = new JsonObject();
                    List<String> allSchemas = new ArrayList<>();
                    for (String s : statistics.schemaLocations.keySet()){
                        JsonArray localJsonArray2 = new JsonArray();
                        localJsonArray2.add(statistics.schemaLocations.get(s));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.schemaLocations.get(s)/new Double(totalFiles))));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.schemaLocations.get(s)/new Double(statistics.totalFiles))));
                        localJsonObject2.add(s, localJsonArray2);
                        allSchemas.add(s);
                    }
                    for (String s : schemaLocations.keySet()){
                        if (!allSchemas.contains(s)){
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(0);
                            localJsonArray2.add("0.00%");
                            localJsonArray2.add("0.00%");
                            localJsonObject2.add(s, localJsonArray2);
                        }
                    }
                    schemaLocationsLocalJsonObject.add(ccode, localJsonObject2);


                    localJsonObject2 = new JsonObject();
                    allSchemas = new ArrayList<>();
                    for (String s : statistics.otherUnitIdTypes.keySet()){
                        JsonArray localJsonArray2 = new JsonArray();
                        localJsonArray2.add(statistics.otherUnitIdTypes.get(s));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.otherUnitIdTypes.get(s)/new Double(cCounter))));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.otherUnitIdTypes.get(s)/new Double(statistics.cCounter))));
                        localJsonObject2.add(s, localJsonArray2);
                        allSchemas.add(s);
                    }
                    for (String s : otherUnitIdTypes.keySet()){
                        if (!allSchemas.contains(s)){
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(0);
                            localJsonArray2.add("0.00%");
                            localJsonArray2.add("0.00%");
                            localJsonObject2.add(s, localJsonArray2);
                        }
                    }
                    otherUnitIdsLocalJsonObject.add(ccode, localJsonObject2);



                    localJsonObject2 = new JsonObject();
                    allSchemas = new ArrayList<>();
                    for (String s : statistics.archdescElementsMap.keySet()){
                        JsonArray localJsonArray2 = new JsonArray();
                        localJsonArray2.add(statistics.archdescElementsMap.get(s));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.archdescElementsMap.get(s)/new Double(totalFiles))));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.archdescElementsMap.get(s)/new Double(statistics.totalFiles))));
                        localJsonObject2.add(s, localJsonArray2);
                        allSchemas.add(s);
                    }
                    for (String s : archdescElementsMap.keySet()){
                        if (!allSchemas.contains(s)){
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(0);
                            localJsonArray2.add("0.00%");
                            localJsonArray2.add("0.00%");
                            localJsonObject2.add(s, localJsonArray2);
                        }
                    }
                    archElementsLocalJsonObject.add(ccode, localJsonObject2);


                    localJsonObject2 = new JsonObject();
                    allSchemas = new ArrayList<>();
                    for (String s : statistics.archdescDidElementsMap.keySet()){
                        JsonArray localJsonArray2 = new JsonArray();
                        localJsonArray2.add(statistics.archdescDidElementsMap.get(s));
                        try {
                            localJsonArray2.add(getPrettyPercentage(new Double(statistics.archdescDidElementsMap.get(s)/new Double(archdescElementsMap.get("did")))));
                            localJsonArray2.add(getPrettyPercentage(new Double(statistics.archdescDidElementsMap.get(s)/new Double(statistics.archdescElementsMap.get("did")))));
                        } catch (Exception e) {
//                            e.printStackTrace();
                            localJsonArray2.add("--%");
                            localJsonArray2.add("--%");
                        }
                        localJsonObject2.add(s, localJsonArray2);
                        allSchemas.add(s);
                    }
                    for (String s : archdescDidElementsMap.keySet()){
                        if (!allSchemas.contains(s)){
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(0);
                            localJsonArray2.add("0.00%");
                            localJsonArray2.add("0.00%");
                            localJsonObject2.add(s, localJsonArray2);
                        }
                    }
                    archDidElementsLocalJsonObject.add(ccode, localJsonObject2);



                    localJsonObject2 = new JsonObject();
                    allSchemas = new ArrayList<>();
                    for (String s : statistics.cElementsMap.keySet()){
                        JsonArray localJsonArray2 = new JsonArray();
                        localJsonArray2.add(statistics.cElementsMap.get(s));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.cElementsMap.get(s)/new Double(cCounter))));
                        localJsonArray2.add(getPrettyPercentage(new Double(statistics.cElementsMap.get(s)/new Double(statistics.cCounter))));
                        localJsonObject2.add(s, localJsonArray2);
                        allSchemas.add(s);
                    }
                    for (String s : cElementsMap.keySet()){
                        if (!allSchemas.contains(s)){
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(0);
                            localJsonArray2.add("0.00%");
                            localJsonArray2.add("0.00%");
                            localJsonObject2.add(s, localJsonArray2);
                        }
                    }
                    cElementsLocalJsonObject.add(ccode, localJsonObject2);




                    localJsonObject2 = new JsonObject();
                    allSchemas = new ArrayList<>();
                    for (String s : statistics.cDidElementsMap.keySet()){
                        try {
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(statistics.cDidElementsMap.get(s));
                            localJsonArray2.add(getPrettyPercentage(new Double(statistics.cDidElementsMap.get(s)/new Double(cElementsMap.get("did")))));
                            localJsonArray2.add(getPrettyPercentage(new Double(statistics.cDidElementsMap.get(s)/new Double(statistics.cElementsMap.get("did")))));
                            localJsonObject2.add(s, localJsonArray2);
                        } catch (Exception e) {
//                            e.printStackTrace();
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(statistics.cDidElementsMap.get(s));
                            localJsonArray2.add("--%");
                            localJsonArray2.add("--%");
                            localJsonObject2.add(s, localJsonArray2);
                        }
                        allSchemas.add(s);
                    }
                    for (String s : cDidElementsMap.keySet()){
                        if (!allSchemas.contains(s)){
                            JsonArray localJsonArray2 = new JsonArray();
                            localJsonArray2.add(0);
                            localJsonArray2.add("0.00%");
                            localJsonArray2.add("0.00%");
                            localJsonObject2.add(s, localJsonArray2);
                        }
                    }
                    cDidElementsLocalJsonObject.add(ccode, localJsonObject2);
                }
            }


            JsonObject countryLocalJsonObject3 = new JsonObject();
            List<String> allSchemas2 = new ArrayList<>();
            for (String s : otherUnitIdTypes.keySet()) {
                localJsonArray = new JsonArray();
                localJsonArray.add(otherUnitIdTypes.get(s));
                localJsonArray.add(getPrettyPercentage(new Double(otherUnitIdTypes.get(s)/new Double(cCounter))));
                countryLocalJsonObject3.add(s, localJsonArray);
                allSchemas2.add(s);
            }
            otherUnitIdsLocalJsonObject.add("total", countryLocalJsonObject3);


            JsonObject countryLocalJsonObject = new JsonObject();
            for (String s : unitIdInfoMap.keySet()){
                localJsonArray = new JsonArray();
                localJsonArray.add(unitIdInfoMap.get(s));
                localJsonArray.add(getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
                countryLocalJsonObject.add(s, localJsonArray);
            }
            localJsonObject.add("total", countryLocalJsonObject);

            JsonObject localJsonObject2 = new JsonObject();
            List<String> allSchemas = new ArrayList<>();
            for (String s : schemaLocations.keySet()){
                JsonArray localJsonArray2 = new JsonArray();
                localJsonArray2.add(schemaLocations.get(s));
                localJsonArray2.add(getPrettyPercentage(new Double(schemaLocations.get(s)/new Double(totalFiles))));
                localJsonObject2.add(s, localJsonArray2);
                allSchemas.add(s);
            }
            for (String s : schemaLocations.keySet()){
                if (!allSchemas.contains(s)){
                    JsonArray localJsonArray2 = new JsonArray();
                    localJsonArray2.add(0);
                    localJsonArray2.add("0.00%");
                    localJsonArray2.add("0.00%");
                    localJsonObject2.add(s, localJsonArray2);
                }
            }
            schemaLocationsLocalJsonObject.add("total", localJsonObject2);

            localJsonObject2 = new JsonObject();
            allSchemas = new ArrayList<>();
            for (String s : archdescElementsMap.keySet()){
                JsonArray localJsonArray2 = new JsonArray();
                localJsonArray2.add(archdescElementsMap.get(s));
                localJsonArray2.add(getPrettyPercentage(new Double(archdescElementsMap.get(s)/new Double(totalFiles))));
                localJsonObject2.add(s, localJsonArray2);
                allSchemas.add(s);
            }
            for (String s : archdescElementsMap.keySet()){
                if (!allSchemas.contains(s)){
                    JsonArray localJsonArray2 = new JsonArray();
                    localJsonArray2.add(0);
                    localJsonArray2.add("0.00%");
                    localJsonArray2.add("0.00%");
                    localJsonObject2.add(s, localJsonArray2);
                }
            }
            archElementsLocalJsonObject.add("total", localJsonObject2);

            localJsonObject2 = new JsonObject();
            allSchemas = new ArrayList<>();
            for (String s : archdescDidElementsMap.keySet()){
                try {
                    JsonArray localJsonArray2 = new JsonArray();
                    localJsonArray2.add(archdescDidElementsMap.get(s));
                    localJsonArray2.add(getPrettyPercentage(new Double(archdescDidElementsMap.get(s)/new Double(archdescElementsMap.get("did")))));
                    localJsonObject2.add(s, localJsonArray2);
                } catch (Exception e) {
//                    e.printStackTrace();
                    JsonArray localJsonArray2 = new JsonArray();
                    localJsonArray2.add(archdescDidElementsMap.get(s));
                    localJsonArray2.add("--%");
                    localJsonObject2.add(s, localJsonArray2);
                }
                allSchemas.add(s);
            }
            for (String s : archdescDidElementsMap.keySet()){
                if (!allSchemas.contains(s)){
                    JsonArray localJsonArray2 = new JsonArray();
                    localJsonArray2.add(0);
                    localJsonArray2.add("0.00%");
                    localJsonArray2.add("0.00%");
                    localJsonObject2.add(s, localJsonArray2);
                }
            }
            archDidElementsLocalJsonObject.add("total", localJsonObject2);


            localJsonObject2 = new JsonObject();
            allSchemas = new ArrayList<>();
            for (String s : cElementsMap.keySet()){
                JsonArray localJsonArray2 = new JsonArray();
                localJsonArray2.add(cElementsMap.get(s));
                localJsonArray2.add(getPrettyPercentage(new Double(cElementsMap.get(s)/new Double(cCounter))));
                localJsonObject2.add(s, localJsonArray2);
                allSchemas.add(s);
            }
            for (String s : cElementsMap.keySet()){
                if (!allSchemas.contains(s)){
                    JsonArray localJsonArray2 = new JsonArray();
                    localJsonArray2.add(0);
                    localJsonArray2.add("0.00%");
                    localJsonArray2.add("0.00%");
                    localJsonObject2.add(s, localJsonArray2);
                }
            }
            cElementsLocalJsonObject.add("total", localJsonObject2);


            localJsonObject2 = new JsonObject();
            allSchemas = new ArrayList<>();
            for (String s : cDidElementsMap.keySet()){
                JsonArray localJsonArray2 = new JsonArray();
                localJsonArray2.add(cDidElementsMap.get(s));
                try {
                    localJsonArray2.add(getPrettyPercentage(new Double(cDidElementsMap.get(s)/new Double(cElementsMap.get("did")))));
                } catch (Exception e) {
                    localJsonArray2.add("--%");
                }
                localJsonObject2.add(s, localJsonArray2);
                allSchemas.add(s);
            }
            for (String s : cDidElementsMap.keySet()){
                if (!allSchemas.contains(s)){
                    JsonArray localJsonArray2 = new JsonArray();
                    localJsonArray2.add(0);
                    localJsonArray2.add("0.00%");
                    localJsonArray2.add("0.00%");
                    localJsonObject2.add(s, localJsonArray2);
                }
            }
            cDidElementsLocalJsonObject.add("total", localJsonObject2);


            jsonObject.add("unitid_info_"+lektiko, localJsonObject);
            jsonObject.add("schema_locations_"+lektiko, schemaLocationsLocalJsonObject);
            jsonObject.add("arch_elements_"+lektiko, archElementsLocalJsonObject);
            jsonObject.add("arch_did_elements_"+lektiko, archDidElementsLocalJsonObject);
            jsonObject.add("c_elements_"+lektiko, cElementsLocalJsonObject);
            jsonObject.add("c_did_elements_"+lektiko, cDidElementsLocalJsonObject);
            jsonObject.add("other_unitid_types_"+lektiko, otherUnitIdsLocalJsonObject);
        }
//        else if (type.equals("country")) {
//            localJsonObject = new JsonObject();
//
//            for (String ccode : perInsitutionInfoStatistics.keySet()) {
//                Statistics statistics = perInsitutionInfoStatistics.get(ccode);
//                if (statistics.totalFiles > 0) {
//                    JsonObject countryLocalJsonObject = new JsonObject();
//                    for (String s : statistics.unitIdInfoMap.keySet()) {
//                        localJsonArray = new JsonArray();
//                        localJsonArray.add(statistics.unitIdInfoMap.get(s));
//                        localJsonArray.add(getPrettyPercentage(new Double(statistics.unitIdInfoMap.get(s)/new Double(statistics.cCounter))));
//                        countryLocalJsonObject.add(s, localJsonArray);
//                    }
//                    localJsonObject.add(ccode, countryLocalJsonObject);
//                }
//            }
//            JsonObject countryLocalJsonObject = new JsonObject();
//            for (String s : unitIdInfoMap.keySet()){
//                localJsonArray = new JsonArray();
//                localJsonArray.add(unitIdInfoMap.get(s));
//                localJsonArray.add(getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
//                countryLocalJsonObject.add(s, localJsonArray);
//            }
//            localJsonObject.add("total", countryLocalJsonObject);
//
//            jsonObject.add("unitid_info", localJsonObject);
//        }
//        else if (type.equals("institution")) {
//            JsonObject countryLocalJsonObject = new JsonObject();
//            for (String s : unitIdInfoMap.keySet()){
//                localJsonArray = new JsonArray();
//                localJsonArray.add(unitIdInfoMap.get(s));
//                localJsonArray.add(getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
//                countryLocalJsonObject.add(s, localJsonArray);
//            }
//            jsonObject.add("total", countryLocalJsonObject);
//        }



        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        FileWriter fileWriter = new FileWriter(filePath+"/info.json");
        gson.toJson(jsonObject, fileWriter);
        fileWriter.flush();
        fileWriter.close();

        return jsonObject;
    }

    public void writeExcel(String filePath, JsonObject jsonObject, String type, String country, String institution) throws IOException {
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }

        String lektiko = "";
        if (type.equals("total")) {
            lektiko = "country";
        }
        else if (type.equals("country")) {
            lektiko = "institution";
        }

        try {
            SpreadsheetDocument document = SpreadsheetDocument.newSpreadsheetDocument();
            Table sheet1 = document.getSheetByIndex(0);
            sheet1.setTableName("total_adapted_GeneralStats");
            Table sheet2 = Table.newTable(document);
            sheet2.setTableName("total_adapted_ElementStats");


            //Sheet 1

            fillCell(sheet1, 0, 0, "Number of files");
            fillCell(sheet1, 0, 1, "Size of all files");
            fillCell(sheet1, 0, 2, "Biggest file (size)");
            fillCell(sheet1, 0, 3, "Biggest file (country)");
            fillCell(sheet1, 0, 4, "Biggest file (institution)");
            fillCell(sheet1, 0, 5, "Biggest file (file name)");
            fillCell(sheet1, 0, 6, "Smallest file (size)");
            fillCell(sheet1, 0, 7, "Smallest file (country)");
            fillCell(sheet1, 0, 8, "Smallest file (institution)");
            fillCell(sheet1, 0, 9, "Smallest file (file name)");
            fillCell(sheet1, 0, 10, "Number of components");

            fillCell(sheet1, 1, 0, jsonObject.get("total_files").getAsInt());
            fillCell(sheet1, 1, 1, jsonObject.get("total_size").getAsString());
            fillCell(sheet1, 1, 2, jsonObject.get("max_size").getAsString());
            if (country != null){

                fillCell(sheet1, 1, 3, countryMap.get(country) +" ("+country+")");
            }
            else {
                String country2 = jsonObject.get("max_country").getAsString();
                fillCell(sheet1, 1, 3, countryMap.get(country2) +" ("+country2+")");
            }
            if (institution != null){
                fillCell(sheet1, 1, 4, institution);
            }
            else {
                fillCell(sheet1, 1, 4, jsonObject.get("max_institution").getAsString());
            }
            fillCell(sheet1, 1, 5, jsonObject.get("max_filename").getAsString());
            fillCell(sheet1, 1, 6, jsonObject.get("min_size").getAsString());
            if (country != null){
                fillCell(sheet1, 1, 7, countryMap.get(country) +" ("+country+")");
            }
            else {
                String country2 = jsonObject.get("min_country").getAsString();
                fillCell(sheet1, 1, 7, countryMap.get(country2) +" ("+country2+")");
            }
            if (institution != null){
                fillCell(sheet1, 1, 8, institution);
            }
            else {
                fillCell(sheet1, 1, 8, jsonObject.get("min_institution").getAsString());
            }
            fillCell(sheet1, 1, 9, jsonObject.get("min_filename").getAsString());
            fillCell(sheet1, 1, 10, jsonObject.get("c_counter").getAsInt());



            //Sheet 2
            fillCell(sheet2, 3, 0, " total (#)");
            fillCell(sheet2, 4, 0, " total (%)");
            List<String> allCountriesOrInstitutions = findAllCountriesOrInstitutions(jsonObject);
            if (allCountriesOrInstitutions != null) {
                for (int i = 0; i < allCountriesOrInstitutions.size(); i++) {
                    String country2 = allCountriesOrInstitutions.get(i);
                    String countryToDisplay;
                    if (countryMap.containsKey(country2)){
                        countryToDisplay = countryMap.get(country2) +" ("+country2+")";
                    }
                    else {
                        countryToDisplay = allAIs.get(country2) +" ("+country2+")";
                    }

                    fillCell(sheet2, i * 3 + 5, 0, countryToDisplay + " (#)");
                    fillCell(sheet2, i * 3 + 1 + 5, 0,  countryToDisplay + " (%)");
                    fillCell(sheet2, i * 3 + 2 + 5, 0, countryToDisplay + " (%)");
                }
            }
            int row = 1;
            fillCell(sheet2, 0, row, "Schema location");

            fillSchemaLocationCell(sheet2, row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (new)", "urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/schemas/ead/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, https)", "urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, http)", "urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, schemas..., https)", "urn:isbn:1-931666-22-9 https://schemas.archivesportaleurope.net/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, schemas..., http)", "urn:isbn:1-931666-22-9 http://schemas.archivesportaleurope.net/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (new, without XLink)", "urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/schemas/ead/apeEAD.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, http, without XLink)", "urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, https, without XLink)", "urn:isbn:1-931666-22-9 https://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, schemas..., https)", "urn:isbn:1-931666-22-9 https://schemas.archivesportaleurope.net/profiles/apeEAD.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (old, schemas..., http)", "urn:isbn:1-931666-22-9 http://schemas.archivesportaleurope.net/profiles/apeEAD.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Expected schema location (no http)", "urn:isbn:1-931666-22-9 apeEAD.xsd");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "No schema location", "", "no_schema_location");
            fillSchemaLocationCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Other schema location", "Anything else, more details on request", "other_schema_location");

            fillCell(sheet2, 0, ++row, "Component description");

            fillMetricCell(sheet2, row, jsonObject, lektiko, allCountriesOrInstitutions, "Reference codes of the materials", "05 - At least one did/unitid/@type = \"call number\"", "metric05");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Several reference codes of the materials", "07 - Two or more did/unitid/@type = \"call number\"", "metric07");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Identifiers of the components", "14 - With @id attribute", "metric14");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Reference codes of the materials plus idenifiers of the components", "06 - At least one did/unitid/@type = \"call number\" + @id attribute", "metric06");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Identifiers of the materials (incl. reference codes)", "01 - At least one did/unitid (any or no type)", "metric01");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Identifiers of the materials plus identifiers of the components", "02 - At least one did/unitid (of any of no type) + @id attribute", "metric02");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "No identifiers of the materials", "03 - No did/unitid (of any or no type)", "metric03");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "No identifiers of the materials, but identifiers of the components instead", "04 - No did/unitid (of any or no type), but with @id attribute", "metric01");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "No identifiers of the components", "17 - No @id attribute", "metric17");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "No identifiers of the components, but identifiers of the materials instead", "18 - No @id attribute, but at least one did/unitid (of any type or no type)", "metric18");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "No identifiers at all (neither materials nor components)", "19 - No @id attribute nor at least one did/unitid (of any type or no type)", "metric19");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Other identifiers of the materials (one or more)", "08 - At least one did/unitid/@type <> \"call number\"", "metric08");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "", "09 - At least one did/unitid/@type <> \"call number\" plus at least one did/unitid/@type = \"call number\"", "metric09");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "", "10 - At least one did/unitid/@type <> \"call number\", but no did/unitid/@type = \"call number\"", "metric10");

            fillOtherUnitIdTypeCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Types of other identifiers of the materials", "did/unitid/@type = \"former call number\"", "former call number");
            fillOtherUnitIdTypeCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "", "did/unitid/@type = \"file reference\"","file reference");
            fillOtherUnitIdTypeCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "",  "Any other (non-apeEAD) types, more details on request", "other_unitid_type");

            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "Unknown identifiers of the materials (one or more)", "11 - At least one did/unitid with no @type attribute at all", "metric11");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "", "12 - At least one did/unitid with no @type attribute at all plus at least one did/unitid@type = \"call number\"", "metric12");
            fillMetricCell(sheet2, ++row, jsonObject, lektiko, allCountriesOrInstitutions, "", "13 - At least one did/unitid with no @type attribute at all, but no did/unitid@type = \"call number\"", "metric13");

            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Title",  "unittitle", "unittitle");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Date of creation",  "unitdate", "unitdate");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Extent, dimensions",  "physdesc", "physdesc");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Link to digital object",  "dao", "dao");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Name of creator",  "origination", "origination");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Name of institution",  "repository", "repository");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Language of material",  "langmaterial", "langmaterial");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Container",  "container", "container");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Physical location",  "physloc", "physloc");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Material specific details",  "materialspec", "materialspec");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Note",  "note", "note");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Header",  "head", "head");
            fillElementCell(sheet2, ++row, "c_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other",  "Any other elements (i.e. elements allowed in did in general, but not in apeEAD)", "other_c_did_element");

            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Scope and content",  "scopecontent", "scopecontent");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Conditions governing reproduction",  "userestrict", "userestrict");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Preferred citation",  "prefercite", "prefercite");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Conditions governing access",  "accessrestrict", "accessrestrict");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Controlled access headings",  "controlaccess", "controlaccess");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Administrative/biographical history",  "bioghist", "bioghist");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Archival history",  "custodhist", "custodhist");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other finding aids",  "otherfindaid", "otherfindaid");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Existence and location of copies",  "altformavail and/or originalsloc", "altformavail");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Related units of description",  "relatedmaterial and/or separatedmaterial", "relatedmaterial");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Publication note",  "bibliography", "bibliography");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Source of acquisition",  "acqinfo", "acqinfo");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Appraisal",  "appraisal", "appraisal");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Accruals",  "accruals", "accruals");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "System of arrangement",  "arrangement", "arrangement");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Processing information",  "processinfo", "processinfo");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "File plan",  "fileplan", "fileplan");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Physical characteristics and technical requirements",  "phystech", "phystech");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other descriptive information",  "odd", "odd");
            fillElementCell(sheet2, ++row, "c_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other",  "Any other elements (i.e. elements allowed in did in general, but not in apeEAD)", "other_c_element");

            fillCell(sheet2, 0, ++row, "Highest level description");

            fillElementCell(sheet2, row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Reference codes of the materials",  "unitid", "unitid");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Title",  "unittitle", "unittitle");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Date of creation",  "unitdate", "unitdate");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Extent, dimensions",  "physdesc", "physdesc");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Link to digital object",  "dao", "dao");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Name of creator",  "origination", "origination");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Name of institution",  "repository", "repository");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Language of material",  "langmaterial", "langmaterial");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Container",  "container", "container");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Physical location",  "physloc", "physloc");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Material specific details",  "materialspec", "materialspec");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Note",  "note", "note");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Header",  "head", "head");
            fillElementCell(sheet2, ++row, "arch_did_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other",  "Any other elements (i.e. elements allowed in did in general, but not in apeEAD)", "other_archdesc_did_element");

            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Scope and content",  "scopecontent", "scopecontent");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Conditions governing reproduction",  "userestrict", "userestrict");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Preferred citation",  "prefercite", "prefercite");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Conditions governing access",  "accessrestrict", "accessrestrict");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Controlled access headings",  "controlaccess", "controlaccess");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Administrative/biographical history",  "bioghist", "bioghist");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Archival history",  "custodhist", "custodhist");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other finding aids",  "otherfindaid", "otherfindaid");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Existence and location of copies",  "altformavail and/or originalsloc", "altformavail");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Related units of description",  "relatedmaterial and/or separatedmaterial", "relatedmaterial");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Publication note",  "bibliography", "bibliography");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Source of acquisition",  "acqinfo", "acqinfo");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Appraisal",  "appraisal", "appraisal");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Accruals",  "accruals", "accruals");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "System of arrangement",  "arrangement", "arrangement");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Processing information",  "processinfo", "processinfo");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "File plan",  "fileplan", "fileplan");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Physical characteristics and technical requirements",  "phystech", "phystech");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other descriptive information",  "odd", "odd");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Description of components",  "dsc", "dsc");
            fillElementCell(sheet2, ++row, "arch_elements", jsonObject, lektiko, allCountriesOrInstitutions, "Other",  "Any other elements (i.e. elements allowed in did in general, but not in apeEAD)", "other_archdesc_element");

            OutputStream outputStream = new FileOutputStream(filePath+"/info.xls");
            document.save(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillMetricCell(Table sheet, int row, JsonObject jsonObject, String lektiko, List<String> allCountriesOrInstitutions, String metricLabel, String metricExplanation, String metric){
        fillCell(sheet, 1, row, metricLabel);
        fillCell(sheet, 2, row, metricExplanation);
        JsonElement jsonEle = jsonObject.get("unitid_info_"+lektiko);
        JsonObject jsonObject1 = null;
        if (jsonEle == null){
            jsonEle = jsonObject.get("unitid_info");
            jsonObject1 = jsonEle.getAsJsonObject();
        }
        else {
            jsonObject1 = jsonEle.getAsJsonObject().get("total").getAsJsonObject();
        }
        fillCell(sheet, 3, row, jsonObject1.get(metric).getAsJsonArray().get(0).getAsInt());
        fillCell(sheet, 4, row, jsonObject1.get(metric).getAsJsonArray().get(1).getAsString());
        if (!type.equals("institution")){
            for (int i = 0; i < allCountriesOrInstitutions.size(); i++) {
                jsonObject1 = jsonEle.getAsJsonObject().get(allCountriesOrInstitutions.get(i)).getAsJsonObject();

                fillCell(sheet, 5+i*3, row, jsonObject1.get(metric).getAsJsonArray().get(0).getAsInt());
                fillCell(sheet, 6+i*3, row, jsonObject1.get(metric).getAsJsonArray().get(1).getAsString());
                fillCell(sheet, 7+i*3, row, jsonObject1.get(metric).getAsJsonArray().get(2).getAsString());
            }
        }
    }

    private void fillSchemaLocationCell(Table sheet, int row, JsonObject jsonObject, String lektiko, List<String> allCountriesOrInstitutions, String schemaLocationLabel, String schemaLocation){
        fillSchemaLocationCell(sheet, row, jsonObject, lektiko, allCountriesOrInstitutions, schemaLocationLabel, schemaLocation, schemaLocation);
    }

    private void fillSchemaLocationCell(Table sheet, int row, JsonObject jsonObject, String lektiko, List<String> allCountriesOrInstitutions, String schemaLocationLabel, String schemaLocationDescription, String schemaLocation){
        fillCell(sheet, 1, row, schemaLocationLabel);
        fillCell(sheet, 2, row, schemaLocationDescription);
        JsonElement jsonEle = jsonObject.get("schema_locations_"+lektiko);
        JsonObject jsonObject1 = null;
        if (jsonEle == null){
            jsonEle = jsonObject.get("schema_locations");
            jsonObject1 = jsonEle.getAsJsonObject();
        }
        else {
            jsonObject1 = jsonEle.getAsJsonObject().get("total").getAsJsonObject();
        }
        fillCell(sheet, 3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(0).getAsInt());
        fillCell(sheet, 4, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(1).getAsString());

        if (!type.equals("institution")){
            for (int i = 0; i < allCountriesOrInstitutions.size(); i++) {
                jsonObject1 = jsonEle.getAsJsonObject().get(allCountriesOrInstitutions.get(i)).getAsJsonObject();

                fillCell(sheet, 5+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(0).getAsInt());
                fillCell(sheet, 6+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(1).getAsString());
                fillCell(sheet, 7+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(2).getAsString());
            }
        }
    }

    private void fillOtherUnitIdTypeCell(Table sheet, int row, JsonObject jsonObject, String lektiko, List<String> allCountriesOrInstitutions, String schemaLocationLabel, String schemaLocationDescription, String schemaLocation){
        fillCell(sheet, 1, row, schemaLocationLabel);
        fillCell(sheet, 2, row, schemaLocationDescription);
        JsonElement jsonEle = jsonObject.get("other_unitid_types_"+lektiko);
        JsonObject jsonObject1 = null;
        if (jsonEle == null){
            jsonEle = jsonObject.get("other_unitid_types");
            jsonObject1 = jsonEle.getAsJsonObject();
        }
        else {
            jsonObject1 = jsonEle.getAsJsonObject().get("total").getAsJsonObject();
        }
        try {
            fillCell(sheet, 3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(0).getAsInt());
            fillCell(sheet, 4, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(1).getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!type.equals("institution")){
            for (int i = 0; i < allCountriesOrInstitutions.size(); i++) {
                jsonObject1 = jsonEle.getAsJsonObject().get(allCountriesOrInstitutions.get(i)).getAsJsonObject();

                fillCell(sheet, 5+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(0).getAsInt());
                fillCell(sheet, 6+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(1).getAsString());
                fillCell(sheet, 7+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(2).getAsString());
            }
        }
    }

    private void fillElementCell(Table sheet, int row, String elementName, JsonObject jsonObject, String lektiko, List<String> allCountriesOrInstitutions, String schemaLocationLabel, String schemaLocationDescription, String schemaLocation){
        fillCell(sheet, 1, row, schemaLocationLabel);
        fillCell(sheet, 2, row, schemaLocationDescription);
        JsonElement jsonEle = jsonObject.get(elementName+"_"+lektiko);
        JsonObject jsonObject1 = null;
        if (jsonEle == null){
            jsonEle = jsonObject.get(elementName);
            try {
                jsonObject1 = jsonEle.getAsJsonObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            jsonObject1 = jsonEle.getAsJsonObject().get("total").getAsJsonObject();
        }
        try {
            fillCell(sheet, 3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(0).getAsInt());
            fillCell(sheet, 4, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(1).getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!type.equals("institution")){
            for (int i = 0; i < allCountriesOrInstitutions.size(); i++) {
                try {
                    jsonObject1 = jsonEle.getAsJsonObject().get(allCountriesOrInstitutions.get(i)).getAsJsonObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fillCell(sheet, 5+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(0).getAsInt());
                fillCell(sheet, 6+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(1).getAsString());
                fillCell(sheet, 7+i*3, row, jsonObject1.get(schemaLocation).getAsJsonArray().get(2).getAsString());
            }
        }
    }

    public void writeCSV(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }

        FileWriter fileWriter = new FileWriter(filePath+"/info.csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("total files;"+totalFiles);
        printWriter.println("total size;"+ getFileSize(totalSize));
        printWriter.println("max size;"+ getFileSize(maxSize));
        printWriter.println("min size;"+ getFileSize(minSize));
        if (type.equals("total")) {
            printWriter.println("max country;" + maxFileCountry);
            printWriter.println("max institution;" + maxFileInstitution);
            printWriter.println("max file name;" + maxFileName);

            printWriter.println("min country;" + minFileCountry);
            printWriter.println("min institution;" + minFileInsitution);
            printWriter.println("min file name;" + minFileName);
        }
        else if (type.equals("country")) {
            printWriter.println("max institution;" + maxFileInstitution);
            printWriter.println("max file name;" + maxFileName);

            printWriter.println("min institution;" + minFileInsitution);
            printWriter.println("min file name;" + minFileName);
        }
        else if (type.equals("institution")) {
            printWriter.println("max file name;" + maxFileName);

            printWriter.println("min file name;" + minFileName);
        }

        printWriter.println("c counter;"+cCounter);
//        printWriter.println("c counter2;"+cCounter2);
        printWriter.println("c leaves;"+cCounterLeaves);

        int counter = 0;
        for (String s : schemaLocations.keySet()){
            if (counter==0) {
                printWriter.println("schema locations;" + s+";"+schemaLocations.get(s));
            }
            else {
                printWriter.println(";" + s + ";" + schemaLocations.get(s));
            }
            counter++;
        }

        counter = 0;
        for (String s : archdescElementsMap.keySet()){
            if (counter==0) {
                printWriter.println("arch elements;" + s+";"+archdescElementsMap.get(s)+";"+getPrettyPercentage(new Double(archdescElementsMap.get(s))/new Double(totalFiles)));
            }
            else {
                printWriter.println(";" + s + ";" + archdescElementsMap.get(s)+";"+getPrettyPercentage(new Double(archdescElementsMap.get(s))/new Double(totalFiles)));
            }
            counter++;
        }

        counter = 0;
        for (String s : archdescDidElementsMap.keySet()){
            if (counter==0) {
                printWriter.println("arch did elements;" + s+";"+archdescDidElementsMap.get(s)+";"+getPrettyPercentage(new Double(archdescDidElementsMap.get(s))/new Double(archdescElementsMap.get("did"))));
            }
            else {
                printWriter.println(";" + s + ";" + archdescDidElementsMap.get(s)+";"+getPrettyPercentage(new Double(archdescDidElementsMap.get(s))/new Double(archdescElementsMap.get("did"))));
            }
            counter++;
        }

        counter = 0;
        for (String s : cElementsMap.keySet()){
            if (counter==0) {
                printWriter.println("c elements;" + s+";"+cElementsMap.get(s)+";"+getPrettyPercentage(new Double(cElementsMap.get(s))/new Double(cCounter)));
            }
            else {
                printWriter.println(";" + s + ";" + cElementsMap.get(s)+";"+getPrettyPercentage(new Double(cElementsMap.get(s))/new Double(cCounter)));
            }
            counter++;
        }

        counter = 0;
        for (String s : cDidElementsMap.keySet()){
            if (counter==0) {
                printWriter.println("c did elements;" + s+";"+cDidElementsMap.get(s)+";"+getPrettyPercentage(new Double(cDidElementsMap.get(s))/new Double(cElementsMap.get("did"))));
            }
            else {
                printWriter.println(";" + s + ";" + cDidElementsMap.get(s)+";"+getPrettyPercentage(new Double(cDidElementsMap.get(s))/new Double(cElementsMap.get("did"))));
            }
            counter++;
        }

        counter = 0;
        for (String s : otherUnitIdTypes.keySet()){
            if (counter==0) {
                printWriter.println("other unitid types;" + s+";"+otherUnitIdTypes.get(s));
            }
            else {
                printWriter.println(";" + s + ";" + otherUnitIdTypes.get(s));
            }
            counter++;
        }

//        counter = 0;
//        for (String s : cLeavesElementsMap.keySet()){
//            if (counter==0) {
//                printWriter.println("c (leaves) elements;" + s+";"+cLeavesElementsMap.get(s));
//            }
//            else {
//                printWriter.println(";" + s + ";" + cLeavesElementsMap.get(s));
//            }
//            counter++;
//        }
//
//        counter = 0;
//        for (String s : cLeavesDidElementsMap.keySet()){
//            if (counter==0) {
//                printWriter.println("c (leaves) did elements;" + s+";"+cLeavesDidElementsMap.get(s));
//            }
//            else {
//                printWriter.println(";" + s + ";" + cLeavesDidElementsMap.get(s));
//            }
//            counter++;
//        }
//
//        counter = 0;
//        for (String s : otherUnitIdTypesForLeaves.keySet()){
//            if (counter==0) {
//                printWriter.println("other unitid types (leaves);" + s+";"+otherUnitIdTypesForLeaves.get(s));
//            }
//            else {
//                printWriter.println(";" + s + ";" + otherUnitIdTypesForLeaves.get(s));
//            }
//            counter++;
//        }




//        counter = 0;
//        for (String s : unitIdInfoMap.keySet()){
//            String trans = this.newMetricName(s);
//            if (trans!=null) {
//                if (counter == 0) {
//                    printWriter.println("unitid info;" + trans + ";" + unitIdInfoMap.get(s));
//                } else {
//                    printWriter.println(";" + trans + ";" + unitIdInfoMap.get(s));
//                }
//                counter++;
//            }
//        }

//        counter = 0;
//        for (String s : unitIdInfoMapForLeaves.keySet()){
//            String trans = this.newMetricName(s);
//            if (trans!=null) {
//                if (counter == 0) {
//                    printWriter.println("unitid info (leaves);" + trans + ";" + unitIdInfoMapForLeaves.get(s));
//                } else {
//                    printWriter.println(";" + trans + ";" + unitIdInfoMapForLeaves.get(s));
//                }
//                counter++;
//            }
//        }

        if (type.equals("total")) {
            counter = 0;
            String str = "";
            for (String s : perCountryInfoStatistics.keySet()) {
                Statistics statistics = perCountryInfoStatistics.get(s);
                if (statistics.totalFiles > 0) {
                    if (counter > 0) str += ";";
                    str += s+";";
                    counter++;
                }
            }
            printWriter.println("unitid info (per country + total);;"+str+";Total;");

            List<String> lines = new ArrayList<>();
            boolean firsttime = true;
            for (String ccode : perCountryInfoStatistics.keySet()) {
                counter = 0;
                Statistics statistics = perCountryInfoStatistics.get(ccode);
                if (statistics.totalFiles > 0) {
                    for (String s : statistics.unitIdInfoMap.keySet()) {
                        String trans = this.newMetricName(s);
                        if (trans != null) {
                            if (firsttime) {
                                lines.add(";" + trans + ";" + statistics.unitIdInfoMap.get(s)+";" + getPrettyPercentage(new Double(statistics.unitIdInfoMap.get(s)/new Double(statistics.cCounter))));
                            } else {
                                lines.set(counter, lines.get(counter) + ";" + statistics.unitIdInfoMap.get(s)+";" + getPrettyPercentage(new Double(statistics.unitIdInfoMap.get(s)/new Double(statistics.cCounter))));
                            }
//                        printWriter.println(";" + trans + ";" + unitIdInfoMap.get(s));

                        }
                        counter++;
                    }
                    firsttime = false;
                }
            }

            counter = 0;
            for (String s : unitIdInfoMap.keySet()){
                String trans = this.newMetricName(s);
                if (trans!=null) {
                    lines.set(counter, lines.get(counter) + ";" + unitIdInfoMap.get(s) + ";" + getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
                    counter++;
                }
            }

            for (String s : lines){
                printWriter.println(s);
            }
        }
        else if (type.equals("country")) {
            counter = 0;
            String str = "";
            for (String s : perInsitutionInfoStatistics.keySet()) {
                Statistics statistics = perInsitutionInfoStatistics.get(s);
                if (statistics.totalFiles > 0) {
                    if (counter > 0) str += ";";
                    str += s+";";
                    counter++;
                }
            }
            printWriter.println("unitid info (per inst + total);;"+str+";Total;");

            List<String> lines = new ArrayList<>();
            boolean firsttime = true;
            for (String ccode : perInsitutionInfoStatistics.keySet()) {
                counter = 0;
                Statistics statistics = perInsitutionInfoStatistics.get(ccode);
                if (statistics.totalFiles > 0) {
                    for (String s : statistics.unitIdInfoMap.keySet()) {
                        String trans = this.newMetricName(s);
                        if (trans != null) {
                            if (firsttime) {
                                lines.add(";" + trans + ";" + statistics.unitIdInfoMap.get(s)+";" + getPrettyPercentage(new Double(statistics.unitIdInfoMap.get(s)/new Double(statistics.cCounter))));
                            } else {
                                lines.set(counter, lines.get(counter) + ";" + statistics.unitIdInfoMap.get(s)+";" + getPrettyPercentage(new Double(statistics.unitIdInfoMap.get(s)/new Double(statistics.cCounter))));
                            }
//                        printWriter.println(";" + trans + ";" + unitIdInfoMap.get(s));

                        }
                        counter++;
                    }
                    firsttime = false;
                }
            }

            counter = 0;
            for (String s : unitIdInfoMap.keySet()){
                String trans = this.newMetricName(s);
                if (trans!=null) {
                    lines.set(counter, lines.get(counter) + ";" + unitIdInfoMap.get(s) + ";" + getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
                    counter++;
                }
            }

            for (String s : lines){
                printWriter.println(s);
            }
        }
        else if (type.equals("institution")) {
            counter = 0;
            for (String s : unitIdInfoMap.keySet()){
                String trans = this.newMetricName(s);
                if (trans!=null) {
                    if (counter == 0) {
                        printWriter.println("unitid info;" + trans + ";" + unitIdInfoMap.get(s) + ";" + getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
                    } else {
                        printWriter.println(";" + trans + ";" + unitIdInfoMap.get(s) + ";" + getPrettyPercentage(new Double(unitIdInfoMap.get(s)/new Double(cCounter))));
                    }
                    counter++;
                }
            }
        }

        printWriter.close();
    }

    private String newMetricName(String s){
        if (s.startsWith("metric")){
            if (s.equals("metric01")){
                return "01. at least one did/unitid";
            }
            else if (s.equals("metric02")){
                return "02. at least one did/unitid + @id";
            }
            else if (s.equals("metric03")){
                return "03. no did/unitid";
            }
            else if (s.equals("metric04")){
                return "04. no did/unitid + @id";
            }
            else if (s.equals("metric05")){
                return "05. at least one did/unitid/@type = \"call number\"";
            }
            else if (s.equals("metric06")){
                return "06. at least one did/unitid/@type = \"call number\" + @id";
            }
            else if (s.equals("metric07")){
                return "07. >=2 did/unitid/@type = \"call number\"";
            }
            else if (s.equals("metric08")){
                return "08. >=1 did/unitid/@type <> \"call number\"";
            }
            else if (s.equals("metric09")){
                return "09. >=1 did/unitid/@type = call number + >=1 did/unitid/@type <> \"call number\"";
            }
            else if (s.equals("metric10")){
                return "10. no did/unitid/@type = call number + >=1 did/unitid/@type <> \"call number\"";
            }
            else if (s.equals("metric11")){
                return "11. at least one did/unitid + no @type attribute at all";
            }
            else if (s.equals("metric12")){
                return "12. same as 11 plus at least one did/unitid@type=\"call number\"";
            }
            else if (s.equals("metric13")){
                return "13. same as 11 but no did/unitid@type=\"call number\"";
            }
            else if (s.equals("metric14")){
                return "14. with @id attribute";
            }
            else if (s.equals("metric15")){
                return "15. with @id attribute also include at least one did/unitid@type=\"call number\"";
            }
            else if (s.equals("metric16")) {
                return "16. with @id attribute also include at least one did/unitid (of any type or with no @type attribute at all)";
            }
            else if (s.equals("metric17")){
                return "17. no @id attribute";
            }
            else if (s.equals("metric18")){
                return "18. no @id attribute instead include at least one did/unitid (of any type or with no @type attribute at all)";
            }
            else if (s.equals("metric19")){
                return "19. no @id attribute neither include at least one did/unitid (of any type or with no @type attribute at all)";
            }
        }
        return null;
    }

    private String newMetricShortName(String s){
        if (s.startsWith("metric")){
            if (s.equals("metric01")){
                return "01";
            }
            else if (s.equals("metric02")){
                return "02";
            }
            else if (s.equals("metric03")){
                return "03";
            }
            else if (s.equals("metric04")){
                return "04";
            }
            else if (s.equals("metric05")){
                return "05";
            }
            else if (s.equals("metric06")){
                return "06";
            }
            else if (s.equals("metric07")){
                return "07";
            }
            else if (s.equals("metric08")){
                return "08";
            }
            else if (s.equals("metric09")){
                return "09";
            }
            else if (s.equals("metric10")){
                return "10";
            }
            else if (s.equals("metric11")){
                return "11";
            }
            else if (s.equals("metric12")){
                return "12";
            }
            else if (s.equals("metric13")){
                return "13";
            }
            else if (s.equals("metric14")){
                return "14";
            }
            else if (s.equals("metric15")){
                return "15";
            }
            else if (s.equals("metric16")) {
                return "16";
            }
            else if (s.equals("metric17")){
                return "17";
            }
            else if (s.equals("metric18")){
                return "18";
            }
            else if (s.equals("metric19")){
                return "19";
            }
        }
        return null;
    }

    private static String getFileSize(Double fileSize){
        Double fs;
        String type;
        if (fileSize  / (1024 * 1024 * 1024) > 1){
            fs = fileSize  / (1024 * 1024 * 1024);
            type="GB";
        }
        else if (fileSize  / (1024 * 1024) > 1){
            fs = fileSize  / (1024 * 1024);
            type="MB";
        }
        else {
            fs = fileSize  / (1024);
            type="KB";
        }

        Double fs2 = Math.floor(fs*100) / 100;

        return fs2 + " " + type;
    }

    private static String getPrettyPercentage(Double number){
        Double fs2 = Math.floor(number*10000) / 100;
        return fs2 + "%";
    }

    protected void fillHeaderCell(Table table, int col, int row, String stringValue) {
        table.getColumnByIndex(col).setUseOptimalWidth(true);
        Cell cell = table.getCellByPosition(col, row);
        Font font = cell.getFont();
        font.setFontStyle(StyleTypeDefinitions.FontStyle.BOLD);
//        font.setSize(10);
        cell.setFont(font);
//        cell.setDisplayText(stringValue);
        cell.setStringValue(stringValue);
    }

    protected void fillCell(Table table, int col, int row, String stringValue) {
        table.getColumnByIndex(col).setUseOptimalWidth(true);
        Cell cell = table.getCellByPosition(col, row);
        cell.setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.LEFT);
//        cell.setDisplayText(stringValue);
        cell.setStringValue(stringValue);
    }

    protected void fillCell(Table table, int col, int row, int stringValue) {
        Cell cell = table.getCellByPosition(col, row);
        cell.setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.LEFT);
        cell.setDisplayText(stringValue + "");
//        cell.setStringValue(stringValue + "");
    }

    protected void fillCell(Table table, int col, int row, long stringValue) {
        Cell cell = table.getCellByPosition(col, row);
        cell.setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.LEFT);
        cell.setDisplayText(stringValue + "");
//        cell.setStringValue(stringValue + "");
    }

    private List<String> findAllCountriesOrInstitutions(JsonObject jsonObject){
        List<String> response = new ArrayList<>();
        if (jsonObject.has("schema_locations_country")){
            JsonObject jsonObject1 = jsonObject.get("schema_locations_country").getAsJsonObject();
            response.addAll(jsonObject1.keySet());
            response.remove("total");
            return response;
        }
        else if (jsonObject.has("schema_locations_institution")){
            JsonObject jsonObject1 = jsonObject.get("schema_locations_institution").getAsJsonObject();
            response.addAll(jsonObject1.keySet());
            response.remove("total");
            return response;
        }
        return null;
    }
}
