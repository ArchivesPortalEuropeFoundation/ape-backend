package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Country;

/**
 * 
 * @author Patricia
 *
 */

public interface CountryDAO extends GenericDAO<Country, Integer> {
	public List<Country> getCountriesOrderByName();
	public List<Country> getCountries(String isoname);
	public Country getCountryByCname (String cname);
	public List<Country> getCountriesWithArchivalInstitutionsWithEAG();
	public List<Country> getCountriesWithSearchableItems();
	public List<Country> getCountries(List<Integer> countryIds);
}

