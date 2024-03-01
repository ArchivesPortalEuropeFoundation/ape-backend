package eu.apenet.commons.utils.analyzers.eaccpf;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.analyzers.utils.SocialUtils;
import eu.apenet.dpt.utils.eaccpf.*;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.noggit.CharArr;
import org.noggit.JSONWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialInfoExtractor {

    private boolean writeToFile = false;

    public String extractStringInfo(EacCpf eacCpf){
        String repoPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
        return extractStringInfo(repoPath, eacCpf);
    }

    public String extractStringInfo(String repoPath, EacCpf eacCpf) {
        Map map = extractInfo(repoPath, eacCpf);
        if (map.size() == 0) return null;

        CharArr out = new CharArr();
        JSONWriter jsonWriter = new JSONWriter(out);
        jsonWriter.setIndentSize(-1);
        jsonWriter.write(map);
        String s = out.toString();

        return s;
    }

    public Map extractInfo(EacCpf eacCpf) {
        String repoPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
        return extractInfo(repoPath, eacCpf);
    }

    public Map extractInfo(String repoPath, EacCpf eacCpf) {

        String localPath = eacCpf.getPath();
        String fullPath = repoPath + localPath;
        File eaccpfFile = new File(fullPath);


        String temp = eaccpfFile.getParent();
        temp = temp.replace("/repo/", "/social/");
        String newDir = temp + File.separator + eacCpf.getId();
        if (writeToFile) {
            File newDirFile = new File((newDir));
            if (newDirFile.exists()) {
                try {
                    FileUtils.deleteDirectory(newDirFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            newDirFile.mkdirs();
        }

        eu.apenet.dpt.utils.eaccpf.EacCpf eacCpf1 = null;
        try {
            InputStream eaccpfStream = FileUtils.openInputStream(eaccpfFile);
            JAXBContext jaxbContext = JAXBContext.newInstance(eu.apenet.dpt.utils.eaccpf.EacCpf.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eacCpf1 = (eu.apenet.dpt.utils.eaccpf.EacCpf) jaxbUnmarshaller.unmarshal(eaccpfStream);
            eaccpfStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        Map info = new HashMap<>();

        if (eacCpf1 != null) {
            //Title
            List<Object> nameOrParallerList = eacCpf1.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry();
            Object preferredNameEntry = null;
            Object authorizedNameEntry = null;
            Object otherNameEntry = null;
            List<Object> nameEntriesForKeywords = new ArrayList<>();
            for (Object object : nameOrParallerList) {
                if (object instanceof Identity.NameEntry) {
                    Identity.NameEntry nameEntry = (Identity.NameEntry) object;
                    if ("preferred".equals(nameEntry.getLocalType())) {
                        if (preferredNameEntry == null) {
                            preferredNameEntry = nameEntry;
                        } else {
                            nameEntriesForKeywords.add(nameEntry);
                        }
                    } else if ("authorized".equals(nameEntry.getLocalType())) {
                        if (preferredNameEntry != null || authorizedNameEntry != null) {
                            nameEntriesForKeywords.add(nameEntry);
                        }
                        if (authorizedNameEntry == null) {
                            authorizedNameEntry = nameEntry;
                        }
                    } else {
                        if (preferredNameEntry != null || authorizedNameEntry != null || otherNameEntry != null) {
                            nameEntriesForKeywords.add(nameEntry);
                        }
                        if (otherNameEntry == null) {
                            otherNameEntry = nameEntry;
                        }
                    }
                } else if (object instanceof NameEntryParallel) {
                    NameEntryParallel nameEntry = (NameEntryParallel) object;
                    if ("preferred".equals(nameEntry.getLocalType())) {
                        if (preferredNameEntry == null) {
                            preferredNameEntry = nameEntry;
                        } else {
                            nameEntriesForKeywords.add(nameEntry);
                        }
                    } else if ("authorized".equals(nameEntry.getLocalType())) {
                        if (authorizedNameEntry == null) {
                            authorizedNameEntry = nameEntry;
                        }
                        if (preferredNameEntry != null) {
                            nameEntriesForKeywords.add(nameEntry);
                        }
                    } else {
                        if (otherNameEntry == null) {
                            otherNameEntry = nameEntry;
                        }
                        if (preferredNameEntry != null || authorizedNameEntry != null) {
                            nameEntriesForKeywords.add(nameEntry);
                        }
                    }
                }
            }

            List<Part> parts = null;
            String typeOfParts = null;
            if (preferredNameEntry != null) {
                if (preferredNameEntry instanceof Identity.NameEntry) {
                    parts = ((Identity.NameEntry) preferredNameEntry).getPart();
                } else if (preferredNameEntry instanceof NameEntryParallel) {
                    parts = ((NameEntryParallel) preferredNameEntry).getNameEntry().get(0).getPart();
                }
                typeOfParts = "preferred";
            } else if (authorizedNameEntry != null) {
                if (authorizedNameEntry instanceof Identity.NameEntry) {
                    parts = ((Identity.NameEntry) authorizedNameEntry).getPart();
                } else if (authorizedNameEntry instanceof NameEntryParallel) {
                    parts = ((NameEntryParallel) authorizedNameEntry).getNameEntry().get(0).getPart();
                }
                typeOfParts = "authorized";
            } else if (otherNameEntry != null) {
                if (otherNameEntry instanceof Identity.NameEntry) {
                    parts = ((Identity.NameEntry) otherNameEntry).getPart();
                } else if (otherNameEntry instanceof NameEntryParallel) {
                    parts = ((NameEntryParallel) otherNameEntry).getNameEntry().get(0).getPart();
                }
                typeOfParts = "other";
            }

            String title = null;
            if (parts != null) {
                title = handleNameParts(parts,typeOfParts);
            }
            if (title == null) {
                if (eacCpf.getTitle() != null) {
                    title = eacCpf.getTitle();
                }
//                else {
//                    title = SocialUtils.DEFAULT_TITLE;
//                }
            }
            if (title != null) {
                if (SocialUtils.TITLE_MAX_LENGTH > 0) {
                    info.put("title", StringUtils.left(title, SocialUtils.TITLE_MAX_LENGTH));
                }
                else {
                    info.put("title", title);
                }
            }

            //Description
            String abstractDescr = "";
            if (eacCpf1.getCpfDescription().getDescription() != null) {
                if (eacCpf1.getCpfDescription().getDescription().getBiogHist() != null) {
                    if (eacCpf1.getCpfDescription().getDescription().getBiogHist().size() > 0) {
                        if (eacCpf1.getCpfDescription().getDescription().getBiogHist().get(0).getAbstract() != null) {
                            abstractDescr = eacCpf1.getCpfDescription().getDescription().getBiogHist().get(0).getAbstract().getContent();
                        }
                        List<Object> ps = eacCpf1.getCpfDescription().getDescription().getBiogHist().get(0).getChronListOrPOrCitation();
                        if (ps != null) {
                            for (Object object : ps) {
                                if (object instanceof P) {
                                    P p = (P) object;
                                    abstractDescr += " " + p.getContent();
                                }
                            }
                        }
                    }
                }
                if (abstractDescr.trim().length() == 0) {
                    if (eacCpf1.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses() != null) {
                        if (eacCpf1.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses().size() > 0) {
                            for (Object object : eacCpf1.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses()) {
                                if (object instanceof GeneralContext) {
                                    GeneralContext generalContext = (GeneralContext) object;
                                    List<Object> ps = generalContext.getMDiscursiveSet();
                                    for (Object object1 : ps) {
                                        if (object1 instanceof P) {
                                            P p = (P) object1;
                                            abstractDescr += " " + p.getContent();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
//            if (abstractDescr.trim().length() == 0) {
//                abstractDescr = SocialUtils.DEFAULT_EACCPF_DESCRIPTION;
//            }
            if (abstractDescr.trim().length() == 0) {
                if (SocialUtils.DESCRIPTION_MAX_LENGTH > 0) {
                    info.put("description", StringUtils.left(abstractDescr, SocialUtils.DESCRIPTION_MAX_LENGTH));
                } else {
                    info.put("description", abstractDescr);
                }
            }

            //Keywords
            List<String> allKeywords = new ArrayList<>();
            for (Object object : nameEntriesForKeywords) {
                if (object instanceof Identity.NameEntry) {
                    Identity.NameEntry nameEntry = (Identity.NameEntry) object;
                    String keywords = handleKeywordNameParts(nameEntry.getPart());
                    allKeywords.add(keywords);
                } else if (object instanceof NameEntryParallel) {
                    NameEntryParallel nameEntry = (NameEntryParallel) object;
                    for (NameEntry nameEntry1 : nameEntry.getNameEntry()) {
                        String keywords = handleKeywordNameParts(nameEntry1.getPart());
                        allKeywords.add(keywords);
                    }
                }
            }
            String finalKeywords = "";
            String finalKeywordsShort = "";
            boolean limitReached = false;
            for (String s : allKeywords) {
                finalKeywords += ((finalKeywords.length() != 0) ? ", " : "") + s;
                if (!limitReached) {
                    finalKeywordsShort = finalKeywords;
                    if (finalKeywordsShort.length() > SocialUtils.KEYWORDS_MAX_LENGTH) {
                        limitReached = true;
                    }
                }
            }
            if (finalKeywords.length() > 0) {
                if (SocialUtils.KEYWORDS_MAX_LENGTH > 0) {
                    info.put("keywords", finalKeywordsShort);
                }
                else {
                    info.put("keywords", finalKeywords);
                }
            }
        }
//        else {
//            info.put("title", SocialUtils.DEFAULT_TITLE);
//            info.put("title-short", SocialUtils.DEFAULT_TITLE);
//
//            info.put("description", SocialUtils.DEFAULT_EACCPF_DESCRIPTION);
//            info.put("description-short", SocialUtils.DEFAULT_EACCPF_DESCRIPTION);
//        }


        //Publisher
        ArchivalInstitution archivalInstitution = eacCpf.getArchivalInstitution();
        String publisher = archivalInstitution.getAiname();
        info.put("publisher", publisher);

        //Hashtags
//        info.put("hashtags", SocialUtils.EACCPF_HASHTAGS);


        return writeInfo(newDir, info, writeToFile);

    }

    private Map writeInfo(String path, Map map, boolean writeToFile){
        CharArr out = new CharArr();
        JSONWriter jsonWriter = new JSONWriter(out);
        jsonWriter.write(map);

        String s = out.toString();

        if (writeToFile) {
            try {
                FileWriter myWriter = new FileWriter(path + File.separator + "eaccpf.json");
                myWriter.write(s);
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private String handleNameParts(List<Part> parts, String typeOfParts) {
        String firstname = null;
        String surname = null;
        String persname = null;
        String famname = null;
        String corponame = null;
        String other = "";
        for (Part part : parts) {
            if ("firstname".equals(part.getLocalType())) {
                firstname = part.getContent();
            } else if ("surname".equals(part.getLocalType())) {
                surname = part.getContent();
            } else if ("persname".equals(part.getLocalType())) {
                persname = part.getContent();
            } else if ("famname".equals(part.getLocalType())) {
                famname = part.getContent();
            } else if ("corponame".equals(part.getLocalType())) {
                corponame = part.getContent();
            } else {
                other += (other.length() == 0 ? "" : ", ") + part.getContent();
            }
        }
        if (firstname != null || surname != null || persname != null || famname != null || corponame != null){
            String response = "";
            if (firstname != null){
                response += firstname;
            }
            if (surname != null){
                response += (response.length() == 0 ? "" : " ") + surname;
            }
            if (persname != null){
                response += (response.length() == 0 ? "" : "; ") + persname;
            }
            if (famname != null){
                response += (response.length() == 0 ? "" : "; ") + famname;
            }
            if (corponame != null){
                response += (response.length() == 0 ? "" : "; ") + corponame;
            }
            return response;
        }
        else {
            return other;
        }
    }

    private String handleKeywordNameParts(List<Part> parts){
        String response = "";
        for (Part part : parts){
            response += " " + part.getContent();
        }
        return response.trim();
    }
}
