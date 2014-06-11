/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.services.eaccpf.CreateEacCpfTask;
import eu.apenet.dpt.utils.eaccpf.*;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.UploadMethod;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author papp
 */
public class CreateEacCpf {

    private EacCpf eacCpf = new EacCpf();
    private Map parameters;
    private eu.apenet.persistence.vo.EacCpf newEac = new eu.apenet.persistence.vo.EacCpf();
    private EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
    private int aiId;

    //global StringBuilder for date format
    StringBuilder standardDate = new StringBuilder();

    public CreateEacCpf(HttpServletRequest request, int aiId) {
        this.parameters = request.getParameterMap();
        this.aiId = aiId;

        Control control = fillControl();
        CpfDescription cpfDescription = fillCpfDescription();

        eacCpf.setControl(control);
        eacCpf.setCpfDescription(cpfDescription);
    }

    public EacCpf getEacCpf() {
        return eacCpf;
    }

    private Control fillControl() {
        Control control = new Control();

        // eac-cpf/control/recordId
        if (control.getRecordId() == null) {
            control.setRecordId(new RecordId());
        }
        if (parameters.containsKey("apeId") || (String[]) parameters.get("apeId") != null) {
            String[] content = (String[]) parameters.get("apeId");
            if (content.length == 1) {
                control.getRecordId().setValue(content[0]);
            }
        } else {
            UploadMethod uploadMethod = new UploadMethod();
            uploadMethod.setMethod(UploadMethod.HTTP);
            uploadMethod.setId(3);
            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
            String tempIdentifier = "eac_" + archivalInstitution.getRepositorycode();
            newEac.setUploadDate(new java.util.Date());
            newEac.setIdentifier(tempIdentifier);
            newEac.setUploadMethod(uploadMethod);
            newEac.setArchivalInstitution(archivalInstitution);
            newEac.setPath(CreateEacCpfTask.getPath(XmlType.EAC_CPF, archivalInstitution));
            newEac.setTitle("temporary title");
            newEac = eacCpfDAO.store(newEac);

            control.getRecordId().setValue("eac_" + Integer.toString(newEac.getId()));
        }

        // eacCpf/control/otherRecordId
        int counter = 1;
        String parameterName = "textLocalId_";
        while (parameters.containsKey(parameterName + counter) || (String[]) parameters.get(parameterName + counter) != null) {
            OtherRecordId otherRecordId = new OtherRecordId();
            String[] content = (String[]) parameters.get(parameterName + counter);
            if (content.length == 1) {
                otherRecordId.setContent(content[0]);
            }
            otherRecordId.setLocalType("original");
            control.getOtherRecordId().add(otherRecordId);
            counter++;
        }

        // eacCpf/control/maintenanceStatus
        if (control.getMaintenanceStatus() == null) {
            MaintenanceStatus maintenanceStatus = new MaintenanceStatus();
            maintenanceStatus.setValue("new");
            control.setMaintenanceStatus(maintenanceStatus);
        } else {
            control.getMaintenanceStatus().setValue("revised");
        }

        // eacCpf/control/publicationStatus
        if (control.getPublicationStatus() == null) {
            control.setPublicationStatus(new PublicationStatus());
        }
        control.getPublicationStatus().setValue("approved");

        // eacCpf/control/maintenanceAgency
        if (control.getMaintenanceAgency() == null) {
            control.setMaintenanceAgency(new MaintenanceAgency());
        }

        // eacCpf/control/maintenanceAgency/agencyCode
        if (control.getMaintenanceAgency().getAgencyCode() == null) {
            control.getMaintenanceAgency().setAgencyCode(new AgencyCode());
        }
        String[] content = (String[]) parameters.get("responsibleInstitution");
        if (content.length == 1) {
            control.getMaintenanceAgency().getAgencyCode().setValue(content[0]);
        }

        // eacCpf/control/maintenanceAgency/agencyName
        if (parameters.containsKey("responsibleInstitution") && (String[]) parameters.get("responsibleInstitution") != null) {
            if (control.getMaintenanceAgency().getAgencyName() == null) {
                control.getMaintenanceAgency().setAgencyName(new AgencyName());
            }
            control.getMaintenanceAgency().getAgencyName().setContent("European test archives");
        }

        // eacCpf/control/languageDeclaration
        if (!"----".equals(((String[]) parameters.get("controlLanguage"))[0])
                || !"----".equals(((String[]) parameters.get("controlScript"))[0])) {
            LanguageDeclaration languageDeclaration = new LanguageDeclaration();

            if (!"----".equals(((String[]) parameters.get("controlLanguage"))[0])) {
                Language language = new Language();
                language.setLanguageCode(((String[]) parameters.get("controlLanguage"))[0]);
                languageDeclaration.setLanguage(language);
            }

            if (!"----".equals(((String[]) parameters.get("controlScript"))[0])) {
                Script script = new Script();
                script.setScriptCode(((String[]) parameters.get("controlScript"))[0]);
                languageDeclaration.setScript(script);
            }
            control.setLanguageDeclaration(languageDeclaration);
        }

        // eacCpf/control/conventionDeclaration items
        String[] conventionValues = {"ISO 639-2b", "ISO 3166-1", "ISO 8601", "ISO 15511", "ISO 15924"};
        for (String conventionValue : conventionValues) {
            ConventionDeclaration conventionDeclaration = new ConventionDeclaration();
            Abbreviation abbreviation = new Abbreviation();

            abbreviation.setValue(conventionValue);
            conventionDeclaration.setAbbreviation(abbreviation);
            conventionDeclaration.setCitation(new Citation());
            control.getConventionDeclaration().add(conventionDeclaration);
        }

        // eacCpf/control/maintenanceHistory
        boolean exists = true;
        if (control.getMaintenanceHistory() == null) {
            control.setMaintenanceHistory(new MaintenanceHistory());
            exists = false;
        }

        // eacCpf/control/maintenanceHistory/maintenanceEvent
        MaintenanceEvent maintenanceEvent = new MaintenanceEvent();

        // eacCpf/control/maintenanceHistory/maintenanceEvent/eventType
        if (maintenanceEvent.getEventType() == null) {
            maintenanceEvent.setEventType(new EventType());
        }
        if (exists) {
            maintenanceEvent.getEventType().setValue("revised");
        } else {
            maintenanceEvent.getEventType().setValue("created");
        }

        // eacCpf/control/maintenanceHistory/maintenanceEvent/eventDateTime
        if (maintenanceEvent.getEventDateTime() == null) {
            maintenanceEvent.setEventDateTime(new EventDateTime());
        }
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
        maintenanceEvent.getEventDateTime().setStandardDateTime(df.format(new GregorianCalendar().getTime()));

        // eacCpf/control/maintenanceHistory/maintenanceEvent/agentType
        if (maintenanceEvent.getAgentType() == null) {
            AgentType agentType = new AgentType();
            agentType.setValue("human");
            maintenanceEvent.setAgentType(agentType);
        }

        // eacCpf/control/maintenanceHistory/maintenanceEvent/agent
        if (maintenanceEvent.getAgent() == null) {
            maintenanceEvent.setAgent(new Agent());
        }
        if (parameters.containsKey("responsiblePerson") && ((String[]) parameters.get("responsiblePerson")).length != 0 && !((String[]) parameters.get("responsiblePerson"))[0].isEmpty()) {
            maintenanceEvent.getAgent().setContent(((String[]) parameters.get("responsiblePerson"))[0]);
        } else {
            maintenanceEvent.getAgent().setContent("automatically created agent");
        }

        EventDescription eventDescription = new EventDescription();
        eventDescription.setContent("Converted_apeEAC-CPF_version_Dashboard");
        maintenanceEvent.setEventDescription(eventDescription);

        // MaintenanceHistory
        control.getMaintenanceHistory().getMaintenanceEvent().add(maintenanceEvent);

        return control;
    }

    private CpfDescription fillCpfDescription() {
        CpfDescription cpfDescription = new CpfDescription();

        Identity identity = fillIdentity();
        cpfDescription.setIdentity(identity);

        Description description = fillDescription();
        cpfDescription.setDescription(description);

        if (!((String[]) parameters.get("textCpfRelationName_1"))[0].isEmpty() || !((String[]) parameters.get("textResRelationName_1"))[0].isEmpty()) {
            Relations relations = fillRelations();
            cpfDescription.setRelations(relations);
        }
        return cpfDescription;
    }

    private Identity fillIdentity() {
        Identity identity = new Identity();
        //general counter and auxilliary vars
        int tableCounter;
        int rowCounter;
        int actualDateRows;
        String parameterName1;
        String parameterName2;
        String parameterName3;
        String parameterName4;
        String[] parameterContent;
        LinkedList<Identity.NameEntry> nameEntries = new LinkedList<Identity.NameEntry>();

        // /eacCpf/cpfDescription/identity/entityId
        tableCounter = 1;
        parameterName1 = "textPersonTypeId_";
        parameterName2 = "textPersonId_";

        while (parameters.containsKey(parameterName2 + tableCounter) || (String[]) parameters.get(parameterName2 + tableCounter) != null) {
            EntityId entityId = new EntityId();
            parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
            if (parameterContent.length == 1) {
                entityId.setContent(parameterContent[0]);
            }
            if (parameters.containsKey(parameterName1 + tableCounter) || (String[]) parameters.get(parameterName1 + tableCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName1 + tableCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    entityId.setLocalType(parameterContent[0]);
                }
            }
            if (!entityId.getContent().isEmpty()) {
                identity.getEntityId().add(entityId);
            }
            tableCounter++;
        }

        // /eacCpf/cpfDescription/identity/entityType
        EntityType entityType = new EntityType();
        entityType.setValue(((String[]) parameters.get("cpfType"))[0]);
        identity.setEntityType(entityType);

        // /eacCpf/cpfDescription/identity/nameEntry
        tableCounter = 1;
        rowCounter = 1;
        parameterName1 = "_part_";
        parameterName2 = "identityNameLanguage_";
        parameterName3 = "identityFormOfName_";
        parameterName4 = "_comp_";

        while (parameters.containsKey("identityPersonName_" + tableCounter + parameterName1 + rowCounter) || (String[]) parameters.get("identityPersonName_" + tableCounter + parameterName1 + rowCounter) != null) {

            Identity.NameEntry nameEntry = new Identity.NameEntry();
            parameterContent = (String[]) parameters.get(parameterName3 + tableCounter);
            if (parameterContent.length == 1) {
                if (!parameterContent[0].equals("")) {
                    nameEntry.setLocalType(parameterContent[0]);
                }
            }

            // /eacCpf/cpfDescription/identity/nameEntry/part
            while ((String[]) parameters.get("identityPersonName_" + tableCounter + parameterName1 + rowCounter) != null || parameters.containsKey("identityPersonName_" + tableCounter + parameterName1 + rowCounter)) {
                if (!((String[]) parameters.get("identityPersonName_" + tableCounter + parameterName1 + rowCounter))[0].isEmpty()) {
                    Part part = new Part();
                    parameterContent = (String[]) parameters.get("identityPersonName_" + tableCounter + parameterName4 + rowCounter);
                    if (parameterContent.length == 1) {
                        part.setLocalType(parameterContent[0]);
                    }
                    parameterContent = (String[]) parameters.get("identityPersonName_" + tableCounter + parameterName1 + rowCounter);
                    if (parameterContent.length == 1) {
                        part.setContent(parameterContent[0]);
                    }
                    if (parameters.containsKey(parameterName2 + tableCounter) || (String[]) parameters.get(parameterName2 + tableCounter) != null) {
                        parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
                        if (parameterContent.length == 1) {
                            if (!parameterContent[0].equals("")) {
                                part.setLang(parameterContent[0]);
                            }
                        }
                    }
                    nameEntry.getPart().add(part);
                }
                rowCounter++;
            }

            //add any dates
            rowCounter = 1;
            actualDateRows = Integer.parseInt(((String[]) parameters.get("identityPersonName_" + tableCounter + "_rows"))[0]);

            // If there are any dates
            if (actualDateRows > 0) {
                UseDates useDates = new UseDates();
                //create a date set for 2 or more date rows, single elements for one date row
                if (actualDateRows > 1) {
                    DateSet dateSet = new DateSet();
                    while (parameters.containsKey("identityPersonName_" + tableCounter + "_date_1_Year_" + rowCounter) || (String[]) parameters.get("identityPersonName_" + tableCounter + "_date_1_Year_" + rowCounter) != null || parameters.containsKey("identityPersonName_" + tableCounter + "_date_unknown_1_" + rowCounter)) {
                        //if the rows has two dates, we need a date range, otherwise a simple date will cut it
                        if (parameters.containsKey("identityPersonName_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("identityPersonName_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                            DateRange dateRange = createDateRange("identityPersonName_" + tableCounter, rowCounter);
                            dateSet.getDateOrDateRange().add(dateRange);
                        } else {
                            //Apply same procedure as above to single date
                            Date date = createDate("identityPersonName_" + tableCounter, rowCounter);
                            dateSet.getDateOrDateRange().add(date);
                        }
                        rowCounter++;
                    }
                    useDates.setDateSet(dateSet);
                } else if (actualDateRows == 1) {
                    //if the rows has two dates, we need a date range, otherwise a simple date will cut it
                    if (parameters.containsKey("identityPersonName_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("identityPersonName_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                        DateRange dateRange = createDateRange("identityPersonName_" + tableCounter, rowCounter);
                        useDates.setDateRange(dateRange);
                    } else {
                        //Apply same procedure as above to single date
                        Date date = createDate("identityPersonName_" + tableCounter, rowCounter);
                        useDates.setDate(date);
                    }
                }
                nameEntry.setUseDates(useDates);
            }
            //collect all name entries in one list for check if <nameEntryParallel> is required or not
            nameEntries.add(nameEntry);
            tableCounter++;
        }

//        /*
//         If there are more than one autorized and/or alternative forms,
//         wrap these in a <nameEntryParallel> element.
//         Then add the remaining name forms to the list.
//         */
//        //Determine quantities
//        int counterAuth = 0;
//        int counterAlt = 0;
//        for (Identity.NameEntry nameEntry : nameEntries) {
//            if (nameEntry.getLocalType().equals("authorized")) {
//                counterAuth++;
//            }
//            if (nameEntry.getLocalType().equals("alternative")) {
//                counterAlt++;
//            }
//        }
//        //Separate multiple authorized forms
//        if (counterAuth > 1) {
//            LinkedList<Identity.NameEntry> authForms = new LinkedList<Identity.NameEntry>();
//            NameEntryParallel nameEntryParallel = new NameEntryParallel();
//            for (Identity.NameEntry iNameEntry : nameEntries) {
//                if (iNameEntry.getLocalType().equals("authorized")) {
//                    NameEntry nameEntry = new NameEntry();
//                    nameEntry.setLang(iNameEntry.getLang());
//                    nameEntry.getPart().addAll(iNameEntry.getPart());
//                    nameEntryParallel.getContent().add(nameEntry);
//                    authForms.add(iNameEntry);
//                }
//            }
//            nameEntryParallel.setLocalType("authorized");
//            identity.getNameEntryParallelOrNameEntry().add(nameEntryParallel);
//            nameEntries.removeAll(authForms);
//        }
//        //Separate multiple alternative forms
//        if (counterAlt > 1) {
//            LinkedList<Identity.NameEntry> altForms = new LinkedList<Identity.NameEntry>();
//            for (Identity.NameEntry nameEntry : nameEntries) {
//                if (nameEntry.getLocalType().equals("alternative")) {
//                    altForms.add(nameEntry);
//                }
//            }
//            NameEntryParallel nameEntryParallel = new NameEntryParallel();
//            nameEntryParallel.getContent().addAll(altForms);
//            nameEntryParallel.setLocalType("alternative");
//            identity.getNameEntryParallelOrNameEntry().add(nameEntryParallel);
//            nameEntries.removeAll(altForms);
//        }
//        //Add remaining forms
        identity.getNameEntryParallelOrNameEntry().addAll(nameEntries);

        return identity;
    }

    private Description fillDescription() {
        Description description = new Description();
        //general counter and auxilliary vars
        int tableCounter;
        int rowCounter;
        int actualDateRows;
        String parameterName1;
        String parameterName2;
        String parameterName3;
        String parameterName4;
        String parameterName5;
        String parameterName6;
        String parameterName7;
        String[] parameterContent;

        // /eacCpf/cpfDescription/description/existDates
        rowCounter = 1;
        actualDateRows = Integer.parseInt(((String[]) parameters.get("dateExistenceTable_rows"))[0]);

        ExistDates existDates = new ExistDates();

        // If there are any dates
        if (actualDateRows > 0) {
            //create a date set for 2 or more date rows, single elements for one date row
            if (actualDateRows > 1) {
                DateSet dateSet = new DateSet();
                while (parameters.containsKey("dateExistenceTable_date_1_Year_" + rowCounter) || (String[]) parameters.get("dateExistenceTable_date_1_Year_" + rowCounter) != null || parameters.containsKey("dateExistenceTable_date_unknown_1_" + rowCounter)) {
                    //if the rows has two dates, we need a date range, otherwise a simple date will cut it
                    if (parameters.containsKey("dateExistenceTable_date_2_Year_" + rowCounter) || parameters.containsKey("dateExistenceTable_date_unknown_2_" + rowCounter)) {
                        DateRange dateRange = createDateRange("dateExistenceTable", rowCounter);
                        dateSet.getDateOrDateRange().add(dateRange);
                    } else {
                        //Apply same procedure as above to single date
                        Date date = createDate("dateExistenceTable", rowCounter);
                        dateSet.getDateOrDateRange().add(date);
                    }
                    rowCounter++;
                }
                existDates.setDateSet(dateSet);
            } else if (actualDateRows == 1) {
                if (parameters.containsKey("dateExistenceTable_date_2_Year_1") || parameters.containsKey("dateExistenceTable_date_unknown_2_1")) {
                    DateRange dateRange = createDateRange("dateExistenceTable", 1);
                    existDates.setDateRange(dateRange);
                } else {
                    //Apply same procedure as above to single date
                    Date date = createDate("dateExistenceTable", rowCounter);
                    existDates.setDate(date);
                }
            }
        }
        description.setExistDates(existDates);

        // /eacCpf/cpfDescription/description/places
        tableCounter = 1;
        rowCounter = 1;
        parameterName1 = "place_";
        parameterName2 = "placeLanguage_";
        parameterName3 = "linkPlaceVocab_";
        parameterName4 = "placeCountry_";
        parameterName5 = "_addressDetails_";
        parameterName6 = "_addressComponent_";
        parameterName7 = "placeRole_";

        if (!((String[]) parameters.get(parameterName1 + tableCounter))[0].isEmpty()) {
            Places places = new Places();

            while ((String[]) parameters.get(parameterName1 + tableCounter) != null || parameters.containsKey(parameterName1 + tableCounter)) {
                Place place = new Place();

                PlaceEntry placeEntry = new PlaceEntry();

                // .../places/place@countryCode
                if ((String[]) parameters.get(parameterName4 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName4 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        placeEntry.setCountryCode(parameterContent[0]);
                    }
                }

                // .../places/place@localType
                if ((String[]) parameters.get(parameterName7 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName7 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        placeEntry.setLocalType(parameterContent[0]);
                    }
                }

                // .../places/place@vocabularySource
                if ((String[]) parameters.get(parameterName3 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName3 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        placeEntry.setVocabularySource(parameterContent[0]);
                    }
                }

                // .../places/place@xml:lang
                if ((String[]) parameters.get(parameterName2 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        placeEntry.setLang(parameterContent[0]);
                    }
                }

                // .../places/place
                if ((String[]) parameters.get(parameterName1 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName1 + tableCounter);
                    if (parameterContent.length == 1) {
                        placeEntry.setContent(parameterContent[0]);
                    }
                }
                place.getPlaceEntry().add(placeEntry);

                // .../places/place/address
                if ((String[]) parameters.get("placeTable_" + tableCounter + parameterName5 + rowCounter) != null
                        && !((String[]) parameters.get("placeTable_" + tableCounter + parameterName5 + rowCounter))[0].isEmpty()) {
                    Address address = new Address();

                    while ((String[]) parameters.get("placeTable_" + tableCounter + parameterName5 + rowCounter) != null || parameters.containsKey("placeTable_" + tableCounter + parameterName5 + rowCounter)) {
                        if (!((String[]) parameters.get("placeTable_" + tableCounter + parameterName5 + rowCounter))[0].isEmpty()) {
                            AddressLine addressLine = new AddressLine();

                            // .../places/place/address/addressLine@localType
                            if ((String[]) parameters.get("placeTable_" + tableCounter + parameterName6 + rowCounter) != null) {
                                parameterContent = (String[]) parameters.get("placeTable_" + tableCounter + parameterName6 + rowCounter);
                                if (parameterContent.length == 1 && !parameterContent[0].equals("----")) {
                                    addressLine.setLocalType(parameterContent[0]);
                                }
                            }

                            // .../places/place/address/addressLine@xml:lang
                            if ((String[]) parameters.get(parameterName2 + tableCounter) != null) {
                                parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
                                if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                                    addressLine.setLang(parameterContent[0]);
                                }
                            }

                            // .../places/place/address/addressLine
                            if ((String[]) parameters.get("placeTable_" + tableCounter + parameterName5 + rowCounter) != null) {
                                parameterContent = (String[]) parameters.get("placeTable_" + tableCounter + parameterName5 + rowCounter);
                                if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                                    addressLine.setContent(parameterContent[0]);
                                }
                            }

                            address.getAddressLine().add(addressLine);
                        }
                        rowCounter++;
                    }
                    place.setAddress(address);
                }

                //add any dates
                rowCounter = 1;
                actualDateRows = Integer.parseInt(((String[]) parameters.get("placeTable_" + tableCounter + "_rows"))[0]);

                // If there are any dates
                if (actualDateRows > 0) {
                    //create a date set for 2 or more date rows, single elements for one date row
                    if (actualDateRows > 1) {
                        DateSet dateSet = new DateSet();
                        while (parameters.containsKey("placeTable_" + tableCounter + "_date_1_Year_" + rowCounter) || (String[]) parameters.get("placeTable_" + tableCounter + "_date_1_Year_" + rowCounter) != null || parameters.containsKey("placeTable_" + tableCounter + "_date_unknown_1_" + rowCounter)) {
                            //if the rows has two dates, we need a date range, otherwise a simple date will cut it
                            if (parameters.containsKey("placeTable_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("placeTable_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                                DateRange dateRange = createDateRange("placeTable_" + tableCounter, rowCounter);
                                dateSet.getDateOrDateRange().add(dateRange);
                            } else {
                                //Apply same procedure as above to single date
                                Date date = createDate("placeTable_" + tableCounter, rowCounter);
                                dateSet.getDateOrDateRange().add(date);
                            }
                            rowCounter++;
                        }
                        place.setDateSet(dateSet);
                    } else if (actualDateRows == 1) {
                        //if the rows have two dates, we need a date range, otherwise a simple date will cut it
                        if (parameters.containsKey("placeTable_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("placeTable_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                            DateRange dateRange = createDateRange("placeTable_" + tableCounter, rowCounter);
                            place.setDateRange(dateRange);
                        } else {
                            //Apply same procedure as above to single date
                            Date date = createDate("placeTable_" + tableCounter, rowCounter);
                            place.setDate(date);
                        }
                    }
                }
                places.getPlace().add(place);
                tableCounter++;
            }

            description.getPlacesOrLocalDescriptionsOrLegalStatuses().add(places);
        }

        // /eacCpf/cpfDescription/description/functions
        tableCounter = 1;
        rowCounter = 1;
        parameterName1 = "function_";
        parameterName2 = "functionLanguage_";
        parameterName3 = "linkFunctionVocab_";
        parameterName4 = "functionDescription_";
        parameterName5 = "_place_";
        parameterName6 = "_country_";

        if (!((String[]) parameters.get(parameterName1 + tableCounter))[0].isEmpty()) {
            Functions functions = new Functions();

            while ((String[]) parameters.get(parameterName1 + tableCounter) != null || parameters.containsKey(parameterName1 + tableCounter)) {
                Function function = new Function();

                // retrieve language, if available
                String language = null;
                if ((String[]) parameters.get(parameterName2 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        language = parameterContent[0];
                    }
                }

                Term term = new Term();

                // .../functions/function/term@vocabularySource
                if ((String[]) parameters.get(parameterName3 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName3 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        term.setVocabularySource(parameterContent[0]);
                    }
                }

                // .../functions/function/term@xml:lang
                if (language != null) {
                    term.setLang(language);
                }

                // .../functions/function/term
                if ((String[]) parameters.get(parameterName1 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName1 + tableCounter);
                    if (parameterContent.length == 1) {
                        term.setContent(parameterContent[0]);
                    }
                }
                function.setTerm(term);

                // .../functions/function/descriptiveNote
                if ((String[]) parameters.get(parameterName4 + tableCounter) != null && !((String[]) parameters.get(parameterName4 + tableCounter))[0].isEmpty()) {
                    parameterContent = (String[]) parameters.get(parameterName4 + tableCounter);
                    if (parameterContent.length == 1) {
                        DescriptiveNote descriptiveNote = new DescriptiveNote();
                        P p = new P();
                        if (language != null) {
                            p.setLang(language);
                        }
                        p.setContent(parameterContent[0]);
                        descriptiveNote.getP().add(p);
                        function.setDescriptiveNote(descriptiveNote);
                    }
                }

                // .../functions/function/placeEntry
                if ((String[]) parameters.get("functionTable_" + tableCounter + parameterName5 + rowCounter) != null) {
                    while (parameters.containsKey("functionTable_" + tableCounter + parameterName5 + rowCounter) && (!((String[]) parameters.get("functionTable_" + tableCounter + parameterName5 + rowCounter))[0].isEmpty()) || parameters.containsKey("functionTable_" + tableCounter + parameterName5 + rowCounter)) {
                        PlaceEntry placeEntry = new PlaceEntry();

                        // .../placeEntry@countryCode
                        if ((String[]) parameters.get("functionTable_" + tableCounter + parameterName6 + rowCounter) != null) {
                            parameterContent = (String[]) parameters.get("functionTable_" + tableCounter + parameterName6 + rowCounter);
                            if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                                placeEntry.setCountryCode(parameterContent[0]);
                            }
                        }

                        // .../placeEntry@xml:lang
                        if (language != null) {
                            placeEntry.setLang(language);
                        }

                        // .../placeEntry
                        if ((String[]) parameters.get("functionTable_" + tableCounter + parameterName5 + rowCounter) != null) {
                            parameterContent = (String[]) parameters.get("functionTable_" + tableCounter + parameterName5 + rowCounter);
                            if (parameterContent.length == 1) {
                                placeEntry.setContent(parameterContent[0]);
                            }
                        }

                        function.getPlaceEntry().add(placeEntry);
                        rowCounter++;
                    }
                }

                //add any dates
                rowCounter = 1;
                actualDateRows = Integer.parseInt(((String[]) parameters.get("functionTable_" + tableCounter + "_rows"))[0]);

                // If there are any dates
                if (actualDateRows > 0) {
                    //create a date set for 2 or more date rows, single elements for one date row
                    if (actualDateRows > 1) {
                        DateSet dateSet = new DateSet();
                        while (parameters.containsKey("functionTable_" + tableCounter + "_date_1_Year_" + rowCounter) || (String[]) parameters.get("functionTable_" + tableCounter + "_date_1_Year_" + rowCounter) != null || parameters.containsKey("functionTable_" + tableCounter + "_date_unknown_1_" + rowCounter)) {
                            //if the rows has two dates, we need a date range, otherwise a simple date will cut it
                            if (parameters.containsKey("functionTable_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("functionTable_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                                DateRange dateRange = createDateRange("functionTable_" + tableCounter, rowCounter);
                                dateSet.getDateOrDateRange().add(dateRange);
                            } else {
                                //Apply same procedure as above to single date
                                Date date = createDate("functionTable_" + tableCounter, rowCounter);
                                dateSet.getDateOrDateRange().add(date);
                            }
                            rowCounter++;
                        }
                        function.setDateSet(dateSet);
                    } else if (actualDateRows == 1) {
                        //if the rows have two dates, we need a date range, otherwise a simple date will cut it
                        if (parameters.containsKey("functionTable_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("functionTable_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                            DateRange dateRange = createDateRange("functionTable_" + tableCounter, rowCounter);
                            function.setDateRange(dateRange);
                        } else {
                            //Apply same procedure as above to single date
                            Date date = createDate("functionTable_" + tableCounter, rowCounter);
                            function.setDate(date);
                        }
                    }
                }

                functions.getFunction().add(function);
                tableCounter++;
            }

            description.getPlacesOrLocalDescriptionsOrLegalStatuses().add(functions);
        }

        // /eacCpf/cpfDescription/description/occupations
        tableCounter = 1;
        rowCounter = 1;
        parameterName1 = "occupation_";
        parameterName2 = "occupationLanguage_";
        parameterName3 = "linkOccupationVocab_";
        parameterName4 = "occupationDescription_";
        parameterName5 = "_place_";
        parameterName6 = "_country_";

        if (!((String[]) parameters.get(parameterName1 + tableCounter))[0].isEmpty()) {
            Occupations occupations = new Occupations();

            while ((String[]) parameters.get(parameterName1 + tableCounter) != null || parameters.containsKey(parameterName1 + tableCounter)) {
                Occupation occupation = new Occupation();

                // retrieve language, if available
                String language = null;
                if ((String[]) parameters.get(parameterName2 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        language = parameterContent[0];
                    }
                }

                Term term = new Term();

                // .../occupations/occupation/term@vocabularySource
                if ((String[]) parameters.get(parameterName3 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName3 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                        term.setVocabularySource(parameterContent[0]);
                    }
                }

                // .../occupations/occupation/term@xml:lang
                if (language != null) {
                    term.setLang(language);
                }

                // .../occupations/occupation/term
                if ((String[]) parameters.get(parameterName1 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName1 + tableCounter);
                    if (parameterContent.length == 1) {
                        term.setContent(parameterContent[0]);
                    }
                }
                occupation.setTerm(term);

                // .../occupations/occupation/descriptiveNote
                if ((String[]) parameters.get(parameterName4 + tableCounter) != null && !((String[]) parameters.get(parameterName4 + tableCounter))[0].isEmpty()) {
                    parameterContent = (String[]) parameters.get(parameterName4 + tableCounter);
                    if (parameterContent.length == 1) {
                        DescriptiveNote descriptiveNote = new DescriptiveNote();
                        P p = new P();
                        if (language != null) {
                            p.setLang(language);
                        }
                        p.setContent(parameterContent[0]);
                        descriptiveNote.getP().add(p);
                        occupation.setDescriptiveNote(descriptiveNote);
                    }
                }

                // .../occupations/occupation/placeEntry
                if ((String[]) parameters.get("occupationTable_" + tableCounter + parameterName5 + rowCounter) != null) {
                    while (parameters.containsKey("occupationTable_" + tableCounter + parameterName5 + rowCounter) && (!((String[]) parameters.get("occupationTable_" + tableCounter + parameterName5 + rowCounter))[0].isEmpty()) || parameters.containsKey("occupationTable_" + tableCounter + parameterName5 + rowCounter)) {
                        PlaceEntry placeEntry = new PlaceEntry();

                        // .../placeEntry@countryCode
                        if ((String[]) parameters.get("occupationTable_" + tableCounter + parameterName6 + rowCounter) != null) {
                            parameterContent = (String[]) parameters.get("occupationTable_" + tableCounter + parameterName6 + rowCounter);
                            if (parameterContent.length == 1 && !parameterContent[0].equals("")) {
                                placeEntry.setCountryCode(parameterContent[0]);
                            }
                        }

                        // .../placeEntry@xml:lang
                        if (language != null) {
                            placeEntry.setLang(language);
                        }

                        // .../placeEntry
                        if ((String[]) parameters.get("occupationTable_" + tableCounter + parameterName5 + rowCounter) != null) {
                            parameterContent = (String[]) parameters.get("occupationTable_" + tableCounter + parameterName5 + rowCounter);
                            if (parameterContent.length == 1) {
                                placeEntry.setContent(parameterContent[0]);
                            }
                        }

                        occupation.getPlaceEntry().add(placeEntry);
                        rowCounter++;
                    }
                }

                //add any dates
                rowCounter = 1;
                actualDateRows = Integer.parseInt(((String[]) parameters.get("occupationTable_" + tableCounter + "_rows"))[0]);

                // If there are any dates
                if (actualDateRows > 0) {
                    //create a date set for 2 or more date rows, single elements for one date row
                    if (actualDateRows > 1) {
                        DateSet dateSet = new DateSet();
                        while (parameters.containsKey("occupationTable_" + tableCounter + "_date_1_Year_" + rowCounter) || (String[]) parameters.get("occupationTable_" + tableCounter + "_date_1_Year_" + rowCounter) != null || parameters.containsKey("occupationTable_" + tableCounter + "_date_unknown_1_" + rowCounter)) {
                            //if the rows has two dates, we need a date range, otherwise a simple date will cut it
                            if (parameters.containsKey("occupationTable_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("occupationTable_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                                DateRange dateRange = createDateRange("occupationTable_" + tableCounter, rowCounter);
                                dateSet.getDateOrDateRange().add(dateRange);
                            } else {
                                //Apply same procedure as above to single date
                                Date date = createDate("occupationTable_" + tableCounter, rowCounter);
                                dateSet.getDateOrDateRange().add(date);
                            }
                            rowCounter++;
                        }
                        occupation.setDateSet(dateSet);
                    } else if (actualDateRows == 1) {
                        //if the rows has two dates, we need a date range, otherwise a simple date will cut it
                        if (parameters.containsKey("occupationTable_" + tableCounter + "_date_2_Year_" + rowCounter) || parameters.containsKey("occupationTable_" + tableCounter + "_date_unknown_2_" + rowCounter)) {
                            DateRange dateRange = createDateRange("occupationTable_" + tableCounter, rowCounter);
                            occupation.setDateRange(dateRange);
                        } else {
                            //Apply same procedure as above to single date
                            Date date = createDate("occupationTable_" + tableCounter, rowCounter);
                            occupation.setDate(date);
                        }
                    }
                }

                occupations.getOccupation().add(occupation);
                tableCounter++;
            }

            description.getPlacesOrLocalDescriptionsOrLegalStatuses().add(occupations);
        }

        // /eacCpf/cpfDescription/description/structureOrGenealogy
        tableCounter = 1;
        parameterName1 = "genealogyDescription_";
        parameterName2 = "genealogyLanguage_";

        if (!((String[]) parameters.get(parameterName1 + tableCounter))[0].isEmpty()) {
            StructureOrGenealogy genealogy = null;

            while ((String[]) parameters.get(parameterName1 + tableCounter) != null || parameters.containsKey(parameterName1 + tableCounter)) {
                if (genealogy == null) {
                    genealogy = new StructureOrGenealogy();
                }
                P p = new P();

                //determine language of entry, if available
                if ((String[]) parameters.get(parameterName2 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("----")) {
                        p.setLang(parameterContent[0]);
                    }
                }

                // /eacCpf/cpfDescription/relations/cpfRelation@cpfRelationType
                if ((String[]) parameters.get(parameterName1 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName1 + tableCounter);
                    if (parameterContent.length == 1) {
                        p.setContent(parameterContent[0]);
                    }
                }

                if (!p.getContent().equals("")) {
                    genealogy.getMDiscursiveSet().add(p);
                }
                tableCounter++;
            }
            if (genealogy != null) {
                description.getPlacesOrLocalDescriptionsOrLegalStatuses().add(genealogy);
            }
        }

        // /eacCpf/cpfDescription/description/bioghist
        tableCounter = 1;
        parameterName1 = "biographyDescription_";
        parameterName2 = "biographyLanguage_";

        if (!((String[]) parameters.get(parameterName1 + tableCounter))[0].isEmpty()) {
            BiogHist biogHist = null;

            while ((String[]) parameters.get(parameterName1 + tableCounter) != null || parameters.containsKey(parameterName1 + tableCounter)) {
                if (biogHist == null) {
                    biogHist = new BiogHist();
                }
                P p = new P();

                // determine language of entry, if available
                if ((String[]) parameters.get(parameterName2 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName2 + tableCounter);
                    if (parameterContent.length == 1 && !parameterContent[0].equals("----")) {
                        p.setLang(parameterContent[0]);
                    }
                }

                // determine <p> content
                if ((String[]) parameters.get(parameterName1 + tableCounter) != null) {
                    parameterContent = (String[]) parameters.get(parameterName1 + tableCounter);
                    if (parameterContent.length == 1) {
                        p.setContent(parameterContent[0]);
                    }
                }

                if (!p.getContent().equals("")) {
                    biogHist.getChronListOrPOrCitation().add(p);
                }
                tableCounter++;
            }
            if (biogHist != null) {
                description.getBiogHist().add(biogHist);
            }
        }

        return description;
    }

    private Relations fillRelations() {
        Relations relations = new Relations();
        //general counter and auxilliary vars
        int relationCounter;
        int organisationCounter;
        String parameterName1;
        String parameterName2;
        String parameterName3;
        String parameterName4;
        String parameterName5;
        String parameterName6;
        String parameterName7;
        String parameterName8;
        String[] parameterContent;

        // /eacCpf/cpfDescription/relations/cpfRelation
        relationCounter = 1;
        organisationCounter = 1;
        parameterName1 = "textCpfRelationName_";
        parameterName2 = "cpfRelationLanguage_";
        parameterName3 = "textCpfRelationId_";
        parameterName4 = "textCpfRelationLink_";
        parameterName5 = "cpfRelationType_";
        parameterName6 = "textareaCpfRelationDescription_";
        parameterName7 = "_textCpfRelRespOrgPerson_";
        parameterName8 = "_textCpfRelRespOrgId_";

        while ((parameters.containsKey(parameterName5 + relationCounter) || (String[]) parameters.get(parameterName5 + relationCounter) != null) && !((String[]) parameters.get(parameterName5 + relationCounter))[0].isEmpty()) {
            CpfRelation cpfRelation = new CpfRelation();
            String language = null;

            //determine language of entry, if available
            if ((String[]) parameters.get(parameterName2 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName2 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].equals("----")) {
                    language = parameterContent[0];
                }
            }

            // /eacCpf/cpfDescription/relations/cpfRelation@cpfRelationType
            if ((String[]) parameters.get(parameterName5 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName5 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    cpfRelation.setCpfRelationType(parameterContent[0]);
                }
            }

            // /eacCpf/cpfDescription/relations/cpfRelation@xlink:href
            if ((String[]) parameters.get(parameterName4 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName4 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    cpfRelation.setHref(parameterContent[0]);
                }
            }

            // /eacCpf/cpfDescription/relations/cpfRelation/descriptiveNote
            if ((String[]) parameters.get(parameterName6 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName6 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    DescriptiveNote descriptiveNote = new DescriptiveNote();
                    P p = new P();
                    if (language != null && !language.isEmpty()) {
                        p.setLang(language);
                    }
                    p.setContent(parameterContent[0]);
                    descriptiveNote.getP().add(p);
                    cpfRelation.setDescriptiveNote(descriptiveNote);
                }
            }
            // /eacCpf/cpfDescription/relations/cpfRelation/relationEntry[title]
            if (parameters.containsKey(parameterName1 + relationCounter) || (String[]) parameters.get(parameterName1 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName1 + relationCounter);
                if (!parameterContent[0].isEmpty()) {
                    RelationEntry relationEntry = new RelationEntry();
                    if (language != null && !language.isEmpty()) {
                        relationEntry.setLang(language);
                    }
                    relationEntry.setLocalType("title");
                    if (parameterContent.length == 1) {
                        relationEntry.setContent(parameterContent[0]);
                    }
                    cpfRelation.getRelationEntry().add(relationEntry);
                }
            }

            // /eacCpf/cpfDescription/relations/cpfRelation/relationEntry[id]
            if (parameters.containsKey(parameterName3 + relationCounter) || (String[]) parameters.get(parameterName3 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName3 + relationCounter);
                if (!parameterContent[0].isEmpty()) {
                    RelationEntry relationEntry = new RelationEntry();
                    if (language != null && !language.isEmpty()) {
                        relationEntry.setLang(language);
                    }
                    relationEntry.setLocalType("id");
                    if (parameterContent.length == 1) {
                        relationEntry.setContent(parameterContent[0]);
                    }
                    cpfRelation.getRelationEntry().add(relationEntry);
                }
            }

            // /eacCpf/cpfDescription/relations/cpfRelation/relationEntry[agencyName] + /eacCpf/cpfDescription/relations/cpfRelation/relationEntry[agencyCode]
            while (parameters.containsKey("cpfRelationsTable_" + relationCounter + parameterName7 + organisationCounter) || (String[]) parameters.get("cpfRelationsTable_" + relationCounter + parameterName7 + organisationCounter) != null
                    || parameters.containsKey("cpfRelationsTable_" + relationCounter + parameterName8 + organisationCounter) || (String[]) parameters.get("cpfRelationsTable_" + relationCounter + parameterName8 + organisationCounter) != null) {
                if ((String[]) parameters.get("cpfRelationsTable_" + relationCounter + parameterName7 + organisationCounter) != null) {
                    parameterContent = (String[]) parameters.get("cpfRelationsTable_" + relationCounter + parameterName7 + organisationCounter);
                    if (!parameterContent[0].isEmpty()) {
                        RelationEntry relationEntry = new RelationEntry();
                        if (language != null && !language.isEmpty()) {
                            relationEntry.setLang(language);
                        }
                        relationEntry.setLocalType("agencyName");
                        if (parameterContent.length == 1) {
                            relationEntry.setContent(parameterContent[0]);
                        }
                        cpfRelation.getRelationEntry().add(relationEntry);
                    }
                }

                if ((String[]) parameters.get("cpfRelationsTable_" + relationCounter + parameterName8 + organisationCounter) != null) {
                    parameterContent = (String[]) parameters.get("cpfRelationsTable_" + relationCounter + parameterName8 + organisationCounter);
                    if (!parameterContent[0].isEmpty()) {
                        RelationEntry relationEntry = new RelationEntry();
                        if (language != null && !language.isEmpty()) {
                            relationEntry.setLang(language);
                        }
                        relationEntry.setLocalType("agencyCode");
                        if (parameterContent.length == 1) {
                            relationEntry.setContent(parameterContent[0]);
                        }
                        cpfRelation.getRelationEntry().add(relationEntry);
                    }
                }
                organisationCounter++;
            }

            relations.getCpfRelation().add(cpfRelation);
            organisationCounter = 1;
            relationCounter++;
        }

        // /eacCpf/cpfDescription/relations/resourceRelation
        relationCounter = 1;
        organisationCounter = 1;
        parameterName1 = "textResRelationName_";
        parameterName2 = "resRelationLanguage_";
        parameterName3 = "textResRelationId_";
        parameterName4 = "textResRelationLink_";
        parameterName5 = "resRelationType_";
        parameterName6 = "textareaResRelationDescription_";
        parameterName7 = "_textResRelRespOrgPerson_";
        parameterName8 = "_textResRelRespOrgId_";

        while ((parameters.containsKey(parameterName5 + relationCounter) || (String[]) parameters.get(parameterName5 + relationCounter) != null) && !((String[]) parameters.get(parameterName5 + relationCounter))[0].isEmpty()) {
            ResourceRelation resRelation = new ResourceRelation();
            String language = null;

            //determine language of entry, if available
            if ((String[]) parameters.get(parameterName2 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName2 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].equals("----")) {
                    language = parameterContent[0];
                }
            }

            // /eacCpf/cpfDescription/relations/resourceRelation@resourceRelationType
            if ((String[]) parameters.get(parameterName5 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName5 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    resRelation.setResourceRelationType(parameterContent[0]);
                }
            }

            // /eacCpf/cpfDescription/relations/resourceRelation@xlink:href
            if ((String[]) parameters.get(parameterName4 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName4 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    resRelation.setHref(parameterContent[0]);
                }
            }

            // /eacCpf/cpfDescription/relations/resourceRelation/descriptiveNote
            if ((String[]) parameters.get(parameterName6 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName6 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    DescriptiveNote descriptiveNote = new DescriptiveNote();
                    P p = new P();
                    if (language != null && !language.isEmpty()) {
                        p.setLang(language);
                    }
                    p.setContent(parameterContent[0]);
                    descriptiveNote.getP().add(p);
                    resRelation.setDescriptiveNote(descriptiveNote);
                }
            }
            // /eacCpf/cpfDescription/relations/resourceRelation/relationEntry[title]
            if (parameters.containsKey(parameterName1 + relationCounter) || (String[]) parameters.get(parameterName1 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName1 + relationCounter);
                if (!parameterContent[0].isEmpty()) {
                    RelationEntry relationEntry = new RelationEntry();
                    if (language != null && !language.isEmpty()) {
                        relationEntry.setLang(language);
                    }
                    relationEntry.setLocalType("title");
                    if (parameterContent.length == 1) {
                        relationEntry.setContent(parameterContent[0]);
                    }
                    resRelation.getRelationEntry().add(relationEntry);
                }
            }

            // /eacCpf/cpfDescription/relations/resourceRelation/relationEntry[id]
            if (parameters.containsKey(parameterName3 + relationCounter) || (String[]) parameters.get(parameterName3 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName3 + relationCounter);
                if (!parameterContent[0].isEmpty()) {
                    RelationEntry relationEntry = new RelationEntry();
                    if (language != null && !language.isEmpty()) {
                        relationEntry.setLang(language);
                    }
                    relationEntry.setLocalType("id");
                    if (parameterContent.length == 1) {
                        relationEntry.setContent(parameterContent[0]);
                    }
                    resRelation.getRelationEntry().add(relationEntry);
                }
            }

            // /eacCpf/cpfDescription/relations/resourceRelation/relationEntry[agencyName] + /eacCpf/cpfDescription/relations/resourceRelation/relationEntry[agencyCode]
            while (parameters.containsKey("resRelationsTable_" + relationCounter + parameterName7 + organisationCounter) || (String[]) parameters.get("resRelationsTable_" + relationCounter + parameterName7 + organisationCounter) != null
                    || parameters.containsKey("resRelationsTable_" + relationCounter + parameterName8 + organisationCounter) || (String[]) parameters.get("resRelationsTable_" + relationCounter + parameterName8 + organisationCounter) != null) {
                if ((String[]) parameters.get("resRelationsTable_" + relationCounter + parameterName7 + organisationCounter) != null) {
                    parameterContent = (String[]) parameters.get("resRelationsTable_" + relationCounter + parameterName7 + organisationCounter);
                    if (!parameterContent[0].isEmpty()) {
                        RelationEntry relationEntry = new RelationEntry();
                        if (language != null && !language.isEmpty()) {
                            relationEntry.setLang(language);
                        }
                        relationEntry.setLocalType("agencyName");
                        if (parameterContent.length == 1) {
                            relationEntry.setContent(parameterContent[0]);
                        }
                        resRelation.getRelationEntry().add(relationEntry);
                    }
                }

                if ((String[]) parameters.get("resRelationsTable_" + relationCounter + parameterName8 + organisationCounter) != null) {
                    parameterContent = (String[]) parameters.get("resRelationsTable_" + relationCounter + parameterName8 + organisationCounter);
                    if (!parameterContent[0].isEmpty()) {
                        RelationEntry relationEntry = new RelationEntry();
                        if (language != null && !language.isEmpty()) {
                            relationEntry.setLang(language);
                        }
                        relationEntry.setLocalType("agencyCode");
                        if (parameterContent.length == 1) {
                            relationEntry.setContent(parameterContent[0]);
                        }
                        resRelation.getRelationEntry().add(relationEntry);
                    }
                }
                organisationCounter++;
            }

            relations.getResourceRelation().add(resRelation);
            organisationCounter = 1;
            relationCounter++;
        }

// /eacCpf/cpfDescription/relations/functionRelation
        relationCounter = 1;
        organisationCounter = 1;
        parameterName1 = "textFncRelationName_";
        parameterName2 = "fncRelationLanguage_";
        parameterName3 = "textFncRelationId_";
        parameterName4 = "textFncRelationLink_";
        parameterName5 = "fncRelationType_";
        parameterName6 = "textareaFncRelationDescription_";
        parameterName7 = "_textFncRelRespOrgPerson_";
        parameterName8 = "_textFncRelRespOrgId_";

        while ((parameters.containsKey(parameterName5 + relationCounter) || (String[]) parameters.get(parameterName5 + relationCounter) != null) && !((String[]) parameters.get(parameterName5 + relationCounter))[0].isEmpty()) {
            FunctionRelation fncRelation = new FunctionRelation();
            String language = null;

            //determine language of entry, if available
            if ((String[]) parameters.get(parameterName2 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName2 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].equals("----")) {
                    language = parameterContent[0];
                }
            }

            // /eacCpf/cpfDescription/relations/functionRelation@functionRelationType
            if ((String[]) parameters.get(parameterName5 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName5 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    fncRelation.setFunctionRelationType(parameterContent[0]);
                }
            }

            // /eacCpf/cpfDescription/relations/functionRelation@xlink:href
            if ((String[]) parameters.get(parameterName4 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName4 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    fncRelation.setHref(parameterContent[0]);
                }
            }

            // /eacCpf/cpfDescription/relations/functionRelation/descriptiveNote
            if ((String[]) parameters.get(parameterName6 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName6 + relationCounter);
                if (parameterContent.length == 1 && !parameterContent[0].isEmpty()) {
                    DescriptiveNote descriptiveNote = new DescriptiveNote();
                    P p = new P();
                    if (language != null && !language.isEmpty()) {
                        p.setLang(language);
                    }
                    p.setContent(parameterContent[0]);
                    descriptiveNote.getP().add(p);
                    fncRelation.setDescriptiveNote(descriptiveNote);
                }
            }
            // /eacCpf/cpfDescription/relations/functionRelation/relationEntry[title]
            if (parameters.containsKey(parameterName1 + relationCounter) || (String[]) parameters.get(parameterName1 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName1 + relationCounter);
                if (!parameterContent[0].isEmpty()) {
                    RelationEntry relationEntry = new RelationEntry();
                    if (language != null && !language.isEmpty()) {
                        relationEntry.setLang(language);
                    }
                    relationEntry.setLocalType("title");
                    if (parameterContent.length == 1) {
                        relationEntry.setContent(parameterContent[0]);
                    }
                    fncRelation.getRelationEntry().add(relationEntry);
                }
            }

            // /eacCpf/cpfDescription/relations/functionRelation/relationEntry[id]
            if (parameters.containsKey(parameterName3 + relationCounter) || (String[]) parameters.get(parameterName3 + relationCounter) != null) {
                parameterContent = (String[]) parameters.get(parameterName3 + relationCounter);
                if (!parameterContent[0].isEmpty()) {
                    RelationEntry relationEntry = new RelationEntry();
                    if (language != null && !language.isEmpty()) {
                        relationEntry.setLang(language);
                    }
                    relationEntry.setLocalType("id");
                    if (parameterContent.length == 1) {
                        relationEntry.setContent(parameterContent[0]);
                    }
                    fncRelation.getRelationEntry().add(relationEntry);
                }
            }

            // /eacCpf/cpfDescription/relations/functionRelation/relationEntry[agencyName] + /eacCpf/cpfDescription/relations/functionRelation/relationEntry[agencyCode]
            while (parameters.containsKey("fncRelationsTable_" + relationCounter + parameterName7 + organisationCounter) || (String[]) parameters.get("fncRelationsTable_" + relationCounter + parameterName7 + organisationCounter) != null
                    || parameters.containsKey("fncRelationsTable_" + relationCounter + parameterName8 + organisationCounter) || (String[]) parameters.get("fncRelationsTable_" + relationCounter + parameterName8 + organisationCounter) != null) {
                if ((String[]) parameters.get("fncRelationsTable_" + relationCounter + parameterName7 + organisationCounter) != null) {
                    parameterContent = (String[]) parameters.get("fncRelationsTable_" + relationCounter + parameterName7 + organisationCounter);
                    if (!parameterContent[0].isEmpty()) {
                        RelationEntry relationEntry = new RelationEntry();
                        if (language != null && !language.isEmpty()) {
                            relationEntry.setLang(language);
                        }
                        relationEntry.setLocalType("agencyName");
                        if (parameterContent.length == 1) {
                            relationEntry.setContent(parameterContent[0]);
                        }
                        fncRelation.getRelationEntry().add(relationEntry);
                    }
                }

                if ((String[]) parameters.get("fncRelationsTable_" + relationCounter + parameterName8 + organisationCounter) != null) {
                    parameterContent = (String[]) parameters.get("fncRelationsTable_" + relationCounter + parameterName8 + organisationCounter);
                    if (!parameterContent[0].isEmpty()) {
                        RelationEntry relationEntry = new RelationEntry();
                        if (language != null && !language.isEmpty()) {
                            relationEntry.setLang(language);
                        }
                        relationEntry.setLocalType("agencyCode");
                        if (parameterContent.length == 1) {
                            relationEntry.setContent(parameterContent[0]);
                        }
                        fncRelation.getRelationEntry().add(relationEntry);
                    }
                }
                organisationCounter++;
            }

            relations.getFunctionRelation().add(fncRelation);
            organisationCounter = 1;
            relationCounter++;
        }

        return relations;
    }

    private DateRange createDateRange(String tableName, int rowCounter) {
        String date1year = tableName + "_date_1_Year_";
        String date1month = tableName + "_date_1_Month_";
        String date1day = tableName + "_date_1_Day_";
        String date1text = tableName + "_date_1_";
        String date1unknown = tableName + "_date_unknown_1_";
        String date2year = tableName + "_date_2_Year_";
        String date2month = tableName + "_date_2_Month_";
        String date2day = tableName + "_date_2_Day_";
        String date2text = tableName + "_date_2_";
        String date2unknown = tableName + "_date_unknown_2_";

        //clear StringBuilder first
        standardDate.delete(0, standardDate.length());

        DateRange dateRange = new DateRange();
        FromDate fromDate = new FromDate();
        if (!parameters.containsKey(date1unknown + rowCounter)) {
            //retrieve year
            standardDate.append(((String[]) parameters.get(date1year + rowCounter))[0]);
            //if available, retrieve month
            if (!((String[]) parameters.get(date1month + rowCounter))[0].isEmpty()) {
                standardDate.append("-");
                standardDate.append(((String[]) parameters.get(date1month + rowCounter))[0]);
                //if available, add day of month
                if (!((String[]) parameters.get(date1day + rowCounter))[0].isEmpty()) {
                    standardDate.append("-");
                    standardDate.append(((String[]) parameters.get(date1day + rowCounter))[0]);
                }
            }
            //set element values
            fromDate.setStandardDate(standardDate.toString());
            fromDate.setContent(((String[]) parameters.get(date1text + rowCounter))[0]);
        } else {
            fromDate.setStandardDate("0001");
            fromDate.setContent("unknown");
        }
        dateRange.setFromDate(fromDate);
        //delete StringBuilder contents
        standardDate.delete(0, standardDate.length());

        //Apply same procedure for ToDate
        ToDate toDate = new ToDate();
        if (!parameters.containsKey(date2unknown + rowCounter)) {
            standardDate.append(((String[]) parameters.get(date2year + rowCounter))[0]);
            if (!((String[]) parameters.get(date2month + rowCounter))[0].isEmpty()) {
                standardDate.append("-");
                standardDate.append(((String[]) parameters.get(date2month + rowCounter))[0]);
                if (!((String[]) parameters.get(date2day + rowCounter))[0].isEmpty()) {
                    standardDate.append("-");
                    standardDate.append(((String[]) parameters.get(date2day + rowCounter))[0]);
                }
            }
            toDate.setStandardDate(standardDate.toString());
            toDate.setContent(((String[]) parameters.get(date2text + rowCounter))[0]);
        } else {
            toDate.setStandardDate("2099");
            toDate.setContent("unknown");
        }
        dateRange.setToDate(toDate);

        return dateRange;
    }

    private Date createDate(String tableName, int rowCounter) {
        String date1year = tableName + "_date_1_Year_";
        String date1month = tableName + "_date_1_Month_";
        String date1day = tableName + "_date_1_Day_";
        String date1text = tableName + "_date_1_";
        String date1unknown = tableName + "_date_unknown_1_";

        //clear StringBuilder first
        standardDate.delete(0, standardDate.length());

        Date date = new Date();
        if (!parameters.containsKey(date1unknown + rowCounter)) {
            //retrieve year
            standardDate.append(((String[]) parameters.get(date1year + rowCounter))[0]);
            //if available, retrieve month
            if (!((String[]) parameters.get(date1month + rowCounter))[0].isEmpty()) {
                standardDate.append("-");
                standardDate.append(((String[]) parameters.get(date1month + rowCounter))[0]);
                //if available, add day of month
                if (!((String[]) parameters.get(date1day + rowCounter))[0].isEmpty()) {
                    standardDate.append("-");
                    standardDate.append(((String[]) parameters.get(date1day + rowCounter))[0]);
                }
            }
            //set element values
            date.setStandardDate(standardDate.toString());
            date.setContent(((String[]) parameters.get(date1text + rowCounter))[0]);
        } else {
            date.setStandardDate("0001");
            date.setContent("unknown");
        }
        return date;
    }
}
