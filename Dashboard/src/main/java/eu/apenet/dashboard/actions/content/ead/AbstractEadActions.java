package eu.apenet.dashboard.actions.content.ead;

import java.util.Properties;

import javax.servlet.http.HttpSession;

import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.dashboard.actions.content.AbstractTypeActions;

public abstract class AbstractEadActions extends AbstractTypeActions {

    private static final String STATIC = "changeToStatic";
    private static final String DYNAMIC = "changeToDynamic";

    @Override
    public String execute() throws Exception {
        if (VALIDATE.equals(action)) {
            return validateEad();
        } else if (CONVERT.equals(action)) {
            return convertEad(getConversionParameters());
        } else if (PUBLISH.equals(action)) {
            return publishEad();
        } else if (UNPUBLISH.equals(action)) {
            return unpublishEad();
        } else if (DELETE.equals(action)) {
            return deleteEad();
        } else if (CONVERT_VALIDATE.equals(action)) {
            return convertValidateEad(getConversionParameters());
        } else if (CONVERT_VALIDATE_PUBLISH.equals(action)) {
            return convertValidatePublishEad(getConversionParameters());
        } else if (DELETE_ESE_EDM.equals(action)) {
            return deleteEseEdm();
        } else if (DELETE_FROM_EUROPEANA.equals(action)) {
            return deleteFromEuropeana();
        } else if (DELIVER_TO_EUROPEANA.equals(action)) {
            return deliverToEuropeana();
        } else if (DELETE_FROM_QUEUE.equals(action)) {
            return deleteFromQueue();
        } else if (DYNAMIC.equals(action)) {
            return changeToDynamic();
        } else if (STATIC.equals(action)) {
            return changeToStatic();
        } else if (REINDEX_SOCIAL.equals(action)) {
            return reindexSocial();
        }
        return ERROR;
    }

    public abstract String validateEad();

    public abstract String convertEad(Properties properties);

    public abstract String publishEad();

    public abstract String unpublishEad();

    public abstract String deleteEad();

    public abstract String convertValidateEad(Properties properties);

    public abstract String convertValidatePublishEad(Properties properties);

    public abstract String changeToDynamic();

    public abstract String changeToStatic();

    public abstract String reindexSocial();

    @Override
    protected Properties getConversionParameters() {
        Properties parameters = new Properties();

        // Recover the conversion options from session.
        HttpSession session = getServletRequest().getSession();
        // Options related to DAO type.
        this.fillDaoTypePrarameters(session, parameters);

        // Options related to rights statement for digital objects.
        this.fillRightsDigitalObjectPrarameters(session, parameters);

        // Options related to rights statement for EAD data.
        this.fillRightsEadDataPrarameters(session, parameters);

        return parameters;
    }

    /**
     * Method to recover the DAO type preferences from session and added them as
     * conversion parameters.
     *
     * If the default option is not set, the default value "UNSPECIFIED" is
     * assigned.
     *
     * By default the value for use existing DAO type is "TRUE".
     *
     * @param session Current <HttpSession>.
     * @param parameters The <Properties> that store the conversion parameters.
     */
    private void fillDaoTypePrarameters(HttpSession session, Properties parameters) {
        // Recover the options related to DAO type.
        String option_default = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT);
        String option_use_existing = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_USE_EXISTING);

        // Check the values recovered.
        // Selected value for use existing DAO type.
        boolean option_use_existing_bool = true;
        if (option_use_existing != null) {
            option_use_existing_bool = !Boolean.parseBoolean(option_use_existing);
        }

        // Selected value for default DAO type.
        if (option_default == null) {
            option_default = "UNSPECIFIED";
        }

        // Add the conversion options related to DAO type to the properties.
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT, option_default);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_USE_EXISTING, Boolean.toString(option_use_existing_bool));
    }

    /**
     * Method to recover the rights statement, description and holder for
     * digital objects from session and added them as conversion parameters.
     *
     * If the rights statement is not filled, the description and holder are
     * also set as empty values.
     *
     * @param session Current <HttpSession>.
     * @param parameters The <Properties> that store the conversion parameters.
     */
    private void fillRightsDigitalObjectPrarameters(HttpSession session, Properties parameters) {
        // Recover the options related to rights statement for digital objects.
        String option_default_rights_digital = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_DIGITAL);
        String option_default_rights_digital_text = "";
        String option_rights_digital_description = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_DESCRIPTION);
        String option_rights_digital_holder = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_DIGITAL_HOLDER);

        // Check the values recovered.
        // Selected rights statement for digital objects.
        if (option_default_rights_digital == null
                || option_default_rights_digital.equalsIgnoreCase("---")) {
            option_default_rights_digital = "";
        }

        // Recover the text for the selected rights statement for digital objects.
        if (!option_default_rights_digital.isEmpty()) {
            option_default_rights_digital_text = this.recoverRightsStatementText(option_default_rights_digital);
        }

        // Description added for the rights statement for digital objects.
        if (option_rights_digital_description == null || option_default_rights_digital.isEmpty()) {
            option_rights_digital_description = "";
        }

        // Rights holder added for the rights statement for digital objects.
        if (option_rights_digital_holder == null || option_default_rights_digital.isEmpty()) {
            option_rights_digital_holder = "";
        }

        // Add the conversion options related to rights statement for digital
        // objects to the properties.
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL, option_default_rights_digital);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL_TEXT, option_default_rights_digital_text);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_DESCRIPTION, option_rights_digital_description);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_HOLDER, option_rights_digital_holder);
    }

    /**
     * Method to recover the rights statement, description and holder for EAD
     * data from session and added them as conversion parameters.
     *
     * If the rights statement is not filled, the description and holder are
     * also set as empty values.
     *
     * @param session Current <HttpSession>.
     * @param parameters The <Properties> that store the conversion parameters.
     */
    private void fillRightsEadDataPrarameters(HttpSession session, Properties parameters) {
        // Recover the options related to rights statement for EAD data.
        String option_default_rights_ead = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_DEFAULT_RIGHTS_EAD);
        String option_default_rights_ead_text = "";
        String option_rights_ead_description = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_DESCRIPTION);
        String option_rights_ead_holder = (String) session.getAttribute(AjaxConversionOptionsConstants.OPTIONS_RIGHTS_EAD_HOLDER);

        // Check the values recovered.
        // Selected rights statement for EAD data.
        if (option_default_rights_ead == null
                || option_default_rights_ead.equalsIgnoreCase("---")) {
            option_default_rights_ead = "";
        }

        // Recover the text for the selected rights statement for EAD data.
        if (!option_default_rights_ead.isEmpty()) {
            option_default_rights_ead_text = this.recoverRightsStatementText(option_default_rights_ead);
        }

        // Description added for the rights statement for EAD data.
        if (option_rights_ead_description == null || option_default_rights_ead.isEmpty()) {
            option_rights_ead_description = "";
        }

        // Rights holder added for the rights statement for EAD data.
        if (option_rights_ead_holder == null || option_default_rights_ead.isEmpty()) {
            option_rights_ead_holder = "";
        }

        // Add the conversion options related to rights statement for EAD data
        // to the properties.
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD, option_default_rights_ead);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD_TEXT, option_default_rights_ead_text);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_DESCRIPTION, option_rights_ead_description);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_HOLDER, option_rights_ead_holder);
    }

    /**
     * Method to recover the rights statement text from the rights statement URL
     * selected.
     *
     * @param option_default_rights rights statement URL selected.
     *
     * @return Rights statement text.
     */
    private String recoverRightsStatementText(String option_default_rights) {
        String option_default_rights_text = null;

        // Check the URL selected
        if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution.
            option_default_rights_text = getText("content.message.rights.creative.attribution");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATES.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, No Derivatives.
            option_default_rights_text = getText("content.message.rights.creative.attribution.no.derivates");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NON_COMERCIAL.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_NO_DERIVATES.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial, No Derivatives.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial.no.derivates");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_SHARE.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial, ShareAlike.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial.sharealike");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_SHARE.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, ShareAlike .
            option_default_rights_text = getText("content.message.rights.creative.attribution.sharealike");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_CC0_PUBLIC.equalsIgnoreCase(option_default_rights)) {
            //  Creative Commons CC0 Public Domain Dedication .
            option_default_rights_text = getText("content.message.rights.creative.public.domain");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT.equalsIgnoreCase(option_default_rights)) {
            // Europeana In Copyright
            option_default_rights_text = getText("ead2ese.content.license.europeana.incopyright");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT_EU_ORPHAN_WORK.equalsIgnoreCase(option_default_rights)) {
            // Europeana In Copyright, EU Orphan Works
            option_default_rights_text = getText("ead2ese.content.license.europeana.incopyright.euorphan");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT_EDUCATIONAL_USE_ONLY.equalsIgnoreCase(option_default_rights)) {
            // Europeana In Copyright, educational use only
            option_default_rights_text = getText("ead2ese.content.license.europeana.incopyright.eduuse");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_NO_COPYRIGHT_NONCOMMERCIAL_USE_ONLY.equalsIgnoreCase(option_default_rights)) {
            // Europeana No Copyright, non-commercial use only
            option_default_rights_text = getText("ead2ese.content.license.europeana.nocopyright.noncommercial");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_NO_COPYRIGHT_NONCOMMERCIAL_USE_ONLY.equalsIgnoreCase(option_default_rights)) {
            // Europeana No Copyright, other known legal restrictions
            option_default_rights_text = getText("ead2ese.content.license.europeana.nocopyright.otherlegal");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_COPYRIGHT_NOT_EVALUATED.equalsIgnoreCase(option_default_rights)) {
            // Europeana, copyright not evaluated
            option_default_rights_text = getText("ead2ese.content.license.europeana.copyrightnotevaluated");
        } else if (AjaxConversionOptionsConstants.PUBLIC_DOMAIN_MARK.equalsIgnoreCase(option_default_rights)) {
            // Public Domain Mark.
            option_default_rights_text = getText("content.message.rights.public.domain");
        } else {
            // Unknown.
            option_default_rights_text = getText("content.message.rights.unknown");
        }

        return option_default_rights_text;
    }
}
