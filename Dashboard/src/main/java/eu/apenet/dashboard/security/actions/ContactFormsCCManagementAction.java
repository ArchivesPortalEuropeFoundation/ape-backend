package eu.apenet.dashboard.security.actions;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.SecurityService.LoginResult;
import eu.apenet.dashboard.security.SecurityService.LoginResult.LoginResultType;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.User;

public class ContactFormsCCManagementAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7836284338030276475L;
	private Integer countryId;
	private User userCM;
	private boolean acceptcc;

	
	private void buildBreadcrumb() {
		super.buildBreadcrumbs();
		this.addBreadcrumb(null,getText("al.menu.contactfomrs.cc"));
	}

	@Override
	public String execute() throws Exception {
		SecurityContext securityContext = SecurityContext.get();
		if  (securityContext.isCountryManager()){
			countryId = securityContext.getCountryId();
		}

		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		UserDAO userDAO = DAOFactory.instance().getUserDAO();
		Country country = countryDAO.findById(countryId);
		userCM = userDAO.getCountryManagerOfCountry(country);

		buildBreadcrumb();
		return SUCCESS;
	}
	public String changeSetting(){
		SecurityContext securityContext = SecurityContext.get();
		if  (securityContext.isCountryManager()){
			countryId = securityContext.getCountryId();
		}
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		UserDAO userDAO = DAOFactory.instance().getUserDAO();
		Country country = countryDAO.findById(countryId);
		userCM = userDAO.getCountryManagerOfCountry(country);

		userCM.setContactFormsAsCC(acceptcc);
		userDAO.update(userCM);

		buildBreadcrumb();

		getServletRequest().setAttribute("success" , true);

		return SUCCESS;	
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public void setUserCM(User userCM) {
		this.userCM = userCM;
	}

	public User getUserCM() {
		return userCM;
	}

	public void setAcceptcc(boolean acceptcc) {
		this.acceptcc = acceptcc;
	}

	public boolean isAcceptcc() {
		return acceptcc;
	}
}
