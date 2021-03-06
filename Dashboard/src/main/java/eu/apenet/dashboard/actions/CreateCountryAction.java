package eu.apenet.dashboard.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import com.neovisionaries.i18n.CountryCode;

/**
 * @author Jara Alvarez
 
 To create a new country in the system
 
 		//      - Insert registers in country and couAlternativeNames entities
		//		- To create the folders in repo
		//		- To change the AL.xml with a new node with minimum information

 */
public class CreateCountryAction extends ActionSupport{

	private final Logger log = Logger.getLogger(getClass());
	
	private List<Lang> languagesList = new ArrayList<Lang>();
	private List<Country> countriesList = new ArrayList<Country>();
	private String englishCountryName;
	private String  isoCountryName;
	private Integer languageSelected;
	private Integer countrySelected;
	private String pathRepo = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR;

	private Locale tempLocale;
	private String europeTempCountryCode;
	static Semaphore sem = new Semaphore(1,true) ;
	
	public List<Lang> getLanguagesList() {
		return languagesList;
	}

	public void setLanguagesList(List<Lang> languagesList) {
		this.languagesList = languagesList;
	}
	
	public String getEnglishCountryName() {
		return englishCountryName;
	}

	public void setEnglishCountryName(String englishCountryName) {
		this.englishCountryName = englishCountryName;
	}
	
	public String getIsoCountryName() {
		return isoCountryName;
	}

	public void setIsoCountryName(String isoCountryName) {
		this.isoCountryName = isoCountryName;
	}

	public Integer getLanguageSelected() {
		return languageSelected;
	}

	public void setLanguageSelected(Integer languageSelected) {
		this.languageSelected = languageSelected;
	}
	
	public List<Country> getCountriesList() {
		return countriesList;
	}

	public void setCountriesList(List<Country> countriesList) {
		this.countriesList = countriesList;
	}

	public Integer getCountrySelected() {
		return countrySelected;
	}

	public void setCountrySelected(Integer countrySelected) {
		this.countrySelected = countrySelected;
	}

	public String getPathRepo() {
		return pathRepo;
	}

	public void setPathRepo(String pathRepo) {
		this.pathRepo = pathRepo;
	}
	
	@Override
	public void validate(){
		log.debug("Validating textfields in creating country process...");
		if (this.getEnglishCountryName()!= null){
			if (this.getEnglishCountryName().length() == 0){ //empty
				addFieldError("englishCountryName", getText("createCountry.englishCountryName"));
			}
		}
		if (this.getIsoCountryName()!=null && (this.getIsoCountryName().length()!=2 && this.getIsoCountryName().length()!=3)){
			addFieldError("isoCountryName", getText("createCountry.isoCountryNameWrong"));
		}else if(this.getIsoCountryName()!=null){
            tempLocale = null;
            CountryCode countryCode = CountryCode.getByCodeIgnoreCase(getIsoCountryName());
            if(countryCode != null) {
                tempLocale = countryCode.toLocale();
            }
			if(tempLocale == null && (this.getIsoCountryName().equals("EU") || this.getIsoCountryName().equals("EUR"))) {
                europeTempCountryCode = "EU";
            } else if(tempLocale==null || !((this.getIsoCountryName().length()==3 && tempLocale.getISO3Country().toUpperCase().equals(this.getIsoCountryName().toUpperCase())) || (this.getIsoCountryName().length()==2 && tempLocale.getCountry().toUpperCase().equals(this.getIsoCountryName().toUpperCase())))){
				addFieldError("isoCountryName", getText("createCountry.isoCountryNameWrong"));
			}
		}
	}

	public String execute() throws Exception{
		
		return SUCCESS;
	}
	
	public String storeCountry(){
		
		String result= null;
		
		if ((this.getEnglishCountryName()!= null) && (this.getIsoCountryName()!= null) && (this.getIsoCountryName() != null))
		{				
			CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
			CouAlternativeNameDAO couAltNamesDAO = DAOFactory.instance().getCouAlternativeNameDAO();
			CouAlternativeName newCouAltName = new CouAlternativeName();
			
			LangDAO langDao = DAOFactory.instance().getLangDAO();
			Lang language = langDao.getLangByIsoname("ENG");
			
			try {
				
				if ((countryDAO.getCountryByCname(this.getEnglishCountryName().trim()) == null)) 
				{
					JpaUtil.beginDatabaseTransaction();
                    Country newCountry = new Country();
					newCountry.setCname(this.getEnglishCountryName().toUpperCase());
                    if(tempLocale != null) {
					    newCountry.setIsoname(tempLocale.getCountry().toUpperCase());
                    } else {
                        newCountry.setIsoname(europeTempCountryCode);
                    }
					newCountry.setAlOrder(0);					
					countryDAO.insertSimple(newCountry);
					
					newCouAltName.setCouAnName(this.getEnglishCountryName());
					newCouAltName.setLang(language);
					newCouAltName.setCountry(newCountry);
					couAltNamesDAO.insertSimple(newCouAltName);
					
					log.debug("Country "+this.getEnglishCountryName()+" stored");
					File file = new File(pathRepo = pathRepo + this.getIsoCountryName().toUpperCase());
			        if(!file.exists())
					    file.mkdir();
			        log.debug("Directory "+this.getIsoCountryName() +" created");
			        
		        
			        JpaUtil.commitDatabaseTransaction();
					result= SUCCESS;
					addActionMessage(getText("createCountry.newCountryStored"));
				}else
				{
					addActionMessage(getText("createCountry.countryAlreadyStored"));
					result=INPUT;
				}
				
			}catch(Exception e){
				result = ERROR;				
			}finally{
				if (result.equals(ERROR)){
					JpaUtil.rollbackDatabaseTransaction();
					File file = new File(pathRepo = pathRepo + this.getIsoCountryName());
					if (file.exists())
						file.delete();
				}
			}			
		}
		else
			result= INPUT;
		
		return result;
	}

}	
