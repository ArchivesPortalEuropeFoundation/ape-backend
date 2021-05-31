/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.RightsInformation;
import java.util.List;

/**
 *
 * @author apef
 */
public interface RightsInformationDAO extends GenericDAO<RightsInformation, Integer>{
    public List<RightsInformation> getRightsInformations();
    public RightsInformation getRightsInformation(Integer rightsInformationId);
    public RightsInformation getRightsInformation(String rightsInformationAbbreviation);
}
