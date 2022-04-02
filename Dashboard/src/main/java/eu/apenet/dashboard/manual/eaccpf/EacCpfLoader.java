/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf;

import eu.apenet.dpt.utils.eaccpf.Places;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.CpfRelation;
import eu.apenet.dpt.utils.eaccpf.EntityId;
import eu.apenet.dpt.utils.eaccpf.Occupations;
import eu.apenet.dpt.utils.eaccpf.StructureOrGenealogy;
import eu.apenet.dpt.utils.eaccpf.OtherRecordId;
import eu.apenet.dpt.utils.eaccpf.FunctionRelation;
import eu.apenet.dpt.utils.eaccpf.Date;
import eu.apenet.dpt.utils.eaccpf.Functions;
import eu.apenet.dpt.utils.eaccpf.P;
import eu.apenet.dpt.utils.eaccpf.ResourceRelation;
import eu.apenet.dpt.utils.eaccpf.Function;
import eu.apenet.dpt.utils.eaccpf.Occupation;
import eu.apenet.dpt.utils.eaccpf.Identity;
import eu.apenet.dpt.utils.eaccpf.NameEntryParallel;
import eu.apenet.dpt.utils.eaccpf.BiogHist;
import eu.apenet.dpt.utils.eaccpf.Place;
import eu.apenet.dpt.utils.eaccpf.DateRange;
import eu.apenet.dpt.utils.eaccpf.NameEntry;
import eu.apenet.dashboard.manual.eaccpf.util.BiographyType;
import eu.apenet.dashboard.manual.eaccpf.util.DateType;
import eu.apenet.dashboard.manual.eaccpf.util.FunctionType;
import eu.apenet.dashboard.manual.eaccpf.util.GenealogyType;
import eu.apenet.dashboard.manual.eaccpf.util.IdentifierType;
import eu.apenet.dashboard.manual.eaccpf.util.NameEntryType;
import eu.apenet.dashboard.manual.eaccpf.util.OccupationType;
import eu.apenet.dashboard.manual.eaccpf.util.PlaceType;
import eu.apenet.dashboard.manual.eaccpf.util.RelationType;
import eu.apenet.dashboard.manual.eaccpf.util.SimpleDate;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author papp
 */
public class EacCpfLoader {
    //main data object

    protected EacCpf eacCpf;
    //path of file to load
    private String path;
    //fields taken from control element
    private String recordId;
    private List<String> otherRecordIds;
    private String agencyCode;
    private String controlLanguageCode;
    private String controlScriptCode;
    private String controlResponsiblePerson;
    //fields taken from cpfDescription/identity element
    private String entityType;
    private List<NameEntryType> nameEntries;
    private List<IdentifierType> identifiers;
    //fields taken from cpfDescription/description element
    private List<DateType> existDates;
    private List<PlaceType> places;
    private List<FunctionType> functions;
    private List<OccupationType> occupations;
    private List<GenealogyType> genealogies;
    private List<BiographyType> biographies;
    //fields taken from cpfDescription/relations element
    private List<RelationType> cpfRelations;
    private List<RelationType> resRelations;
    private List<RelationType> fncRelations;

    public EacCpfLoader() {
    }

    /*
     *
     */
    public boolean fillEacCpf(File eacCpfFile) {
        boolean result = true;
        EacCpf eacCpfFromFile = null;

        try {
            InputStream eacCpfStream = FileUtils.openInputStream(eacCpfFile);

            JAXBContext jaxbContext = JAXBContext.newInstance(EacCpf.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eacCpfFromFile = (EacCpf) jaxbUnmarshaller.unmarshal(eacCpfStream);

            eacCpfStream.close();
        } catch (JAXBException jaxbe) {
//            log.info(jaxbe.getMessage());
        } catch (IOException ioe) {
//            log.info(ioe.getMessage());
        }
//        this.setRecordId(this.getIdUsedInAPE());
//        this.setSelfRecordId(this.getIdUsedInAPE());
        if (eacCpfFromFile == null) {
//            this.setCountryCode(this.getInitialCountryCode());
//            log.info("no previous EAG file");
        } else {
            this.eacCpf = eacCpfFromFile;
            result = this.loadValuesEacCpf();
        }

        return result;
    }

    /**
     * Input method.
     */
    public boolean loadValuesEacCpf() {
        if (this.eacCpf == null
                || (this.eacCpf.getControl() == null && this.eacCpf.getCpfDescription() == null)) {
            return false;
        }

        // Fill values of "Identity" tab.
        loadIdentityTabValues();
        // Fill values of "Description" tab.
        loadDescriptionTabValues();
        // Fill values of "Relations" tab.
        loadRelationsTabValues();
        // Fill values of "Control" tab.
        loadControlTabValues();

        return true;
    }

    private void loadIdentityTabValues() {
        // Type of entity
        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getIdentity() != null
                && this.eacCpf.getCpfDescription().getIdentity().getEntityType() != null
                && this.eacCpf.getCpfDescription().getIdentity().getEntityType().getValue() != null
                && !this.eacCpf.getCpfDescription().getIdentity().getEntityType().getValue().isEmpty()) {
            this.setEntityType(this.eacCpf.getCpfDescription().getIdentity().getEntityType().getValue());
        }

        // Name entries
        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getIdentity() != null
                && this.eacCpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry() != null
                && !this.eacCpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().isEmpty()) {
            this.nameEntries = new ArrayList<NameEntryType>();
            for (Object object : this.eacCpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry()) {
                if (object instanceof NameEntryParallel) {
                    NameEntryParallel nameEntryParallel = (NameEntryParallel) object;
                    for (Object nameEntry : nameEntryParallel.getAuthorizedFormOrAlternativeForm()) {
                        if (nameEntry instanceof NameEntry) {
                            NameEntryType nameEntryType = new NameEntryType();
                            nameEntries.add(nameEntryType.fillDataWith((NameEntry) nameEntry, nameEntryParallel));
                        }
                    }
                }
                if (object instanceof Identity.NameEntry) {
                    Identity.NameEntry nameEntry = (Identity.NameEntry) object;
                    NameEntryType nameEntryType = new NameEntryType();
                    nameEntries.add(nameEntryType.fillDataWith(nameEntry));
                }
            }
        }

        // Identifiers
        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getIdentity() != null
                && this.eacCpf.getCpfDescription().getIdentity().getEntityId() != null
                && !this.eacCpf.getCpfDescription().getIdentity().getEntityId().isEmpty()) {
            this.identifiers = new ArrayList<IdentifierType>();
            for (EntityId entityId : this.eacCpf.getCpfDescription().getIdentity().getEntityId()) {
                IdentifierType identifierType = new IdentifierType();
                identifiers.add(identifierType.fillDataWith(entityId));
            }
        }

        //EXIST DATES
        this.existDates = new ArrayList<DateType>();

        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getDescription() != null
                && this.eacCpf.getCpfDescription().getDescription().getExistDates() != null) {
            if (this.eacCpf.getCpfDescription().getDescription().getExistDates().getDateSet() != null
                    && !this.eacCpf.getCpfDescription().getDescription().getExistDates().getDateSet().getDateOrDateRange().isEmpty()) {
                for (int k = 0; k < this.eacCpf.getCpfDescription().getDescription().getExistDates().getDateSet().getDateOrDateRange().size(); k++) {
                    Object object = this.eacCpf.getCpfDescription().getDescription().getExistDates().getDateSet().getDateOrDateRange().get(k);
                    if (object instanceof Date) {
                        DateType dateType = new DateType(new SimpleDate(0));
                        existDates.add(dateType.fillDataWith((Date) object));
                    }
                    if (object instanceof DateRange) {
                        DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                        existDates.add(dateType.fillDataWith((DateRange) object));
                    }
                }
            }
            if (this.eacCpf.getCpfDescription().getDescription().getExistDates().getDate() != null) {
                DateType dateType = new DateType(new SimpleDate(0));
                existDates.add(dateType.fillDataWith(this.eacCpf.getCpfDescription().getDescription().getExistDates().getDate()));
            }
            if (this.eacCpf.getCpfDescription().getDescription().getExistDates().getDateRange() != null) {
                DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                existDates.add(dateType.fillDataWith(this.eacCpf.getCpfDescription().getDescription().getExistDates().getDateRange()));
            }
        }
    }

    private void loadDescriptionTabValues() {
        //PLACES, FUNCTIONS, OCCUPATIONS, GENEALOGIES
        if (this.eacCpf.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses() != null
                && !this.eacCpf.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses().isEmpty()) {
            List<Object> descriptionObjects = this.eacCpf.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses();
            places = new ArrayList<PlaceType>();
            functions = new ArrayList<FunctionType>();
            occupations = new ArrayList<OccupationType>();
            genealogies = new ArrayList<GenealogyType>();

            for (Object object : descriptionObjects) {
                if (object instanceof Places) {
                    Places placeObjects = (Places) object;
                    if (placeObjects.getPlace() != null
                            && !placeObjects.getPlace().isEmpty()) {
                        for (Place place : placeObjects.getPlace()) {
                            PlaceType placeType = new PlaceType();
                            places.add(placeType.fillDataWith(place));
                        }
                    }
                }
                if (object instanceof Functions) {
                    Functions functionObjects = (Functions) object;
                    if (functionObjects.getFunction() != null
                            && !functionObjects.getFunction().isEmpty()) {
                        for (Function function : functionObjects.getFunction()) {
                            FunctionType functionType = new FunctionType();
                            functions.add(functionType.fillDataWith(function));
                        }
                    }
                }
                if (object instanceof Occupations) {
                    Occupations occupationObjects = (Occupations) object;
                    if (occupationObjects.getOccupation() != null
                            && !occupationObjects.getOccupation().isEmpty()) {
                        for (Occupation occupation : occupationObjects.getOccupation()) {
                            OccupationType occupationType = new OccupationType();
                            occupations.add(occupationType.fillDataWith(occupation));
                        }
                    }
                }
                if (object instanceof StructureOrGenealogy) {
                    if (((StructureOrGenealogy) object).getMDiscursiveSet() != null
                            && !((StructureOrGenealogy) object).getMDiscursiveSet().isEmpty()) {
                        List<Object> paragraphs = ((StructureOrGenealogy) object).getMDiscursiveSet();
                        for (Object paragraph : paragraphs) {
                            if (paragraph instanceof P) {
                                GenealogyType genealogyType = new GenealogyType();
                                genealogies.add(genealogyType.fillDataWith((P) paragraph));
                            }
                        }
                    }
                }
            }
        }

        //BIOGRAPHIES
        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getDescription() != null
                && this.eacCpf.getCpfDescription().getDescription().getBiogHist() != null) {

            biographies = new ArrayList<BiographyType>();

            for (BiogHist biogHist : this.eacCpf.getCpfDescription().getDescription().getBiogHist()) {
                if (biogHist.getChronListOrPOrCitation() != null
                        && !biogHist.getChronListOrPOrCitation().isEmpty()) {
                    List<Object> paragraphs = biogHist.getChronListOrPOrCitation();
                    for (Object paragraph : paragraphs) {
                        if (paragraph instanceof P) {
                            BiographyType biographyType = new BiographyType();
                            biographies.add(biographyType.fillDataWith((P) paragraph));
                        }
                    }
                }
            }
        }
    }

    private void loadRelationsTabValues() {
        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getRelations() != null
                && this.eacCpf.getCpfDescription().getRelations().getCpfRelation() != null
                && !this.eacCpf.getCpfDescription().getRelations().getCpfRelation().isEmpty()) {
            this.cpfRelations = new ArrayList<RelationType>();
            for (CpfRelation cpfRelation : this.eacCpf.getCpfDescription().getRelations().getCpfRelation()) {
                RelationType relationType = new RelationType();
                cpfRelations.add(relationType.fillDataWith(cpfRelation));
            }
        }
        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getRelations() != null
                && this.eacCpf.getCpfDescription().getRelations().getResourceRelation() != null
                && !this.eacCpf.getCpfDescription().getRelations().getResourceRelation().isEmpty()) {
            this.resRelations = new ArrayList<RelationType>();
            for (ResourceRelation resRelation : this.eacCpf.getCpfDescription().getRelations().getResourceRelation()) {
                RelationType relationType = new RelationType();
                resRelations.add(relationType.fillDataWith(resRelation));
            }
        }
        if (this.eacCpf.getCpfDescription() != null && this.eacCpf.getCpfDescription().getRelations() != null
                && this.eacCpf.getCpfDescription().getRelations().getFunctionRelation() != null
                && !this.eacCpf.getCpfDescription().getRelations().getFunctionRelation().isEmpty()) {
            this.fncRelations = new ArrayList<RelationType>();
            for (FunctionRelation fncRelation : this.eacCpf.getCpfDescription().getRelations().getFunctionRelation()) {
                RelationType relationType = new RelationType();
                fncRelations.add(relationType.fillDataWith(fncRelation));
            }
        }
    }

    private void loadControlTabValues() {
        // Unique identifier of entity in APE database
        if (this.eacCpf.getControl() != null && this.eacCpf.getControl().getRecordId() != null
                && this.eacCpf.getControl().getRecordId().getValue() != null
                && !this.eacCpf.getControl().getRecordId().getValue().isEmpty()) {
            this.setRecordId(this.eacCpf.getControl().getRecordId().getValue());
        } else {
            Random random = new Random();
            int fakeId = random.nextInt(1000000000);
            this.setRecordId(Integer.toString(fakeId));
        }

        // Other identifiers of entity
        if (this.eacCpf.getControl() != null && this.eacCpf.getControl().getOtherRecordId() != null) {
            for (int i = 0; i < this.eacCpf.getControl().getOtherRecordId().size(); i++) {
                if (otherRecordIds == null) {
                    otherRecordIds = new ArrayList<String>();
                }
                OtherRecordId otherRecordId = this.eacCpf.getControl().getOtherRecordId().get(i);
                if (otherRecordId.getContent() != null && !otherRecordId.getContent().isEmpty()) {
                    this.otherRecordIds.add(otherRecordId.getContent());
                }
            }
        }

        // Name of person/institution responsible for the description.
        if (this.eacCpf.getControl() != null && this.eacCpf.getControl().getMaintenanceHistory() != null
                && !this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().isEmpty()) {
            if (this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().size() - 1).getAgent() != null
                    && this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().size() - 1).getAgent().getContent() != null
                    && !this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().size() - 1).getAgent().getContent().isEmpty()) {
                this.setControlResponsiblePerson(this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eacCpf.getControl().getMaintenanceHistory().getMaintenanceEvent().size() - 1).getAgent().getContent());
            }
        }

        // Identifier of responsible institution.
        if (this.eacCpf.getControl() != null && this.eacCpf.getControl().getMaintenanceAgency() != null) {
            if (this.eacCpf.getControl().getMaintenanceAgency().getAgencyCode() != null
                    && this.eacCpf.getControl().getMaintenanceAgency().getAgencyCode().getValue() != null
                    && !this.eacCpf.getControl().getMaintenanceAgency().getAgencyCode().getValue().isEmpty()) {
                this.setAgencyCode(this.eacCpf.getControl().getMaintenanceAgency().getAgencyCode().getValue());
            }
        }

        // Used languages and scripts for the description.
        if (this.eacCpf.getControl() != null && this.eacCpf.getControl().getLanguageDeclaration() != null) {
            if (this.eacCpf.getControl().getLanguageDeclaration().getLanguage() != null
                    && this.eacCpf.getControl().getLanguageDeclaration().getLanguage().getLanguageCode() != null
                    && !this.eacCpf.getControl().getLanguageDeclaration().getLanguage().getLanguageCode().isEmpty()) {
                this.setControlLanguageCode(this.eacCpf.getControl().getLanguageDeclaration().getLanguage().getLanguageCode());
            }
            if (this.eacCpf.getControl().getLanguageDeclaration().getScript() != null
                    && this.eacCpf.getControl().getLanguageDeclaration().getScript().getScriptCode() != null
                    && !this.eacCpf.getControl().getLanguageDeclaration().getScript().getScriptCode().isEmpty()) {
                this.setControlScriptCode(this.eacCpf.getControl().getLanguageDeclaration().getScript().getScriptCode());
            }
        }
    }

    /**
     * *****************************************************************************
     * GETTERS AND SETTERS ****************************************************************************
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public List<String> getOtherRecordIds() {
        return otherRecordIds;
    }

    public void setOtherRecordIds(List<String> otherRecordIds) {
        this.otherRecordIds = otherRecordIds;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getControlLanguageCode() {
        return controlLanguageCode;
    }

    public void setControlLanguageCode(String controlLanguageCode) {
        this.controlLanguageCode = controlLanguageCode;
    }

    public String getControlScriptCode() {
        return controlScriptCode;
    }

    public void setControlScriptCode(String controlScriptCode) {
        this.controlScriptCode = controlScriptCode;
    }

    public String getControlResponsiblePerson() {
        return controlResponsiblePerson;
    }

    public void setControlResponsiblePerson(String controlResponsiblePerson) {
        this.controlResponsiblePerson = controlResponsiblePerson;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<NameEntryType> getNameEntries() {
        return nameEntries;
    }

    public void setNameEntries(List<NameEntryType> nameEntries) {
        this.nameEntries = nameEntries;
    }

    public List<IdentifierType> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<IdentifierType> identifiers) {
        this.identifiers = identifiers;
    }

    public List<DateType> getExistDates() {
        return existDates;
    }

    public void setExistDates(List<DateType> existDates) {
        this.existDates = existDates;
    }

    public List<PlaceType> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceType> places) {
        this.places = places;
    }

    public List<FunctionType> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionType> functions) {
        this.functions = functions;
    }

    public List<OccupationType> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<OccupationType> occupations) {
        this.occupations = occupations;
    }

    public List<GenealogyType> getGenealogies() {
        return genealogies;
    }

    public void setGenealogies(List<GenealogyType> genealogies) {
        this.genealogies = genealogies;
    }

    public List<BiographyType> getBiographies() {
        return biographies;
    }

    public void setBiographies(List<BiographyType> biographies) {
        this.biographies = biographies;
    }

    public List<RelationType> getCpfRelations() {
        return cpfRelations;
    }

    public void setCpfRelations(List<RelationType> cpfRelations) {
        this.cpfRelations = cpfRelations;
    }

    public List<RelationType> getResRelations() {
        return resRelations;
    }

    public void setResRelations(List<RelationType> resRelations) {
        this.resRelations = resRelations;
    }

    public List<RelationType> getFncRelations() {
        return fncRelations;
    }

    public void setFncRelations(List<RelationType> fncRelations) {
        this.fncRelations = fncRelations;
    }
}
