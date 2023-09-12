/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.apenet.dashboard.actions.content.eaccpf;

import eu.apenet.dashboard.actions.content.AbstractTypeActions;
import java.util.Properties;

/**
 *
 * @author papp
 */
public abstract class AbstractEacCpfActions extends AbstractTypeActions {

    public abstract String validateEacCpf();

    public abstract String convertEacCpf(Properties properties);

    public abstract String publishEacCpf();

    public abstract String unpublishEacCpf();

    public abstract String deleteEacCpf();

    public abstract String convertValidateEacCpf(Properties properties);

    public abstract String convertValidatePublishEacCpf(Properties properties);

    public abstract String reindexSocial();

    @Override
    public abstract String deleteEseEdm();

    @Override
    public abstract String deleteFromEuropeana();

    @Override
    public abstract String deliverToEuropeana();

    @Override
    public abstract String deleteFromQueue();

    @Override
    public String execute() throws Exception {
        if (VALIDATE.equals(action)) {
            return validateEacCpf();
        } else if (CONVERT.equals(action)) {
            return convertEacCpf(getConversionParameters());
        } else if (PUBLISH.equals(action)) {
            return publishEacCpf();
        } else if (UNPUBLISH.equals(action)) {
            return unpublishEacCpf();
        } else if (DELETE.equals(action)) {
            return deleteEacCpf();
        } else if (CONVERT_VALIDATE.equals(action)) {
            return convertValidateEacCpf(getConversionParameters());
        } else if (CONVERT_VALIDATE_PUBLISH.equals(action)) {
            return convertValidatePublishEacCpf(getConversionParameters());
        } else if (DELETE_ESE_EDM.equals(action)) {
            return deleteEseEdm();
        } else if (DELETE_FROM_EUROPEANA.equals(action)) {
            return deleteFromEuropeana();
        } else if (DELIVER_TO_EUROPEANA.equals(action)) {
            return deliverToEuropeana();
        } else if (DELETE_FROM_QUEUE.equals(action)) {
            return deleteFromQueue();
        }
        else if (REINDEX_SOCIAL.equals(action)) {
            return reindexSocial();
        }
        return ERROR;
    }

    @Override
    protected Properties getConversionParameters() {
        Properties parameters = new Properties();
        //        HttpSession session = getServletRequest().getSession();
        //        String option_default = (String) session.getAttribute(AjaxControllerAbstractAction.OPTIONS_DEFAULT);
        //        String option_use_existing = (String) session.getAttribute(AjaxControllerAbstractAction.OPTIONS_USE_EXISTING);
        //        boolean option_use_existing_bool = true;
        //        if (option_use_existing != null) {
        //            option_use_existing_bool = !Boolean.parseBoolean(option_use_existing);
        //        }
        //        if (option_default == null) {
        //            option_default = "UNSPECIFIED";
        //        }
        //        parameters.put("defaultRoleType", option_default);
        //        parameters.put("useDefaultRoleType", Boolean.toString(option_use_existing_bool));
        return parameters;
    }

}
