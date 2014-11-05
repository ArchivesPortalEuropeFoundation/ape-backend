package eu.apenet.dashboard.manual.ingestionprofile;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.actions.content.ead.BatchEadActions;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ingestionprofile;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;

/**
 * Class to manage the action "Apply profiles" to EADs an EAC-CPFs.
 */
public class ApplyProfileAction extends AbstractInstitutionAction {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -1221434016010591344L;

	// Id of the EAD or EAC to apply the profile.
	private Integer id;
	// Type of the XML to apply the profile.
	private String xmlTypeId;
	// Items selected in batch mode.
	private String batchItems;
	// Id of the profile to apply.
	private String selectedProfile;

	// Set of possible profiles to apply.
	private Set<SelectItem> profilesSet = new TreeSet<SelectItem>();

	// Logger.
	private static final Logger LOG = Logger.getLogger(ApplyProfileAction.class);

	// Constructor.
	public ApplyProfileAction() {
		super();
	}

	/**
	 * Method to construct the route to the current section.
	 */
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb("contentmanager.action", getText("breadcrumb.section.contentmanager"));
		addBreadcrumb(getText("breadcrumb.section.applyProfile"));
	}

	/**
	 * Method to add the item(s) to the queue to be processed using a profile.
	 *
	 * @return Result of the action.
	 */
	@Override
	public String execute() {
		String result = Action.SUCCESS;

		// Recover the selected profile.
		if (StringUtils.isNotBlank(this.getSelectedProfile())) {
			IngestionprofileDAO ingestionprofileDAO = DAOFactory.instance().getIngestionprofileDAO();
			Ingestionprofile ingestionprofile = ingestionprofileDAO.getIngestionprofile(Long.valueOf(this.getSelectedProfile()));

			if (ingestionprofile != null) {
		        try {
		        	// Check if is a batch action or a single action.
		        	if (StringUtils.isNotBlank(this.getBatchItems())) {
		        		result = this.batchAction(ingestionprofile);
		        	} else {
		        		result = this.singleItemAction(ingestionprofile);
		        	}
		        } catch (Exception ex) {
		        	LOG.error("Failed when adding the file into the queue", ex);
		        	addActionError(getText("dashboard.applyProfile.noFile"));
		        	result = Action.ERROR;
		        }
			}
		}

		return result;
	}

	/**
	 * Method to recover the list of profiles for the current type in the
	 * current institution.
	 *
	 * @return Result of the action.
	 */
	@Override
	public String input() {
		// Check if the action is batch with items selected and has items selected.
		List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(AjaxControllerAbstractAction.LIST_IDS);
		if (BatchEadActions.SELECTED_ITEMS.equals(this.getBatchItems())
				&& (ids == null || ids.isEmpty())) {
			addActionError(getText("content.message.noSelected"));
			return ERROR;
		}

		try {
			LOG.debug("Try to recover the list of profiles associated to the XMLType: " + XmlType.getType(Integer.parseInt(this.getXmlTypeId())).getName());

			if ((this.getId() != null || StringUtils.isNotBlank(this.getBatchItems()))
					&& StringUtils.isNotBlank(this.getXmlTypeId())) {
				// Recover the profiles list associated to the file type passed.
				IngestionprofileDAO ingestionprofileDAO = DAOFactory.instance().getIngestionprofileDAO();

				List<Ingestionprofile> profilesList = ingestionprofileDAO.getIngestionprofiles(this.getAiId(), XmlType.getType(Integer.parseInt(this.getXmlTypeId())).getIdentifier());

				if (profilesList != null  && !profilesList.isEmpty()) {
					for (Ingestionprofile ingestionprofile: profilesList) {
						this.getProfilesSet().add(new SelectItem(ingestionprofile.getId(), ingestionprofile.getNameProfile()));
					}

					LOG.debug("Recovered " + this.getProfilesSet().size() + " profiles associated to the XMLType: " + XmlType.getType(Integer.parseInt(this.getXmlTypeId())).getName());

					return Action.SUCCESS;
				}

				LOG.debug("There is no profiles associated to the XMLType: " + XmlType.getType(Integer.parseInt(this.getXmlTypeId())).getName());

				return Action.NONE;
			}

			LOG.error("There is no items to recover the associated profiles.");

			return Action.ERROR;
		} catch (NumberFormatException nfe) {
			LOG.error("Error parsing the XMLType of the item to an integer value.");

			return Action.ERROR;
		}
	}

	/**
	 * Method to add a batch of items to the queue to be processed using a
	 * profile.
	 *
	 * @param ingestionprofile Profile which should be used to process the file.
	 *
	 * @return Result of the action.
	 *
	 * @throws Exception Exception while adding the items to the queue.
	 */
	private String batchAction(Ingestionprofile ingestionprofile) throws Exception {
		LOG.debug("Try to add the queue a bach of items of the XMLType: " + XmlType.getType(Integer.parseInt(this.getXmlTypeId())).getName() + " using the profile: " + ingestionprofile.getNameProfile());

		// Check the type of batch action.
		if (BatchEadActions.SELECTED_ITEMS.equals(this.getBatchItems())) {
			LOG.debug("Process only the selected items.");

			// Batch associated to "only selected" items.
			List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(AjaxControllerAbstractAction.LIST_IDS);
			if (ids != null && !ids.isEmpty()) {
				// Check the type of document.
				if (this.getXmlTypeId().equals(Integer.toString(XmlType.EAC_CPF.getIdentifier()))) {
					LOG.debug("Process selected EAC-CPFs.");

					EacCpfService.addBatchToQueue(ids, this.getAiId(), QueueAction.USE_PROFILE, retrieveProperties(ingestionprofile));
				} else {
					LOG.debug("Process selected EADs.");

					EadService.addBatchToQueue(ids, this.getAiId(), XmlType.getType(Integer.parseInt(this.getXmlTypeId())), QueueAction.USE_PROFILE, retrieveProperties(ingestionprofile));
				}
				return Action.SUCCESS;
			} else {
				LOG.debug("There is no selected items.");

				addActionError(getText("dashboard.applyProfile.noFile"));
				return Action.ERROR;
			}
		} else if (BatchEadActions.SEARCHED_ITEMS.equals(this.getBatchItems())) {
			LOG.debug("Process only the searched items.");

			// Batch associated to "only search results" items.
			ContentSearchOptions contentSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);

			// Check the type of document.
			if (this.getXmlTypeId().equals(Integer.toString(XmlType.EAC_CPF.getIdentifier()))) {
				LOG.debug("Process searched EAC-CPFs.");

				EacCpfService.addBatchToQueue(contentSearchOptions, QueueAction.USE_PROFILE, retrieveProperties(ingestionprofile));
			} else {
				LOG.debug("Process searched EADs.");

				EadService.addBatchToQueue(contentSearchOptions, QueueAction.USE_PROFILE, retrieveProperties(ingestionprofile));
			}
			return Action.SUCCESS;
		} else {
			LOG.debug("Process all items.");

			// Batch associated to "all" items.
			// Check the type of document.
			if (this.getXmlTypeId().equals(Integer.toString(XmlType.EAC_CPF.getIdentifier()))) {
				LOG.debug("Process all EAC-CPFs.");
				
			} else {
				LOG.debug("Process all EADs.");

				EadService.addBatchToQueue(null, this.getAiId(), XmlType.getType(Integer.parseInt(this.getXmlTypeId())), QueueAction.USE_PROFILE, retrieveProperties(ingestionprofile));
			}
			return Action.SUCCESS;
		}
	}

	/**
	 * Method to add a single item to the queue to be processed using a profile.
	 *
	 * @param ingestionprofile Profile which should be used to process the file.
	 *
	 * @return Result of the action.
	 *
	 * @throws Exception Exception while adding the item to the queue.
	 */
	private String singleItemAction(Ingestionprofile ingestionprofile) throws Exception {
		// Check if the id of the item is not empty.
		if (this.getId() != null) {
			LOG.debug("Try to add the queue the item with id: " + this.getId() + " of the XMLType: " + XmlType.getType(Integer.parseInt(this.getXmlTypeId())).getName() + " using the profile: " + ingestionprofile.getNameProfile());

			// Check the type of document.
			if (this.getXmlTypeId().equals(XmlType.EAC_CPF.getIdentifier())) {
				EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
				EacCpf eacCpf = eacCpfDAO.findById(this.getId());
				EacCpfService.useProfileAction(eacCpf, retrieveProperties(ingestionprofile));
			} else {
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				Ead ead = eadDAO.findById(this.getId(), XmlType.getType(Integer.valueOf(this.getXmlTypeId())).getClazz());
				EadService.useProfileAction(ead, retrieveProperties(ingestionprofile));
			}

			LOG.debug("Item added to the queue.");
		} else {
			LOG.error("There is no possible to add an item to the queue without the id of the element.");

			addActionError(getText("dashboard.applyProfile.noFile"));
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}

	/**
	 * Method to parse the options selected in the profile passed to a property object.
	 *
	 * @param ingestionprofile Selected profile to parse.
	 *
	 * @return Property object with the options selected in the profile.
	 */
	private static Properties retrieveProperties(Ingestionprofile ingestionprofile) {
		LOG.debug("Start parsing the profile with name " + ingestionprofile.getNameProfile() + " to a property object.");

		Properties properties = new Properties();
		properties.setProperty(QueueItem.XML_TYPE, ingestionprofile.getFileType() + "");
		properties.setProperty(QueueItem.NO_EADID_ACTION, ingestionprofile.getNoeadidAction().getId() + "");
		properties.setProperty(QueueItem.EXIST_ACTION, ingestionprofile.getExistAction().getId() + "");
		properties.setProperty(QueueItem.DAO_TYPE, ingestionprofile.getDaoType().getId() + "");
		properties.setProperty(QueueItem.DAO_TYPE_CHECK, ingestionprofile.getDaoTypeFromFile() + "");
		properties.setProperty(QueueItem.UPLOAD_ACTION, ingestionprofile.getUploadAction().getId() + "");
		properties.setProperty(QueueItem.CONVERSION_TYPE, ingestionprofile.getEuropeanaConversionType() + "");
		properties.setProperty(QueueItem.DATA_PROVIDER, ingestionprofile.getEuropeanaDataProvider() + "");
		properties.setProperty(QueueItem.DATA_PROVIDER_CHECK, ingestionprofile.getEuropeanaDataProviderFromFile() + "");
		properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE, ingestionprofile.getEuropeanaDaoType() + "");
		properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE_CHECK, ingestionprofile.getEuropeanaDaoTypeFromFile() + "");
		properties.setProperty(QueueItem.LANGUAGES, ingestionprofile.getEuropeanaLanguages() + "");
		properties.setProperty(QueueItem.LANGUAGE_CHECK, ingestionprofile.getEuropeanaLanguagesFromFile() + "");
		properties.setProperty(QueueItem.LICENSE, ingestionprofile.getEuropeanaLicense() + "");
		properties.setProperty(QueueItem.LICENSE_DETAILS, ingestionprofile.getEuropeanaLicenseDetails() + "");
		properties.setProperty(QueueItem.LICENSE_ADD_INFO, ingestionprofile.getEuropeanaAddRights() + "");
		properties.setProperty(QueueItem.INHERIT_FILE_CHECK, ingestionprofile.getEuropeanaInheritElementsCheck()+"");
		properties.setProperty(QueueItem.INHERIT_FILE, ingestionprofile.getEuropeanaInheritElements()+"");
		properties.setProperty(QueueItem.INHERIT_ORIGINATION_CHECK, ingestionprofile.getEuropeanaInheritOriginCheck()+"");
		properties.setProperty(QueueItem.INHERIT_ORIGINATION, ingestionprofile.getEuropeanaInheritOrigin()+"");
		properties.setProperty(QueueItem.SOURCE_OF_IDENTIFIERS, ingestionprofile.getSourceOfIdentifiers()+"");

		LOG.debug("End parsing the profile with name " + ingestionprofile.getNameProfile() + " to a property object.");

		return properties;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the xmlTypeId
	 */
	public String getXmlTypeId() {
		return this.xmlTypeId;
	}

	/**
	 * @param xmlTypeId the xmlTypeId to set
	 */
	public void setXmlTypeId(String xmlTypeId) {
		this.xmlTypeId = xmlTypeId;
	}

	/**
	 * @return the batchItems
	 */
	public String getBatchItems() {
		return this.batchItems;
	}

	/**
	 * @param batchItems the batchItems to set
	 */
	public void setBatchItems(String batchItems) {
		this.batchItems = batchItems;
	}

	/**
	 * @return the selectedProfile
	 */
	public String getSelectedProfile() {
		return this.selectedProfile;
	}

	/**
	 * @param selectedProfile the selectedProfile to set
	 */
	public void setSelectedProfile(String selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

	/**
	 * @return the profilesSet
	 */
	public Set<SelectItem> getProfilesSet() {
		return this.profilesSet;
	}

	/**
	 * @param profilesSet the profilesSet to set
	 */
	public void setProfilesSet(Set<SelectItem> profilesSet) {
		this.profilesSet = profilesSet;
	}

}
