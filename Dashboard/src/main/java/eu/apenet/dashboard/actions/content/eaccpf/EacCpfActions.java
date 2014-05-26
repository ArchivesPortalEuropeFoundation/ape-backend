/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.eaccpf;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.utils.ContentUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author papp
 */
public class EacCpfActions extends AbstractEacCpfActions {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String convertValidatePublishEacCpf(Properties properties) {
        try {
            String language = retrieveCurrentLanguage();

            EacCpfService.convertValidatePublish(id, properties, language);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String convertValidateEacCpf(Properties properties) {
        try {
            String language = retrieveCurrentLanguage();

            EacCpfService.convertValidate(id, properties, language);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String convertEacCpf(Properties properties) {
        try {
            String language = retrieveCurrentLanguage();

            EacCpfService.convert(id, properties, language);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String validateEacCpf() {
        try {
            EacCpfService.validate(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String publishEacCpf() {
        try {
            EacCpfService.publish(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String unpublishEacCpf() {
        try {
            EacCpfService.unpublish(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteEacCpf() {
        try {
            EacCpfService.delete(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteEseEdm() {
        try {
            EacCpfService.deleteEdm(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteFromEuropeana() {
        try {
            EacCpfService.deleteFromEuropeana(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deliverToEuropeana() {
        try {
            EacCpfService.deliverToEuropeana(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteFromQueue() {
        try {
            EacCpfService.deleteFromQueue(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String download() {
        try {
            File file = EacCpfService.download(getId());
            ContentUtils.downloadXml(this.getServletRequest(), getServletResponse(), file);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    private String retrieveCurrentLanguage() {
        String result = ActionContext.getContext().getLocale().toString();
        return result;
    }
}
