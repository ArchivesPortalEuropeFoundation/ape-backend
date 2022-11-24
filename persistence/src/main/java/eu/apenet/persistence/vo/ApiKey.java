/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

/**
 *
 * @author Mahbub
 */
@Entity
@Table(name = "api_key")
public class ApiKey extends BaseEntity {
    private long liferayUserId;

    @Column(nullable = true)
    private long modxUserId;

    private String firstName;
    private String lastName;

    @Email
    @Column(nullable=false)
    private String emailAddress;

    @URL
    private String url;

    @Column(length = 36, nullable=false)
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public long getLiferayUserId() {
        return liferayUserId;
    }

    public void setLiferayUserId(long liferayUserId) {
        this.liferayUserId = liferayUserId;
    }

    public void setModxUserId(long modxUserId) {
        this.modxUserId = modxUserId;
    }

    public long getModxUserId() {
        return modxUserId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
