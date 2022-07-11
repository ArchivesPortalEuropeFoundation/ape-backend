package eu.apenet.commons.infraestructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.DisplayUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.User;

/**
 *
 * @author Eloy Date: 8th, Nov
 *
 * This class represents the Navigation Tree
 */
public class NavigationTree {

    private static final Logger log = Logger.getLogger(NavigationTree.class);
    private ResourceBundleSource resourceBundleSource;

    public NavigationTree(ResourceBundleSource resourceBundleSource) throws APEnetException {
        this.resourceBundleSource = resourceBundleSource;
    }

    protected String getLanguage() {
        return resourceBundleSource.getLocale().getLanguage().substring(0, 2);
    }

    public List<CountryUnit> getALCountriesWithArchivalInstitutionsWithEAG() throws APEnetException {
        List<CountryUnit> countries = new ArrayList<CountryUnit>();
        try {
            List<Country> cos = DAOFactory.instance().getCountryDAO().getCountriesWithArchivalInstitutionsWithEAG();
            for (Country co : cos) {
                countries.add(getCountryUnit(co));
            }

        } catch (Exception e) {
            log.error("Error retrieving all the countries within the Archival Landscape.", e);
            throw new APEnetException(e.getMessage(), e);
        }
        return countries;
    }

    public CountryUnit getCountryUnit(Country cos) throws APEnetException {
        if (cos != null) {
            String otherCountryName = DisplayUtils.getLocalizedCountryNameFixed(resourceBundleSource, cos);
            return new CountryUnit(cos, otherCountryName);
        } else {
            return new CountryUnit(null, "No Country");
        }
    }
//	//This method obtains all the countries within the Archival Landscape

    public CountryUnit getCountry(Integer couId) throws APEnetException {
        try {
            Country cos = DAOFactory.instance().getCountryDAO().findById(couId);
            return getCountryUnit(cos);
        } catch (Exception e) {
            log.error("Error retrieving all the countries within the Archival Landscape.", e);
            throw new APEnetException(e.getMessage(), e);
        }
    }

    //This method obtains all the Archival Institutions which have a specific parent_ai_id
    public List<ArchivalInstitutionUnit> getArchivalInstitutionsByParentAiId(String parentAiId) throws APEnetException {
        List<ArchivalInstitutionUnit> archivalInstitutionUnitList = new ArrayList<ArchivalInstitutionUnit>();

        //It is necessary to find out the parent_ai_id
        if (parentAiId.startsWith("country_")) {
            //The parent for this archival institution is a country.
            //It is necessary to find all the archival institutions for this country selected
            //which parent_ai_id is null
            Integer couId = Integer.parseInt(parentAiId.substring(parentAiId.lastIndexOf('_') + 1));
            ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
            List<ArchivalInstitution> archivalInstitutionList = archivalInstitutionDao.getRootArchivalInstitutionsByCountryId(couId);
            for (ArchivalInstitution anArchivalInstitutionList : archivalInstitutionList) {
                Integer numberOfArchivalInstitutions = 0;
                if (anArchivalInstitutionList.isGroup()) {
                    numberOfArchivalInstitutions = archivalInstitutionDao.countArchivalInstitutionsByParentAiId(anArchivalInstitutionList.getAiId());
                }
                archivalInstitutionUnitList.add(new ArchivalInstitutionUnit(anArchivalInstitutionList.getAiId(), anArchivalInstitutionList.getAiname(), null, anArchivalInstitutionList.getEncodedRepositorycode(), anArchivalInstitutionList.getEagPath(), anArchivalInstitutionList.isGroup(), numberOfArchivalInstitutions, getLanguage(), anArchivalInstitutionList.getAlorder()));
            }

        } else if (parentAiId.startsWith("aicontent_") || parentAiId.startsWith("ainocontent_") || parentAiId.startsWith("aigroup_")) {
            //The parent for this archival institution is an archival institution or an archival institution group.
            //It is necessary to find all the archival institutions for this archival institution selected
            //which parent_ai_id is equal to ai_id
            Integer pId = Integer.parseInt(parentAiId.substring(parentAiId.lastIndexOf('_') + 1));

            ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
            List<ArchivalInstitution> archivalInstitutionList = archivalInstitutionDao.getArchivalInstitutionsByParentAiId(pId, false);

            for (ArchivalInstitution anArchivalInstitutionList : archivalInstitutionList) {
                Integer numberOfArchivalInstitutions = 0;
                if (anArchivalInstitutionList.isGroup()) {
                    numberOfArchivalInstitutions = archivalInstitutionDao.countArchivalInstitutionsByParentAiId(anArchivalInstitutionList.getAiId());
                }
                archivalInstitutionUnitList.add(new ArchivalInstitutionUnit(anArchivalInstitutionList.getAiId(), anArchivalInstitutionList.getAiname(), null, anArchivalInstitutionList.getEncodedRepositorycode(), anArchivalInstitutionList.getEagPath(), anArchivalInstitutionList.isGroup(), numberOfArchivalInstitutions, getLanguage(), anArchivalInstitutionList.getAlorder()));
            }
        }
        return archivalInstitutionUnitList;
    }

    public List<ArchivalInstitutionUnit> filterArchivalInstitutionsWithEAG(List<ArchivalInstitutionUnit> archivalInstitutionList) {
        List<ArchivalInstitutionUnit> archivalInstitutionListTmp = new ArrayList<ArchivalInstitutionUnit>();
        for (ArchivalInstitutionUnit archivalInstitution : archivalInstitutionList) {
            if (archivalInstitution.getPathEAG() != null) {
                archivalInstitutionListTmp.add(archivalInstitution);
            }
        }
        return archivalInstitutionListTmp;
    }

    public ResourceBundleSource getResourceBundleSource() {
        return resourceBundleSource;
    }
}
