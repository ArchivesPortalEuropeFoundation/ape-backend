package eu.apenet.persistence.factory;

import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.hibernate.AiAlternativeNameHibernateDAO;
import eu.apenet.persistence.hibernate.ArchivalInstitutionHibernateDAO;
import eu.apenet.persistence.hibernate.CLevelHibernateDAO;
import eu.apenet.persistence.hibernate.CoordinatesHibernateDAO;
import eu.apenet.persistence.hibernate.CouAlternativeNameHibernateDAO;
import eu.apenet.persistence.hibernate.CountryHibernateDAO;
import eu.apenet.persistence.hibernate.DptUpdateHibernateDAO;
import eu.apenet.persistence.hibernate.EadContentHibernateDAO;
import eu.apenet.persistence.hibernate.EadHibernateDAO;
import eu.apenet.persistence.hibernate.EseHibernateDAO;
import eu.apenet.persistence.hibernate.EseStateHibernateDAO;
import eu.apenet.persistence.hibernate.FindingAidHibernateDAO;
import eu.apenet.persistence.hibernate.HoldingsGuideHibernateDAO;
import eu.apenet.persistence.hibernate.LangHibernateDAO;
import eu.apenet.persistence.hibernate.ResumptionTokenHibernateDAO;
import eu.apenet.persistence.hibernate.SentMailRegisterHibernateDAO;
import eu.apenet.persistence.hibernate.UploadMethodHibernateDAO;
import eu.apenet.persistence.hibernate.UserHibernateDAO;
import eu.apenet.persistence.hibernate.UserRoleHibernateDAO;
import eu.apenet.persistence.hibernate.WarningsHibernateDAO;
import eu.archivesportaleurope.persistence.jpa.dao.*;

/**
 * Implementation: If you write a new DAO, this class has to know about it.
 *
 * @author Paul
 */
public class HibernateDAOFactory extends DAOFactory {

    @Override
    public SentMailRegisterDAO getSentMailRegisterDAO() {
        return (SentMailRegisterDAO) instantiateDAO(SentMailRegisterHibernateDAO.class);
    }

    @Override
    public FindingAidDAO getFindingAidDAO() {
        return (FindingAidDAO) instantiateDAO(FindingAidHibernateDAO.class);
    }

    @Override
    public UpFileDAO getUpFileDAO() {
        return (UpFileDAO) instantiateDAO(UpFileJpaDAO.class);
    }

    @Override
    public HoldingsGuideDAO getHoldingsGuideDAO() {
        return (HoldingsGuideDAO) instantiateDAO(HoldingsGuideHibernateDAO.class);
    }

    @Override
    public WarningsDAO getWarningsDAO() {
        return (WarningsDAO) instantiateDAO(WarningsHibernateDAO.class);
    }

    @Override
    public ArchivalInstitutionDAO getArchivalInstitutionDAO() {
        return (ArchivalInstitutionDAO) instantiateDAO(ArchivalInstitutionHibernateDAO.class);
    }

    @Override
    public UserDAO getUserDAO() {
        return (UserDAO) instantiateDAO(UserHibernateDAO.class);
    }

    @Override
    public CountryDAO getCountryDAO() {
        return (CountryDAO) instantiateDAO(CountryHibernateDAO.class);
    }

    @Override
    public LangDAO getLangDAO() {
        return (LangDAO) instantiateDAO(LangHibernateDAO.class);
    }

    @Override
    public EseDAO getEseDAO() {
        return (EseDAO) instantiateDAO(EseHibernateDAO.class);
    }

    @Override
    public UploadMethodDAO getUploadMethodDAO() {
        return (UploadMethodDAO) instantiateDAO(UploadMethodHibernateDAO.class);
    }

    @Override
    public EseStateDAO getEseStateDAO() {
        return (EseStateDAO) instantiateDAO(EseStateHibernateDAO.class);
    }

    @Override
    public CLevelDAO getCLevelDAO() {
        return (CLevelDAO) instantiateDAO(CLevelHibernateDAO.class);
    }

    @Override
    public EadContentDAO getEadContentDAO() {
        return (EadContentDAO) instantiateDAO(EadContentHibernateDAO.class);
    }

    @Override
    public AiAlternativeNameDAO getAiAlternativeNameDAO() {
        return (AiAlternativeNameDAO) instantiateDAO(AiAlternativeNameHibernateDAO.class);
    }

    @Override
    public CouAlternativeNameDAO getCouAlternativeNameDAO() {
        return (CouAlternativeNameDAO) instantiateDAO(CouAlternativeNameHibernateDAO.class);
    }

    @Override
    public DptUpdateDAO getDptUpdateDAO() {
        return (DptUpdateDAO) instantiateDAO(DptUpdateHibernateDAO.class);
    }

    @Override
    public ResumptionTokenDAO getResumptionTokenDAO() {
        return (ResumptionTokenDAO) instantiateDAO(ResumptionTokenHibernateDAO.class);
    }

    @Override
    public QueueItemDAO getQueueItemDAO() {
        return (QueueItemDAO) instantiateDAO(QueueItemJpaDAO.class);
    }

    @Override
    public UserRoleDAO getUserRoleDAO() {
        return (UserRoleDAO) instantiateDAO(UserRoleHibernateDAO.class);
    }

    @Override
    public EadDAO getEadDAO() {
        return (EadDAO) instantiateDAO(EadHibernateDAO.class);
    }

    @Override
    public HgSgFaRelationDAO getHgSgFaRelationDAO() {
        return (HgSgFaRelationDAO) instantiateDAO(HgSgFaRelationJpaDAO.class);
    }

    @Override
    public ArchivalInstitutionOaiPmhDAO getArchivalInstitutionOaiPmhDAO() {
        return (ArchivalInstitutionOaiPmhDAO) instantiateDAO(ArchivalInstitutionOaiPmhJpaDAO.class);
    }

    @Override
    public IngestionprofileDAO getIngestionprofileDAO() {
        return (IngestionprofileDAO) instantiateDAO(IngestionprofileJpaDAO.class);
    }

    @Override
    public CoordinatesDAO getCoordinatesDAO() {
        return (CoordinatesDAO) instantiateDAO(CoordinatesHibernateDAO.class);
    }

    @Override
    public EacCpfDAO getEacCpfDAO() {
        return (EacCpfDAO) instantiateDAO(EacCpfJpaDAO.class);
    }

    @Override
    public TopicDAO getTopicDAO() {
        return (TopicDAO) instantiateDAO(TopicJpaDAO.class);
    }

    @Override
    public TopicMappingDAO getTopicMappingDAO() {
        return (TopicMappingDAO) instantiateDAO(TopicMappingJpaDAO.class);
    }

    @Override
    public XslUploadDAO getXslUploadDAO() {
        return (XslUploadDAO) instantiateDAO(XslUploadJpaDAO.class);
    }

    @Override
    public FtpDAO getFtpDAO() {
        return (FtpDAO) instantiateDAO(FtpJpaDAO.class);
    }

    @Override
    public ApiKeyDAO getApiKeyDAO() {
        return (ApiKeyDAO) instantiateDAO(ApiKeyDAO.class);
    }

    @Override
    public Ead3DAO getEad3DAO() {
        return (Ead3DAO) instantiateDAO(Ead3JpaDAO.class);
    }

    @Override
    public ReindexDocDAO getReindexDocDAO() {
        return (ReindexDocDAO) instantiateDAO(ReindexDocJpaDAO.class);
    }

    @Override
    public RightsInformationDAO getRightsInformationDAO() {
        return (RightsInformationDAO) instantiateDAO(RightsInformationJpaDAO.class);
    }
}
