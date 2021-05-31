/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.rightsinformation;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.RightsInformation;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author apef
 */
public class RightsInformationAction extends AbstractInstitutionAction {

    private final Logger log = Logger.getLogger(getClass());

    private static final String PUBLIC_DOMAIN_MARK = "PDM";
    private static final String CREATIVECOMMONS_CC0 = "CC0";
    private static final String CREATIVECOMMONS_CC_BY = "CC BY";
    private static final String CREATIVECOMMONS_CC_BY_SA = "CC BY-SA";
    private static final String CREATIVECOMMONS_CC_BY_ND = "CC BY-ND";
    private static final String CREATIVECOMMONS_CC_BY_NC = "CC BY-NC";
    private static final String CREATIVECOMMONS_CC_BY_NC_SA = "CC BY-NC-SA";
    private static final String CREATIVECOMMONS_CC_BY_NC_ND = "CC BY-NC-ND";
    private static final String IN_COPYRIGHT_EU_ORPHAN_WORK = "InC-EU-OW";
    private static final String NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS = "NoC-OKLR";
    private static final String COPYRIGHT_NOT_EVALUATED = "CNE";

    private List<SelectItem> rightsList = new ArrayList<>();

    private String rights;
    private String description;
    private String rightsHolder;
    private boolean newInstitution = false;
    private boolean searchableItems;
    private boolean ccOrPdmLicence;
    private String currentRightsSelection;

    @Override
    public String execute() throws Exception {
        ArchivalInstitution archivalInstitution;
        String state = SUCCESS;

        try {
            archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
            if (archivalInstitution.getEagPath() == null) {
                if (archivalInstitution.getRightsInformation() == null) {
                    state = INPUT;
                    newInstitution = true;
                    log.info("New institution");
                } else {
                    state = "noEAG";
                }
            }
            retrieveRightsInfoForInstitution(archivalInstitution);
            searchableItems = archivalInstitution.isContainSearchableItems();
        } catch (Exception e) {
            log.error("Something has happened!");
        }
        return state;
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
        List<RightsInformation> rightsInformations = DAOFactory.instance().getRightsInformationDAO().getRightsInformations();
        rightsInformations.forEach((rightsInformation) -> {
            if (!(rightsInformation.getAbbreviation().equals(IN_COPYRIGHT_EU_ORPHAN_WORK)
                    || rightsInformation.getAbbreviation().equals(NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS)
                    || rightsInformation.getAbbreviation().equals(COPYRIGHT_NOT_EVALUATED))) {
                rightsList.add(new SelectItem(rightsInformation.getId(), rightsInformation.getRightsName()));
            }
        });
    }

    private void retrieveRightsInfoForInstitution(ArchivalInstitution archivalInstitution) {
        RightsInformation rightsPreselection;
        ccOrPdmLicence = false;
        if (archivalInstitution.getRightsInformation() == null) {
            rightsPreselection = DAOFactory.instance().getRightsInformationDAO().getRightsInformation(CREATIVECOMMONS_CC_BY_SA);
            rightsHolder = archivalInstitution.getAiname();
        } else {
            rightsPreselection = archivalInstitution.getRightsInformation();
            if (archivalInstitution.getRightsHolder() != null) {
                rightsHolder = archivalInstitution.getRightsHolder();
            } else {
                rightsHolder = archivalInstitution.getAiname();
            }
            if (rightsPreselection.getAbbreviation().equals(PUBLIC_DOMAIN_MARK)
                    || rightsPreselection.getAbbreviation().equals(CREATIVECOMMONS_CC0)
                    || rightsPreselection.getAbbreviation().equals(CREATIVECOMMONS_CC_BY)
                    || rightsPreselection.getAbbreviation().equals(CREATIVECOMMONS_CC_BY_NC)
                    || rightsPreselection.getAbbreviation().equals(CREATIVECOMMONS_CC_BY_NC_ND)
                    || rightsPreselection.getAbbreviation().equals(CREATIVECOMMONS_CC_BY_NC_SA)
                    || rightsPreselection.getAbbreviation().equals(CREATIVECOMMONS_CC_BY_ND)
                    || rightsPreselection.getAbbreviation().equals(CREATIVECOMMONS_CC_BY_SA)) {
                ccOrPdmLicence = true;
            }
        }
        rights = rightsPreselection.getId().toString();
        currentRightsSelection = rights;
        description = rightsPreselection.getDescription();
    }

    public String cancel() throws Exception {
        log.info("Rights declaration cancelled");
        return SUCCESS;
    }

    public String save() throws Exception {
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitution(getAiId());
        RightsInformation rightsInformation = DAOFactory.instance().getRightsInformationDAO().getRightsInformation(Integer.parseInt(rights));
        archivalInstitution.setRightsInformation(rightsInformation);
        archivalInstitution.setRightsInformationId(rightsInformation.getId());
        if (!archivalInstitution.getAiname().equals(rightsHolder)) {
            archivalInstitution.setRightsHolder(rightsHolder);
        }
        archivalInstitutionDAO.store(archivalInstitution);
        log.info("Rights declaration saved");
        return SUCCESS;
    }

    public List<SelectItem> getRightsList() {
        return rightsList;
    }

    public void setRightsList(List<SelectItem> rightsList) {
        this.rightsList = rightsList;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRightsHolder() {
        return rightsHolder;
    }

    public void setRightsHolder(String rightsHolder) {
        this.rightsHolder = rightsHolder;
    }

    public boolean isNewInstitution() {
        return newInstitution;
    }

    public boolean isSearchableItems() {
        return searchableItems;
    }

    public boolean isCcOrPdmLicence() {
        return ccOrPdmLicence;
    }

    public String getCurrentRightsSelection() {
        return currentRightsSelection;
    }
}
