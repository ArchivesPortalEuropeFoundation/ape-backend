package eu.apenet.scripts;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.analyzers.ead.SocialInfoExtractor;
import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import eu.archivesportaleurope.commons.config.ScriptsConfig;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import org.apache.log4j.Logger;
import sun.misc.Cleaner;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

public class BatchSocialInfoExtractor {

    private static final String CONFIG_PROPERTIES_PATH = "CONFIG_PROPERTIES_PATH";

    private final Logger log = Logger.getLogger(getClass());

    public static void main (String[] args) throws NamingException {
        System.out.println("Hello from CLI world!");
        (new BatchSocialInfoExtractor()).doTheJob();

    }

    private void doTheJob(){
        String repoPath = ((ScriptsConfig) APEnetUtilities.getConfig()).getRepoDirPath();
        SocialInfoExtractor eadSocialInfoExtractor = new SocialInfoExtractor();
        eu.apenet.commons.utils.analyzers.eaccpf.SocialInfoExtractor eacSocialInfoExtractor = new eu.apenet.commons.utils.analyzers.eaccpf.SocialInfoExtractor();

        boolean doFA = true;
        boolean doHG = false;
        boolean doSG = false;
        boolean doEAC = false;

//        EadContent eadContent = DAOFactory.instance().getEadContentDAO().findById((long)82);
//        socialInfoExtractor.extractInfo(repoPath, eadContent);

//        EacCpf eacCpf = DAOFactory.instance().getEacCpfDAO().findById(1149);
//        eu.apenet.commons.utils.analyzers.eaccpf.SocialInfoExtractor socialInfoExtractor1 = new eu.apenet.commons.utils.analyzers.eaccpf.SocialInfoExtractor();
//        socialInfoExtractor1.extractInfo(repoPath, eacCpf);

        List<Class> allClasses = new ArrayList<>();
        if (doFA) {
            allClasses.add(FindingAid.class);
        }
        if (doHG) {
            allClasses.add(HoldingsGuide.class);
        }
        if (doSG) {
            allClasses.add(SourceGuide.class);
        }

        for (Class myClass : allClasses){
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setContentClass(myClass);
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setHasMetaContent(2); //2: null metacontent

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            Long count = eadDAO.countEads(contentSearchOptions);
            System.out.println("Count: " + count);

            int pageSize = 1000;
            contentSearchOptions.setPageSize(pageSize);
            int numberOfPages = count.intValue() / pageSize + 1;
            System.out.println("count: " + count + " --> Pages: " + numberOfPages);

            for (int i = 0; i < Math.min(numberOfPages, 25); i++) {
                System.out.println("working for page: " + (i + 1));
                contentSearchOptions.setPageNumber(i + 1);
                List<Ead> eads = eadDAO.getEads(contentSearchOptions);

                JpaUtil.beginDatabaseTransaction();
                EntityManager entityManager = JpaUtil.getEntityManager();

                int counter = 0;
                for (Ead ead : eads) {
//                    System.out.println("working for item: " + (counter) + " -- (page: " + (i+1)+")  --> ead id" + ead.getId() );

                    EadContent eadContent = ead.getEadContent();
                    if (eadContent != null) {
//                        System.out.println("\ttime1: " + (new Date()).toString());
//                        Map response = eadSocialInfoExtractor.extractInfo(repoPath, eadContent);
                        String response = eadSocialInfoExtractor.extractStringInfo(repoPath, eadContent);
//                        System.out.println("\ttime2: " + (new Date()).toString());

                        ead.setMetaContent(response);
//                        eadDAO.update(ead);
                        entityManager.merge(ead);
                    }

                    ead = null;
                    counter++;
                }
                eads = null;

                JpaUtil.commitDatabaseTransaction();
            }
        }

        //Eac
        if (doEAC) {
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setContentClass(EacCpf.class);
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setHasMetaContent(2); //2: null metacontent
            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
            Long count = eacCpfDAO.countEacCpfs(contentSearchOptions);
            System.out.println("Count: " + count);

            int pageSize = 1000;
            contentSearchOptions.setPageSize(pageSize);
            int numberOfPages = count.intValue() / pageSize + 1;
            System.out.println("count: " + count + " --> Pages: " + numberOfPages);


            for (int i = 0; i < Math.min(numberOfPages, 25); i++) {
                System.out.println("working for page: " + (i + 1));
                contentSearchOptions.setPageNumber(i + 1);
                List<EacCpf> eacCpfs = eacCpfDAO.getEacCpfs(contentSearchOptions);

                JpaUtil.beginDatabaseTransaction();
                EntityManager entityManager = JpaUtil.getEntityManager();

                for (EacCpf eacCpf : eacCpfs) {
                    String content = eacSocialInfoExtractor.extractStringInfo(repoPath, eacCpf);
                    eacCpf.setMetaContent(content);
//                eacCpfDAO.update(eacCpf);
                    entityManager.merge(eacCpf);
                }

                JpaUtil.commitDatabaseTransaction();
            }
        }
    }

    public static String compressString(String data) {
        byte[] input = data.getBytes();
        byte[] output = new byte[input.length];

        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        int compressedDataLength = deflater.deflate(output);

        String s = new String(output);
        System.out.println("l1: " + data.length());
        System.out.println("l2: " + s.length());
        return s;
    }
}
