package eu.apenet.api.eag;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.*;
import com.opensymphony.xwork2.Action;
import eu.apenet.commons.infraestructure.NavigationTree;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CoordinatesDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeoApiAction {

    private String aiId;
    private String aiName;
    private String aiRepositoryCode;

    private String countryCode;
    private String repositoryName;


    protected static final String FOLDER_LAZY = "\"isFolder\": true, \"isLazy\": true";
    protected static final String UTF8 = "UTF-8";
    protected static final String END_ARRAY = "]\n";
    protected static final String START_ARRAY = "[\n";
    protected static final String END_ITEM = "}";
    protected static final String START_ITEM = "{";
    protected static final String FOLDER_WITH_CHILDREN = "\"isFolder\": true, \"children\": \n";
    protected static final String COMMA = ",";
    private static final String MORE_CLASS = "more";
    private static final String MORE_TEXT = "advancedsearch.context.more";
    protected static final String ADVANCEDSEARCH_TEXT_NOTITLE = "advancedsearch.text.notitle";

    public GeoApiAction() {

    }

    public String execute() {
        HttpServletResponse response = ServletActionContext.getResponse();

        try {

            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            ArchivalInstitution archivalInstitution = null;
            if (aiId != null) {
                archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(aiId));
                aiName = archivalInstitution.getAiname();
                aiRepositoryCode = archivalInstitution.getRepositorycode();
            }
            else if (aiName != null) {
                archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByAiName(aiName);
                aiRepositoryCode = archivalInstitution.getRepositorycode();
                aiId = ""+archivalInstitution.getAiId();
            }
            else if (aiRepositoryCode != null) {
                archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);
                aiId = ""+archivalInstitution.getAiId();
                aiName = archivalInstitution.getAiname();
            }

            boolean onlyBounds = false;//archivalInstitution!=null;

            CoordinatesDAO coordinatesDAO = DAOFactory.instance().getCoordinatesDAO();
            List<Coordinates> reposList = new ArrayList<Coordinates>();

            if (!onlyBounds){
                // Always recovers all the coordinates.
                if (archivalInstitution!=null){
                    if (repositoryName == null) {
                        reposList = coordinatesDAO.findCoordinatesByArchivalInstitution(archivalInstitution);
                    }
                    else {
                        List<Coordinates> repoCoordinatesList = coordinatesDAO.findCoordinatesByArchivalInstitution(archivalInstitution);

                        if (repoCoordinatesList != null && !repoCoordinatesList.isEmpty()) {
                            Coordinates coordinates = null;
                            if (repoCoordinatesList.size() > 1) {
                                Iterator<Coordinates> repoCoordinatesIt = repoCoordinatesList.iterator();
                                if (repositoryName != null && !repositoryName.isEmpty()) {
                                    // Select the proper element from database.
                                    while (repoCoordinatesIt.hasNext()) {
                                        Coordinates coordinatesTest = repoCoordinatesIt.next();
                                        if (repositoryName.startsWith(coordinatesTest.getNameInstitution())) {
                                            coordinates = coordinatesTest;
                                        }
                                    }
                                }
                            }

                            // At this point, if coordinates still null, set the value of the
                            // first element of the list.
                            if (coordinates == null) {
                                coordinates = repoCoordinatesList.get(0);
                            }

                            if (coordinates!=null) {
                                if (coordinates.getLat() != 0 || coordinates.getLon() != 0) {
                                    //control elements outside the printable earth coordinates (-77 to 82) and (-177 to 178)
                                    if ((coordinates.getLat() >= -77 && coordinates.getLat() <= 82) && (coordinates.getLon() >= -177 && coordinates.getLon() <= 178)) {
                                        reposList.add(coordinates);
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    reposList = coordinatesDAO.getCoordinates();
                }

                // Remove coordinates with values (0, 0).
                if (reposList != null && !reposList.isEmpty()) {
                    // New list without (0, 0) values.
                    List<Coordinates> cleanReposList = new ArrayList<Coordinates>();
                    Iterator<Coordinates> reposIt = reposList.iterator();
                    while (reposIt.hasNext()) {
                        Coordinates coordinates = reposIt.next();
                        if (coordinates.getLat() != 0 || coordinates.getLon() != 0) {
                            //control elements outside the printable earth coordinates (-77 to 82) and (-177 to 178)
                            if ((coordinates.getLat() >=-77 && coordinates.getLat() <= 82) && (coordinates.getLon() >=-177 && coordinates.getLon() <= 178)) {
                                cleanReposList.add(coordinates);
                            }
                        }
                    }
                    // Pass the clean array to the existing one.
                    reposList = cleanReposList;
                }
            }
            // Check the part to center.
            if (repositoryName != null && !repositoryName.isEmpty()
                    && aiId != null && !aiId.isEmpty()) {
                writeToResponseAndClose(generateGmapsJSON(reposList, null, aiId, repositoryName), response);
            } else if (aiId != null && !aiId.isEmpty()) {
                writeToResponseAndClose(generateGmapsJSON(reposList, null, aiId, null), response);
            } else if (countryCode != null && !countryCode.isEmpty()) {
                writeToResponseAndClose(generateGmapsJSON(reposList, countryCode, null, null), response);
            } else {
                writeToResponseAndClose(generateGmapsJSON(reposList, null, null, null), response);
            }
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
        }

        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.SUCCESS;
    }


    private void writeToResponseAndClose(StringBuilder stringBuilder, HttpServletResponse resourceResponse)
            throws UnsupportedEncodingException, IOException {
        resourceResponse.setCharacterEncoding(UTF8);
        resourceResponse.setContentType("application/json");
        Writer writer =  new OutputStreamWriter(resourceResponse.getOutputStream(), UTF8);
//        Writer writer = getResponseWriter(resourceResponse);
        writer.write(stringBuilder.toString());
        writer.flush();
        writer.close();
    }

    private StringBuilder generateGmapsJSON(List<Coordinates> repoList, String countryCode, String institutionID, String repositoryName) {
        StringBuilder builder = new StringBuilder();
        builder.append(START_ITEM);
        builder.append("\"count\":" + repoList.size());
        builder.append(COMMA);
        builder.append("\"repos\":");

        builder.append(START_ARRAY);
        if(repoList!=null){
            Iterator<Coordinates> itRepoList = repoList.iterator();
            while(itRepoList.hasNext()){
                Coordinates repo = itRepoList.next();
                builder.append(buildNode(repo));
                if(itRepoList.hasNext()){
                    builder.append(COMMA);
                }
            }
        }
        builder.append(END_ARRAY);

        // Add the center values.
        if (repositoryName != null && !repositoryName.isEmpty()
                && institutionID != null && !institutionID.isEmpty()) {
            // Call the method to add the bounds for the repository of the institution.
            builder.append(this.buildInstitutionBounds(institutionID, repositoryName));
        } else if (institutionID != null && !institutionID.isEmpty()) {
            // Call the method to add the bounds for the institution.
            builder.append(this.buildInstitutionBounds(institutionID, null));
        } else if (countryCode != null && !countryCode.isEmpty()) {
            // Call the method to add the bounds for the country.
            builder.append(this.buildCountryBounds(countryCode));
        }else{

            //To know if map must be focused in Europe
            String focusOnEurope = "true";//PropertiesUtil.get(PropertiesKeys.APE_GOOGLEMAPS_FOCUS_ON_EUROPE);

            //To get Map bounds to fit the map in Europe
            String southwestLatitude = "71.199541";//PropertiesUtil.get(PropertiesKeys.APE_GOOGLEMAPS_CENTER_SOUTHWEST_LATITUDE);
            String southwestLongitude = "-26.426213";//PropertiesUtil.get(PropertiesKeys.APE_GOOGLEMAPS_CENTER_SOUTHWEST_LONGITUDE);
            String northeastLatitude = "35.685282";//PropertiesUtil.get(PropertiesKeys.APE_GOOGLEMAPS_CENTER_NORTHEAST_LATITUDE);
            String northeastLongitude = "30.233942";//PropertiesUtil.get(PropertiesKeys.APE_GOOGLEMAPS_CENTER_NORTHEAST_LONGITUDE);
            if(southwestLatitude!=null && southwestLongitude!=null && northeastLatitude!=null && northeastLongitude!=null){
                if(focusOnEurope.compareTo("true")==0){
                    // Call the method to add the bounds for Europe.
                    builder.append(this.centerMapBuilder(southwestLatitude.toString(),southwestLongitude.toString(),northeastLatitude.toString(),northeastLongitude.toString()));
                }
            }else{
                // bounds to the markers
            }
        }
        builder.append(END_ITEM);
        return builder;
    }

    /**
     * Method to build the data for the institution bounds.
     *
     * @param institutionID {@link String} the Institution for recover the bounds.
     * @return Element with the bounds for the institution passed.
     */
    private StringBuilder buildInstitutionBounds(String institutionID, String repositoryName) {
//        this.log.debug("Method start: \"buildInstitutionBounds\"");
        StringBuilder builder = new StringBuilder();
        // Recover the list of coordinates for the current institution.
        CoordinatesDAO coordinatesDAO = DAOFactory.instance().getCoordinatesDAO();
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(institutionID));
        List<Coordinates> repoCoordinatesList = coordinatesDAO.findCoordinatesByArchivalInstitution(archivalInstitution);

        // If the list contains more than one elemet, find the proper element.
        if (repoCoordinatesList != null && !repoCoordinatesList.isEmpty()) {
            Coordinates coordinates = null;
            if (repoCoordinatesList.size() > 1) {
                Iterator<Coordinates> repoCoordinatesIt = repoCoordinatesList.iterator();
                if (repositoryName != null && !repositoryName.isEmpty()) {
                    // Select the proper element from database.
                    while (repoCoordinatesIt.hasNext()) {
                        Coordinates coordinatesTest = repoCoordinatesIt.next();
                        if (repositoryName.startsWith(coordinatesTest.getNameInstitution())) {
                            coordinates = coordinatesTest;
                        }
                    }
                } else {
                    // First element in database (main institution)
                    while (repoCoordinatesIt.hasNext()) {
                        Coordinates coordinatesTest = repoCoordinatesIt.next();
                        if (coordinates != null) {
                            if (coordinates.getId() > coordinatesTest.getId()) {
                                coordinates = coordinatesTest;
                            }
                        } else {
                            coordinates = coordinatesTest;
                        }
                    }
                }
            }

            // At this point, if coordinates still null, set the value of the
            // first element of the list.
            if (coordinates == null) {
                coordinates = repoCoordinatesList.get(0);
            }

            // if coords=0,0 or null call to show the country
            if(coordinates.getLat()==0.0 && coordinates.getLon()==0.0){
                builder.append(buildCountryBounds(archivalInstitution.getCountry().getIsoname()));
            }
            else{
                // Build bounds node.
                builder.append(COMMA);
                builder.append("\"bounds\":");

                // Build coordinates node.
                builder.append(START_ARRAY);
                builder.append(START_ITEM);
                builder.append("\"latitude\":\"" + coordinates.getLat() + "\"");
                builder.append(COMMA);
                builder.append("\"longitude\":\"" + coordinates.getLon() + "\"");
                builder.append(END_ITEM);
                builder.append(END_ARRAY);
            }
        }
        else{
            builder.append(buildCountryBounds(archivalInstitution.getCountry().getIsoname()));
        }
//        this.log.debug("End method: \"buildInstitutionBounds\"");
        return builder;
    }

    /**
     * Method to build the data for the country bounds.
     *
     * @param countryCode {@link String} Country code for recover the bounds.
     * @return Element with the bounds for the country code passed.
     */
    private StringBuilder buildCountryBounds(String countryCode) {
//        this.log.debug("Method start: \"buildCountryBounds\"");
        StringBuilder builder = new StringBuilder();
        CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
        List<Country> countriesList = countryDAO.getCountries(countryCode);

        if (countriesList != null && !countriesList.isEmpty()) {
            String selectedCountryName = countriesList.get(0).getCname();
            // Issue #1924 - To locate correctly the country "Georgia" instead
            // of the state "Georgia", it's needed to add in the address, which
            // is passed to the geolocator, the string ", Europe".
            // Whit this change all the European countries are located
            // correctly.
            // NOTE: currently exists a country with name "Europe", which
            // represents the country for the archive "Historical Archives of
            // the European Union", for this one it's not needed to add the
            // string in the address.
            if (!selectedCountryName.trim().equalsIgnoreCase("EUROPE")) {
                builder.append(this.mapBuilder(selectedCountryName + ", Europe"));
            } else {
                builder.append(this.mapBuilder(selectedCountryName));
            }
        }
//        this.log.debug("End method: \"buildCountryBounds\"");
        return builder;
    }

    /**
     * Method for map builder
     * @param location {@link String}
     * @return builder {@link StringBuilder}
     */
    private StringBuilder mapBuilder(String location){
//        this.log.debug("Method start: \"mapBuilder\"");
        StringBuilder builder = new StringBuilder();
        // Try to recover the coordinates to bound.
        Geocoder geocoder = new Geocoder();

        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).getGeocoderRequest();
        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

        if (geocoderResponse.getStatus().equals(GeocoderStatus.OK)) {
            List<GeocoderResult> geocoderResultList = geocoderResponse.getResults();

            // Always recover the first result.
            if (geocoderResultList.size() > 0) {
                GeocoderResult geocoderResult = geocoderResultList.get(0);

                // Get Geometry Object.
                GeocoderGeometry geocoderGeometry = geocoderResult.getGeometry();
                // Get Bounds Object.
                LatLngBounds latLngBounds = geocoderGeometry.getBounds();

                // Get southwest bound.
                LatLng southwestLatLng = latLngBounds.getSouthwest();
                // Get southwest latitude.
                Double southwestLatitude = southwestLatLng.getLat().doubleValue();
                // Get southwest longitude.
                Double southwestLongitude = southwestLatLng.getLng().doubleValue();

                // Get northeast bound.
                LatLng northeastLatLng = latLngBounds.getNortheast();
                // Get northeast latitude.
                Double northeastLatitude = northeastLatLng.getLat().doubleValue();
                // Get northeast longitude.
                Double northeastLongitude = northeastLatLng.getLng().doubleValue();

                builder.append(this.centerMapBuilder(southwestLatitude.toString(),
                        southwestLongitude.toString(),
                        northeastLatitude.toString(),
                        northeastLongitude.toString()));

            }
        }
//        this.log.debug("End method: \"mapBuilder\"");
        return builder;
    }

    private StringBuilder centerMapBuilder(String southwestLatitude, String southwestLongitude,

                                           String northeastLatitude, String northeastLongitude) {
//        this.log.debug("Method start: \"centerMapBuilder\"");
        StringBuilder builder = new StringBuilder();
        // Build bounds node.
        builder.append(COMMA);
        builder.append("\"bounds\":");

        //coordinates shouln't be with wrong characters, but as there are manually typed, it may be controlled

        builder.append(START_ARRAY);
        // Build southwest node.
        builder.append(START_ITEM);
        builder.append("\"latitude\":\"" + replaceQuotesAndReturns(southwestLatitude) + "\"");
        builder.append(COMMA);
        builder.append("\"longitude\":\"" + replaceQuotesAndReturns(southwestLongitude) + "\"");
        builder.append(END_ITEM);

        // Build northeast node.
        builder.append(COMMA);
        builder.append(START_ITEM);
        builder.append("\"latitude\":\"" + replaceQuotesAndReturns(northeastLatitude) + "\"");
        builder.append(COMMA);
        builder.append("\"longitude\":\"" + replaceQuotesAndReturns(northeastLongitude) + "\"");
        builder.append(END_ITEM);
        builder.append(END_ARRAY);
//        this.log.debug("End method: \"centerMapBuilder\"");
        return builder;
    }

    private StringBuilder buildNode(Coordinates repo){
        StringBuilder builder = new StringBuilder();
        builder.append(START_ITEM);
        builder.append("\"latitude\":\""+repo.getLat()+"\"");
        builder.append(COMMA);
        builder.append("\"longitude\":\""+repo.getLon()+"\"");
        builder.append(COMMA);
        //this escapes " in field
        builder.append("\"name\":\""+replaceQuotesAndReturns(repo.getNameInstitution())+"\"");
        ArchivalInstitution ai = repo.getArchivalInstitution();
        if(ai!=null){
            builder.append(COMMA);
            builder.append("\"aiId\":\""+ai.getAiId()+"\"");
        }
        //Parse street, postalCity and country
        builder.append(COMMA);
        builder.append("\"street\":\""+replaceQuotesAndReturns(repo.getStreet())+"\"");
        builder.append(COMMA);
        builder.append("\"postalcity\":\""+replaceQuotesAndReturns(repo.getPostalCity())+"\"");
        builder.append(COMMA);
        builder.append("\"country\":\""+replaceQuotesAndReturns(repo.getCountry())+"\"");
        builder.append(END_ITEM);
        return builder;
    }

    private String replaceQuotesAndReturns(String string) {
        String result = string;
        if (result != null) {
            result = result.replaceAll("[/]", "");
            result = replaceQuotesAndReturnsForTree(result);
        }
        return result;
    }

    private String replaceQuotesAndReturnsForTree(String string) {
        String result = string;
        if (result != null) {
            result = result.replaceAll("\"", "'");
            result = result.replaceAll("[\n\t\r\\\\%;]", "");
            result = result.trim();
        }
        return result;
    }

    public String getAiId() {
        return aiId;
    }

    public String getAiName() {
        return aiName;
    }

    public String getAiRepositoryCode() {
        return aiRepositoryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setAiId(String aiId) {
        this.aiId = aiId;
    }

    public void setAiName(String aiName) {
        this.aiName = aiName;
    }

    public void setAiRepositoryCode(String aiRepositoryCode) {
        this.aiRepositoryCode = aiRepositoryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
