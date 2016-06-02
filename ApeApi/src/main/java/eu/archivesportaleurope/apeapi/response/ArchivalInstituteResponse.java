/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 *
 * @author kaisar
 */
public class ArchivalInstituteResponse {

    private String name;
    private int id;
    private String country;
    private int countryId;
    private int numberOfFindingAids;
    private String repositoryCode;

    public ArchivalInstituteResponse(ArchivalInstitution ai) {
        this.name = ai.getAiname();
        this.country = ai.getCountry().getCname();
        this.id = ai.getAiId();
        this.countryId = ai.getCountryId();
        this.numberOfFindingAids = ai.getFindingAids().size();
        this.repositoryCode = ai.getRepositorycode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getNumberOfFindingAids() {
        return numberOfFindingAids;
    }

    public void setNumberOfFindingAids(int numberOfFindingAids) {
        this.numberOfFindingAids = numberOfFindingAids;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }
}