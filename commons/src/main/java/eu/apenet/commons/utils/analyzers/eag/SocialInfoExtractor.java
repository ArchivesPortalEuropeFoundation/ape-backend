package eu.apenet.commons.utils.analyzers.eag;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.analyzers.utils.SocialUtils;
import eu.apenet.dpt.utils.eag2012.*;
import eu.apenet.persistence.vo.ArchivalInstitution;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialInfoExtractor {

    public Map extractInfo(ArchivalInstitution archivalInstitution) {
        String repoPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
        return extractInfo(repoPath, archivalInstitution);
    }

    public Map extractInfo(String repoPath, ArchivalInstitution archivalInstitution) {

        Map<String, Object> jsonMap = new HashMap<>();

        Eag eag = null;
        try {
            File eagFile = new File(repoPath + archivalInstitution.getEagPath());
            InputStream eagStream = FileUtils.openInputStream(eagFile);
            JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eag = (Eag) jaxbUnmarshaller.unmarshal(eagStream);
            eagStream.close();
        } catch (JAXBException jaxbe) {
//            log.info(jaxbe.getMessage());
        } catch (IOException ioe) {
//            log.info(ioe.getMessage());
        }

        String aiTitle = "";
        String aiDescription = "";
        String keywords = "";
        String keywordsShort = "";

        if (eag != null) {
            try {
                aiTitle = eag.getArchguide().getIdentity().getAutform().get(0).getContent();
            } catch (Exception e) {

            }
            try {
                List<Repository> repositories = eag.getArchguide().getDesc().getRepositories().getRepository();
                if (repositories != null && repositories.size() > 0) {
                    for (Repository repository : repositories) {
                        if (repository.getRepositoryName() != null && repository.getRepositoryName().size() > 0) {
                            aiTitle += "; " + repository.getRepositoryName().get(0).getContent();
                        }
                    }

                    loop1:
                    for (Repository repository : repositories) {
                        if (repository.getRepositorhist() != null && repository.getRepositorhist().getDescriptiveNote() != null &&
                                repository.getRepositorhist().getDescriptiveNote().getP() != null) {
                            for (P p : repository.getRepositorhist().getDescriptiveNote().getP()) {
                                aiDescription += "; " + p.getContent();
//                                if (aiDescription.length() > SocialUtils.DESCRIPTION_MAX_LENGTH) {
//                                    aiDescription = aiDescription.substring(0, SocialUtils.DESCRIPTION_MAX_LENGTH) + "...";
//                                    break loop1;
//                                }
                            }
                        } else if (repository.getHoldings() != null && repository.getHoldings().getDescriptiveNote() != null &&
                                repository.getHoldings().getDescriptiveNote().getP() != null) {
                            for (P p : repository.getRepositorhist().getDescriptiveNote().getP()) {
                                aiDescription += "; " + p.getContent();
//                                if (aiDescription.length() > SocialUtils.DESCRIPTION_MAX_LENGTH) {
//                                    aiDescription = aiDescription.substring(0, SocialUtils.DESCRIPTION_MAX_LENGTH) + "...";
//                                    break loop1;
//                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Keywords
            List<String> allKeywords = new ArrayList<>();
            if (eag.getArchguide().getIdentity().getAutform().size()>1){
                for (int i=1; i<eag.getArchguide().getIdentity().getAutform().size(); i++){
                    Autform autform = eag.getArchguide().getIdentity().getAutform().get(i);
                    allKeywords.add(autform.getContent());
                }
            }
            List<Repository> repositories = eag.getArchguide().getDesc().getRepositories().getRepository();
            if (repositories != null && repositories.size() > 1) {
                for (int i=1; i<repositories.size(); i++){
                 allKeywords.add(repositories.get(i).getRepositoryName().get(0).getContent());
                }
            }
            try {
                List<Parform> parforms = eag.getArchguide().getIdentity().getParform();
                for (Parform parform : parforms){
                    allKeywords.add(parform.getContent());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<RepositoryType> repositoryTypes = eag.getArchguide().getIdentity().getRepositoryType();
                for (RepositoryType repositoryType : repositoryTypes){
                    allKeywords.add(repositoryType.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String finalKeywords = "";
            String finalKeywordsShort = "";
            boolean limitReached = false;
            for (String s : allKeywords) {
                keywords += ((keywords.length() != 0) ? ", " : "") + s;
                if (!limitReached) {
                    keywordsShort = keywords;
                    if (keywordsShort.length() > SocialUtils.KEYWORDS_MAX_LENGTH) {
                        limitReached = true;
                    }
                }
            }
        }
        if (aiTitle.length() == 0){
            aiTitle = archivalInstitution.getAiname();
            if (aiTitle==null || aiTitle.length()==0){
                aiTitle = archivalInstitution.getAutform();
            }
            if (aiTitle == null){
                aiTitle = SocialUtils.DEFAULT_TITLE;
            }
        }
        if (aiDescription.length() == 0){
            aiDescription = SocialUtils.DEFAULT_EAG_DESCRIPTION;
        }

        if (SocialUtils.TITLE_MAX_LENGTH > 0) {
            jsonMap.put("title", StringUtils.left(aiTitle, SocialUtils.TITLE_MAX_LENGTH));
        }
        else {
            jsonMap.put("title", aiTitle);
        }

        if (SocialUtils.DESCRIPTION_MAX_LENGTH > 0) {
            jsonMap.put("description", StringUtils.left(aiDescription, SocialUtils.DESCRIPTION_MAX_LENGTH));
        } else {
            jsonMap.put("description", aiDescription);
        }

        if (keywords.length() > 0) {
            if (SocialUtils.KEYWORDS_MAX_LENGTH > 0) {
                jsonMap.put("keywords", keywordsShort);
            }
            else {
                jsonMap.put("keywords", keywords);
            }
        }

        //Hashtags
//        jsonMap.put("hashtags", SocialUtils.EAG_HASHTAGS);

        return jsonMap;
    }
}
