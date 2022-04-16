/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.commons.xslt.extensions;

import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.apache.log4j.Logger;

/**
 *
 * @author stefan
 */
public class EacRecordidCheckerExtension extends ExtensionFunctionDefinition {

    private static final long serialVersionUID = 654874379518389994L;
    private static final StructuredQName funcname = new StructuredQName("ape",
            "http://www.archivesportaleurope.eu/xslt/extensions", "linkedEacCpf");
    private static final Logger LOG = Logger.getLogger(EacRecordidCheckerExtension.class);
    private EacRecordidCheckerCall eacRecordidCheckerCall;

    public EacRecordidCheckerExtension(Integer aiId, boolean preview) {
        this.eacRecordidCheckerCall = new EacRecordidCheckerCall(aiId, preview);
    }

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 1;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sts) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return eacRecordidCheckerCall;
    }

    private static class EacRecordidCheckerCall extends ExtensionFunctionCall {

        private static final long serialVersionUID = 6761914863093345493L;
        private Integer aiId;
        private boolean preview;

        public EacRecordidCheckerCall(Integer aiId, boolean preview) {
            this.aiId = aiId;
            this.preview = preview;
        }

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            if (sequences.length == 1) {
                Item firstArgument = sequences[0].head();
                String value = "notavailable";
                if (firstArgument != null) {
                    String eacRecordid = firstArgument.getStringValue();

                    EacCpfDAO eacCpfDao = DAOFactory.instance().getEacCpfDAO();
                    if (eacCpfDao.isEacCpfIdIndexed(eacRecordid, EacCpf.class) != null) {
                        if (preview) {
                            value = "indexed-preview";
                        } else {
                            value = "indexed";
                        }
                    } else if (eacCpfDao.isEacCpfIdUsed(eacRecordid, aiId, EacCpf.class) != null) {
                        value = "notindexed";
                    }
                }
                return StringValue.makeStringValue(value);
            } else {
                return StringValue.makeStringValue("ERROR");
            }
        }
    }

}
