package eu.apenet.commons.solr.facet;

import eu.apenet.commons.solr.Ead3SolrFields;
import java.util.ArrayList;
import java.util.List;

import eu.apenet.commons.solr.SolrFields;

public enum FacetType {
//    EAD3_COUNTRY(Ead3SolrFields.COUNTRY, Ead3SolrFields.COUNTRY, true, true, "country."),
//    EAD3_AI(Ead3SolrFields.AI, Ead3SolrFields.AI, true),
//    TITLE_PROPER(Ead3SolrFields.TITLE_PROPER, Ead3SolrFields.ROOT_DOC_ID, true),
    EAD3_TYPE(Ead3SolrFields.DAO_TYPE, Ead3SolrFields.DAO_TYPE, false, true, "advancedsearch.facet.value.daoType."),
    EAD3_LEVEL(Ead3SolrFields.LEVEL_NAME, Ead3SolrFields.LEVEL_NAME, false, true, "advancedsearch.facet.value.level."),
//    EAD3_START_DATE(Ead3SolrFields.START_DATE, true, true),
//    EAD3_END_DATE(Ead3SolrFields.END_DATE, true, false),
    COUNTRY(Ead3SolrFields.COUNTRY, Ead3SolrFields.COUNTRY_ID, true, true, "country."),
    AI(Ead3SolrFields.AI, Ead3SolrFields.AI_ID, true),
    EAG_METADATA_USE(SolrFields.EAG_LICENCE_SHAREABLE, false, true, "eag.licence.shareable.", false, true),
    EAC_CPF_METADATA_USE(SolrFields.EAG_LICENCE_SHAREABLE, false, true, "eaccpf.licence.shareable.", false, true),
//    FOND(SolrFields.TITLE_OF_FOND, SolrFields.FOND_ID, true), //ToDo: Merge
    TOPIC(Ead3SolrFields.TOPIC, false, true, "topics.", true),
    TYPE(Ead3SolrFields.RECORD_TYPE, false, true, "advancedsearch.text."),
    LEVEL(Ead3SolrFields.LEVEL_NAME, false, true, "advancedsearch.facet.value.level."),
    DAO(Ead3SolrFields.DAO, false, true, "advancedsearch.facet.value.dao."),
    ROLEDAO(Ead3SolrFields.DAO_TYPE, false, true, "advancedsearch.facet.value.daoType.", true),
    DATE_TYPE(Ead3SolrFields.DATE_TYPE, false, true, "advancedsearch.facet.value.datetype."),
    START_DATE(Ead3SolrFields.START_DATE, true, true),
    END_DATE(Ead3SolrFields.END_DATE, true, false),
    
    EAC_CPF_START_DATE(SolrFields.EAC_CPF_START_DATE, true, true),
    EAC_CPF_END_DATE(SolrFields.EAC_CPF_END_DATE, true, false),
    EAC_CPF_PLACES(SolrFields.EAC_CPF_FACET_PLACES, false, false),
    EAC_CPF_OCCUPATION(SolrFields.EAC_CPF_FACET_OCCUPATION, false, false),
    EAC_CPF_FUNCTION(SolrFields.EAC_CPF_FACET_FUNCTION, false, false),
    EAC_CPF_MANDATE(SolrFields.EAC_CPF_FACET_MANDATE, false, false),
    EAC_CPF_ENTITY_TYPE(SolrFields.EAC_CPF_FACET_ENTITY_TYPE, false, true, "advancedsearch.facet.value.eaccpf.entitytype."),
    LANGUAGE(Ead3SolrFields.LANGUAGE, false, true, "language."),
    EAG_AI_GROUPS(SolrFields.EAG_AI_GROUPS_FACET, SolrFields.EAG_AI_GROUP_ID, true),
    EAG_REPOSITORY_TYPE(SolrFields.EAG_REPOSITORY_TYPE, false, true, "eag2012.options.institutionType.");
    private final String name;
    private final String refinementField;
    private final boolean multiSelect;
    private final boolean hasId;
    private final boolean valueIsKey;
    private final String prefix;
    private final boolean date;
    private final boolean startDate;
    private boolean needToBeLowercase = false;

    private FacetType(String name, boolean isDateType, boolean isStartDate) {
        this.name = name;
        this.refinementField = name;
        this.hasId = false;
        this.valueIsKey = false;
        this.prefix = null;
        this.date = isDateType;
        this.startDate = isStartDate;
        if (date) {
            this.multiSelect = false;
        } else {
            this.multiSelect = true;
        }
    }

    private FacetType(String name, String refinementField, boolean hasId, boolean valueIsKey, String prefix) {
        this.name = name;
        this.refinementField = refinementField;
        this.hasId = hasId;
        this.valueIsKey = valueIsKey;
        this.prefix = prefix;
        this.date = false;
        this.startDate = false;
        this.multiSelect = true;
    }

    private FacetType(String name, boolean hasId, boolean valueIsKey, String prefix, boolean needToBeLowercase, boolean isMultiselect) {
        this.name = name;
        this.refinementField = name;
        this.hasId = hasId;
        this.valueIsKey = valueIsKey;
        this.prefix = prefix;
        this.date = false;
        this.startDate = false;
        this.multiSelect = isMultiselect;
        this.needToBeLowercase = needToBeLowercase;

    }

    private FacetType(String name, boolean hasId, boolean valueIsKey, String prefix) {
        this(name, name, hasId, valueIsKey, prefix);
    }

    private FacetType(String name, boolean hasId, boolean valueIsKey, String prefix, boolean needToBeLowercase) {
        this(name, hasId, valueIsKey, prefix);
        this.needToBeLowercase = needToBeLowercase;
    }

    private FacetType(String name, String refinementField, boolean hasId) {
        this(name, refinementField, hasId, false, null);
    }

    public String getNameWithLabel() {
        if (multiSelect) {
            return "{!ex=" + name + "}" + name;
        } else {
            return name;
        }

    }

    public String getRefinementFieldWithLabel() {
        if (multiSelect) {
            return "{!tag=" + name + "}" + refinementField;
        } else {
            return refinementField;
        }

    }

    public String getName() {
        return name;
    }

    public String getRefinementField() {
        return refinementField;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public static FacetType getFacetByName(String facetName) {
        for (FacetType facet : FacetType.values()) {
            if (facet.name.equals(facetName)) {
                return facet;
            }
        }
        return null;
    }

    public static List<ListFacetSettings> getDefaultDateListFacetSettings() {
        List<ListFacetSettings> result = new ArrayList<ListFacetSettings>();

        result.add(new ListFacetSettings(FacetType.START_DATE));
        result.add(new ListFacetSettings(FacetType.END_DATE));
        return result;
    }

    public static List<ListFacetSettings> getEad3DateListFacetSettings() {
        List<ListFacetSettings> result = new ArrayList<>();

        result.add(new ListFacetSettings(FacetType.START_DATE));
        result.add(new ListFacetSettings(FacetType.END_DATE));
        return result;
    }

    public static List<ListFacetSettings> getDefaultEadListFacetSettings() {
        List<ListFacetSettings> result = new ArrayList<ListFacetSettings>();
        result.add(new ListFacetSettings(FacetType.COUNTRY));
        result.add(new ListFacetSettings(FacetType.AI));
        result.add(new ListFacetSettings(FacetType.TOPIC, true));
        result.add(new ListFacetSettings(FacetType.TYPE));
        result.add(new ListFacetSettings(FacetType.EAD3_LEVEL));
        result.add(new ListFacetSettings(FacetType.DAO));
        result.add(new ListFacetSettings(FacetType.ROLEDAO));
        result.add(new ListFacetSettings(FacetType.DATE_TYPE));
        result.add(new ListFacetSettings(FacetType.START_DATE));
        result.add(new ListFacetSettings(FacetType.END_DATE));
        return result;
    }

    public static List<ListFacetSettings> getDefaultEad3ListFacetSettings() {
        List<ListFacetSettings> results = new ArrayList<>();
        results.add(new ListFacetSettings(FacetType.COUNTRY));
        results.add(new ListFacetSettings(FacetType.AI));
        results.add(new ListFacetSettings(FacetType.EAD3_LEVEL));
        results.add(new ListFacetSettings(FacetType.EAD3_TYPE));
        results.add(new ListFacetSettings(FacetType.START_DATE));
        results.add(new ListFacetSettings(FacetType.END_DATE));
        return results;
    }

    public static List<ListFacetSettings> getDefaultEagListFacetSettings() {
        List<ListFacetSettings> result = new ArrayList<ListFacetSettings>();
        result.add(new ListFacetSettings(FacetType.COUNTRY));
        result.add(new ListFacetSettings(FacetType.EAG_AI_GROUPS));
        result.add(new ListFacetSettings(FacetType.EAG_REPOSITORY_TYPE));
        result.add(new ListFacetSettings(FacetType.EAG_METADATA_USE));
        return result;
    }

    public static List<ListFacetSettings> getDefaultEacCPfListFacetSettings() {
        List<ListFacetSettings> result = new ArrayList<ListFacetSettings>();
        result.add(new ListFacetSettings(FacetType.COUNTRY));
        result.add(new ListFacetSettings(FacetType.AI));
        result.add(new ListFacetSettings(FacetType.EAC_CPF_ENTITY_TYPE));
        result.add(new ListFacetSettings(FacetType.EAC_CPF_METADATA_USE));
        result.add(new ListFacetSettings(FacetType.EAC_CPF_PLACES, true, 2));
        //result.add(new ListFacetSettings(FacetType.EAC_CPF_OCCUPATION, true,2));	
        //result.add(new ListFacetSettings(FacetType.EAC_CPF_MANDATE, true,2));	
        //result.add(new ListFacetSettings(FacetType.EAC_CPF_FUNCTION, true,2));    
        result.add(new ListFacetSettings(FacetType.LANGUAGE));
        result.add(new ListFacetSettings(FacetType.DATE_TYPE));
        result.add(new ListFacetSettings(FacetType.EAC_CPF_START_DATE));
        result.add(new ListFacetSettings(FacetType.EAC_CPF_END_DATE));
        return result;
    }

    public boolean isHasId() {
        return hasId;
    }

    public boolean isValueIsKey() {
        return valueIsKey;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isDate() {
        return date;
    }
    
    public boolean isStartDate() {
        return this.date & this.startDate;
    }

    public boolean isNeedToBeLowercase() {
        return needToBeLowercase;
    }

}
