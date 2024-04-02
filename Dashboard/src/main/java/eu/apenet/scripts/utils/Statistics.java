package eu.apenet.scripts.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    public Map<String, Map<String, Double>> perCountryInfo = new HashMap<>();
    public Map<String, Map<String, Double>> perInsitutionInfo = new HashMap<>();

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
    public Map<String, List<String>> schemaLocationsMap = new HashMap<>();
    public int cCounter = 0;
    public int cCounter2 = 0;
    public int cCounterLeaves = 0;

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
        else if (type.equals("institutions")) {
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
}
