/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.ApiKey;

/**
 *
 * @author kaisar
 */
public interface ApiKeyDAO extends GenericDAO<ApiKey, Integer> {

    public ApiKey findByLiferayUserId(long liferayUserId);

    public ApiKey findByModxUserId(long modxUserId);
}
