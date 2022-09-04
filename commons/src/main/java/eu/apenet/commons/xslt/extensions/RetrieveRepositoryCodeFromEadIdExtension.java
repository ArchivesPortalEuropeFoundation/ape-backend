package eu.apenet.commons.xslt.extensions;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.util.ApeUtil;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

/**
 * Class for retrieve the identifier of the Archival Institution from the
 * identifier of an apeEAD.
 */
public class RetrieveRepositoryCodeFromEadIdExtension extends ExtensionFunctionDefinition {

    /**
     * Serializable.
     */
    private static final long serialVersionUID = -5996799246531865681L;

    /**
     * Name of the function to call.
     */
    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
            "aiFromEad");

    private RetrieveRepositoryCodeFromEadIdCall retrieveRepositoryCodeFromEadIdCall;

    public RetrieveRepositoryCodeFromEadIdExtension() {
        this.retrieveRepositoryCodeFromEadIdCall = new RetrieveRepositoryCodeFromEadIdCall();
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.OPTIONAL_STRING};
    }

    @Override
    public StructuredQName getFunctionQName() {
        return RetrieveRepositoryCodeFromEadIdExtension.funcname;
    }

    @Override
    public SequenceType getResultType(SequenceType[] arg0) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return this.retrieveRepositoryCodeFromEadIdCall;
    }

    public int getMinimumNumberOfArguments() {
        return 2;
    }

    public int getMaximumNumberOfArguments() {
        return 2;
    }

    class RetrieveRepositoryCodeFromEadIdCall extends ExtensionFunctionCall {

        /**
         * Serializable.
         */
        private static final long serialVersionUID = 864847688629772008L;

        /**
         * Constructor.
         */
        public RetrieveRepositoryCodeFromEadIdCall() {
            super();
        }

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            if (sequences != null && sequences.length == 2) {
                String firstArgValue = sequences[0].head().getStringValue();
                String secondArgValue = sequences[1].head().getStringValue();
                String value = "";
                String type = "";

                // apeEAD and EAD3 lookup.
                EadDAO eadDAO = DAOFactory.instance().getEadDAO();
                Ead ead = null;
                Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
                Ead3 ead3 = null;
                CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
                CLevel clevel = null;

                if (firstArgValue != null && !firstArgValue.isEmpty()) {
                    if (firstArgValue.startsWith("ead3:")) {
                        String ead3recordid = firstArgValue.substring(5);
                        String unitid = null;
                        if (ead3recordid.contains("::")) {
                            unitid = ead3recordid.replace("::", "-");
                            ead3recordid = ead3recordid.split("::")[0];
                        }
                        // Checks if a EAD3 with the provided identifier exists.
                        if (secondArgValue != null && !secondArgValue.isEmpty()) {
                            // And repository code.
                            ead3 = ead3DAO.getEad3ByIdentifier(secondArgValue, ead3recordid, true);
                            if (unitid != null){
                                clevel = clevelDAO.getCLevelByUnitId(unitid, secondArgValue, ead3recordid);
                            }
                        } else {
                            // First FA with identifier.
                            ead3 = ead3DAO.getFirstPublishedEad3ByIdentifier(ead3recordid, true);
                            if (unitid != null){
                                clevel = clevelDAO.getCLevelByUnitId(unitid, ead3.getId().toString());
                            }
                        }
                        type = "ead3";
                    } else {
                        // Checks if exists a FA with the provided identifier.
                        if (secondArgValue != null && !secondArgValue.isEmpty()) {
                            // And repository code.
                            ead = eadDAO.getEadByEadid(FindingAid.class, secondArgValue, firstArgValue, true);
                            type = "fa";
                        } else {
                            // First FA with identifier.
                            ead = eadDAO.getFirstPublishedEadByEadid(FindingAid.class, firstArgValue);
                            type = "fa";
                        }

                        if (ead == null) {
                            // Checks if exists a HG with the provided identifier.
                            if (secondArgValue != null && !secondArgValue.isEmpty()) {
                                // And repository code.
                                ead = eadDAO.getEadByEadid(HoldingsGuide.class, secondArgValue, firstArgValue, true);
                                type = "hg";
                            } else {
                                // First HG with identifier.
                                ead = eadDAO.getFirstPublishedEadByEadid(HoldingsGuide.class, firstArgValue);
                                type = "hg";
                            }
                        }

                        if (ead == null) {
                            // Checks if exists a SG with the provided identifier.
                            if (secondArgValue != null && !secondArgValue.isEmpty()) {
                                // And repository code.
                                ead = eadDAO.getEadByEadid(SourceGuide.class, secondArgValue, firstArgValue, true);
                                type = "sg";
                            } else {
                                // First SG with identifier.
                                ead = eadDAO.getFirstPublishedEadByEadid(SourceGuide.class, firstArgValue);
                                type = "sg";
                            }
                        }
                    }
                }

                if (ead != null) {
                    String repositoryCode = "";
                    if (secondArgValue != null && !secondArgValue.isEmpty()) {
                        repositoryCode = secondArgValue;
                    } else {
                        repositoryCode = ead.getArchivalInstitution().getRepositorycode();
                    }

//                    value = "aicode/" + repositoryCode + "/type/" + type + "/id/" + ApeUtil.encodeSpecialCharacters(ead.getEadid());
                    value = "?repositoryCode="+repositoryCode+"&levelName=archdesc&t="+type+"&recordId="+ead.getEadid();
                } else if (ead3 != null) {
                    String repositoryCode = "";
                    if (secondArgValue != null && !secondArgValue.isEmpty()) {
                        repositoryCode = secondArgValue;
                    } else {
                        repositoryCode = ead3.getArchivalInstitution().getRepositorycode();
                    }

//                    value = "aicode/" + repositoryCode + "/type/" + type + "/id/" + ApeUtil.encodeSpecialCharacters(ead3.getIdentifier());
                    value = "?repositoryCode="+repositoryCode+"&levelName=archdesc&t="+type+"&recordId="+ead.getEadid();
                    if (clevel != null) {
//                        value = value.concat("/unitid/" + ApeUtil.encodeSpecialCharacters(clevel.getUnitid()));
                        value = "?repositoryCode="+repositoryCode+"&levelName=clevel&t="+type+"&recordId="+ead.getEadid()+"&c=C"+clevel.getId();
                    }
                }

                return StringValue.makeStringValue(value);
            } else {
                return StringValue.makeStringValue("ERROR");
            }
        }

    }

}
