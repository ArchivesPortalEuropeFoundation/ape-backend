package eu.apenet.commons.utils.analyzers.utils;

import eu.apenet.commons.utils.analyzers.eag.SocialInfoExtractor;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.EadContent;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class SocialUtils {

    public static int DESCRIPTION_MAX_LENGTH = 250;
    public static int KEYWORDS_MAX_LENGTH = 250;
    public static int TITLE_MAX_LENGTH = 250;
    public static int MAX_IMAGE_COUNT = 5;
    public static List<String> EAD_HASHTAGS = Arrays.asList(new String[]{"hashtag1","hashtag2"});
    public static List<String> EACCPF_HASHTAGS = Arrays.asList(new String[]{"hashtag1","hashtag2"});
    public static List<String> EAG_HASHTAGS = Arrays.asList(new String[]{"hashtag1","hashtag2"});
    public static String DEFAULT_TITLE = "No title given";
    public static String DEFAULT_EAD_DESCRIPTION = "To be decided with Marta";
    public static String DEFAULT_EACCPF_DESCRIPTION = "To be decided with Marta";
    public static String DEFAULT_EAG_DESCRIPTION = "To be decided with Marta";
    public static String DEFAULT_DAO_URL = "https://www.archivesportaleurope.net/assets/images/fb_img.jpg";

    public static Map<String, Object> getJsonInfoForEadContent(String repoPath, EadContent eadContent){

        Map map = null;
        if (eadContent != null) {
//            String newDir = getDir(repoPath, eadContent);
//            String finalPath = newDir + File.separator + "archdesc.json";
//
//            try {
//                return readFileInString(finalPath);
//            } catch (Exception e) {
////            e.printStackTrace();
//            }
            String metacontent = eadContent.getEad().getMetaContent();
            if (metacontent != null && metacontent.length()>0){
                try {
                    map = convertJsonToMap(metacontent);
                    //return (Map)map.get("archdesc");

                    if (map.containsKey("archdesc")) {
                        map = (Map) map.get("archdesc");
                    }
                    else {
                        map = null;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (map == null) {
            map = new HashMap<>();
        }

        if (!map.containsKey("title")) {
            String title = eadContent != null ? eadContent.getTitleproper() : null;
            if (title == null) {
                title = eadContent != null ? eadContent.getUnittitle() : null;
                if (title == null) {
                    title = SocialUtils.DEFAULT_TITLE;
                }
            }
            if (SocialUtils.TITLE_MAX_LENGTH > 0) {
                map.put("title", StringUtils.left(title, SocialUtils.TITLE_MAX_LENGTH));
            }
            else {
                map.put("title", title);
            }
        }
        if (!map.containsKey("description")) {
            if (SocialUtils.DESCRIPTION_MAX_LENGTH > 0) {
                map.put("description", StringUtils.left(SocialUtils.DEFAULT_EAD_DESCRIPTION, SocialUtils.DESCRIPTION_MAX_LENGTH));
            } else {
                map.put("description", SocialUtils.DEFAULT_EAD_DESCRIPTION);
            }
        }

        if (!map.containsKey("dao")) {
            List<String> daos = new ArrayList<>();
            daos.add(SocialUtils.DEFAULT_DAO_URL);
            map.put("dao", daos);
        }

        map.put("hashtags", SocialUtils.EAD_HASHTAGS);

        return map;
    }

    public static Map<String, Object> getJsonInfoForCLevel(String repoPath, CLevel cLevel){
        Map map = null;

        if (cLevel != null) {
            String path = cLevel.getOrderId() + "";

            EadContent eadContent = cLevel.getEadContent();
//            String newDir = getDir(repoPath, eadContent);
//
            CLevel cLevelIter = cLevel;
            while (cLevelIter.getParent() != null) {
                cLevelIter = cLevelIter.getParent();
                path = cLevelIter.getOrderId() + "." + path;
            }
//
//            String finalPath = newDir + File.separator + path + File.separator + "c.json";
//            try {
//                return readFileInString(finalPath);
//            } catch (Exception e) {
////            e.printStackTrace();
//            }

            String metacontent = eadContent.getEad().getMetaContent();
            if (metacontent != null && metacontent.length()>0){
                try {
                    map = convertJsonToMap(metacontent);
                    if (map.containsKey(path)) {
                        map = (Map) map.get(path);
                        //return (Map) map.get(path);
                    }
                    else {
                        map = null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (map == null) {
            map = new HashMap<>();
        }

        if (!map.containsKey("title")) {
            String title = cLevel != null ? cLevel.getUnittitle() : null;
            if (title == null) {
                title = SocialUtils.DEFAULT_TITLE;
            }
            if (SocialUtils.TITLE_MAX_LENGTH > 0) {
                map.put("title", StringUtils.left(title, SocialUtils.TITLE_MAX_LENGTH));
            }
            else {
                map.put("title", title);
            }
        }
        if (!map.containsKey("description")) {
            if (SocialUtils.DESCRIPTION_MAX_LENGTH > 0) {
                map.put("description", StringUtils.left(SocialUtils.DEFAULT_EAD_DESCRIPTION, SocialUtils.DESCRIPTION_MAX_LENGTH));
            } else {
                map.put("description", SocialUtils.DEFAULT_EAD_DESCRIPTION);
            }
        }

        if (!map.containsKey("dao")) {
            List<String> daos = new ArrayList<>();
            daos.add(SocialUtils.DEFAULT_DAO_URL);
            map.put("dao", daos);
        }

        map.put("hashtags", SocialUtils.EAD_HASHTAGS);

        return map;
    }

    public static Map<String, Object> getJsonInfoForEag(ArchivalInstitution archivalInstitution){
        Map map = null;

        if (archivalInstitution != null){
            SocialInfoExtractor socialInfoExtractor = new SocialInfoExtractor();
            try {
                map = socialInfoExtractor.extractInfo(archivalInstitution);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (map == null) {
            map = new HashMap<>();
        }
        if (!map.containsKey("title")) {
            if (SocialUtils.TITLE_MAX_LENGTH > 0) {
                map.put("title", StringUtils.left(SocialUtils.DEFAULT_TITLE, SocialUtils.TITLE_MAX_LENGTH));
            }
            else {
                map.put("title", SocialUtils.DEFAULT_TITLE);
            }
        }

        if (!map.containsKey("description")) {
            if (SocialUtils.DESCRIPTION_MAX_LENGTH > 0) {
                map.put("description", StringUtils.left(SocialUtils.DEFAULT_EAG_DESCRIPTION, SocialUtils.DESCRIPTION_MAX_LENGTH));
            } else {
                map.put("description", SocialUtils.DEFAULT_EAG_DESCRIPTION);
            }
        }

        map.put("hashtags", SocialUtils.EAG_HASHTAGS);

        return map;
    }

    public static Map<String, Object> getJsonInfoForEacCpf(String repoPath, EacCpf eacCpf){

        Map map = null;

//        if (eacCpf != null) {
//            String newDir = getDir(repoPath, eacCpf);
//            String finalPath = newDir + File.separator + "eaccpf.json";
//
//            try {
//                return readFileInString(finalPath);
//            } catch (Exception e) {
////            e.printStackTrace();
//            }
//        }
        if (eacCpf != null && eacCpf.getMetaContent() != null){
            try {
                map = convertJsonToMap(eacCpf.getMetaContent());
                return map;
            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }

//        Map<String, Object> jsonMap = new HashMap<>();
//        String title = eacCpf!=null?eacCpf.getTitle():null;
//        if (title == null) {
//            title = DEFAULT_TITLE;
//        }
//        jsonMap.put("title", title);
//        jsonMap.put("title-short", StringUtils.left(title, TITLE_MAX_LENGTH));
//        jsonMap.put("description", DEFAULT_EACCPF_DESCRIPTION);
//        jsonMap.put("description-short", StringUtils.left(DEFAULT_EACCPF_DESCRIPTION, DESCRIPTION_MAX_LENGTH));
//        if (eacCpf != null) {
//            jsonMap.put("publisher", eacCpf.getArchivalInstitution().getAutform());
//        }
//        jsonMap.put("hashtags", EACCPF_HASHTAGS);
//        return jsonMap;


        if (map == null) {
            map = new HashMap<>();
        }

        if (!map.containsKey("title")) {
            String title = eacCpf!=null?eacCpf.getTitle():null;
            if (title == null) {
                title = SocialUtils.DEFAULT_TITLE;
            }
            if (SocialUtils.TITLE_MAX_LENGTH > 0) {
                map.put("title", StringUtils.left(title, SocialUtils.TITLE_MAX_LENGTH));
            }
            else {
                map.put("title", title);
            }
        }
        if (!map.containsKey("description")) {
            if (SocialUtils.DESCRIPTION_MAX_LENGTH > 0) {
                map.put("description", StringUtils.left(SocialUtils.DEFAULT_EACCPF_DESCRIPTION, SocialUtils.DESCRIPTION_MAX_LENGTH));
            } else {
                map.put("description", SocialUtils.DEFAULT_EACCPF_DESCRIPTION);
            }
        }
        if (!map.containsKey("publisher")) {
            if (eacCpf != null) {
                map.put("publisher", eacCpf.getArchivalInstitution().getAutform());
            }
        }

        map.put("hashtags", SocialUtils.EACCPF_HASHTAGS);
        return map;
    }

    private static String getDir(String repoPath, EadContent eadContent){
        String localPath = eadContent.getEad().getPath();
        String fullPath = repoPath + localPath;
        File eadXmlFile = new File(fullPath);
        String temp = eadXmlFile.getParent();
        temp = temp.replace("/repo/", "/social/");
        String newDir = temp+File.separator+eadContent.getEcId();

        return newDir;
    }

    private static String getDir(String repoPath, EacCpf eacCpf){
        String localPath = eacCpf.getPath();
        String fullPath = repoPath + localPath;
        File eadXmlFile = new File(fullPath);
        String temp = eadXmlFile.getParent();
        temp = temp.replace("/repo/", "/social/");
        String newDir = temp+File.separator+eacCpf.getId();

        return newDir;
    }

    private static Map<String, Object> readFileInString(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), "UTF-8"));

        String str;
        String content = "";
        while ((str = br.readLine()) != null) {
            content += str;
        }

        return convertJsonToMap(content);
    }

    public static Map<String, Object> convertJsonToMap(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> map = new HashMap<>();
        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            Object valueObj = jsonObject.get(key);
            if (valueObj instanceof JSONObject) {
                JSONObject value = (JSONObject) jsonObject.get(key);
                Map newMap = new HashMap();
                Iterator iterator2 = value.keys();
                while (iterator2.hasNext()) {
                    String key2 = (String) iterator2.next();
                    newMap.put(key2, value.get(key2));
                }
                map.put(key, newMap);
            }
            else {
                map.put(key, valueObj);
            }
        }
        return map;
    }
}
