package eu.apenet.persistence.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ValidatedState;

public class EadSearchOptions implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7670523402797168198L;
	private int pageNumber = 1;
	private int pageSize = 20;
	private long firstResult = -1;;
	private String searchTerms;
	private String searchTermsField;
	private String orderByField = "id";
	private boolean orderByAscending = true;
	private Class<? extends Ead> eadClazz = FindingAid.class;
	private Integer archivalInstitionId = null;
	private Boolean published = null;
	private Boolean converted = null;
	private Boolean publishedToAll = null;
	private Boolean dynamic = null;
	private Boolean linked = null;
	private List<ValidatedState> validated = new ArrayList<ValidatedState>();
	private List<EuropeanaState> europeana = new ArrayList<EuropeanaState>();
	private List<QueuingState> queuing = new ArrayList<QueuingState>();
	private List<Integer> ids = new ArrayList<Integer>();
	public EadSearchOptions(){
		
	}
	public EadSearchOptions(EadSearchOptions eadSearchOptions){
		this.pageNumber = eadSearchOptions.getPageNumber();
		this.pageSize = eadSearchOptions.getPageSize();
		this.firstResult = eadSearchOptions.getFirstResult();
		this.searchTerms = eadSearchOptions.getSearchTerms();
		this.searchTermsField = eadSearchOptions.getSearchTermsField();
		this.orderByField = eadSearchOptions.getOrderByField();
		this.orderByAscending = eadSearchOptions.isOrderByAscending();
		this.eadClazz = eadSearchOptions.getEadClazz();
		this.archivalInstitionId = eadSearchOptions.getArchivalInstitionId();
		this.published = eadSearchOptions.getPublished();
		this.converted = eadSearchOptions.getConverted();
		this.publishedToAll = eadSearchOptions.getPublishedToAll();
		this.linked = eadSearchOptions.getLinked();
		this.dynamic = eadSearchOptions.getDynamic();
		for (ValidatedState validatedState: eadSearchOptions.getValidated()){
			validated.add(validatedState);
		}
		for (EuropeanaState europeanaState: eadSearchOptions.getEuropeana()){
			europeana.add(europeanaState);
		}
		for (QueuingState queuingState: eadSearchOptions.getQueuing()){
			queuing.add(queuingState);
		}
		for (Integer id: eadSearchOptions.getIds()){
			ids.add(id);
		}
	}

	public Class<? extends Ead> getEadClazz() {
		return eadClazz;
	}

	public void setEadClazz(Class<? extends Ead> eadClazz) {
		this.eadClazz = eadClazz;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderByField() {
		return orderByField;
	}

	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}

	public boolean isOrderByAscending() {
		return orderByAscending;
	}

	public void setOrderByAscending(boolean orderByAscending) {
		this.orderByAscending = orderByAscending;
	}

	public Integer getArchivalInstitionId() {
		return archivalInstitionId;
	}

	public void setArchivalInstitionId(Integer archivalInstitionId) {
		this.archivalInstitionId = archivalInstitionId;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean searchable) {
		this.published = searchable;
	}

	public Boolean getConverted() {
		return converted;
	}

	public void setConverted(Boolean converted) {
		this.converted = converted;
	}



	public List<ValidatedState> getValidated() {
		return validated;
	}

	public void setValidated(List<ValidatedState> validated) {
		this.validated = validated;
	}
	public void setValidated(ValidatedState validated) {
		this.validated.clear();
		this.validated.add(validated);
	}

	public List<EuropeanaState> getEuropeana() {
		return europeana;
	}

	public void setEuropeana(List<EuropeanaState> europeana) {
		this.europeana = europeana;
	}
	public void setEuropeana(EuropeanaState europeana) {
		this.europeana.clear();
		this.europeana.add(europeana);
	}
	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	public String getSearchTermsField() {
		return searchTermsField;
	}

	public void setSearchTermsField(String searchTermsField) {
		this.searchTermsField = searchTermsField;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public long getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(long firstResult) {
		this.firstResult = firstResult;
	}

	public Boolean getPublishedToAll() {
		return publishedToAll;
	}

	public void setPublishedToAll(Boolean publishedToAll) {
		this.publishedToAll = publishedToAll;
	}
	public List<QueuingState> getQueuing() {
		return queuing;
	}
	public void setQueuing(List<QueuingState> queuing) {
		this.queuing = queuing;
	}
	public void setQueuing(QueuingState queuingState) {
		this.queuing.clear();
		this.queuing.add(queuingState);
	}
	public Boolean getLinked() {
		return linked;
	}
	public void setLinked(Boolean linked) {
		this.linked = linked;
	}
	public Boolean getDynamic() {
		return dynamic;
	}
	public void setDynamic(Boolean dynamic) {
		this.dynamic = dynamic;
	}
	
}
