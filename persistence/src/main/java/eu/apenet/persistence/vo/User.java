package eu.apenet.persistence.vo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dashboard_user")
public class User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1492601852051717666L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", foreignKey = @ForeignKey(name="partner_cou_id_fkey"))
	private Country country;
	@Column(name = "country_id", insertable = false, updatable = false)
	private Integer countryId;
    @Column(name = "is_active")
	private boolean isActive;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_role_id", foreignKey = @ForeignKey(name="partner_role_id_fkey"))
	private UserRole userRole;
	@Column(name = "email_address")
	private String emailAddress;
	private String password;
	@Column(name = "secret_question")
	private String secretQuestion;
	@Column(name = "secret_answer")
	private String secretAnswer;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@OneToMany(mappedBy="partner")
	private Set<ArchivalInstitution> archivalInstitutions = new HashSet<ArchivalInstitution>(0);
	@Column(name = "contact_forms_as_cm")
	private boolean contactFormsAsCC;

	public User() {
	}

	public User(int PId, Country country, UserRole roleType, String emailAddress, String pwd) {
		this.id = PId;
		this.country = country;
		this.userRole = roleType;
		this.emailAddress = emailAddress;
		this.password = pwd;
	}

	public User(boolean isActive, String emailAddress, String pwd) {
		this.isActive = isActive;
		this.emailAddress = emailAddress;
		this.password = pwd;
	}

	public User(String emailAddress, String pwd) {
		this.emailAddress = emailAddress;
		this.password = pwd;
	}

	public User(Country country, boolean isActive, String emailAddress, String pwd) {
		this.country = country;
		this.isActive = isActive;
		this.emailAddress = emailAddress;
		this.password = pwd;
	}

	public User(Country country, boolean isActive, UserRole roleType, String emailAddress, String pwd) {
		this.country = country;
		this.isActive = isActive;
		this.userRole = roleType;
		this.emailAddress = emailAddress;
		this.password = pwd;
	}

	public User(boolean isActive, UserRole roleType, String firstName, String lastName, String emailAddress,
			String pwd) {
		this.isActive = isActive;
		this.userRole = roleType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = pwd;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int PId) {
		this.id = PId;
	}

	@Deprecated
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public UserRole getUserRole() {
		return this.userRole;
	}

	public void setUserRole(UserRole roleType) {
		this.userRole = roleType;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public String getSecretQuestion() {
		return this.secretQuestion;
	}

	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	public String getSecretAnswer() {
		return this.secretAnswer;
	}

	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String PName) {
		this.firstName = PName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String PSurname) {
		this.lastName = PSurname;
	}


	public Set<ArchivalInstitution> getArchivalInstitutions() {
		return this.archivalInstitutions;
	}

	public void setArchivalInstitutions(Set<ArchivalInstitution> archivalInstitutions) {
		this.archivalInstitutions = archivalInstitutions;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

	public boolean isActive() {
		return this.isActive;
	}

	public void setContactFormsAsCC(boolean contactFormsAsCC) {
		this.contactFormsAsCC = contactFormsAsCC;
	}

	public boolean isContactFormsAsCC() {
		return contactFormsAsCC;
	}
}
