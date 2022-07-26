package eu.apenet.api.ead.tree;

import eu.apenet.commons.types.XmlType;
import java.util.List;
import java.util.Locale;

import eu.apenet.persistence.factory.DAOFactory;
import org.apache.commons.lang.StringUtils;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.archivesportaleurope.util.ApeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * JSON Writer for the directory tree
 *
 * @author bastiaan
 *
 */

public class EadTreeJSONWriter extends AbstractJSONWriter {

    private static final String MORE_VALUE_BEFORE = "before";
    private static final String MORE_VALUE_AFTER = "after";
    public static final int MAX_NUMBER_OF_CLEVELS = 20;
    private static final String END_ITEM_WITH_RETURN = "}\n";
    private static final String END_ITEM_WITH_COMMA = "},";
    private CLevelDAO clevelDAO;
//    private EadDAO eadDAO;
    private EadContentDAO eadContentDAO;

    public EadTreeJSONWriter(){
        clevelDAO = DAOFactory.instance().getCLevelDAO();
        eadContentDAO = DAOFactory.instance().getEadContentDAO();
    }

    public CLevelDAO getClevelDAO() {
        return clevelDAO;
    }

    public void setClevelDAO(CLevelDAO clevelDAO) {
        this.clevelDAO = clevelDAO;
    }

    public EadContentDAO getEadContentDAO() {
        return eadContentDAO;
    }

    public void setEadContentDAO(EadContentDAO eadContentDAO) {
        this.eadContentDAO = eadContentDAO;
    }

    public void writeJSON(EadTreeParams eadParams, HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            Locale locale = request.getLocale();

            if (eadParams.getMax() == null) {
                eadParams.setMax(MAX_NUMBER_OF_CLEVELS);
            }
            if (eadParams.getMax() > MAX_NUMBER_OF_CLEVELS) {
                eadParams.setMax(MAX_NUMBER_OF_CLEVELS);
            }
            if (eadParams.getParentId() != null) {
                List<CLevel> clevels = clevelDAO.findChildCLevels(eadParams.getParentId(), eadParams.getOrderId(),
                        eadParams.getMax());
                writeToResponseAndClose(generateCLevelJSON(clevels, eadParams, locale), response);
            } else if (eadParams.getMore() != null) {
                /*
		 * used for more option
                 */
                List<CLevel> clevels = new ArrayList<>();
                if (eadParams.getXmlTypeName().contains(XmlType.EAD_3.getResourceName())) {
                    EadContent eadContent = eadContentDAO.findById(eadParams.getEcId());
                    clevels = clevelDAO.findTopEad3CLevels(eadContent.getEad3().getId(), eadParams.getOrderId(), eadParams.getMax());
                } else {
                    clevels = clevelDAO.findTopCLevels(eadParams.getEcId(),
                            eadParams.getOrderId(),
                            eadParams.getMax());
                }
                writeToResponseAndClose(generateCLevelJSON(clevels, eadParams, locale), response);
            } else if (StringUtils.isNotBlank(eadParams.getSolrId())) {
                Long solrId = Long.parseLong(eadParams.getSolrId().substring(1));
                CLevel clevel = clevelDAO.findById(solrId);
                writeToResponseAndClose(generateJSONWithSelectedItem(clevel, eadParams, locale),
                        response);
            } else if (eadParams.getEcId() != null) {
                EadContent eadContent = eadContentDAO.findById(eadParams.getEcId());
                List<CLevel> clevels = new ArrayList<>();
                if (eadParams.getXmlTypeName().contains(XmlType.EAD_3.getResourceName())) {

                    clevels = clevelDAO.findTopEad3CLevels(eadContent.getEad3().getId(), eadParams.getOrderId(), eadParams.getMax());
                } else {
                    clevels = clevelDAO.findTopCLevels(eadContent.getEcId(), eadParams.getOrderId(),
                            eadParams.getMax());
                }
                StringBuilder topCLevelsBuffer = generateCLevelJSON(clevels, eadParams, locale);
                writeToResponseAndClose(generateRootJSON(eadContent, topCLevelsBuffer, false, true, eadParams, locale),
                        response);

            }

        } catch (Exception e) {
            log.error(ApeUtil.generateThrowableLog(e));
        }
    }

    private StringBuilder generateCLevelJSON(List<CLevel> clevels, EadTreeParams eadParams, Locale locale) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(START_ARRAY);
        if (!MORE_VALUE_AFTER.equalsIgnoreCase(eadParams.getMore()) && clevels.size() > 0) {
            CLevel firstCLevel = clevels.get(0);
            addBefore(buffer, firstCLevel, eadParams, locale);
        }
        for (int i = 0; i < clevels.size(); i++) {
            CLevel clevel = clevels.get(i);
            buffer.append(START_ITEM);
            addTitle(buffer, clevel, locale);
            buffer.append(COMMA);
            addId(buffer, clevel.getId());
            addChildren(buffer, clevel, eadParams, locale);
            if (i < clevels.size() - 1) {
                buffer.append(END_ITEM_WITH_COMMA);
            } else {
                buffer.append(END_ITEM);
            }
        }
        if (!MORE_VALUE_BEFORE.equalsIgnoreCase(eadParams.getMore()) && clevels.size() > 0
                && clevels.size() == MAX_NUMBER_OF_CLEVELS) {
            CLevel lastCLevel = clevels.get(clevels.size() - 1);
            addAfter(buffer, lastCLevel, eadParams, locale);
        }
        buffer.append(END_ARRAY);
        return buffer;
    }

    private StringBuilder generateJSONWithSelectedItem(CLevel clevel, EadTreeParams eadParams, Locale locale) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(START_ARRAY);
        addBefore(buffer, clevel, eadParams, locale);
        buffer.append(START_ITEM);
        addTitle(buffer, clevel, locale);
        buffer.append(COMMA);
        addId(buffer, clevel.getId());
        buffer.append(COMMA);
        buffer.append("\"selected\":true, \"activate\": true");

        addChildren(buffer, clevel, eadParams, locale);
        buffer.append(END_ITEM);

        addAfter(buffer, clevel, eadParams, locale);
        buffer.append(END_ARRAY);
        return generateParentCLevelJSON(clevel, buffer, eadParams, locale);
    }

    private StringBuilder generateParentCLevelJSON(CLevel child, StringBuilder childBuffer, EadTreeParams eadParams, Locale locale) {
        CLevel parent = child.getParent();
        if (parent != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(START_ARRAY);
            addBefore(buffer, parent, eadParams, locale);
            buffer.append(START_ITEM);
            addTitle(buffer, parent, locale);
            buffer.append(COMMA);
            addId(buffer, parent.getId());
            buffer.append(COMMA);
            addExpand(buffer);
            buffer.append(COMMA);
            buffer.append(FOLDER_WITH_CHILDREN);
            buffer.append(childBuffer);
            buffer.append(END_ITEM);
            addAfter(buffer, parent, eadParams, locale);
            buffer.append(END_ARRAY);
            return generateParentCLevelJSON(parent, buffer, eadParams, locale);
        } else {
            EadContent eadContent = null;
            if (child.getEad3()!=null) {
                eadContent = child.getEad3().getEadContent();
            } else {
                eadContent = child.getEadContent();
            }
            return generateRootJSON(eadContent, childBuffer, true, false, eadParams, locale);
        }
    }

    private StringBuilder generateRootJSON(EadContent eadContent, StringBuilder childBuffer,
            boolean expand, boolean selected, EadTreeParams eadParams, Locale locale) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(START_ARRAY);
        buffer.append(START_ITEM);
        addTitle(buffer, eadContent.getTitleproper(), locale);
        buffer.append(COMMA);
        buffer.append("\"id\":"+(eadContent.getHgId()!=null?eadContent.getHgId():(eadContent.getSgId()!=null?eadContent.getSgId():(eadContent.getFaId()!=null?eadContent.getFaId():""))));
        buffer.append(COMMA);
        buffer.append("\"eadid\":\""+(eadContent.getHgId()!=null?eadContent.getHoldingsGuide().getEadid():(eadContent.getSgId()!=null?eadContent.getSourceGuide().getEadid():(eadContent.getFaId()!=null?eadContent.getFindingAid().getEadid():"")))+"\"");
        buffer.append(COMMA);
        buffer.append("\"ead_type\":\""+(eadContent.getHgId()!=null?"hg":(eadContent.getSgId()!=null?"sg":(eadContent.getFaId()!=null?"fa":"")))+"\"");
        if (selected) {
            buffer.append(COMMA);
            buffer.append("\"selected\":true, \"activate\": true");
        }
        buffer.append(COMMA);
        if (eadParams.getXmlTypeName().equalsIgnoreCase(XmlType.EAD_3.getName())) {
            addType(buffer, "frontpage-ead3");
        } else {
            addType(buffer, "frontpage");
        }
        buffer.append(COMMA);
        buffer.append(FOLDER_WITH_CHILDREN);
        buffer.append(childBuffer);
        buffer.append(END_ITEM_WITH_RETURN);
        buffer.append(END_ARRAY);
        return buffer;
    }

    private void addChildren(StringBuilder buffer, CLevel clevel, EadTreeParams eadParams, Locale locale) {
        if (!clevel.isLeaf()) {
            buffer.append(COMMA);
            buffer.append(FOLDER_LAZY);
//            buffer.append(COMMA);
//            addId(buffer, clevel.getId());
        }
    }

    private void addBefore(StringBuilder buffer, CLevel clevel, EadTreeParams eadParams, Locale locale) {
        if (!MORE_VALUE_AFTER.equalsIgnoreCase(eadParams.getMore()) && (clevel.getOrderId() > 0)) {
            buffer.append(START_ITEM);
            addMore(buffer, "eadcontent.more.before", MORE_VALUE_BEFORE, locale);
            buffer.append(COMMA);
            buffer.append(FOLDER_LAZY);
            buffer.append(COMMA);
            if (clevel.getParentId() != null) {
                buffer.append("\"parentId\":");
                buffer.append(" \"" + clevel.getParentId() + "\" ");
                buffer.append(COMMA);
            }
            buffer.append("\"orderId\":");
            int orderId = clevel.getOrderId() - MAX_NUMBER_OF_CLEVELS;
            int max = MAX_NUMBER_OF_CLEVELS;
            if (orderId < 0) {
                max = clevel.getOrderId();
                orderId = 0;

            }
            buffer.append(" \"" + orderId + "\" ");
            buffer.append(COMMA);
            buffer.append("\"max\":");
            buffer.append(" \"" + max + "\" ");
            buffer.append(END_ITEM);
            buffer.append(COMMA);

        }
    }

    private void addAfter(StringBuilder buffer, CLevel clevel, EadTreeParams eadParams, Locale locale) {
        if (!MORE_VALUE_BEFORE.equalsIgnoreCase(eadParams.getMore())) {
            buffer.append(COMMA);
            buffer.append(START_ITEM);
            addMore(buffer, "eadcontent.more.after", MORE_VALUE_AFTER, locale);
            buffer.append(COMMA);
            if (clevel.getParentId() != null) {
                buffer.append("\"parentId\":");
                buffer.append(" \"" + clevel.getParentId() + "\" ");
                buffer.append(COMMA);
            }
            buffer.append("\"orderId\":");
            buffer.append(" \"" + (clevel.getOrderId() + 1) + "\" ");
            buffer.append(COMMA);
            buffer.append(FOLDER_LAZY);
            buffer.append(END_ITEM_WITH_RETURN);
        }

    }

    private void addTitle(StringBuilder buffer, CLevel clevel, Locale locale) {
        addTitle(buffer, clevel.getUnittitle(), locale);
    }

    private void addTitle(StringBuilder buffer, String title, Locale locale) {
        addTitle(null, buffer, title, locale);
    }

    private static void addId(StringBuilder buffer, Long clId) {
        buffer.append("\"id\":");
        buffer.append("\"");
        buffer.append(clId);
        buffer.append("\"");
    }

    private static void addType(StringBuilder buffer, String type) {
        buffer.append("\"type\":");
        buffer.append(" \"" + type);

        buffer.append("\" ");
    }

}
