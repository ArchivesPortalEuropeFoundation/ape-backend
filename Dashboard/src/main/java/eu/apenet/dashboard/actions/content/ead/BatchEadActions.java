package eu.apenet.dashboard.actions.content.ead;

import java.util.List;
import java.util.Properties;

import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.QueueAction;

public class BatchEadActions extends AbstractEadActions {
	public static final String ALL_ITEMS = "all";
	public static final String SELECTED_ITEMS = "only_selected";
	public static final String SEARCHED_ITEMS = "only_searched";
	private String batchItems;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2671921974304007944L;

	public String validateEad() {
		return addBatchToQueue(QueueAction.VALIDATE);
	}

	public String convertEad(Properties properties) {
		return addBatchToQueue(QueueAction.CONVERT, properties);
	}

	public String publishEad() {
		return addBatchToQueue(QueueAction.PUBLISH);
	}

	public String unpublishEad() {
		return addBatchToQueue(QueueAction.UNPUBLISH);
	}

	public String deleteEad() {
		return addBatchToQueue(QueueAction.DELETE);
	}

	@Override
	public String convertValidatePublishEad(Properties properties) {
		return addBatchToQueue(QueueAction.CONVERT_VALIDATE_PUBLISH, properties);
	}

    @Override
    public String convertValidateEad(Properties properties) {
        return addBatchToQueue(QueueAction.CONVERT_VALIDATE, properties);
    }

	@Override
	public String deleteEseEdm() {
		return addBatchToQueue(QueueAction.DELETE_ESE_EDM);
	}

	@Override
	public String deleteFromEuropeana() {
		return addBatchToQueue(QueueAction.DELETE_FROM_EUROPEANA);
	}

	@Override
	public String deliverToEuropeana() {
		return addBatchToQueue(QueueAction.DELIVER_TO_EUROPEANA);
	}
	
	@Override
	public String changeToDynamic() {
		return addBatchToQueue(QueueAction.CHANGE_TO_DYNAMIC);
	}

	@Override
	public String changeToStatic() {
		return addBatchToQueue(QueueAction.CHANGE_TO_STATIC);
	}

	@Override
	public String reindexSocial() {
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteFromQueue() {
		try {
			List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
					AjaxControllerAbstractAction.LIST_IDS);
			if (SELECTED_ITEMS.equals(batchItems) && !ids.isEmpty()) {
				if (ids != null && !ids.isEmpty()) {
					EadService.deleteBatchFromQueue(ids, getAiId(), getXmlType());
					return SUCCESS;
				} else {
					return ERROR;
				}

			} else if (SELECTED_ITEMS.equals(batchItems) && ids.isEmpty()){
				addActionError(getText("content.message.noSelected"));
				return ERROR;
			}
			else if (SEARCHED_ITEMS.equals(batchItems)) {
				ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(
						ContentManagerAction.EAD_SEARCH_OPTIONS);
				EadService.deleteBatchFromQueue(eadSearchOptions);
				return SUCCESS;
			} else {
				EadService.deleteBatchFromQueue(null, getAiId(), getXmlType());
				return SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	private String addBatchToQueue(QueueAction queueAction) {
		return addBatchToQueue(queueAction, new Properties());
	}

	@SuppressWarnings("unchecked")
	private String addBatchToQueue(QueueAction queueAction, Properties properties) {
		try {
			List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
					AjaxControllerAbstractAction.LIST_IDS);
			if (SELECTED_ITEMS.equals(batchItems) && !ids.isEmpty()) {
				if (ids != null && !ids.isEmpty()) {
					EadService.addBatchToQueue(ids, getAiId(), getXmlType(), queueAction, properties);
					return SUCCESS;
				} else {
					return ERROR;
				}

			} else if (SELECTED_ITEMS.equals(batchItems) && ids.isEmpty()){
				addActionError(getText("content.message.noSelected"));
				return ERROR;
			}else if (SEARCHED_ITEMS.equals(batchItems)) {
				ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(
						ContentManagerAction.EAD_SEARCH_OPTIONS);
				EadService.addBatchToQueue(eadSearchOptions, queueAction, properties);
				return SUCCESS;
			} else {
				EadService.addBatchToQueue(null, getAiId(), getXmlType(), queueAction, properties);
				return SUCCESS;
			}
		} catch (Exception e) {
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
