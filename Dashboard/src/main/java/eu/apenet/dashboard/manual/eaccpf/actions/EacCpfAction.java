/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.actions;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.manual.eaccpf.util.MapEntry;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.RightsInformation;

import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author papp
 */
public abstract class EacCpfAction extends AbstractInstitutionAction {

    private Set<MapEntry> languages = new TreeSet<MapEntry>();
    private Map<String, String> scriptList = new TreeMap<String, String>();

    protected static final String CORPORATE_BODY = "corporateBody";
    protected static final String FAMILY = "family";
    protected static final String PERSON = "person";
    protected static final String EMPTY_KEY = "";
    protected static final String EMPTY_VALUE = "----";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;

    private String apeId;

    private List<SelectItem> rightsList = new ArrayList<>();
    private static final String IN_COPYRIGHT_EU_ORPHAN_WORK = "InC-EU-OW";
    private static final String NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS = "NoC-OKLR";
    private static final String COPYRIGHT_NOT_EVALUATED = "CNE";

    private ArchivalInstitution archivalInstitution;
    private String aiDefaultRightsInformation;

    public EacCpfAction() {
        Random random = new Random();
        long fakeId = random.nextLong();
        this.apeId = Long.toString(fakeId);
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();

        archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
        aiDefaultRightsInformation = archivalInstitution.getRightsInformation().getId()+"";

        List<RightsInformation> rightsInformations = DAOFactory.instance().getRightsInformationDAO().getRightsInformations();
        rightsInformations.forEach((rightsInformation) -> {
            if (!(rightsInformation.getAbbreviation().equals(IN_COPYRIGHT_EU_ORPHAN_WORK)
                    || rightsInformation.getAbbreviation().equals(NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS)
                    || rightsInformation.getAbbreviation().equals(COPYRIGHT_NOT_EVALUATED))) {
                SelectItem selectItem = new SelectItem(rightsInformation.getId(), rightsInformation.getRightsName());
                rightsList.add(selectItem);
            }
        });
    }

    protected void setUpLanguages() {
        //Base variable for setting up the list
        String[] isoLanguages = Locale.getISOLanguages();

        // Add empty map entry
        languages.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));
        // Add 639-2/B variants below this line
        languages.add(new MapEntry("alb", new Locale("sq").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("arm", new Locale("hy").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("baq", new Locale("eu").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("bur", new Locale("my").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("chi", new Locale("zh").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("cze", new Locale("cs").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("dut", new Locale("nl").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("fre", new Locale("fr").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("geo", new Locale("ka").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("ger", new Locale("de").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("gre", new Locale("el").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("ice", new Locale("is").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("mac", new Locale("mk").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("mao", new Locale("mi").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("may", new Locale("ms").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("per", new Locale("fa").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("rum", new Locale("ro").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("slo", new Locale("sk").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("tib", new Locale("bo").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("wel", new Locale("cy").getDisplayLanguage(Locale.ENGLISH)));
        // Add remaining 639-2 variants; any T variants of countries listed above will not be added to the set
        for (String language : isoLanguages) {
            Locale locale = new Locale(language);
            String languageCode = locale.getISO3Language();
            String languageDescription = locale.getDisplayLanguage(Locale.ENGLISH);
            languages.add(new MapEntry(languageCode, languageDescription));
        }
    }

     protected void setUpScriptList() {
        // Add empty list entry
        scriptList.put(EMPTY_KEY, EMPTY_VALUE);

        scriptList.put("Arab", getText("eaccpf.commons.script.Arab"));
        scriptList.put("Armn", getText("eaccpf.commons.script.Armn"));
        scriptList.put("Cyrl", getText("eaccpf.commons.script.Cyrl"));
        scriptList.put("Geor", getText("eaccpf.commons.script.Geor"));
        scriptList.put("Grek", getText("eaccpf.commons.script.Grek"));
        scriptList.put("Hebr", getText("eaccpf.commons.script.Hebr"));
        scriptList.put("Latn", getText("eaccpf.commons.script.Latn"));
    }

    public Set<MapEntry> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<MapEntry> languages) {
        this.languages = languages;
    }

    public Map<String, String> getScriptList() {
        return scriptList;
    }

    public void setScriptList(Map<String, String> scriptList) {
        this.scriptList = scriptList;
    }

    public String getDefaultCpfType() {
        return "person";
    }

    public String getApeId() {
        return apeId;
    }

    public void setApeId(String apeId) {
        this.apeId = apeId;
    }

    public void setRightsList(List<SelectItem> rightsList) {
        this.rightsList = rightsList;
    }

    public List<SelectItem> getRightsList() {
        return rightsList;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setAiDefaultRightsInformation(String aiDefaultRightsInformation) {
        this.aiDefaultRightsInformation = aiDefaultRightsInformation;
    }

    public String getAiDefaultRightsInformation() {
        return aiDefaultRightsInformation;
    }
}
