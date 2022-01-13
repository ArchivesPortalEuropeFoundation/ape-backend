/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.actions;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.eaccpf.EacCpfLoader;
import eu.apenet.dashboard.manual.eaccpf.util.DateType;
import eu.apenet.dashboard.manual.eaccpf.util.IdentifierType;
import eu.apenet.dashboard.manual.eaccpf.util.MapEntry;
import eu.apenet.dashboard.manual.eaccpf.util.NameEntryType;
import eu.apenet.dashboard.manual.eaccpf.util.RelationType;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author papp
 */
public class LoadFormAction extends EacCpfAction {

    private Set<MapEntry> countryList = new TreeSet<MapEntry>();
    private Set<MapEntry> formNameList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> componentNameList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> placeEntryList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> addressComponentTypeList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> cpfRelationTypeList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> resRelationTypeList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> fncRelationTypeList = new LinkedHashSet<MapEntry>();

    private String cpfType;
    private String cpfTypeDescriptionText;
    private String cpfTypeIdentifierText;
    private String fileToLoad;
    private File upload;
    private String content;
    private EacCpfLoader loader;
    private String useMode;
    private String defaultLanguage;
    private String defaultScript;
    private String defaultRightsInformation;
    private String defaultRightsDescription;
    private String defaultRightsHolder;
    private String openLabel;

    private static final String OTHER = "other";

    @Override
    public String execute() throws Exception {
        this.loader = new EacCpfLoader();
        if (fileToLoad != null){
            upload = new File(APEnetUtilities.getConfig().getRepoDirPath() + DAOFactory.instance().getEacCpfDAO().findById(Integer.valueOf(fileToLoad)).getPath());
        }
        if (upload != null) {
            boolean result = this.loader.fillEacCpf(upload);
            cpfType = this.loader.getEntityType();
            useMode = "load";
        } else {
            cpfType = getServletRequest().getParameter("cpfType");
            this.loader.setRecordId("");
            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
            if (archivalInstitution.getPartner() != null) {
                this.loader.setControlResponsiblePerson(archivalInstitution.getPartner().getName());
            } else {
                UserDAO partnerdao = DAOFactory.instance().getUserDAO();
                User countryManager = partnerdao.getCountryManagerOfCountry(archivalInstitution.getCountry());
                this.loader.setControlResponsiblePerson(countryManager.getName());
            }
            this.loader.setAgencyCode(DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId()).getRepositorycode());
            useMode = "new";
        }
        if (cpfType.equals(EacCpfAction.PERSON)) {
            cpfTypeDescriptionText = getText("eaccpf.commons.type") + " " + getText("eaccpf.commons.person");
            cpfTypeIdentifierText = getText("eaccpf.identity.identifier.person");
            openLabel = getText("eaccpf.commons.date.open.person");
        } else if (cpfType.equals(EacCpfAction.CORPORATE_BODY)) {
            cpfTypeDescriptionText = getText("eaccpf.commons.type") + " " + getText("eaccpf.commons.corporateBody");
            cpfTypeIdentifierText = getText("eaccpf.identity.identifier.corporateBody");
            openLabel = getText("eaccpf.commons.date.open.corpfam");
        } else if (cpfType.equals(EacCpfAction.FAMILY)) {
            cpfTypeDescriptionText = getText("eaccpf.commons.type") + " " + getText("eaccpf.commons.family");
            cpfTypeIdentifierText = getText("eaccpf.identity.identifier.family");
            openLabel = getText("eaccpf.commons.date.open.corpfam");
        } else {
            cpfTypeDescriptionText = "";
            cpfTypeIdentifierText = "";
            openLabel = "";
        }
        if (this.loader.getIdentifiers() == null) {
            this.loader.setIdentifiers(new ArrayList<IdentifierType>());
        }
        if (this.loader.getNameEntries() == null) {
            this.loader.setNameEntries(new ArrayList<NameEntryType>());
        }
        if (this.loader.getExistDates() == null) {
            this.loader.setExistDates(new ArrayList<DateType>());
        }
        if (this.loader.getCpfRelations() == null) {
            this.loader.setCpfRelations(new ArrayList<RelationType>());
        }
        if (this.loader.getResRelations() == null) {
            this.loader.setResRelations(new ArrayList<RelationType>());
        }
        if (this.loader.getOtherRecordIds() == null) {
            this.loader.setOtherRecordIds(new ArrayList<String>());
        }
        if (getServletRequest().getParameter("useMode") != null) {
            useMode = getServletRequest().getParameter("useMode");
        }
        if (getServletRequest().getParameter("defaultLanguage") != null) {
            defaultLanguage = getServletRequest().getParameter("defaultLanguage");
        }
        if (getServletRequest().getParameter("defaultScript") != null) {
            defaultScript = getServletRequest().getParameter("defaultScript");
        }
        if (getServletRequest().getParameter("defaultRightsInformation") != null) {
            defaultRightsInformation = getServletRequest().getParameter("defaultRightsInformation");
        }
        if (getServletRequest().getParameter("defaultRightsDescription") != null) {
            defaultRightsDescription = getServletRequest().getParameter("defaultRightsDescription");
        }
        if (getServletRequest().getParameter("defaultRightsHolder") != null) {
            defaultRightsHolder = getServletRequest().getParameter("defaultRightsHolder");
        }
        if (fileToLoad != null){
            defaultRightsInformation = ""+this.loader.getRightsInformation().getId();
            defaultRightsDescription = this.loader.getRightsDescription();
            defaultRightsHolder = this.loader.getRightsHolder();
        }

        StringBuilder builder = new StringBuilder();
        Enumeration<String> paramNames = getServletRequest().getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            builder.append(paramName);
            builder.append(": ");
            String[] paramValues = getServletRequest().getParameterValues(paramName);
            for (String string : paramValues) {
                builder.append(string);
                builder.append(", ");
            }
        }
        content = builder.toString();

        setUpLanguages();
        setUpScriptList();
        setUpCountryList();
        setUpFormOfNameList();
        setUpComponentNameList();
        setUpPlaceEntryList();
        setUpAddressComponentTypeList();
        setUpCpfRelationTypeList();
        setUpResRelationTypeList();
        setUpFncRelationTypeList();

        return SUCCESS;
    }

    protected void setUpCountryList() {
        // Add empty map entry
        countryList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));
        // Add countries
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryList.add(new MapEntry(obj.getCountry(), obj.getDisplayCountry(Locale.ENGLISH)));
        }
    }

    protected void setUpFormOfNameList() {
        formNameList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));
        formNameList.add(new MapEntry("authorized", getText("eaccpf.identity.name.form.authorized")));
        formNameList.add(new MapEntry("alternative", getText("eaccpf.identity.name.form.alternative")));
        formNameList.add(new MapEntry("preferred", getText("eaccpf.identity.name.form.preferred")));
        formNameList.add(new MapEntry("abbreviation", getText("eaccpf.identity.name.form.abbreviation")));
        formNameList.add(new MapEntry(OTHER, getText("eaccpf.identity.name.form.other")));
    }

    protected void setUpComponentNameList() {
        if (cpfType.equals(CORPORATE_BODY)) {
            componentNameList.add(new MapEntry("corpname", getText("eaccpf.identity.name.component.corpname")));
        }
        if (cpfType.equals(FAMILY)) {
            componentNameList.add(new MapEntry("famname", getText("eaccpf.identity.name.component.famname")));
        }
        if (cpfType.equals(PERSON)) {
            componentNameList.add(new MapEntry("persname", getText("eaccpf.identity.name.component.persname")));
        }
        if (cpfType.equals(PERSON) || cpfType.equals(FAMILY)) {
            componentNameList.add(new MapEntry("surname", getText("eaccpf.identity.name.component.surname")));
        }
        if (cpfType.equals(PERSON)) {
            componentNameList.add(new MapEntry("firstname", getText("eaccpf.identity.name.component.firstname")));
            componentNameList.add(new MapEntry("birthname", getText("eaccpf.identity.name.component.birthname")));
            componentNameList.add(new MapEntry("title", getText("eaccpf.identity.name.component.title")));
        }
        if (cpfType.equals(PERSON) || cpfType.equals(FAMILY)) {
            componentNameList.add(new MapEntry("prefix", getText("eaccpf.identity.name.component.prefix")));
        }
        componentNameList.add(new MapEntry("suffix", getText("eaccpf.identity.name.component.suffix")));
        componentNameList.add(new MapEntry("alias", getText("eaccpf.identity.name.component.alias")));
        if (cpfType.equals(PERSON)) {
            componentNameList.add(new MapEntry("patronymic", getText("eaccpf.identity.name.component.patronymic")));
        }
        if (cpfType.equals(CORPORATE_BODY)) {
            componentNameList.add(new MapEntry("legalform", getText("eaccpf.identity.name.component.legalform")));
        }
    }

    protected void setUpPlaceEntryList() {
        // Add empty list entry
        placeEntryList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        if (cpfType.equals(PERSON)) {
            placeEntryList.add(new MapEntry("birth", getText("eaccpf.description.combo.place.role.birth")));
        }
        if (cpfType.equals(CORPORATE_BODY)) {
            placeEntryList.add(new MapEntry("foundation", getText("eaccpf.description.combo.place.role.foundation")));
        }
        if (cpfType.equals(PERSON) || cpfType.equals(FAMILY)) {
            placeEntryList.add(new MapEntry("private-residence", getText("eaccpf.description.combo.place.role.private-residence")));
        }
        placeEntryList.add(new MapEntry("business-residence", getText("eaccpf.description.combo.place.role.business-residence")));
        if (cpfType.equals(PERSON)) {
            placeEntryList.add(new MapEntry("death", getText("eaccpf.description.combo.place.role.death")));
        }
        if (cpfType.equals(CORPORATE_BODY)) {
            placeEntryList.add(new MapEntry("suppression", getText("eaccpf.description.combo.place.role.suppression")));
        }
        placeEntryList.add(new MapEntry(OTHER, getText("eaccpf.description.combo.place.role.other")));
    }

    protected void setUpAddressComponentTypeList() {
        // Add empty list entry
        addressComponentTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        addressComponentTypeList.add(new MapEntry("street", getText("eaccpf.description.combo.address.component.street")));
        addressComponentTypeList.add(new MapEntry("postalcode", getText("eaccpf.description.combo.address.component.postalcode")));
        addressComponentTypeList.add(new MapEntry("localentity", getText("eaccpf.description.combo.address.component.localentity")));
        addressComponentTypeList.add(new MapEntry("firstdem", getText("eaccpf.description.combo.address.component.firstdem")));
        addressComponentTypeList.add(new MapEntry("secondem", getText("eaccpf.description.combo.address.component.secondem")));
        addressComponentTypeList.add(new MapEntry("country", getText("eaccpf.description.combo.address.component.country")));
        addressComponentTypeList.add(new MapEntry(OTHER, getText("eaccpf.description.combo.address.component.other")));
    }

    protected void setUpCpfRelationTypeList() {
        // Add empty list entry
        cpfRelationTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        cpfRelationTypeList.add(new MapEntry("identity", getText("eaccpf.relations.cpf.relation.type.identity")));
        cpfRelationTypeList.add(new MapEntry("hierarchical", getText("eaccpf.relations.cpf.relation.type.hierarchical")));
        cpfRelationTypeList.add(new MapEntry("hierarchical-parent", getText("eaccpf.relations.cpf.relation.type.hierarchical-parent")));
        cpfRelationTypeList.add(new MapEntry("hierarchical-child", getText("eaccpf.relations.cpf.relation.type.hierarchical-child")));
        cpfRelationTypeList.add(new MapEntry("temporal", getText("eaccpf.relations.cpf.relation.type.temporal")));
        cpfRelationTypeList.add(new MapEntry("temporal-earlier", getText("eaccpf.relations.cpf.relation.type.temporal-earlier")));
        cpfRelationTypeList.add(new MapEntry("temporal-later", getText("eaccpf.relations.cpf.relation.type.temporal-later")));
        cpfRelationTypeList.add(new MapEntry("family", getText("eaccpf.relations.cpf.relation.type.family")));
        cpfRelationTypeList.add(new MapEntry("associative", getText("eaccpf.relations.cpf.relation.type.associative")));
    }

    protected void setUpResRelationTypeList() {
        // Add empty list entry
        resRelationTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        resRelationTypeList.add(new MapEntry("creatorOf", getText("eaccpf.relations.resource.relation.type.creatorOf")));
        resRelationTypeList.add(new MapEntry("subjectOf", getText("eaccpf.relations.resource.relation.type.subjectOf")));
        resRelationTypeList.add(new MapEntry(OTHER, getText("eaccpf.relations.resource.relation.type.other")));
    }

    protected void setUpFncRelationTypeList() {
        // Add empty list entry
        fncRelationTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        fncRelationTypeList.add(new MapEntry("controls", getText("eaccpf.relations.function.relation.type.controls")));
        fncRelationTypeList.add(new MapEntry("owns", getText("eaccpf.relations.function.relation.type.owns")));
        fncRelationTypeList.add(new MapEntry("performs", getText("eaccpf.relations.function.relation.type.performs")));
    }

    public Set<MapEntry> getCountryList() {
        return countryList;
    }

    public void setCountryList(Set<MapEntry> countryList) {
        this.countryList = countryList;
    }

    public Set<MapEntry> getFormOfNameList() {
        return formNameList;
    }

    public void setFormOfNameList(Set<MapEntry> formOfNameList) {
        this.formNameList = formOfNameList;
    }

    public Set<MapEntry> getComponentOfNameList() {
        return componentNameList;
    }

    public void setComponentOfNameList(Set<MapEntry> componentOfNameList) {
        this.componentNameList = componentOfNameList;
    }

    public Set<MapEntry> getFormNameList() {
        return formNameList;
    }

    public void setFormNameList(Set<MapEntry> formNameList) {
        this.formNameList = formNameList;
    }

    public Set<MapEntry> getComponentNameList() {
        return componentNameList;
    }

    public void setComponentNameList(Set<MapEntry> componentNameList) {
        this.componentNameList = componentNameList;
    }

    public Set<MapEntry> getPlaceEntryList() {
        return placeEntryList;
    }

    public void setPlaceEntryList(Set<MapEntry> placeEntryList) {
        this.placeEntryList = placeEntryList;
    }

    public Set<MapEntry> getAddressComponentTypeList() {
        return addressComponentTypeList;
    }

    public void setAddressComponentTypeList(Set<MapEntry> addressComponentTypeList) {
        this.addressComponentTypeList = addressComponentTypeList;
    }

    public Set<MapEntry> getCpfRelationTypeList() {
        return cpfRelationTypeList;
    }

    public void setCpfRelationTypeList(Set<MapEntry> cpfRelationTypeList) {
        this.cpfRelationTypeList = cpfRelationTypeList;
    }

    public Set<MapEntry> getResRelationTypeList() {
        return resRelationTypeList;
    }

    public void setResRelationTypeList(Set<MapEntry> resRelationTypeList) {
        this.resRelationTypeList = resRelationTypeList;
    }

    public Set<MapEntry> getFncRelationTypeList() {
        return fncRelationTypeList;
    }

    public void setFncRelationTypeList(Set<MapEntry> fncRelationTypeList) {
        this.fncRelationTypeList = fncRelationTypeList;
    }

    public String getCpfType() {
        return cpfType;
    }

    public void setCpfType(String cpfType) {
        this.cpfType = cpfType;
    }

    public void setUpload(File file) {
        this.upload = file;
    }

    public File getUpload() {
        return upload;
    }

    public EacCpfLoader getLoader() {
        return loader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUseMode() {
        return useMode;
    }

    public void setUseMode(String useMode) {
        this.useMode = useMode;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getDefaultScript() {
        return defaultScript;
    }

    public String getCpfTypeDescriptionText() {
        return cpfTypeDescriptionText;
    }

    public String getCpfTypeIdentifierText() {
        return cpfTypeIdentifierText;
    }

    public String getOpenLabel() {
        return openLabel;
    }

    public String getFileToLoad() {
        return fileToLoad;
    }

    public void setFileToLoad(String fileToLoad) {
        this.fileToLoad = fileToLoad;
    }

    public void setDefaultRightsInformation(String defaultRightsInformation) {
        this.defaultRightsInformation = defaultRightsInformation;
    }

    public String getDefaultRightsInformation() {
        return defaultRightsInformation;
    }

    public void setDefaultRightsDescription(String defaultRightsDescription) {
        this.defaultRightsDescription = defaultRightsDescription;
    }

    public void setDefaultRightsHolder(String defaultRightsHolder) {
        this.defaultRightsHolder = defaultRightsHolder;
    }

    public String getDefaultRightsDescription() {
        return defaultRightsDescription;
    }

    public String getDefaultRightsHolder() {
        return defaultRightsHolder;
    }
}
