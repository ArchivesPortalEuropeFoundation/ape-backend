/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.eaccpf;

import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.QueueAction;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author papp
 */
public class BatchEacCpfActions extends AbstractEacCpfActions {

    public static final String ALL_ITEMS = "all";
    public static final String SELECTED_ITEMS = "only_selected";
    public static final String SEARCHED_ITEMS = "only_searched";
    private String batchItems;

    @Override
    public String validateEacCpf() {
        return addBatchToQueue(QueueAction.VALIDATE);
    }

    @Override
    public String convertEacCpf(Properties properties) {
        return addBatchToQueue(QueueAction.CONVERT, properties);
    }

    @Override
    public String publishEacCpf() {
        return addBatchToQueue(QueueAction.PUBLISH);
    }

    @Override
    public String unpublishEacCpf() {
        return addBatchToQueue(QueueAction.UNPUBLISH);
    }

    @Override
    public String deleteEacCpf() {
        return addBatchToQueue(QueueAction.DELETE);
    }

    @Override
    public String convertValidateEacCpf(Properties properties) {
        return addBatchToQueue(QueueAction.CONVERT_VALIDATE, properties);
    }

    @Override
    public String convertValidatePublishEacCpf(Properties properties) {
        return addBatchToQueue(QueueAction.CONVERT_VALIDATE_PUBLISH, properties);
    }

    @Override
    public String reindexSocial() {
        return SUCCESS;
    }

    @Override
    public String deleteEseEdm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFromEuropeana() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deliverToEuropeana() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFromQueue() {
        try {
            List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                    AjaxControllerAbstractAction.LIST_IDS);
            if (SELECTED_ITEMS.equals(batchItems) && !ids.isEmpty()) {
                if (!ids.isEmpty()) {
                    EacCpfService.deleteBatchFromQueue(ids, getAiId());
                    return SUCCESS;
                } else {
                    return ERROR;
                }

            } else if (SELECTED_ITEMS.equals(batchItems) && ids.isEmpty()) {
                addActionError(getText("content.message.noSelected"));
                return ERROR;
            } else if (SEARCHED_ITEMS.equals(batchItems)) {
                ContentSearchOptions eacCpfSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(
                        ContentManagerAction.EAD_SEARCH_OPTIONS);
                EacCpfService.deleteBatchFromQueue(eacCpfSearchOptions);
                return SUCCESS;
            } else {
                EacCpfService.deleteBatchFromQueue(null, getAiId());
                return SUCCESS;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    private String addBatchToQueue(QueueAction queueAction) {
        return addBatchToQueue(queueAction, new Properties());
    }

    private String addBatchToQueue(QueueAction queueAction, Properties properties) {
        try {
            List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                    AjaxControllerAbstractAction.LIST_IDS);
            if (SELECTED_ITEMS.equals(batchItems) && !ids.isEmpty()) {
                if (!ids.isEmpty()) {
                    EacCpfService.addBatchToQueue(ids, getAiId(), queueAction, properties);
                    return SUCCESS;
                } else {
                    return ERROR;
                }

            } else if (SELECTED_ITEMS.equals(batchItems) && ids.isEmpty()) {
                addActionError(getText("content.message.noSelected"));
                return ERROR;
            } else if (SEARCHED_ITEMS.equals(batchItems)) {
                ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(
                        ContentManagerAction.EAD_SEARCH_OPTIONS);
                EacCpfService.addBatchToQueue(eadSearchOptions, queueAction, properties);
                return SUCCESS;
            } else {
                EacCpfService.addBatchToQueue(null, getAiId(), queueAction, properties);
                return SUCCESS;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(String batchItems) {
        this.batchItems = batchItems;
    }
}
