package eu.apenet.api.eag;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.*;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.infraestructure.ArchivalInstitutionUnit;
import eu.apenet.commons.infraestructure.CountryUnit;
import eu.apenet.commons.infraestructure.NavigationTree;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CoordinatesDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Coordinates;
import eu.apenet.persistence.vo.Country;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.*;

public class EagTreeApiAction extends ActionSupport {

    private String nodeId;
    private String countryCode;

    private String all = "false";
    private String sortType = "1";

    private ResourceBundleSource resourceBundleSource;

    protected static final String FOLDER_LAZY = "\"isFolder\": true, \"isLazy\": true";
    protected static final String UTF8 = "UTF-8";
    protected static final String END_ARRAY = "]\n";
    protected static final String START_ARRAY = "[\n";
    protected static final String END_ITEM = "}";
    protected static final String START_ITEM = "{";
    protected static final String FOLDER_WITH_CHILDREN = "\"isFolder\": true, \"children\": \n";
    protected static final String COMMA = ",";
    private static final String FOLDER_NOT_LAZY = "\"isFolder\": true";
    private static final String NO_LINK = "\"noLink\": true";
    private static final String EUROPE = "Europe";
    private static final String MORE_CLASS = "more";
    private static final String MORE_TEXT = "advancedsearch.context.more";
    protected static final String ADVANCEDSEARCH_TEXT_NOTITLE = "advancedsearch.text.notitle";

    private final Logger log = Logger.getLogger(getClass());

    public EagTreeApiAction() {

    }

    public String execute() {
        HttpServletResponse response = ServletActionContext.getResponse();

        try {
            resourceBundleSource = new StrutsResourceBundleSource();
            if (nodeId == null) {
                NavigationTree navigationTree = new NavigationTree(resourceBundleSource);
                List<CountryUnit> countryList = navigationTree.getALCountriesWithArchivalInstitutionsWithEAG();
                for (CountryUnit countryUnit : countryList){
                    countryUnit.setLocalizedName(getText(countryUnit.getLocalizedName(),countryUnit.getLocalizedName()));
                }

                Collections.sort(countryList);
                writeToResponseAndClose(generateDirectoryJSON(navigationTree, countryList), response);
            }
            else {
//                if (StringUtils.isBlank(nodeId) || StringUtils.isBlank(countryCode)) {
//                    StringBuilder builder = new StringBuilder();
//                    builder.append(START_ARRAY);
//                    builder.append(END_ARRAY);
//                    writeToResponseAndClose(builder, response);
//                } else {
                    NavigationTree navigationTree = new NavigationTree(resourceBundleSource);

                List<ArchivalInstitutionUnit> archivalInstitutionList;

                if (Boolean.parseBoolean(all)){
                    archivalInstitutionList = getAllArchivalInstitutions(nodeId, resourceBundleSource.getLocale().getLanguage(),0);
                }
                else {
                    archivalInstitutionList = navigationTree.getArchivalInstitutionsByParentAiId(nodeId);
                }

                    // This filter has been added to display only those final
                    // archival institutions or groups which have eag files uploaded
                    // to the System
                    // Remove it if the user wants to display again all the
                    // institutions even if they doesn't eag files uploaded
                    archivalInstitutionList = navigationTree.filterArchivalInstitutionsWithEAG(archivalInstitutionList);

                if (Boolean.parseBoolean(all)){
                    if (sortType.equals("1")) {
                        Collections.sort(archivalInstitutionList, new Comparator<ArchivalInstitutionUnit>() {
                            @Override
                            public int compare(ArchivalInstitutionUnit o1, ArchivalInstitutionUnit o2) {
                                return o1.getAiname().compareTo(o2.getAiname());
                            }
                        });
                    }
                    else {
//                        Collections.sort(archivalInstitutionList, new Comparator<ArchivalInstitutionUnit>() {
//                            @Override
//                            public int compare(ArchivalInstitutionUnit o1, ArchivalInstitutionUnit o2) {
//                                int comp = o1.getTreeLevel().compareTo(o2.getTreeLevel());
//                                if (comp == 0) {
//                                    return o1.getAlorder().compareTo(o2.getAlorder());
//                                } else {
//                                    return comp;
//                                }
//                            }
//                        });
                    }
                }
                else {
                    Collections.sort(archivalInstitutionList);
                }

                writeToResponseAndClose(generateArchivalInstitutionsTreeJSON(navigationTree, archivalInstitutionList, countryCode),response);
            }


        } catch (APEnetException | IOException e) {
            e.printStackTrace();
        }


        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.SUCCESS;
    }

    private List<ArchivalInstitutionUnit> getAllArchivalInstitutions(String nodeId, String locale, int level){
        List<ArchivalInstitutionUnit> archivalInstitutionUnitList = new ArrayList<ArchivalInstitutionUnit>();

        if (nodeId.startsWith("country_")) {
            Integer couId = Integer.parseInt(nodeId.substring(nodeId.lastIndexOf('_') + 1));
            ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
            List<ArchivalInstitution> archivalInstitutionList = archivalInstitutionDao.getRootArchivalInstitutionsByCountryId(couId);
            Collections.sort(archivalInstitutionList, new Comparator<ArchivalInstitution>() {
                @Override
                public int compare(ArchivalInstitution o1, ArchivalInstitution o2) {
                    return (new Integer(o1.getAlorder())).compareTo((new Integer(o2.getAlorder())));
                }
            });
            for (ArchivalInstitution anArchivalInstitutionList : archivalInstitutionList) {
                Integer numberOfArchivalInstitutions = 0;
                if (anArchivalInstitutionList.isGroup()) {
                    numberOfArchivalInstitutions = archivalInstitutionDao.countArchivalInstitutionsByParentAiId(anArchivalInstitutionList.getAiId());
                }
                if (anArchivalInstitutionList.isGroup()) {
                    archivalInstitutionUnitList.addAll(getAllArchivalInstitutions("aigroup_"+anArchivalInstitutionList.getAiId(), locale, level++));
                }
                else {
                    ArchivalInstitutionUnit archivalInstitutionUnit = new ArchivalInstitutionUnit(anArchivalInstitutionList.getAiId(), anArchivalInstitutionList.getAiname(), null, anArchivalInstitutionList.getEncodedRepositorycode(), anArchivalInstitutionList.getEagPath(), anArchivalInstitutionList.isGroup(), numberOfArchivalInstitutions, locale, anArchivalInstitutionList.getAlorder());
                    archivalInstitutionUnit.setTreeLevel(level++);
                    archivalInstitutionUnitList.add(archivalInstitutionUnit);
                }
            }
        }
        else if (nodeId.startsWith("aicontent_") || nodeId.startsWith("ainocontent_") || nodeId.startsWith("aigroup_")){
            Integer pId = Integer.parseInt(nodeId.substring(nodeId.lastIndexOf('_') + 1));

            ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
            List<ArchivalInstitution> archivalInstitutionList = archivalInstitutionDao.getArchivalInstitutionsByParentAiId(pId, false);

            Collections.sort(archivalInstitutionList, new Comparator<ArchivalInstitution>() {
                @Override
                public int compare(ArchivalInstitution o1, ArchivalInstitution o2) {
                    return (new Integer(o1.getAlorder())).compareTo((new Integer(o2.getAlorder())));
                }
            });

            for (ArchivalInstitution anArchivalInstitutionList : archivalInstitutionList) {
                Integer numberOfArchivalInstitutions = 0;
                if (anArchivalInstitutionList.isGroup()) {
                    numberOfArchivalInstitutions = archivalInstitutionDao.countArchivalInstitutionsByParentAiId(anArchivalInstitutionList.getAiId());
                }
                if (anArchivalInstitutionList.isGroup()) {
                    archivalInstitutionUnitList.addAll(getAllArchivalInstitutions("aigroup_"+anArchivalInstitutionList.getAiId(), locale, level++));
                }
                else {
                    ArchivalInstitutionUnit archivalInstitutionUnit = new ArchivalInstitutionUnit(anArchivalInstitutionList.getAiId(), anArchivalInstitutionList.getAiname(), null, anArchivalInstitutionList.getEncodedRepositorycode(), anArchivalInstitutionList.getEagPath(), anArchivalInstitutionList.isGroup(), numberOfArchivalInstitutions, locale, anArchivalInstitutionList.getAlorder());
                    archivalInstitutionUnit.setTreeLevel(level++);
                    archivalInstitutionUnitList.add(archivalInstitutionUnit);
                }

            }
        }

        return archivalInstitutionUnitList;
    }

    private void writeToResponseAndClose(StringBuilder stringBuilder, HttpServletResponse resourceResponse)
            throws UnsupportedEncodingException, IOException {
        resourceResponse.setCharacterEncoding(UTF8);
        resourceResponse.setContentType("application/json");
        Writer writer =  new OutputStreamWriter(resourceResponse.getOutputStream(), UTF8);
//        Writer writer = getResponseWriter(resourceResponse);
        writer.write(stringBuilder.toString());
        writer.flush();
        writer.close();
    }

    private StringBuilder generateDirectoryJSON(NavigationTree navigationTree, List<CountryUnit> countryList) {
        this.log.debug("Method start: \"generateDirectoryJSON\"");
        StringBuilder builder = new StringBuilder();
        builder.append(START_ARRAY);
        builder.append(START_ITEM);
        addTitle(builder, navigationTree.getResourceBundleSource().getString("directory.text.directory"),
                navigationTree.getResourceBundleSource().getLocale());
        addGoogleMapsAddress(builder, EUROPE);
        builder.append(COMMA);
        addExpand(builder);
        builder.append(COMMA);
        builder.append(FOLDER_WITH_CHILDREN);
        builder.append(generateCountriesTreeJSON(navigationTree, countryList));
        builder.append(END_ITEM);
        builder.append(END_ARRAY);
        this.log.debug("End method: \"generateDirectoryJSON\"");
        return builder;
    }

    private StringBuilder generateCountriesTreeJSON(NavigationTree navigationTree, List<CountryUnit> countryList) {
        this.log.debug("Method start: \"fillEAG2012\"");
        CountryUnit countryUnit = null;
        StringBuilder builder = new StringBuilder();
        builder.append(START_ARRAY);
        for (int i = 0; i < countryList.size(); i++) {
            // It is necessary to build a JSON response to display all the
            // countries in Directory Tree
            countryUnit = countryList.get(i);
            builder.append(START_ITEM);
            addTitle(builder, countryUnit.getLocalizedName(), navigationTree.getResourceBundleSource().getLocale());
            builder.append(COMMA);
            builder.append(FOLDER_LAZY);
            builder.append(COMMA);
            addKey(builder, countryUnit.getCountry().getId(), null, "country");
            addGoogleMapsAddress(builder, countryUnit.getCountry().getCname());
            addCountryCode(builder, countryUnit.getCountry().getIsoname());
            builder.append(END_ITEM);
            if (i != countryList.size() - 1) {
                builder.append(COMMA);
            }
        }

        builder.append(END_ARRAY);
        countryUnit = null;
        this.log.debug("End method: \"fillEAG2012\"");
        return builder;

    }

    private StringBuilder generateArchivalInstitutionsTreeJSON(NavigationTree navigationTree,
                                                               List<ArchivalInstitutionUnit> archivalInstitutionList, String countryCode) {
        this.log.debug("Method start: \"generateArchivalInstitutionsTreeJSON\"");
        Locale locale = navigationTree.getResourceBundleSource().getLocale();
        StringBuilder buffer = new StringBuilder();
        ArchivalInstitutionUnit archivalInstitutionUnit = null;

        buffer.append(START_ARRAY);
        for (int i = 0; i < archivalInstitutionList.size(); i++) {
            // It is necessary to build a JSON response to display all the
            // archival institutions in Directory Tree
            archivalInstitutionUnit = archivalInstitutionList.get(i);
            if (archivalInstitutionUnit.getIsgroup() && archivalInstitutionUnit.isHasArchivalInstitutions()) {
                // The Archival Institution is a group and it has archival
                // institutions within it
                buffer.append(START_ITEM);
                addTitle(buffer, archivalInstitutionUnit.getAiname(), locale);
                buffer.append(COMMA);
                buffer.append(FOLDER_LAZY);
                buffer.append(COMMA);
//                buffer.append("\"numberOfChildren\":"+archivalInstitutionUnit.getNumberOfArchivalInstitutions());
//                buffer.append(COMMA);
                addKey(buffer, archivalInstitutionUnit.getAiId(), archivalInstitutionUnit.getRepoCode(), "archival_institution_group");
                addCountryCode(buffer, countryCode);
                buffer.append(END_ITEM);
            } else if (archivalInstitutionUnit.getIsgroup() && !archivalInstitutionUnit.isHasArchivalInstitutions()) {
                // The Archival Institution is a group but it doesn't have any
                // archival institutions within it
                buffer.append(START_ITEM);
                addTitle(buffer, archivalInstitutionUnit.getAiname(), locale);
                buffer.append(COMMA);
                buffer.append(FOLDER_NOT_LAZY);
                buffer.append(COMMA);
                buffer.append(NO_LINK);
                buffer.append(COMMA);
//                buffer.append("\"numberOfChildren\":0");
//                buffer.append(COMMA);
                addKey(buffer, archivalInstitutionUnit.getAiId(), archivalInstitutionUnit.getRepoCode(), "archival_institution_group");
                addCountryCode(buffer, countryCode);
                buffer.append(END_ITEM);
            } else if (!archivalInstitutionUnit.getIsgroup()) {
                // The Archival Institution is a leaf
                buffer.append(START_ITEM);
                addTitle(buffer, archivalInstitutionUnit.getAiname(), locale);
                buffer.append(COMMA);
                if (archivalInstitutionUnit.getPathEAG() != null && !archivalInstitutionUnit.getPathEAG().equals("")) {
                    // The archival institution has EAG
                    addKey(buffer, archivalInstitutionUnit.getAiId(), archivalInstitutionUnit.getRepoCode(), "archival_institution_eag");
                } else {
                    addKey(buffer, archivalInstitutionUnit.getAiId(), archivalInstitutionUnit.getRepoCode(), "archival_institution_no_eag");
                    buffer.append(COMMA);
                    buffer.append(NO_LINK);
                }
                addCountryCode(buffer, countryCode);
                buffer.append(END_ITEM);
            }
            if (i != archivalInstitutionList.size() - 1) {
                buffer.append(COMMA);
            }
        }
        buffer.append(END_ARRAY);
        archivalInstitutionUnit = null;
        this.log.debug("End method: \"generateArchivalInstitutionsTreeJSON\"");
        return buffer;

    }

    private void addTitle(StringBuilder buffer, String title, Locale locale) {
        addTitle(null, buffer, title, locale);
    }

    private static void addGoogleMapsAddress(StringBuilder buffer, String address) {
        buffer.append(COMMA);
        buffer.append("\"googleMapsAddress\":\"" + address + "\"");
    }

    private static void addCountryCode(StringBuilder buffer, String countryCode) {
        buffer.append(COMMA);
        buffer.append("\"countryCode\":\"" + countryCode + "\"");
    }

    private static void addKey(StringBuilder buffer, Number key, String repositoryCode, String nodeType) {

        if (nodeType.equals("country")) {
            buffer.append("\"key\":" + "\"country_" + key + "\"");
        } else if (nodeType.equals("archival_institution_group")) {
            buffer.append("\"key\":" + "\"aigroup_" + key + "\"");
        } else if (nodeType.equals("archival_institution_eag")) {
            buffer.append("\"aiRepositoryCode\":" + "\"" + repositoryCode + "\",");
            buffer.append("\"aiId\":");
            buffer.append(" \"" + key);
            buffer.append("\" ");
            buffer.append(COMMA);
            buffer.append("\"key\":" + "\"aieag_" + key + "\"");

        } else if (nodeType.equals("archival_institution_no_eag")) {
            buffer.append("\"key\":" + "\"ainoeag_" + key + "\"");
        }

    }

    protected static void addExpand(StringBuilder buffer) {
        buffer.append("\"expand\":true");
    }

    protected void addTitle(String styleClass, StringBuilder buffer, String title, Locale locale) {
        addNoIcon(buffer);
        String convertedTitle = replaceQuotesAndReturnsForTree(title);
        convertedTitle = replaceLessThan(convertedTitle);
        boolean hasTitle = convertedTitle != null && convertedTitle.length() > 0;
        if (!hasTitle) {
            if (styleClass == null){
                styleClass = "notitle";
            }else {
                styleClass += " notitle";
            }
        }

        if (styleClass != null){
            buffer.append("\"addClass\":");
            buffer.append(" \"" + styleClass + "\"");
            buffer.append(COMMA);
        }

        buffer.append("\"title\":\"");
        if (hasTitle){
            buffer.append(convertedTitle);
        }else {
            buffer.append(resourceBundleSource.getString(ADVANCEDSEARCH_TEXT_NOTITLE));
        }
        buffer.append("\"");
    }

    protected static void addNoIcon(StringBuilder buffer) {
        buffer.append("\"icon\":");
        buffer.append(" false");
        buffer.append(COMMA);
    }

    public static String replaceQuotesAndReturnsForTree(String string) {
        String result = string;
        if (result != null) {
            result = result.replaceAll("\"", "'");
            result = result.replaceAll("[\n\t\r\\\\%;]", "");
            result = result.trim();
        }
        return result;
    }

    public static String replaceLessThan(String string){
        if (string != null){
            return string.replaceAll("<", "&lt;");
        }else {
            return null;
        }
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getAll() {
        return all;
    }

    public String getSortType() {
        return sortType;
    }
}
