package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.apenet.dpt.utils.eag2012.*;
import eu.apenet.persistence.vo.RightsInformation;
import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;

/**
 * Class for fill control object JAXB
 */
public class FillControlObjectJAXB implements ObjectJAXB{
	
	protected Eag2012 eag2012;
	
	protected Eag eag;

	boolean exists ;
	
	private final Logger log = Logger.getLogger(getClass());
	private SimpleDateFormat df;

	@Override
	public Eag ObjectJAXB(Eag2012 eag2012, Eag eag) {
		this.log.debug("Method start: \"ObjectJAXB\"");
		// TODO Auto-generated method stub
		this.eag2012=eag2012;
		this.eag=eag;
		exists=true;
		df = new SimpleDateFormat();
		main();
		this.log.debug("End method: \"ObjectJAXB\"");
		return this.eag;
	}

	/**
	 * Method main with method for fill control of object JAXB
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class FillControlObjectJAXB\"");
		// eag/control/recordId
		createRecordId();		
		// eag/control/maintenanceAgency
		createMaintenanceAgency();
		// eag/control/maintenanceAgency/descriptiveNote
		// TODO:
		// eag/control/maintenanceStatus
		createMaintenanceStatus();			
		// eag/control/maintenanceHistory
		createMaintenanceHistory();
		// eag/control/maintenanceHistory/maintenanceEvent
		// eag/control/maintenanceHistory/maintenanceEvent/agent && /agentType
		createMaintenanceEvent();
		// eag/control/languageDeclarations
		createLanguageDeclarations();
		// eag/control/conventionDeclaration/citation
		createCitation();
		// eag/control/localControl/term
		Term term = new Term();
        df.applyPattern(Eag2012.DATE_FORMAT);
        term.setLastDateTimeVerified(df.format(new GregorianCalendar().getTime()));
		term.setScriptCode(Eag2012.CONTROL_SCRIPT_COD);
		// TODO:		term.setTransliteration(Eag2012.CONTROL_TRANSLITERATION);
		term.setVocabularySource(Eag2012.CONTROL_VOCABULARY_SOURCE);
		term.setValue(Eag2012.EXTENDED);
		// eag/control/localControl
		LocalControl localControl = new LocalControl();
		localControl.setLocalType(Eag2012.LOCAL_TYPE_CONTROL);
		localControl.getTerm().add(term);
		this.eag.getControl().getLocalControl().add(localControl);
		// eag/control/localTypeDeclaration
		// eag/control/publicationStatus
		// eag/control/sources

		//Rights declaration
		RightsInformation rightsInformation = eag2012.getShareEagWithWikimediaLicence();
		RightsDeclaration rightsDeclaration = new RightsDeclaration();
		rightsDeclaration.setLang("eng");
		//Abbreviation
		Abbreviation abbreviation = new Abbreviation();
		abbreviation.setContent(rightsInformation.getAbbreviation());
		rightsDeclaration.setAbbreviation(abbreviation);
		//Citation
		Citation citation = new Citation();
		citation.setHref(rightsInformation.getLink());
		citation.setContent(rightsInformation.getRightsName());
		rightsDeclaration.setCitation(citation);
		//DescriptiveNote
		DescriptiveNote descriptiveNote = new DescriptiveNote();
		P p1 = new P();
		p1.setContent(rightsInformation.getDescription());
		descriptiveNote.getP().add(p1);
		P p2 = new P();
		p2.setContent("The rights holder is the ");
		descriptiveNote.getP().add(p2);
		rightsDeclaration.setDescriptiveNote(descriptiveNote);

		this.eag.getControl().getRightsDeclaration().add(rightsDeclaration);

		this.log.debug("End method: \"Main of class FillControlObjectJAXB\"");
	}

	/**
	 * Method to fill recordId element
	 */
	private void createRecordId(){
		this.log.debug("Method start: \"createRecordId\"");
		// eag/control/recordId
		if (this.eag.getControl().getRecordId() == null) {
			this.eag.getControl().setRecordId(new RecordId());
		}
		this.eag.getControl().getRecordId().setValue(this.eag2012.getRecordIdValue());
		// eag/control/otherRecordId
		if (this.eag2012.getOtherRecordIdValue() != null) {
			for (int i = 0; i < this.eag2012.getOtherRecordIdValue().size(); i++) {
				if (this.eag2012.getOtherRecordIdValue().get(i) != null
						&& !this.eag2012.getOtherRecordIdValue().get(i).isEmpty()) {
					OtherRecordId otherRecordId = new OtherRecordId();
					otherRecordId.setValue(this.eag2012.getOtherRecordIdValue().get(i));
					otherRecordId.setLocalType(this.eag2012.getOtherRecordIdLocalType().get(i));
					this.eag.getControl().getOtherRecordId().add(otherRecordId);
				}
			}
		}
		this.log.debug("End method: \"createRecordId\"");
	}

	/**
	 * Method to fill maintenanceAgency element
	 */
	private void createMaintenanceAgency(){
		this.log.debug("Method start: \"createMaintenanceAgency\"");
		// eag/control/maintenanceAgency
		if (this.eag.getControl().getMaintenanceAgency() == null) {
			this.eag.getControl().setMaintenanceAgency(new MaintenanceAgency());
		}	
		// eag/control/maintenanceAgency/agencyCode
		if (this.eag.getControl().getMaintenanceAgency().getAgencyCode() == null) {
			this.eag.getControl().getMaintenanceAgency().setAgencyCode(new AgencyCode());
		}
		if (this.eag2012.getAgencyCodeValue() != null && !this.eag2012.getAgencyCodeValue().isEmpty()) {
			this.eag.getControl().getMaintenanceAgency().getAgencyCode().setContent(this.eag2012.getAgencyCodeValue());
		} else {
			this.eag.getControl().getMaintenanceAgency().getAgencyCode().setContent(this.eag2012.getRecordIdValue());
		}	
		// eag/control/maintenanceAgency/agencyName
		if (this.eag.getControl().getMaintenanceAgency().getAgencyName() == null) {
			this.eag.getControl().getMaintenanceAgency().setAgencyName(new AgencyName());
		}
		if (this.eag2012.getAgencyNameValue() != null && !this.eag2012.getAgencyNameValue().isEmpty()) {
			this.eag.getControl().getMaintenanceAgency().getAgencyName().setContent(this.eag2012.getAgencyNameValue());
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAgencyNameLang())) {
				this.eag.getControl().getMaintenanceAgency().getAgencyName().setLang(this.eag2012.getAgencyNameLang());
			}
		} else {
			this.eag.getControl().getMaintenanceAgency().getAgencyName().setContent(this.eag2012.getAutformValue().get(0));
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAutformLang().get(0))) {
				this.eag.getControl().getMaintenanceAgency().getAgencyName().setLang(this.eag2012.getAutformLang().get(0));
			}
		}	
		// eag/control/maintenanceAgency/otherAgencyCode
		if (this.eag2012.getOtherAgencyCodeValue() != null && !this.eag2012.getOtherAgencyCodeValue().isEmpty()) {
			for (int i = 0; i < this.eag2012.getOtherRecordIdValue().size(); i++) {
				OtherAgencyCode otherAgencyCode = new OtherAgencyCode();
				otherAgencyCode.setContent(this.eag2012.getOtherAgencyCodeValue().get(i));
		// TODO:otherAgencyCode.setLocalType(this.eag2012.getOtherAgencyCodeLocalType().get(i));
				this.eag.getControl().getMaintenanceAgency().getOtherAgencyCode().add(otherAgencyCode);
			}
		}
		this.log.debug("End method: \"createMaintenanceAgency\"");
	}

	/**
	 * Method to fill maintenanceStatus element
	 */
	private void createMaintenanceStatus(){
		this.log.debug("Method start: \"createMaintenanceStatus\"");
		// eag/control/maintenanceStatus
		if (this.eag.getControl().getMaintenanceStatus() == null) {
			MaintenanceStatus maintenanceStatus = new MaintenanceStatus();
			maintenanceStatus.setValue(Eag2012.EVENTTYPE_NEW);
			this.eag.getControl().setMaintenanceStatus(maintenanceStatus);
		} else {
			this.eag.getControl().getMaintenanceStatus().setValue(Eag2012.EVENTTYPE_REVISED);
		}
		this.log.debug("End method: \"createMaintenanceStatus\"");				
	}

	/**
	 * Method to fill maintenanceHistory element
	 */
	private void createMaintenanceHistory(){
		this.log.debug("Method start: \"createMaintenanceHistory\"");
		// eag/control/maintenanceHistory
		exists = true;
		if (this.eag.getControl().getMaintenanceHistory() == null) {
			this.eag.getControl().setMaintenanceHistory(new MaintenanceHistory());
			exists = false;
		}
		this.log.debug("End method: \"createMaintenanceHistory\"");
	}

	/**
	 * Method to fill maintenanceEvent element
	 */
	private void createMaintenanceEvent(){
		this.log.debug("Method start: \"createMaintenanceEvent\"");
		// eag/control/maintenanceHistory/maintenanceEvent
		MaintenanceEvent maintenanceEvent = new MaintenanceEvent();
		// eag/control/maintenanceHistory/maintenanceEvent/agent && /agentType
		if (maintenanceEvent.getAgent() == null) {
			maintenanceEvent.setAgent(new Agent());
		}
		if (maintenanceEvent.getAgentType() == null) {
			maintenanceEvent.setAgentType(new AgentType());
		}
		if (this.eag2012.getAgentValue() != null && !this.eag2012.getAgentValue().isEmpty()) {
			maintenanceEvent.getAgent().setContent(this.eag2012.getAgentValue());
			maintenanceEvent.getAgentType().setValue(Eag2012.AGENT_TYPE_HUMAN);
		} else {
			maintenanceEvent.getAgent().setContent(Eag2012.AGENT_AUTOMATICALLY);
			maintenanceEvent.getAgentType().setValue(Eag2012.AGENT_TYPE_MACHINE);
		}
		if (this.eag2012.getAgentLang() != null 
				&& !Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAgentLang())) {
			maintenanceEvent.getAgent().setLang(this.eag2012.getAgentLang());
		}
		// eag/control/maintenanceHistory/maintenanceEvent/eventDateTime
		if (maintenanceEvent.getEventDateTime() == null) {
			maintenanceEvent.setEventDateTime(new EventDateTime());
		}		
        df.applyPattern(Eag2012.DATE_FORMAT);
		maintenanceEvent.getEventDateTime().setStandardDateTime(df.format(new GregorianCalendar().getTime()));
		df.applyPattern(Eag2012.DATE_FORMAT_HUMAN);
		maintenanceEvent.getEventDateTime().setContent(df.format(new GregorianCalendar().getTime()));
		// eag/control/maintenanceHistory/maintenanceEvent/eventType
		if (maintenanceEvent.getEventType() == null) {
			maintenanceEvent.setEventType(new EventType());
		}
		if (exists) {
			maintenanceEvent.getEventType().setValue(Eag2012.EVENTTYPE_REVISED);
		} else {
			maintenanceEvent.getEventType().setValue(Eag2012.EVENTTYPE_CREATED);
		}
		this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().add(maintenanceEvent);
		this.log.debug("End method: \"createMaintenanceEvent\"");
	}

	/**
	 * Method to fill languageDeclarations element
	 */
	private void createLanguageDeclarations(){
		this.log.debug("Method start: \"createLanguageDeclarations\"");
		// eag/control/languageDeclarations
		if (this.eag2012.getLanguageLanguageCode() != null
				&& !this.eag2012.getLanguageLanguageCode().isEmpty()
				&& this.eag2012.getScriptScriptCode() != null
				&& !this.eag2012.getScriptScriptCode().isEmpty()) {
			for (int i = 0; i < this.eag2012.getLanguageLanguageCode().size(); i++) {
				LanguageDeclaration languageDeclaration = new LanguageDeclaration();

				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getLanguageLanguageCode().get(i))) {
					Language language = new Language();
					language.setLanguageCode(this.eag2012.getLanguageLanguageCode().get(i));

					languageDeclaration.setLanguage(language);
				}
				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getScriptScriptCode().get(i))) {
					Script script = new Script();
					script.setScriptCode(this.eag2012.getScriptScriptCode().get(i));

					languageDeclaration.setScript(script);
				}
				if (languageDeclaration.getLanguage() != null
						|| languageDeclaration.getScript() != null) {
					if (this.eag.getControl().getLanguageDeclarations() == null) {
						this.eag.getControl().setLanguageDeclarations(new LanguageDeclarations());
					}
					this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().add(languageDeclaration);
				}
			}
		}
		this.log.debug("End method: \"createLanguageDeclarations\"");
	}

	/**
	 * Method to fill citation element
	 */
	private void createCitation(){
		this.log.debug("Method start: \"createCitation\"");
		// eag/control/conventionDeclaration/citation
		if (this.eag2012.getCitationValue() != null && this.eag2012.getAbbreviationValue() != null) {
			for (int i = 0; i < this.eag2012.getCitationValue().size(); i++) {
				Map<String, List<String>> tabsMap = this.eag2012.getCitationValue().get(i);
				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while(tabsIt.hasNext()) {
					String tabsKey = tabsIt.next();
					List<String> valueList = tabsMap.get(tabsKey);
					if (Eag2012.TAB_CONTROL.equalsIgnoreCase(tabsKey)) {
						for (int j = 0; j < valueList.size(); j++) {
							Citation citation = new Citation();
							citation.setContent(valueList.get(j));	
							ConventionDeclaration conventionDeclaration = new ConventionDeclaration();
							conventionDeclaration.getCitation().add(citation);	
							// eag/control/conventionDeclaration/abbreviation
							if (this.eag2012.getAbbreviationValue()!=null && this.eag2012.getAbbreviationValue().size()>j && this.eag2012.getAbbreviationValue().get(j) != null && !this.eag2012.getAbbreviationValue().get(j).isEmpty()) {
								Abbreviation abbreviation = new Abbreviation();
								abbreviation.setContent(this.eag2012.getAbbreviationValue().get(j));	
								conventionDeclaration.setAbbreviation(abbreviation);
							}
							if(citation.getContent()!=null /*&& !citation.getContent().isEmpty()*/){
								if (!citation.getContent().isEmpty()) {
									this.eag.getControl().getConventionDeclaration().add(conventionDeclaration);
								} else if (conventionDeclaration.getAbbreviation() != null){
									if (!conventionDeclaration.getCitation().isEmpty()) {
										for (int k = 0; k < conventionDeclaration.getCitation().size(); k++) {
											conventionDeclaration.getCitation().remove(k);
										}
									}
									this.eag.getControl().getConventionDeclaration().add(conventionDeclaration);
								}
							}
						}
					}
				}
			}
		}
		this.log.debug("End method: \"createCitation\"");
	}
}
