package eu.apenet.dashboard.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import eu.apenet.commons.infraestructure.EmailComposer;
import eu.apenet.commons.infraestructure.EmailComposer.Priority;
import eu.apenet.commons.infraestructure.Emailer;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.DashboardAPEnetException;
import eu.apenet.dashboard.exception.NotAuthorizedException;
import eu.apenet.dashboard.infraestructure.PasswordGenerator;
import eu.apenet.dashboard.security.cipher.BasicDigestPwd;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.SentMailRegisterDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.dao.UserRoleDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.OaiPmhStatus;
import eu.apenet.persistence.vo.SentMailRegister;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;
import eu.archivesportaleurope.harvester.oaipmh.HarvestObject.DateHarvestModel;
import eu.archivesportaleurope.util.ApeUtil;

/**
 * Service for manage users in the Dashboard
 * 
 * @author bastiaan
 * 
 */
public final class UserService {
	private static final String DEFAULT_SECRET_QUESTION = "What is your generated security answer?";

	private final static String ADMINS_EMAIL = PropertiesUtil.get(PropertiesKeys.APE_EMAILS_ADMINS);
	/**
	 * Delete Country Manager by id
	 * 
	 * @param id
	 *            id of User
	 */
	public static void deleteCountryManager(Integer id) {
		if (SecurityContext.get().isAdminOrCoordinator()) {
			UserDAO partnerDao = DAOFactory.instance().getUserDAO();
			User partner = partnerDao.findById(id);
			if (UserRole.ROLE_COUNTRY_MANAGER.equals(partner.getUserRole().getRole())
					&& partner.getArchivalInstitutions().size() == 0) {
				ChangeControl.logOperation("CountryManager: " + partner.getName() + " from country: "
						+ partner.getCountry().getCname() + " is deleted");
				Country country = partner.getCountry();
				partnerDao.delete(partner);
				sendEmailDeleteCountryManager(partner, country);
			}
		}
	}

	/**
	 * Delete Institution Manager
	 * 
	 * @param aiId
	 *            id of Archival Institution
	 * @param id
	 *            id of User
	 */
	public static void deleteInstitutionManager(Integer aiId, Integer id) {
		SecurityContext securityContext = SecurityContext.get();
		if (securityContext.isAdminOrCoordinator() || securityContext.isCountryManager()) {
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			UserDAO partnerDao = DAOFactory.instance().getUserDAO();
			ArchivalInstitution archivalInstitution = archivalInstitutionDAO.findById(aiId);
			User partner = archivalInstitution.getPartner();
			boolean authorized = false;
			if (securityContext.isAdminOrCoordinator()) {
				authorized = true;
			} else if (securityContext.isCountryManager()
					&& securityContext.getCountryId().equals(archivalInstitution.getCountryId())) {
				authorized = true;
			}
			if (authorized) {
				int numberOfArchives = partner.getArchivalInstitutions().size();
				archivalInstitution.setPartner(null);
				archivalInstitution.setPartnerId(null);
				archivalInstitutionDAO.store(archivalInstitution);
				if (numberOfArchives == 1) {
					partnerDao.delete(partner);
					ChangeControl.logOperation("User: " + partner.getName() + " from archive: "
							+ archivalInstitution.getAiname() + " is deleted.");
					sendEmailDeleteInstitutionManager(partner, archivalInstitution);
				} else {
					ChangeControl.logOperation("User: " + partner.getName() + " from archive: "
							+ archivalInstitution.getAiname() + " is no institution manager.");
					sendEmailDeleteAiFromInstitutionManager(partner, archivalInstitution);
				}

			}
		}
	}

	/**
	 * Enable an user
	 * 
	 * @param id
	 *            id of User
	 */
	public static void enableUser(Integer id) {
		SecurityContext securityContext = SecurityContext.get();
		if (securityContext.isAdminOrCoordinator() || securityContext.isCountryManager()) {
			UserDAO partnerDao = DAOFactory.instance().getUserDAO();
			User partner = partnerDao.findById(id);
			boolean authorized = false;
			if (securityContext.isAdminOrCoordinator()) {
				authorized = true;
			} else if (securityContext.isCountryManager()
					&& securityContext.getCountryId().equals(partner.getCountryId())) {
				authorized = true;
			}
			if (authorized) {
				ChangeControl.logOperation("Partner: " + partner.getName() + " is enabled");
				partner.setActive(true);
				partnerDao.update(partner);
			}
		}
	}

	/**
	 * Disable an user
	 * 
	 * @param partnerId
	 *            id of User
	 */
	public static void disableUser(Integer partnerId) {
		SecurityContext securityContext = SecurityContext.get();
		if (securityContext.isAdminOrCoordinator() || securityContext.isCountryManager()) {
			UserDAO partnerDao = DAOFactory.instance().getUserDAO();
			User partner = partnerDao.findById(partnerId);
			boolean authorized = false;
			if (securityContext.isAdminOrCoordinator()) {
				authorized = true;
			} else if (securityContext.isCountryManager()
					&& securityContext.getCountryId().equals(partner.getCountryId())) {
				authorized = true;
			}
			if (authorized) {
				ChangeControl.logOperation("Partner: " + partner.getName() + " is disabled");
				partner.setActive(false);
				partnerDao.update(partner);
			}
		}
	}

	/**
	 * Create a Country Manager
	 * 
	 * @param user
	 *            User template with input field values
	 * @param countryId
	 *            Id of the selected country
	 */
	public static void createCountryManager(User user, Integer countryId) {
		if (SecurityContext.get().isAdminOrCoordinator()) {
			UserDAO partnerDao = DAOFactory.instance().getUserDAO();
			UserRoleDAO roleTypeDAO = DAOFactory.instance().getUserRoleDAO();
			user.setActive(true);
			user.setUserRole(roleTypeDAO.findByExample(new UserRole(UserRole.ROLE_COUNTRY_MANAGER)).get(0));
			Country country = DAOFactory.instance().getCountryDAO().findById(countryId);
			user.setCountry(country);
			user.setPassword(PasswordGenerator.getRandomString());
			user.setSecretQuestion(DEFAULT_SECRET_QUESTION);
			user.setSecretAnswer(PasswordGenerator.getRandomString());
			user.setCountryId(country.getId());
			String userTypedPassword = user.getPassword();
			user.setPassword(BasicDigestPwd.generateDigest(userTypedPassword));
			partnerDao.store(user);
			ChangeControl.logOperation("Country Manager: " + user.getName() + " is created");
			sendEmailCreateCountryManager(user, userTypedPassword);
		}
	}

	/**
	 * Create a new Institution Manager or associate an existing one
	 * 
	 * @param inputUser
	 *            User template with input field values
	 * @param aiId
	 *            Id of Archival Institution that need to be associated
	 * @param existingUserId
	 *            Id of existing user
	 */
	public static void createOrAssociateInstitutionManager(User inputUser, Integer aiId, Integer existingUserId) {
		SecurityContext securityContext = SecurityContext.get();
		if (securityContext.isAdminOrCoordinator() || securityContext.isCountryManager()) {
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			ArchivalInstitution archivalInstitution = archivalInstitutionDAO.findById(aiId);

			boolean authorized = false;
			if (securityContext.isAdminOrCoordinator()) {
				authorized = true;
			} else if (securityContext.isCountryManager()
					&& securityContext.getCountryId().equals(archivalInstitution.getCountryId())) {
				authorized = true;
			}
			if (authorized) {
				UserDAO partnerDao = DAOFactory.instance().getUserDAO();
				User partner = null;
				String userTypedPassword = null;
				if (existingUserId == null) {
					partner = inputUser;
					UserRoleDAO roleTypeDAO = DAOFactory.instance().getUserRoleDAO();
					partner.setActive(true);
					partner.setUserRole(roleTypeDAO.findByExample(new UserRole(UserRole.ROLE_INSTITUTION_MANAGER)).get(
							0));
					partner.setCountry(archivalInstitution.getCountry());
					partner.setCountryId(archivalInstitution.getCountryId());
					partner.setPassword(PasswordGenerator.getRandomString());
					partner.setSecretQuestion(DEFAULT_SECRET_QUESTION);
					partner.setSecretAnswer(PasswordGenerator.getRandomString());
					userTypedPassword = partner.getPassword();
					partner.setPassword(BasicDigestPwd.generateDigest(userTypedPassword));
					partner = partnerDao.store(partner);
				} else {
					partner = partnerDao.findById(existingUserId);
					if (!archivalInstitution.getCountryId().equals(partner.getCountryId())) {
						throw new NotAuthorizedException("Invalid partner");
					}

				}
				archivalInstitution.setPartner(partner);
				archivalInstitution.setPartnerId(partner.getId());
				archivalInstitutionDAO.store(archivalInstitution);
				if (existingUserId == null) {
					ChangeControl.logOperation("Institution Manager: " + partner.getName() + " is created");
					sendEmailCreateInstitutionManager(partner, archivalInstitution, userTypedPassword);
				} else {
					ChangeControl.logOperation("Institution Manager: " + partner.getName()
							+ " associated with institution: " + archivalInstitution.getAiname());
					sendEmailAssociateInstitutionManager(partner, archivalInstitution);
				}
			}

		}
	}

	/**
	 * Change password
	 * 
	 * @param password
	 *            New password
	 * @param validationLink
	 *            validation Link
	 */
	public static void changePassword(String password, String validationLink) {
		UserDAO partnerdao = DAOFactory.instance().getUserDAO();
		SentMailRegisterDAO daoSentMailRegister = DAOFactory.instance().getSentMailRegisterDAO();
		SentMailRegister sentmailregister = daoSentMailRegister.exitsValidationLinkSentMailRegister(validationLink);
		User partner = sentmailregister.getUser();
		partner.setPassword(BasicDigestPwd.generateDigest(password));
		partnerdao.store(partner);
		daoSentMailRegister.delete(sentmailregister);
	}

	/**
	 * Update User
	 * 
	 * @param inputUser
	 */
	public static void updateUser(User inputUser) {
		UserDAO userDAO = DAOFactory.instance().getUserDAO();
		User user = userDAO.findById(inputUser.getId());
		user.setFirstName(inputUser.getFirstName());
		user.setLastName(inputUser.getLastName());
		user.setEmailAddress(inputUser.getEmailAddress());
		user.setSecretAnswer(inputUser.getSecretAnswer());
		user.setSecretQuestion(inputUser.getSecretQuestion());
		if (StringUtils.isNotBlank(inputUser.getPassword())) {
			String newPassword = BasicDigestPwd.generateDigest(inputUser.getPassword());
			if (!user.getPassword().equals(newPassword)) {
				user.setPassword(newPassword);
			}
		}
		userDAO.store(user);
		ChangeControl.logOperation(ChangeControl.MODIFY_REGISTRATION_DATA_OPERATION);
	}

	public static boolean exitsEmailUser(String email) {
		boolean result = false;
		UserDAO partnerdao = DAOFactory.instance().getUserDAO();
		User exitsEmailPartner = partnerdao.exitsEmailUser(email);
		if (exitsEmailPartner != null) {
			result = true;
		}
		return result;
	}

	public static boolean exitsValidationLinkBefore(String validationLink) {
		boolean result = false;
		SentMailRegisterDAO daoSentMailRegister = DAOFactory.instance().getSentMailRegisterDAO();
		SentMailRegister exitsSentmailregister = daoSentMailRegister.exitsSentMailRegisterOnDate(validationLink,
				new Date());
		if (exitsSentmailregister != null) {
			result = true;
		}
		return result;
	}

	public static boolean correctSecretAnswerByEmail(String email, String secretAnswer) {
		boolean result = false;
		UserDAO partnerdao = DAOFactory.instance().getUserDAO();
		User exitsPartnerByEmail = partnerdao.exitsEmailUser(email);
		if (exitsPartnerByEmail.getSecretAnswer().equals(secretAnswer)) {
			result = true;
		}
		return result;
	}

	public static User getForgetUserByEmail(String email) {
		UserDAO partnerdao = DAOFactory.instance().getUserDAO();
		return partnerdao.exitsEmailUser(email);
	}

	public static void sendChangePasswordLink(String emailAddress) throws DashboardAPEnetException {

		String errorMessage = "The process failed. It was not possible to send a recover pwd mail to the normal user";
		try {
			// Init factorys and daos.
			UserDAO partnerdao = DAOFactory.instance().getUserDAO();
			SentMailRegisterDAO daoSentMailRegister = DAOFactory.instance().getSentMailRegisterDAO();
			// Init encoder
			BasicSecureRandomNuGenerator generator = new BasicSecureRandomNuGenerator();

			User exitsUser = partnerdao.exitsEmailUser(emailAddress);
			if (exitsUser != null) {
				// Generate validation link with salt
				String validationLink = generator.generateSecureRandomToken();

				// Init mail composer and sender
				sendEmailChangePasswordLink(exitsUser, validationLink);

				// Generate sent_mail_register
				SentMailRegister sentMailRegister = new SentMailRegister();
				sentMailRegister.setValidationLink(validationLink);
				sentMailRegister.setDate(new Date());
				sentMailRegister.setEmailAddress(exitsUser.getEmailAddress());
				sentMailRegister.setUser(exitsUser);

				// Store sent_mail_register
				daoSentMailRegister.store(sentMailRegister);

			} else {
				throw new DashboardAPEnetException(errorMessage + "There is not user with this email");
			}

		} catch (Exception e) {
			throw new DashboardAPEnetException(e.getMessage() + errorMessage, e);
		}
	}

	private static void sendEmailChangePasswordLink(User partner, String link) throws DashboardAPEnetException {
		String nameAction = "changeForgetPwd.action?validation_link=" + link;
		String url = getBasePath() + nameAction;
		EmailComposer emailComposer = new EmailComposer("emails/changePassword.txt",
				"Link for changing password for the Archives Portal Europe Dashboard.", true, false);
		emailComposer.setProperty("name", partner.getName());
		emailComposer.setProperty("validation-link", url);
		Emailer emailer = new Emailer();
		emailer.sendMessage(partner.getEmailAddress(), null, null, null, emailComposer);

	}

	public static void sendEmailFeedback(String email, String body, String to) throws DashboardAPEnetException {
		EmailComposer emailComposer = new EmailComposer("emails/feedback.txt",
				"Dashboard feedback comments and suggestions", true, false);
		emailComposer.setProperty("email", email);
		emailComposer.setProperty("body", body);
		emailComposer.setProperty("to", to);
		Emailer emailer = new Emailer();
		emailer.sendMessage(to, null, null, email, emailComposer);
	}

	private static void sendEmailCreateCountryManager(User partner, String password) {
		User currentPartner = SecurityService.getCurrentPartner();
		EmailComposer emailComposer = new EmailComposer("emails/createCountryManager.txt",
				"Country Manager account for Archives Portal Europe created.", true, false);
		emailComposer.setProperty("name", partner.getName());
		emailComposer.setProperty("emailAddress", partner.getEmailAddress());
		emailComposer.setProperty("password", password);
		emailComposer.setProperty("country", partner.getCountry().getCname());
		emailComposer.setProperty("secretQuestion", partner.getSecretQuestion());
		emailComposer.setProperty("secretAnswer", partner.getSecretAnswer());
		emailComposer.setProperty("userManager", currentPartner.getName());
		emailComposer.setProperty("dashboardUrl", getBasePath());
		emailComposer.addAttachment("emails/termsofuse.txt", true, "plain/text");
		Emailer emailer = new Emailer();
		emailer.sendMessage(partner.getEmailAddress(), null, currentPartner.getEmailAddress(),
				currentPartner.getEmailAddress(), emailComposer);

	}

	private static void sendEmailCreateInstitutionManager(User partner, ArchivalInstitution archivalInstitution,
			String password) {
		User currentPartner = SecurityService.getCurrentPartner();
		EmailComposer emailComposer = new EmailComposer("emails/createInstitutionManager.txt",
				"Institution Manager account for Archives Portal Europe created.", true, false);
		emailComposer.setProperty("name", partner.getName());
		emailComposer.setProperty("emailAddress", partner.getEmailAddress());
		emailComposer.setProperty("password", password);
		emailComposer.setProperty("country", partner.getCountry().getCname());
		emailComposer.setProperty("secretQuestion", partner.getSecretQuestion());
		emailComposer.setProperty("secretAnswer", partner.getSecretAnswer());
		emailComposer.setProperty("archivalInstitution", archivalInstitution.getAiname());
		emailComposer.setProperty("userManager", currentPartner.getName());
		emailComposer.setProperty("dashboardUrl", getBasePath());
		emailComposer.addAttachment("emails/termsofuse.txt", true, "plain/text");
		Emailer emailer = new Emailer();
		emailer.sendMessage(partner.getEmailAddress(), null, currentPartner.getEmailAddress(),
				currentPartner.getEmailAddress(), emailComposer);

	}

	private static void sendEmailAssociateInstitutionManager(User partner, ArchivalInstitution archivalInstitution) {
		User currentPartner = SecurityService.getCurrentPartner();
		EmailComposer emailComposer = new EmailComposer("emails/associateInstitutionManager.txt",
				"New Institution associated to your Institution Manager account for Archives Portal Europe.", true,
				false);
		emailComposer.setProperty("name", partner.getName());
		emailComposer.setProperty("emailAddress", partner.getEmailAddress());
		emailComposer.setProperty("country", partner.getCountry().getCname());
		emailComposer.setProperty("archivalInstitution", archivalInstitution.getAiname());
		emailComposer.setProperty("userManager", currentPartner.getName());
		emailComposer.setProperty("dashboardUrl", getBasePath());
		emailComposer.addAttachment("emails/termsofuse.txt", true, "plain/text");
		Emailer emailer = new Emailer();
		emailer.sendMessage(partner.getEmailAddress(), null, currentPartner.getEmailAddress(),
				currentPartner.getEmailAddress(), emailComposer);

	}

	private static void sendEmailDeleteCountryManager(User partner, Country country) {
		User currentPartner = SecurityService.getCurrentPartner();
		EmailComposer emailComposer = new EmailComposer("emails/deleteCountryManager.txt",
				"Your Country Manager account for Archives Portal Europe is deleted.", true, false);
		emailComposer.setProperty("name", partner.getName());
		emailComposer.setProperty("emailAddress", partner.getEmailAddress());
		emailComposer.setProperty("country", partner.getCountry().getCname());
		emailComposer.setProperty("userManager", currentPartner.getName());
		Emailer emailer = new Emailer();
		emailer.sendMessage(partner.getEmailAddress(), null, currentPartner.getEmailAddress(),
				currentPartner.getEmailAddress(), emailComposer);

	}

	private static void sendEmailDeleteInstitutionManager(User partner, ArchivalInstitution archivalInstitution) {
		User currentPartner = SecurityService.getCurrentPartner();
		EmailComposer emailComposer = new EmailComposer("emails/deleteInstitutionManager.txt",
				"Your Institution Manager account for Archives Portal Europe is deleted.", true, false);
		emailComposer.setProperty("name", partner.getName());
		emailComposer.setProperty("emailAddress", partner.getEmailAddress());
		emailComposer.setProperty("archivalInstitution", archivalInstitution.getAiname());
		emailComposer.setProperty("userManager", currentPartner.getName());
		Emailer emailer = new Emailer();
		emailer.sendMessage(partner.getEmailAddress(), null, currentPartner.getEmailAddress(),
				currentPartner.getEmailAddress(), emailComposer);

	}

	private static void sendEmailDeleteAiFromInstitutionManager(User partner, ArchivalInstitution archivalInstitution) {
		User currentPartner = SecurityService.getCurrentPartner();
		EmailComposer emailComposer = new EmailComposer("emails/deleteAiFromInstitutionManager.txt",
				"One archive is removed from your Institution Manager account for Archives Portal Europe.", true, false);
		emailComposer.setProperty("name", partner.getName());
		emailComposer.setProperty("emailAddress", partner.getEmailAddress());
		emailComposer.setProperty("archivalInstitution", archivalInstitution.getAiname());
		emailComposer.setProperty("userManager", currentPartner.getName());
		Emailer emailer = new Emailer();
		emailer.sendMessage(partner.getEmailAddress(), null, currentPartner.getEmailAddress(),
				currentPartner.getEmailAddress(), emailComposer);

	}

    public static void sendEmailHarvestFinished( ArchivalInstitution archivalInstitution, int numberEadHarvested, String infoHarvestedServer, DateHarvestModel oldestFileHarvested, DateHarvestModel newestFileHarvested, OaiPmhStatus oaiPmhStatus, String details, String errorsResponsePath) {
		User partner = archivalInstitution.getPartner();
		User countryManager  = DAOFactory.instance().getUserDAO().getCountryManagerOfCountry(archivalInstitution.getCountry());
		String name = "UNKNOWN";
		String toEmail = null;
		String ccEmail = null;
		String bccEmail = null;
		if (partner != null) {
			toEmail = partner.getEmailAddress();
			name = partner.getName();
		}
		if (countryManager != null) {
			if (partner == null){
				toEmail = countryManager.getEmailAddress();
				name = countryManager.getName();
			}else {
				ccEmail = countryManager.getEmailAddress();
			}
		}
		if (toEmail == null){
			if (APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()){
				toEmail =  PropertiesUtil.get(PropertiesKeys.APE_EMAILS_ADMINS);
			}
		}else {
			if (APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()){
				bccEmail =  PropertiesUtil.get(PropertiesKeys.APE_EMAILS_ADMINS);
			}
		}
		if (toEmail != null){
	    	EmailComposer emailComposer = null;
	    	String title = "OAI-PMH harvest process " + oaiPmhStatus.getName()+ " (" + numberEadHarvested + " records of " + archivalInstitution.getAiname() +")";
	    	if (OaiPmhStatus.SUCCEED.equals(oaiPmhStatus)){
	    		emailComposer = new EmailComposer("emails/harvestFinished.txt", title, true, true);
	    	}else {
	    		emailComposer = new EmailComposer("emails/harvestFinishedWithWarnings.txt", title, true, true);
	    	}
	    	emailComposer.setProperty("archivalInstitution", archivalInstitution.getAiname());
	        emailComposer.setProperty("name",name);
	        emailComposer.setProperty("dashboardBase", PropertiesUtil.get(PropertiesKeys.APE_DASHBOARD_DOMAIN));
	        emailComposer.setProperty("numberEadHarvested", numberEadHarvested+"");
	        emailComposer.setProperty("infoHarvestedServer",infoHarvestedServer.replaceAll("\n", "<br/>"));
	        if (oldestFileHarvested != null){
	        	emailComposer.setProperty("oldestFileHarvested", oldestFileHarvested.toString());
	        }else {
	        	emailComposer.setProperty("oldestFileHarvested", "");
	        }
	        if (newestFileHarvested != null){
	        	emailComposer.setProperty("newestFileHarvested", newestFileHarvested.toString());
	        }else {
	        	emailComposer.setProperty("newestFileHarvested", "");
	        } 
	        if (details != null){
		        emailComposer.setProperty("harvestingDetails",details);
		        if (errorsResponsePath != null){
		        	emailComposer.setProperty("errorFileMessage", "Look at the dashboard for the OAI-PMH response that contains errors<br/><br/>");
		        }else {
		        	emailComposer.setProperty("errorFileMessage", "");
		        }	   
	        }else {
	        	emailComposer.setProperty("harvestingDetails","");
	        }
	        Emailer emailer = new Emailer();
        	emailer.sendMessage(toEmail, ccEmail, bccEmail, null, emailComposer);
		}
    }
    public static void sendEmailHarvestFailed(ArchivalInstitution archivalInstitution,  String infoHarvestedServer, String errors, String errorsResponsePath, boolean disabled) {
		User partner = archivalInstitution.getPartner();
		User countryManager  = DAOFactory.instance().getUserDAO().getCountryManagerOfCountry(archivalInstitution.getCountry());
		String name = "UNKNOWN";
		String toEmail = null;
		String ccEmail = null;
		String bccEmail = null;
		if (partner != null) {
			toEmail = partner.getEmailAddress();
			name = partner.getName();
		}
		if (countryManager != null) {
			if (partner == null){
				toEmail = countryManager.getEmailAddress();
				name = countryManager.getName();
			}else {
				ccEmail = countryManager.getEmailAddress();
			}
		}
		if (toEmail == null){
			if (APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()){
				toEmail =  PropertiesUtil.get(PropertiesKeys.APE_EMAILS_ADMINS);
			}
		}else {
			if (APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()){
				bccEmail =  PropertiesUtil.get(PropertiesKeys.APE_EMAILS_ADMINS);
			}
		}
		if (toEmail != null){
	    	String title = "OAI-PMH harvest process FAILED (" + archivalInstitution.getAiname() +")";
	    	String file = "emails/harvestFailed.txt";
	    	if (disabled){
	    		title = "OAI-PMH harvest process FAILED and DISABLED (" + archivalInstitution.getAiname() +")";
	    		file = "emails/harvestFailedAndDisabled.txt";
	    	}
	        EmailComposer emailComposer = new EmailComposer(file, title, true, true);
	        emailComposer.setProperty("archivalInstitution", archivalInstitution.getAiname());
	        emailComposer.setProperty("name", name);
	        emailComposer.setProperty("dashboardBase", PropertiesUtil.get(PropertiesKeys.APE_DASHBOARD_DOMAIN));
	        emailComposer.setProperty("infoHarvestedServer", infoHarvestedServer.replaceAll("\n", "<br/>"));
	        emailComposer.setProperty("harvestingDetails",errors);
	        if (errorsResponsePath != null){
	        	emailComposer.setProperty("errorFileMessage", "Look at the dashboard for the OAI-PMH response that contains errors<br/><br/>");
	        }else {
	        	emailComposer.setProperty("errorFileMessage", "");
	        }
	        Emailer emailer = new Emailer();
	        emailer.sendMessage(toEmail, ccEmail, bccEmail, null, emailComposer);
    	}
    }

	public static String getBasePath() {

		HttpServletRequest requestHttp = ServletActionContext.getRequest();
		String nameMainServer = PropertiesUtil.get(PropertiesKeys.APE_DASHBOARD_DOMAIN);
		return requestHttp.getScheme() + "://" + nameMainServer + requestHttp.getContextPath() + "/";
	}
    public static void sendExceptionToAdmin(String title, Throwable e) {
        Emailer emailer = new Emailer();
        EmailComposer emailComposer = new EmailComposer("emails/admins.txt", title, true, false);
        emailComposer.setProperty("body", ApeUtil.generateThrowableLog(e));
        emailComposer.setPriority(Priority.HIGH);
        emailer.sendMessage(ADMINS_EMAIL, null, null, null, emailComposer);
    }
}
