package eu.apenet.commons.utils.analyzers.ead;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.analyzers.utils.SocialUtils;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EadContent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.noggit.CharArr;
import org.noggit.JSONWriter;

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

public class SocialInfoExtractor {

    private boolean writeToFile = false;

    public String extractStringInfo(EadContent eadContent){
        String repoPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
        return extractStringInfo(repoPath, eadContent);
    }

    public String extractStringInfo(String repoPath, EadContent eadContent) {
        Map map = extractInfo(repoPath, eadContent);
        if (map.size() == 0) return null;

        CharArr out = new CharArr();
        JSONWriter jsonWriter = new JSONWriter(out);
        jsonWriter.setIndentSize(-1);
        jsonWriter.write(map);
        String s = out.toString();

        return s;
    }

    public Map extractInfo(EadContent eadContent) {
        String repoPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
        return extractInfo(repoPath, eadContent);
    }

    public Map extractInfo(String repoPath, EadContent eadContent) {

        Map response = new HashMap();

//        CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
//        CLevel cLevel = cLevelDAO.findById((long)97351);
//        String jsonContents = SocialUtils.getJsonInfoForCLevel(repoPath, cLevel);

        //Get the publisher from the database
        ArchivalInstitution archivalInstitution = null;
        if (eadContent.getFindingAid() != null) {
            archivalInstitution = eadContent.getFindingAid().getArchivalInstitution();
        } else if (eadContent.getHoldingsGuide() != null) {
            archivalInstitution = eadContent.getHoldingsGuide().getArchivalInstitution();
        } else if (eadContent.getSourceGuide() != null) {
            archivalInstitution = eadContent.getSourceGuide().getArchivalInstitution();
        }
        String publisher = archivalInstitution.getAiname();


        String localPath = eadContent.getEad().getPath();
        String fullPath = repoPath + localPath;
        File eadXmlFile = new File(fullPath);

        String temp = eadXmlFile.getParent();
        temp = temp.replace("/repo/", "/social/");
        String newDir = temp + File.separator + eadContent.getEcId();
        if (writeToFile) {
            File newDirFile = new File((newDir));
            if (newDirFile.exists()) {
                try {
                    FileUtils.deleteDirectory(newDirFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            newDirFile.mkdirs();
        }

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = null;
        try {
            reader = xmlInputFactory.createXMLEventReader(new FileInputStream(eadXmlFile.getAbsoluteFile()));

            if (reader != null) {
                CInfo arcDescInfo = new CInfo();
                arcDescInfo.defaultTitle = eadContent.getUnittitle();
                if (eadContent.getUnittitle() == null) {
                    arcDescInfo.defaultTitle = eadContent.getTitleproper();
                }
                arcDescInfo.publisher = publisher;
                CInfo currentCInfo = null;
                Stack<CInfo> stack = new Stack<>();

                boolean inArchDesc = false;
                boolean inCUnitTitle = false;
                boolean inArcDescUnitTitle = false;
                boolean inArcDescTitleProper = false;
                Map<String, Integer> orderMap = new HashMap<>();
                boolean inScopeContent = false;
                boolean inScopeContentP = false;
                String pContent = "";
                boolean inControlAccess = false;
                boolean inSubject = false;
                boolean inCorpname = false;
                boolean inPersname = false;
                boolean inGeoname = false;
                boolean inFamname = false;
                boolean inName = false;

                while (reader.hasNext()) {
                    XMLEvent nextEvent = reader.nextEvent();
                    if (nextEvent.isStartElement()) {
                        StartElement startElement = nextEvent.asStartElement();

                        if (startElement.getName().getLocalPart().equals("archdesc")) {
                            inArchDesc = true;
                        } else if (startElement.getName().getLocalPart().equals("c")) {
                            CInfo cInfo = new CInfo();
                            cInfo.publisher = publisher;

                            //calculate order
                            String whatToCheck = null;
                            if (!stack.empty()) {
                                CInfo cInfo1 = stack.peek();
                                whatToCheck = cInfo1.order;
                            }
                            int order = 0;
                            if (orderMap.containsKey(whatToCheck)) {
                                order = orderMap.get(whatToCheck);
                            }
                            orderMap.put(whatToCheck, order + 1);
                            cInfo.order = (whatToCheck == null ? ("" + order) : (whatToCheck + "." + order));
                            currentCInfo = stack.push(cInfo);
                        } else if (startElement.getName().getLocalPart().equals("unittitle")) {
                            if (!stack.empty()) {
                                inCUnitTitle = true;
                            } else if (inArchDesc) {
                                inArcDescUnitTitle = true;
                            }
                        } else if (startElement.getName().getLocalPart().equals("titleproper")) {
                            inArcDescTitleProper = true;
                        } else if (startElement.getName().getLocalPart().equals("scopecontent")) {
                            inScopeContent = true;
                        } else if (startElement.getName().getLocalPart().equals("p")) {
                            if (inScopeContent) {
                                inScopeContentP = true;
                            }
                        } else if (startElement.getName().getLocalPart().equals("dao")) {
                            if (currentCInfo != null) {
                                Attribute titleAttribute = startElement.getAttributeByName(new QName("http://www.w3.org/1999/xlink", "title", "xlink"));
                                Attribute hrefAttribute = startElement.getAttributeByName(new QName("http://www.w3.org/1999/xlink", "href", "xlink"));
                                if (hrefAttribute != null && titleAttribute != null) {
                                    currentCInfo.addDao(titleAttribute.getValue(), hrefAttribute.getValue());
                                } else if (hrefAttribute != null) {
                                    currentCInfo.addDao("null", hrefAttribute.getValue());
                                }
                            }
                        } else if (startElement.getName().getLocalPart().equals("controlaccess")) {
                            inControlAccess = true;
                        } else if (startElement.getName().getLocalPart().equals("subject")) {
                            if (inControlAccess) {
                                inSubject = true;
                            }
                        } else if (startElement.getName().getLocalPart().equals("corpname")) {
                            if (inControlAccess) {
                                inCorpname = true;
                            }
                        } else if (startElement.getName().getLocalPart().equals("geoname")) {
                            if (inControlAccess) {
                                inGeoname = true;
                            }
                        } else if (startElement.getName().getLocalPart().equals("persname")) {
                            if (inControlAccess) {
                                inPersname = true;
                            }
                        } else if (startElement.getName().getLocalPart().equals("famname")) {
                            if (inControlAccess) {
                                inFamname = true;
                            }
                        } else if (startElement.getName().getLocalPart().equals("name")) {
                            if (inControlAccess) {
                                inName = true;
                            }
                        }
                    } else if (nextEvent.isEndElement()) {
                        EndElement endElement = nextEvent.asEndElement();

                        if (endElement.getName().getLocalPart().equals("c")) {
                            currentCInfo = stack.pop();

                            //Fill empty fields using the parent(s)
                            fillCInfoEmptyFields(currentCInfo, stack, arcDescInfo);

                            Map jsonMap = writeCInfo(newDir, currentCInfo, writeToFile);
                            if (jsonMap != null) {
                                response.put(currentCInfo.order, jsonMap);
                            }
                        } else if (endElement.getName().getLocalPart().equals("archdesc")) {
                            inArchDesc = false;
                            Map jsonMap = writeArcDescInfo(newDir, arcDescInfo, writeToFile);
                            if (jsonMap != null) {
                                response.put("archdesc", jsonMap);
                            }
                        } else if (endElement.getName().getLocalPart().equals("unittitle")) {
                            if (!stack.empty()) {
                                inCUnitTitle = false;
                            } else if (inArcDescUnitTitle) {
                                inArcDescUnitTitle = false;
                            }
                        } else if (endElement.getName().getLocalPart().equals("titleproper")) {
                            if (inArcDescTitleProper) {
                                inArcDescTitleProper = false;
                            }
                        } else if (endElement.getName().getLocalPart().equals("p")) {
                            if (inScopeContentP) {
                                if (currentCInfo != null) {
                                    currentCInfo.addScopeContent(pContent);
                                } else {
                                    arcDescInfo.addScopeContent(pContent);
                                }
                                inScopeContentP = false;
                                pContent = "";
                            }
                        } else if (endElement.getName().getLocalPart().equals("scopecontent")) {
                            inScopeContent = false;
                        } else if (endElement.getName().getLocalPart().equals("controlaccess")) {
                            inControlAccess = false;
                        } else if (endElement.getName().getLocalPart().equals("subject")) {
                            if (inControlAccess) {
                                inSubject = false;
                            }
                        } else if (endElement.getName().getLocalPart().equals("corpname")) {
                            if (inControlAccess) {
                                inCorpname = false;
                            }
                        } else if (endElement.getName().getLocalPart().equals("persname")) {
                            if (inControlAccess) {
                                inPersname = false;
                            }
                        } else if (endElement.getName().getLocalPart().equals("geoname")) {
                            if (inControlAccess) {
                                inGeoname = false;
                            }
                        } else if (endElement.getName().getLocalPart().equals("famname")) {
                            if (inControlAccess) {
                                inFamname = false;
                            }
                        } else if (endElement.getName().getLocalPart().equals("name")) {
                            if (inControlAccess) {
                                inName = false;
                            }
                        }
                    } else if (nextEvent.isCharacters()) {
                        if (inCUnitTitle) {
                            currentCInfo.addUnitTitle(nextEvent.asCharacters().getData());
                        } else if (inArcDescUnitTitle) {
                            arcDescInfo.addUnitTitle(nextEvent.asCharacters().getData());
                        } else if (inArcDescTitleProper) {
                            arcDescInfo.titleProper = nextEvent.asCharacters().getData();
                        } else if (inScopeContentP) {
                            pContent += " " + nextEvent.asCharacters().getData();
                        } else if (inSubject || inCorpname || inPersname || inFamname || inGeoname || inName) {
                            String type = null;
                            if (inSubject) type = "subject";
                            if (inCorpname) type = "corpname";
                            if (inPersname) type = "persname";
                            if (inFamname) type = "famname";
                            if (inGeoname) type = "geoname";
                            if (inName) type = "name";

                            if (currentCInfo != null) {
                                currentCInfo.addKeyword(type, nextEvent.asCharacters().getData());
                            } else {
                                arcDescInfo.addKeyword(type, nextEvent.asCharacters().getData());
                            }
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Map writeArcDescInfo(String path, CInfo info, boolean writeToFile){
        CharArr out = new CharArr();
        JSONWriter jsonWriter = new JSONWriter(out);
        Map map = null;
        try {
            map = info.getMap();
            jsonWriter.write(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = out.toString();

        if (writeToFile) {
            try {
                FileWriter myWriter = new FileWriter(path + File.separator + "archdesc.json");
                myWriter.write(s);
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    private Map writeCInfo(String path, CInfo info, boolean writeToFile){
        CharArr out = new CharArr();
        JSONWriter jsonWriter = new JSONWriter(out);
        Map map = null;
        try {
            map = info.getMap();
            jsonWriter.write(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = out.toString();

        if (writeToFile) {
            try {
                String cPath = info.order.replace(".", File.separator);
                (new File(path + File.separator + cPath)).mkdirs();
                FileWriter myWriter = new FileWriter(path + File.separator + cPath + File.separator + "c.json");
                myWriter.write(s);
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    private void fillCInfoEmptyFields(CInfo cInfo, Stack<CInfo> stack, CInfo arcDescInfo){
        //Add parent(s) title in case no unittitle exists
        if (cInfo.unitTitles == null || cInfo.unitTitles.size() == 0){
            if (stack.empty()){
                String titleToAdd = null;
                if (arcDescInfo.unitTitles != null && arcDescInfo.unitTitles.size() > 0){
                    titleToAdd = arcDescInfo.unitTitles.get(0);
                }
                else if (arcDescInfo.titleProper != null){
                    titleToAdd = arcDescInfo.titleProper;
                }
                else {
                    //All archival description should have a title!!
                    if (arcDescInfo.defaultTitle != null) {
                        titleToAdd = arcDescInfo.defaultTitle;
                    }
                }
                if (titleToAdd != null) {
                    cInfo.addUnitTitle(titleToAdd);
                    cInfo.isTitleFromParent = true;
                }
            }
            else {
                boolean shouldStop = false;
                List<CInfo> poppedInfos = new ArrayList<>();
                while (!shouldStop){
                    CInfo parentCInfo = stack.pop();
                    poppedInfos.add(parentCInfo);
                    if (parentCInfo.unitTitles != null && parentCInfo.unitTitles.size() > 0){
                        String titleToAdd = parentCInfo.unitTitles.get(0);
                        cInfo.addUnitTitle(titleToAdd);
                        cInfo.isTitleFromParent = true;
                        shouldStop = true;
                    }
                    else {
                        if (stack.empty()){
                            String titleToAdd = null;
                            if (arcDescInfo.unitTitles != null && arcDescInfo.unitTitles.size() > 0){
                                titleToAdd = arcDescInfo.unitTitles.get(0);
                            }
                            else if (arcDescInfo.titleProper != null){
                                titleToAdd = arcDescInfo.titleProper;
                            }
                            else {
                                //All archival description should have a title!!
                                if (arcDescInfo.defaultTitle != null) {
                                    titleToAdd = arcDescInfo.defaultTitle;
                                }
                            }
                            if (titleToAdd != null) {
                                cInfo.addUnitTitle(titleToAdd);
                                cInfo.isTitleFromParent = true;
                            }
                            shouldStop = true;
                        }
                    }
                }

                //Recover the poppedInfos
                for (int i = poppedInfos.size()-1; i>=0; i--){
                    stack.push(poppedInfos.get(i));
                }
            }
        }

        if (cInfo.daos != null && cInfo.daos.size() > 0){
            if (!stack.empty()) {
                boolean shouldStop = false;
                List<CInfo> poppedInfos = new ArrayList<>();
                while (!shouldStop) {
                    CInfo parentCInfo = stack.pop();
                    poppedInfos.add(parentCInfo);
                    if (parentCInfo.daos == null || parentCInfo.daos.size() == 0) {
                        parentCInfo.daos = cInfo.daos;
                        parentCInfo.isDaosFromChildren = true;
                    }
                    if (stack.empty()){
                        shouldStop = true;
                    }
                }

                //Recover the poppedInfos
                for (int i = poppedInfos.size() - 1; i >= 0; i--) {
                    stack.push(poppedInfos.get(i));
                }
            }

            if (arcDescInfo.daos == null || arcDescInfo.daos.size() == 0) {
                arcDescInfo.daos = cInfo.daos;
                arcDescInfo.isDaosFromChildren = true;
            }
        }
    }

    private class CInfo {
        public String defaultTitle;
        public List<String> unitTitles;
        public String titleProper;
        public List<String> scopeContents;
        public List<String> subjects = new ArrayList<>();
        public List<String> corpnames = new ArrayList<>();
        public List<String> persnames = new ArrayList<>();
        public List<String> famnames = new ArrayList<>();
        public List<String> names = new ArrayList<>();
        public List<String> geonames = new ArrayList<>();

        public Map<String, List<String>> daos;
        public boolean isDaosFromChildren = false;

        public String publisher;

        public boolean isTitleFromParent = false;
        public String order;

        public void addUnitTitle(String unitTitle){
            if (unitTitles == null){
                unitTitles = new ArrayList<>();
            }
            unitTitles.add(unitTitle);
        }

        public void addScopeContent(String scopeContent){
            if (scopeContents == null){
                scopeContents = new ArrayList<>();
            }
            scopeContents.add(scopeContent);
        }

        public void addDao(String title, String href){
            if (daos == null){
                daos = new HashMap<>();
            }
            if (!daos.containsKey(title)){
                daos.put(title, new ArrayList<>());
            }

            daos.get(title).add(href);
        }

        public void addKeyword(String type, String keyword){
            List<String> keywords = null;
            if (type.equals("subject")){
                keywords = subjects;
            }
            else if (type.equals("corpname")){
                keywords = corpnames;
            }
            else if (type.equals("persname")){
                keywords = persnames;
            }
            else if (type.equals("famname")){
                keywords = famnames;
            }
            else if (type.equals("name")){
                keywords = names;
            }
            else if (type.equals("geoname")){
                keywords = geonames;
            }

            if (keywords == null){
                keywords = new ArrayList<>();
            }
            keywords.add(keyword);
        }

        public Map getMap() {
            Map response = new HashMap<>();

            //Title
            String title = null;
            if (unitTitles != null && unitTitles.size() > 0) {
                String prefix = "";
                if (isTitleFromParent){
                    prefix = "Part of: ";
                }
                title = prefix + unitTitles.get(0);
            }
            else if (titleProper!=null){
                title = titleProper;
            }
            else {
                if (defaultTitle != null) {
                    title = defaultTitle;
                }
//                else {
//                    title = SocialUtils.DEFAULT_TITLE;
//                }
            }
            if (title != null) {
                if (SocialUtils.TITLE_MAX_LENGTH > 0) {
                    response.put("title", StringUtils.left(title, SocialUtils.TITLE_MAX_LENGTH));
                }
                else {
                    response.put("title", title);
                }
            }


            //ScopeContent
            String description = null;
            if (scopeContents != null && scopeContents.size() > 0) {
                description = scopeContents.get(0);
            }
//            else {
//                description = SocialUtils.DEFAULT_EAD_DESCRIPTION;
//            }
            if (description != null) {
                if (SocialUtils.DESCRIPTION_MAX_LENGTH > 0) {
                    response.put("description", StringUtils.left(description, SocialUtils.DESCRIPTION_MAX_LENGTH));
                }
                else {
                    response.put("description", description);
                }
            }

            //Daos
            if (daos != null && daos.size() > 0) {
                List<String> finalDaos = new ArrayList<>();
                loop: for (String key : daos.keySet()){
                    if (!key.equals("thumbnail") && !key.equals("manifest") && !key.equals("service")){
                        for (String dao : daos.get(key)){
                            if (dao.endsWith(".png") || dao.endsWith(".jpg") || dao.endsWith(".jpeg") || dao.endsWith(".gif")){
                                finalDaos.add(dao);
                            }
                        }
                        break loop;
                    }
                }
                if (finalDaos.size() == 0 && daos.containsKey("thumbnail")) {
                    for (String dao : daos.get("thumbnail")){
                        finalDaos.add(dao);
                    }
                }

                if (finalDaos.size() > 0) {
                    response.put("dao", finalDaos);
                }
            }

            //Publisher
            if (publisher != null){
                response.put("publisher", publisher);
            }

            //Keywords
            String keywords = "";
            String keywordsShort = "";
            boolean limitReached = false;

            List<List<String>> allKeywordLists = new ArrayList<>();
            if (subjects!=null) allKeywordLists.add(subjects);
            if (corpnames!=null) allKeywordLists.add(corpnames);
            if (persnames!=null) allKeywordLists.add(persnames);
            if (famnames!=null) allKeywordLists.add(famnames);
            if (geonames!=null) allKeywordLists.add(geonames);
            if (names!=null) allKeywordLists.add(names);

            for (List<String> keywordList : allKeywordLists) {
                for (String s : keywordList) {
                    keywords += ((keywords.length() != 0) ? ", " : "") + s;

                    if (!limitReached) {
                        keywordsShort = keywords;
                        if (keywordsShort.length() > SocialUtils.KEYWORDS_MAX_LENGTH) {
                            limitReached = true;
                        }
                    }
                }
            }

            if (keywords.length() > 0) {
                if (SocialUtils.KEYWORDS_MAX_LENGTH > 0) {
                    response.put("keywords", keywordsShort);
                }
                else {
                    response.put("keywords", keywords);
                }
            }

//            response.put("hashtags", SocialUtils.EAD_HASHTAGS);

            return response;
        }
    }
}
