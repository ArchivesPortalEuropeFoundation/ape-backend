package eu.apenet.scripts;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderStatus;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CoordinatesDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Coordinates;
import eu.archivesportaleurope.commons.config.ScriptsConfig;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EagLocationStatistics {

    private static final String CONFIG_PROPERTIES_PATH = "CONFIG_PROPERTIES_PATH";

    private final Logger log = Logger.getLogger(getClass());

    public static void main (String[] args) throws NamingException {
        System.out.println("Hello from CLI world!");

        (new EagLocationStatistics()).doTheJob();

    }

    private String removeNewLine(String s){

        return s.replaceAll("\\R", " ");
    }

    private void doTheJob(){
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsWithoutGroups();

        String separator = "|";

        for (ArchivalInstitution archivalInstitution: archivalInstitutions){
            String repoPath = ((ScriptsConfig) APEnetUtilities.getConfig()).getRepoDirPath();
            String strPath = repoPath + archivalInstitution.getEagPath();
            File EAGfile = new File(strPath);
            if (EAGfile.exists()){


                try {
                    NodeList nodeRepositoryList = null;
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    dbFactory.setNamespaceAware(true);
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    InputStream sfile = new FileInputStream(strPath);
                    Document doc = dBuilder.parse(sfile);

                    doc.getDocumentElement().normalize();

                    String autForm = null;
                    int autoFormSize = 0;
                    String autFormFull = "";
                    nodeRepositoryList = doc.getElementsByTagNameNS("http://www.archivesportaleurope.net/Portal/profiles/eag_2012/", "autform");
                    if (nodeRepositoryList != null && nodeRepositoryList.getLength() > 0) {
                        autForm = nodeRepositoryList.item(0).getTextContent();
                        autoFormSize = nodeRepositoryList.getLength();

                        for (int j = 0; j < nodeRepositoryList.getLength(); j++) {
                            autFormFull += (j==0?"":" || ") + nodeRepositoryList.item(j).getTextContent();
                        }
                    }

                    nodeRepositoryList = doc.getElementsByTagNameNS("http://www.archivesportaleurope.net/Portal/profiles/eag_2012/", "repository");

                    if (nodeRepositoryList != null && nodeRepositoryList.getLength() > 0) {

                        //for each repository/branch
                        for (int j = 0; j < nodeRepositoryList.getLength(); j++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("RESULT").append(separator);
                            sb.append(archivalInstitution.getAiId()).append(separator);
                            sb.append(archivalInstitution.getRepositorycode()).append(separator);
                            sb.append(archivalInstitution.getEagPath()).append(separator);
                            sb.append(archivalInstitution.getCountry().getCname()).append(separator);
                            if (archivalInstitution.getAiname() != null) {
                                sb.append(removeNewLine(archivalInstitution.getAiname())).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (archivalInstitution.getAutform() != null) {
                                sb.append(removeNewLine(archivalInstitution.getAutform())).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }

                            sb.append(archivalInstitution.isContainSearchableItems()).append(separator);
                            sb.append(removeNewLine(autForm)).append(separator);
                            sb.append(autoFormSize).append(separator);
                            sb.append(removeNewLine(autFormFull)).append(separator);


                            Node repositoryNode = nodeRepositoryList.item(j);
                            NodeList repositoryChildsList = repositoryNode.getChildNodes();
                            boolean isVisitorAddress = false;
                            boolean isNameRecovered = false;
                            boolean isLocationRecovered = false;

                            String repoName = null;
                            String repoStreet = null;   // Institution or repository address (street).
                            String repoPostalCity = null;   // Institution or repository address (municipalityPostalcode).
                            String repoCountry = null;   // Institution or repository address (country).
                            double repoLat=-1000;		// Institution or repository latitude.
                            double repoLon=-1000;
                            String email = null;
                            String phone = null;

                            for (int k=0; k< repositoryChildsList.getLength() && (!isNameRecovered || !isLocationRecovered); k++) {
                                Node repositoryChildNode = repositoryChildsList.item(k);

                                if (repositoryChildNode.getNodeName().equalsIgnoreCase("repositoryName")) {
                                    //repositoryName
                                    isNameRecovered = true;
                                    repoName = removeNewLine(repositoryChildNode.getTextContent());
                                } else if (repositoryChildNode.getNodeName().equalsIgnoreCase("telephone")) {
                                    //telephone
                                    phone = removeNewLine(repositoryChildNode.getTextContent());
                                } else if (repositoryChildNode.getNodeName().equalsIgnoreCase("email")) {
                                    //email
                                    NamedNodeMap emailChildAttributesMap = repositoryChildNode.getAttributes();
                                    for (int l = 0; l < emailChildAttributesMap.getLength(); l++) {
                                        Node attributeNode = emailChildAttributesMap.item(l);
                                        if (attributeNode.getNodeName().equalsIgnoreCase("href")) {
                                            email = removeNewLine(attributeNode.getTextContent());
                                        }
                                    }
                                } else if (repositoryChildNode.getNodeName().equalsIgnoreCase("location")) {
                                    NamedNodeMap repositoryChildAttributesMap = repositoryChildNode.getAttributes();
                                    isVisitorAddress = false;

                                    for (int l = 0; l < repositoryChildAttributesMap.getLength(); l++) {
                                        Node attributeNode = repositoryChildAttributesMap.item(l);
                                        if (attributeNode.getNodeName().equalsIgnoreCase("localType")) {
                                            if (attributeNode.getTextContent().equalsIgnoreCase("visitors address")) {
                                                isVisitorAddress = true;
                                            }
                                        } else if (attributeNode.getNodeName().equalsIgnoreCase("latitude")) {
                                            String latitude = attributeNode.getTextContent();
                                            try {
                                                repoLat = Double.parseDouble(latitude); //Co_lat
                                            } catch (Exception e) {
//                                                log.debug(strPath + " Error: " + this.getCo_name() + ": " + e.toString());
                                            }
                                        } else if (attributeNode.getNodeName().equalsIgnoreCase("longitude")) {
                                            String longitude = attributeNode.getTextContent();
                                            try {
                                                repoLon = Double.parseDouble(longitude); //Co_lon
                                            } catch (Exception e) {
//                                                log.debug(strPath + " Error: " + this.getCo_name() + ": " + e.toString());
                                            }
                                        }
                                    }// for L

                                    if (isVisitorAddress) {
                                        boolean isStreetRecovered = false;
                                        boolean isMunicipalityRecovered = false;
                                        NodeList visitorsAddressChildsList = repositoryChildNode.getChildNodes();
                                        String municipalityPostalcode = "";
                                        String street = "";
                                        String country = "";
                                        for (int l=0; l< visitorsAddressChildsList.getLength() && (!isStreetRecovered || !isMunicipalityRecovered); l++) {
                                            Node visitorsAddressChildNode = visitorsAddressChildsList.item(l);

                                            if (visitorsAddressChildNode.getNodeName().equals("municipalityPostalcode")) {
                                                //municipalityPostalcode
                                                municipalityPostalcode = removeNewLine(visitorsAddressChildNode.getTextContent().trim());
                                                isMunicipalityRecovered = true;
                                            } else if (visitorsAddressChildNode.getNodeName().equals("street")) {
                                                //street
                                                street = removeNewLine(visitorsAddressChildNode.getTextContent().trim());
                                                isStreetRecovered = true;
                                            } else if (visitorsAddressChildNode.getNodeName().equals("country")) {
                                                //country
                                                country = removeNewLine(visitorsAddressChildNode.getTextContent().trim());
                                            }
                                        }// for L
                                        repoStreet = street;
                                        repoPostalCity = municipalityPostalcode;
                                        repoCountry = country;
                                        if (isStreetRecovered || isMunicipalityRecovered) {
                                            isLocationRecovered = true;
                                        }
                                    }// if isVisitorAddress
                                }

//                                if(this.getCo_name() == null||this.getCo_name().trim().isEmpty()){
//                                    this.setCo_name(archivalInstitution.getAiname());
//                                    isNameRecovered = true;
//                                }
//                                archivalInstitution = createCoordinates(archivalInstitution);

                            }// for K

                            if (repoName != null){
                                sb.append(repoName).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (repoStreet != null){
                                sb.append(repoStreet).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (repoPostalCity != null){
                                sb.append(repoPostalCity).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (repoCountry != null){
                                sb.append(repoCountry).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (repoLat != -1000){
                                sb.append(repoLat).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (repoLon != -1000){
                                sb.append(repoLon).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (email != null){
                                sb.append(email).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            if (phone != null){
                                sb.append(phone).append(separator);
                            }
                            else {
                                sb.append(separator);
                            }
                            System.out.println(sb.toString());


//                            if(repoName == null || repoName.trim().isEmpty()){
//                                repoName = archivalInstitution.getAiname();
//                                isNameRecovered = true;
//                            }
//
//                            if (isNameRecovered && isLocationRecovered) {
//                                Geocoder geocoder = new Geocoder();
//
//                                String address = repoStreet + ", " + repoPostalCity + ", " + repoCountry;
//                                GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).getGeocoderRequest();
//                                GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
//
//                                if (geocoderResponse.getStatus().equals(GeocoderStatus.OK)) {
//                                    System.out.println("Localtion: OK");
//                                }
//                                else {
//                                    System.out.println("Location: NOT OK: " + geocoderResponse.toString());
//                                }
//                            }



//                            List<Coordinates> repoCoordinatesList = coordinatesDAO.findCoordinatesByArchivalInstitution(archivalInstitution);
//
//                            if (repoCoordinatesList != null && !repoCoordinatesList.isEmpty()) {
//                                Coordinates coordinates = null;
//                                if (repoCoordinatesList.size() > 1) {
//                                    Iterator<Coordinates> repoCoordinatesIt = repoCoordinatesList.iterator();
//                                    if (repositoryName != null && !repositoryName.isEmpty()) {
//                                        // Select the proper element from database.
//                                        while (repoCoordinatesIt.hasNext()) {
//                                            Coordinates coordinatesTest = repoCoordinatesIt.next();
//                                            if (repositoryName.startsWith(coordinatesTest.getNameInstitution())) {
//                                                coordinates = coordinatesTest;
//                                            }
//                                        }
//                                    }
//                                }
//
//                                // At this point, if coordinates still null, set the value of the
//                                // first element of the list.
//                                if (coordinates == null) {
//                                    coordinates = repoCoordinatesList.get(0);
//                                }
//
//                                if (coordinates!=null) {
//                                    if (coordinates.getLat() != 0 || coordinates.getLon() != 0) {
//                                        //control elements outside the printable earth coordinates (-77 to 82) and (-177 to 178)
//                                        if ((coordinates.getLat() >= -77 && coordinates.getLat() <= 82) && (coordinates.getLon() >= -177 && coordinates.getLon() <= 178)) {
//                                            reposList.add(coordinates);
//                                        }
//                                    }
//                                }
//                            }

                        }//for J
                    }// if nodeAutLi
                    else {
                        System.out.println("ERROR 2 for: " + archivalInstitution.getRepositorycode());
                    }
                } catch (ParserConfigurationException pcEx) {
                    log.error("Error when creating the parser configuration." );
                    log.error(pcEx.getCause());
                } catch (FileNotFoundException fnfEx) {
                    log.error("Error file " + strPath + " not found in system.");
                    log.error(fnfEx.getCause());
                } catch (IOException ioEx) {
                    log.error("Input/Outpur error with file " + strPath);
                    log.error(ioEx.getCause());
                } catch (SAXException saxEx) {
                    log.error("SAX exception with file " + strPath);
                    log.error(saxEx.getCause());
                }
            }
            else {
                System.out.println("ERROR 1 for: " + archivalInstitution.getRepositorycode());
            }
        }
//        Properties properties = System.getProperties();
//        properties.list(System.out);
//
//        String configProperties = System.getProperty("REPOSITORY_DIR_PATH");
//        System.out.println("lala: " + configProperties);
//        configProperties = System.getenv("REPOSITORY_DIR_PATH");
//        System.out.println("lala: " + configProperties);
//
//        Map<String, String> env = System.getenv();
//        for (String envName : env.keySet()) {
//            System.out.format("%s=%s%n", envName, env.get(envName));
//        }
//
//
//        CoordinatesDAO coordinatesDAO = DAOFactory.instance().getCoordinatesDAO();
//        System.out.println("size2: " + coordinatesDAO.getCoordinates().size());
    }
}
