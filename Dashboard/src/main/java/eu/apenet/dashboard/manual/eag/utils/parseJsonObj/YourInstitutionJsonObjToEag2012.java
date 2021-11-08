package eu.apenet.dashboard.manual.eag.utils.parseJsonObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.apenet.dashboard.manual.eag.WebFormEAG2012Action;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.RightsInformation;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.apenet.dashboard.manual.eag.Eag2012;

/**
 * Class for Fill a {@link Eag2012} eag2012 object got from params, fill it with
 * the information provided into {@link JSONObject} JSONObject (got from params)
 * in part of your institution.
 */
public class YourInstitutionJsonObjToEag2012 extends AbstractJsonObjtoEag2012 implements JsonObjToEag2012 {

    private final Logger log = Logger.getLogger(getClass());
    private Eag2012 eag2012;
    private JSONObject yourInstitution;
    private int i;
    private int targetNumber = 1;
    private String target1 = null;

    /**
     * Method that checks if it have to create if the object  <br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of
     * DataEmail
     *
     * @param target {@link String} the target
     * @param key {@link String} the key
     */
    private void parseDataEmail(String target, String key) throws JSONException {
        this.log.debug("Method start: \"parseDataEmail\"");
        targetNumber = 1;
        target1 = target;
        do {
            if (yourInstitution.has(target1)) {
                List<Map<String, Map<String, List<String>>>> listMapEmailList = null;
                if (key.equals("EmailHref")) {
                    listMapEmailList = eag2012.getEmailHref();
                } else if (key.equals("EmailValue")) {
                    listMapEmailList = eag2012.getEmailValue();
                } else if (key.equals("EmailLang")) {
                    listMapEmailList = eag2012.getEmailLang();
                }
                listMapEmailList = createListMapList(listMapEmailList, Eag2012.TAB_YOUR_INSTITUTION, Eag2012.ROOT, yourInstitution.getString(target1), 0, 0, 0, 0);
                if (key.equals("EmailHref")) {
                    eag2012.setEmailHref(listMapEmailList);
                } else if (key.equals("EmailValue")) {
                    eag2012.setEmailValue(listMapEmailList);
                } else if (key.equals("EmailLang")) {
                    eag2012.setEmailLang(listMapEmailList);
                }
            }
            targetNumber++;
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
        } while (yourInstitution.has(target1));
        this.log.debug("End method: \"parseDataEmail\"");
    }

    /**
     * Method that checks if it have to create if the object<br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of DataWeb
     *
     * @param target {@link String} the target
     * @param key {@link String} the key
     */
    private void parseDataWeb(String target, String key) throws JSONException {
        this.log.debug("Method start: \"parseDataWeb\"");
        targetNumber = 1;
        target1 = target;
        do {
            if (yourInstitution.has(target1)) {
                List<Map<String, Map<String, List<String>>>> listMapWebList = null;
                if (key.equals("WebpageHref")) {
                    listMapWebList = eag2012.getWebpageHref();
                } else if (key.equals("WebpageValue")) {
                    listMapWebList = eag2012.getWebpageValue();
                } else if (key.equals("WebpageLang")) {
                    listMapWebList = eag2012.getWebpageLang();
                }
                listMapWebList = createListMapList(listMapWebList, Eag2012.TAB_YOUR_INSTITUTION, Eag2012.ROOT, yourInstitution.getString(target1), 0, 0, 0, 0);
                if (key.equals("WebpageHref")) {
                    eag2012.setWebpageHref(listMapWebList);
                } else if (key.equals("WebpageValue")) {
                    eag2012.setWebpageValue(listMapWebList);
                } else if (key.equals("WebpageLang")) {
                    eag2012.setWebpageLang(listMapWebList);
                }
            }
            targetNumber++;
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
        } while (yourInstitution.has(target1));
        this.log.debug("End method: \"parseDataWeb\"");
    }

    /**
     * Method that checks if it have to create if the object <br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of
     * DataOpening
     *
     * @param target {@link String} the target
     * @param key {@link String} the key
     */
    private void parseDataOpening(String target, String key) throws JSONException {
        this.log.debug("Method start: \"parseDataOpening\"");
        targetNumber = 1;
        target1 = target;
        do {
            if (yourInstitution.has(target1)) {
                List<Map<String, List<String>>> listListOpening = null;
                if (key.equals("OpeningValue")) {
                    listListOpening = eag2012.getOpeningValue();
                } else if (key.equals("OpeningLang")) {
                    listListOpening = eag2012.getOpeningLang();
                } else if (key.equals("OpeningHref")) {
                    listListOpening = eag2012.getOpeningHref();
                } else if (key.equals("closingValues")) {
                    listListOpening = eag2012.getClosingStandardDate();
                } else if (key.equals("ClosingLang")) {
                    listListOpening = eag2012.getClosingLang();
                }
                listListOpening = createData(listListOpening, Eag2012.TAB_YOUR_INSTITUTION, yourInstitution.getString(target1), 0, 0, 0);
                if (key.equals("OpeningValue")) {
                    eag2012.setOpeningValue(listListOpening);
                } else if (key.equals("OpeningLang")) {
                    eag2012.setOpeningLang(listListOpening);
                } else if (key.equals("OpeningHref")) {
                    eag2012.setOpeningHref(listListOpening);
                } else if (key.equals("closingValues")) {
                    eag2012.setClosingStandardDate(listListOpening);
                } else if (key.equals("ClosingLang")) {
                    eag2012.setClosingLang(listListOpening);
                }
            }
            targetNumber++;
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
        } while (yourInstitution.has(target1));
        this.log.debug("End method: \"parseDataOpening\"");
    }

    /**
     * Method that checks if it have to create if the object<br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of
     * DataAccess
     *
     * @param target {
     * @linkString} the target
     * @param key {@link String} the key
     */
    private void parseDataAccess(String target, String key) throws JSONException {
        this.log.debug("Method start: \"parseDataAccess\"");
        targetNumber = 1;
        target1 = target;
        do {
            if (yourInstitution.has(target1)) {
                List<Map<String, List<String>>> listListAccess = null;
                if (key.equals("restAccessValue")) {
                    listListAccess = eag2012.getRestaccessValue();
                } else if (key.equals("restAccessLang")) {
                    listListAccess = eag2012.getRestaccessLang();
                }
                listListAccess = createData(listListAccess, Eag2012.TAB_YOUR_INSTITUTION, yourInstitution.getString(target1), 0, 0, 0);
                if (key.equals("restAccessValue")) {
                    eag2012.setRestaccessValue(listListAccess);
                } else if (key.equals("restAccessLang")) {
                    eag2012.setRestaccessLang(listListAccess);
                }
            }
            targetNumber++;
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
        } while (yourInstitution.has(target1));
        this.log.debug("End method: \"parseDataAccess\"");
    }

    /**
     * Method that checks if it have to create if the object<br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of
     * DataFacilities
     *
     * @param target {@link String} the target
     * @param key {@link String} the key
     */
    private void parseDataFacilities(String target, String key) throws JSONException {
        this.log.debug("Method start: \"parseDataFacilities\"");
        targetNumber = 1;
        target1 = target;
        do {
            if (yourInstitution.has(target1)) {
                if (target.equals("textReferencetoyourinstitutionsholdingsguide")) {
                    Map<String, List<String>> resourceRelationHref = eag2012.getResourceRelationHref();
                    if (resourceRelationHref == null) {
                        resourceRelationHref = new HashMap<String, List<String>>();
                    }
                    List<String> resourceList = null;
                    resourceList = createList(resourceRelationHref, resourceList, Eag2012.TAB_YOUR_INSTITUTION, 0);
                    resourceList.add(replaceIfExistsSpecialReturnString(yourInstitution.getString(target1)));
                    resourceRelationHref.put(Eag2012.TAB_YOUR_INSTITUTION, resourceList);
                    eag2012.setResourceRelationHref(resourceRelationHref);
                } else {
                    List<Map<String, List<String>>> listListFacilities = null;
                    if (key.equals("AccessibilityValue")) {
                        listListFacilities = eag2012.getAccessibilityValue();
                    } else if (key.equals("AccessibilityLang")) {
                        listListFacilities = eag2012.getAccessibilityLang();
                    }
                    listListFacilities = createData(listListFacilities, Eag2012.TAB_YOUR_INSTITUTION, yourInstitution.getString(target1), 0, 0, 0);
                    if (key.equals("AccessibilityValue")) {
                        eag2012.setAccessibilityValue(listListFacilities);
                    } else if (key.equals("AccessibilityLang")) {
                        eag2012.setAccessibilityLang(listListFacilities);
                    }
                }
            }
            targetNumber++;
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
        } while (yourInstitution.has(target1));
        this.log.debug("End method: \"parseDataFacilities\"");
    }

    /**
     * Method that checks if it have to create if the object<br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of
     * visitorAddress
     *
     * @param visitorAddress {@link JSONObject} the visitorAddress
     */
    private void parseVisitorAddressYour(JSONObject visitorAddress) throws JSONException {
        this.log.debug("Method start: \"parseVisitorAddressYour\"");
        if (visitorAddress != null) {
            //visitorAddress
            List<String> listStreets = new ArrayList<String>();
            List<String> listLangStreets = new ArrayList<String>();
            List<String> listCities = new ArrayList<String>();
            List<String> listCountries = new ArrayList<String>();
            List<String> listLatitudes = new ArrayList<String>();
            List<String> listLongitudes = new ArrayList<String>();
            if (visitorAddress.length() > 0) {
                for (int i = 0; i < visitorAddress.length(); i++) {
                    JSONObject visitorAddressTable = visitorAddress.getJSONObject("yiTableVisitorsAddress_" + (i + 1));
                    listStreets.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textYIStreet")));
                    listLangStreets.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("selectYIVASelectLanguage")));
                    listCities.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textYICity")));
                    listCountries.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textYICountry")));
                    listLatitudes.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textYILatitude")));
                    listLongitudes.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textYILongitude")));
                }//"yiTableVisitorsAddress_"+(++i)
                List<Map<String, List<String>>> tempListList = new ArrayList<Map<String, List<String>>>();//at first time list must be in 0 position for first <location> tag into <repository> and
                HashMap<String, List<String>> listTempMap = new HashMap<String, List<String>>();
                listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION, listStreets);
                tempListList.add(listTempMap);
                eag2012.setStreetValue(tempListList);
                tempListList = new ArrayList<Map<String, List<String>>>();
                listTempMap = new HashMap<String, List<String>>();
                listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION, listLangStreets);
                tempListList.add(listTempMap);
                eag2012.setStreetLang(tempListList);
                tempListList = new ArrayList<Map<String, List<String>>>();
                listTempMap = new HashMap<String, List<String>>();
                listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION, listCities);
                tempListList.add(listTempMap);
                eag2012.setCitiesValue(tempListList);
                tempListList = new ArrayList<Map<String, List<String>>>();
                listTempMap = new HashMap<String, List<String>>();
                listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION, listCountries);
                tempListList.add(listTempMap);
                eag2012.setCountryValue(tempListList);
                tempListList = new ArrayList<Map<String, List<String>>>();
                listTempMap = new HashMap<String, List<String>>();
                listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION, listLangStreets);
                tempListList.add(listTempMap);
                List<List<String>> tempList2 = new ArrayList<List<String>>();
                List<String> tempList2List = new ArrayList<String>();
                tempList2List.addAll(listLangStreets);
                tempList2.add(tempList2List);
                eag2012.setCountryLang(tempList2);
                tempListList = new ArrayList<Map<String, List<String>>>();
                listTempMap = new HashMap<String, List<String>>();
                listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION, listLatitudes);
                tempListList.add(listTempMap);
                eag2012.setLocationLatitude(tempListList);
                tempListList = new ArrayList<Map<String, List<String>>>();
                listTempMap = new HashMap<String, List<String>>();
                listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION, listLongitudes);
                tempListList.add(listTempMap);
                eag2012.setLocationLongitude(tempListList);
            }
        }
        this.log.debug("End method: \"parseVisitorAddressYour\"");
    }

    /**
     * Method that checks if it have to create if the object<br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of
     * ourInstitutionFirstPart
     */
    private void parseYourInstitutionFirstPart() throws JSONException {
        this.log.debug("Method start: \"parseYourInstitutionFirstPart\"");
        //your institution - your institution
        if (yourInstitution.has("textYIPersonInstitutionResposibleForTheDescription")) {
            eag2012.setAgentValue(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYIPersonInstitutionResposibleForTheDescription")));
        }
        eag2012.setRepositoridCountrycode(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYIInstitutionCountryCode")));
        eag2012.setRecordIdValue(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYIIdUsedInAPE")));
        //looper
        List<String> otherRecordIds = new ArrayList<String>();
        List<String> localtypeOtherRecordIds = new ArrayList<String>();
        String localTypeNo = replaceIfExistsSpecialReturnString(yourInstitution.getString("selectYICodeISIL"));
        otherRecordIds.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYIIdentifierOfTheInstitution")));
        localtypeOtherRecordIds.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("selectYICodeISIL")));
        if (Eag2012.OPTION_NO.equalsIgnoreCase(localTypeNo)) {
            eag2012.setOtherRepositorId(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYIIdentifierOfTheInstitution")));

        }
        eag2012.setRepositoridRepositorycode(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYIIdUsedInAPE")));
        for (i = 0; yourInstitution.has("otherRepositorId_" + (i)) && yourInstitution.has("selectOtherRepositorIdCodeISIL_" + i); i++) {
            localTypeNo = replaceIfExistsSpecialReturnString(yourInstitution.getString("selectOtherRepositorIdCodeISIL_" + (i)));
            if ((Eag2012.OPTION_NO.equalsIgnoreCase(localTypeNo)) && eag2012.getOtherRepositorId() == null) {
                eag2012.setOtherRepositorId(replaceIfExistsSpecialReturnString(yourInstitution.getString("otherRepositorId_" + (i))));
            }
            otherRecordIds.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("otherRepositorId_" + (i))));
            localtypeOtherRecordIds.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("selectOtherRepositorIdCodeISIL_" + i)));
        }
        if (otherRecordIds.size() > 0) {
            eag2012.setOtherRecordIdValue(otherRecordIds);
        }
        if (localtypeOtherRecordIds.size() > 0) {
            eag2012.setOtherRecordIdLocalType(localtypeOtherRecordIds);
        }
        List<String> tempList = new ArrayList<String>();
        if (yourInstitution.has("textYINameOfTheInstitution")) {
            tempList.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYINameOfTheInstitution")));
            eag2012.setAutformValue(tempList);
        }
        if (yourInstitution.has("selectYINOTISelectLanguage")) {
            tempList = new ArrayList<String>();
            tempList.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("selectYINOTISelectLanguage")));
            eag2012.setAutformLang(tempList);
        }
        if (yourInstitution.has("textYIParallelNameOfTheInstitution")) {
            tempList = new ArrayList<String>();
            tempList.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("textYIParallelNameOfTheInstitution")));
            eag2012.setParformValue(tempList);
        }
        if (yourInstitution.has("selectYIPNOTISelectLanguage")) {
            tempList = new ArrayList<String>();
            tempList.add(replaceIfExistsSpecialReturnString(yourInstitution.getString("selectYIPNOTISelectLanguage")));
            eag2012.setParformLang(tempList);
        }
        this.log.debug("End method: \"parseYourInstitutionFirstPart\"");
    }

    /**
     * Method that checks if it have to create if the object <br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of visitor
     * address
     */
    private void parseYourInstitutionVisitor() throws JSONException {
        this.log.debug("Method start: \"parseYourInstitutionVisitor\"");
        //your institution - visitor address
        JSONObject visitorAddress = yourInstitution.getJSONObject("visitorsAddress");
        parseVisitorAddressYour(visitorAddress);
        this.log.debug("End method: \"parseYourInstitutionVisitor\"");
    }

    /**
     * Method that checks if it have to create if the object<br>
     * and fill it with data the {@link Eag2012} Eag2012 object part of
     * ourInstitutionLastPart(Telephone,email,web,opening,accessibility)
     */
    private void parseYourInstitutionLastPart() throws JSONException {
        this.log.debug("Method start: \"parseYourInstitutionLastPart\"");
        //your institution - last part
        //Telephone 
        if (yourInstitution.has("textYITelephone")) {
            List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
            telephones = createListMapList(telephones, Eag2012.TAB_YOUR_INSTITUTION, Eag2012.ROOT, yourInstitution.getString("textYITelephone"), 0, 0, 0, 0);
            eag2012.setTelephoneValue(telephones);
        }
        // email
        parseDataEmail("textYIEmailAddress", "EmailHref");
        parseDataEmail("textYIEmailLinkTitle", "EmailValue");
        parseDataEmail("selectTextYILangEmail", "EmailLang");
        // web
        parseDataWeb("textYIWebpage", "WebpageHref");
        parseDataWeb("textYIWebpageLinkTitle", "WebpageValue");
        parseDataWeb("selectTextYILangWebpage", "WebpageLang");
        // opening
        parseDataOpening("textYIOpeningTimes", "OpeningValue");
        parseDataOpening("selectTextYIOpeningTimes", "OpeningLang");
        parseDataOpening("linkYIOpeningTimes", "OpeningHref");
        parseDataOpening("yourInstitutionClosingDates", "closingValues");
        parseDataOpening("selectTextYIClosingTimes", "ClosingLang");
        // access		
        if (yourInstitution.has("selectAccessibleToThePublic")) {
            List<Map<String, String>> accessQuestions = eag2012.getAccessQuestion();
            accessQuestions = createDataListString(accessQuestions, Eag2012.TAB_YOUR_INSTITUTION, yourInstitution.getString("selectAccessibleToThePublic"), 0, 0);
            eag2012.setAccessQuestion(accessQuestions);
        }
        parseDataAccess("futherAccessInformation", "restAccessValue");
        parseDataAccess("selectFutherAccessInformation", "restAccessLang");
        // accessibility
        if (yourInstitution.has("selectFacilitiesForDisabledPeopleAvailable")) {
            List<Map<String, String>> accessibilityQuestions = eag2012.getAccessibilityQuestion();
            accessibilityQuestions = createDataListString(accessibilityQuestions, Eag2012.TAB_YOUR_INSTITUTION, yourInstitution.getString("selectFacilitiesForDisabledPeopleAvailable"), 0, 0);
            eag2012.setAccessibilityQuestion(accessibilityQuestions);
        }
        parseDataFacilities("futherInformationOnExistingFacilities", "AccessibilityValue");
        parseDataFacilities("selectFutherAccessInformationOnExistingFacilities", "AccessibilityLang");
        this.log.debug("End method: \"OpenigHours\"");
    }

    /**
     * Method That create de JSONObject the {@link Eag2012} Eag2012 for part
     * yourInstitution
     *
     * @param eag2012 {@link Eag2012} Eag2012
     * @param jsonObj {@link JSONObject} JSONObject
     * @return {@link Eag2012} Eag2012
     */
    @Override
    public Eag2012 JsonObjToEag2012(Eag2012 eag20122, JSONObject jsonObj) throws JSONException {
        eag2012 = eag20122;
        yourInstitution = jsonObj.getJSONObject("yourInstitution");
        if (yourInstitution != null) {
            parseYourInstitutionFirstPart();
            parseYourInstitutionVisitor();
            parseYourInstitutionLastPart();
            if (yourInstitution.has("selectYIContinent")) {
                String continent = replaceIfExistsSpecialReturnString(yourInstitution.getString("selectYIContinent"));
                eag2012.setGeogareaValue(continent);
            }
            if (yourInstitution.has("shareEagWithWikimedia")){
                boolean shareEagWithWikimedia = yourInstitution.getBoolean("shareEagWithWikimedia");
                eag2012.setShareEagWithWikimedia(shareEagWithWikimedia);
                if (shareEagWithWikimedia){
                    eag2012.setShareEagWithWikimediaLicence(DAOFactory.instance().getRightsInformationDAO().getRightsInformation(WebFormEAG2012Action.CC_0));
                }
                else {
                    RightsInformation rightsInformation = DAOFactory.instance().getRightsInformationDAO().getRightsInformation(yourInstitution.getInt("shareEagWithWikimediaLicence"));
                    eag2012.setShareEagWithWikimediaLicence(rightsInformation);
                }
            }
        }
        return eag2012;
    }
}
