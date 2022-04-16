package eu.apenet.commons.xslt.extensions;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;

/**
 * Class for retrieve the identifier of the Archival Institution from the
 * identifier of an apeEAC-CPF.
 */
public class RetrieveRepositoryCodeFromEacIdExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 3948416061438709288L;

	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"aiFromEac");

	private RetrieveRepositoryCodeFromEacIdCall retrieveRepositoryCodeFromEacIdCall;

	public RetrieveRepositoryCodeFromEacIdExtension() {
		this.retrieveRepositoryCodeFromEacIdCall = new RetrieveRepositoryCodeFromEacIdCall();
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return RetrieveRepositoryCodeFromEacIdExtension.funcname;
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return this.retrieveRepositoryCodeFromEacIdCall;
	}

	public int getMinimumNumberOfArguments() {
		return 2;
	}

	public int getMaximumNumberOfArguments() {
		return 2;
	}

	class RetrieveRepositoryCodeFromEacIdCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = -5027606289788857044L;

		/**
		 * Constructor.
		 */
		public RetrieveRepositoryCodeFromEacIdCall() {
			super();
		}

		@Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			if (sequences!= null && sequences.length == 2) {
				String firstArgValue = sequences[0].head().getStringValue();
				String secondArgValue = sequences[1].head().getStringValue();
				String value = "";

				// apeEAC-CPF.
				EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
				EacCpf eacCpf = null;

				if (firstArgValue != null && !firstArgValue.isEmpty()) {
					if (secondArgValue != null && !secondArgValue.isEmpty()) {
						eacCpf = eacCpfDAO.getEacCpfByIdentifier(secondArgValue, firstArgValue, true);
					} else {
						eacCpf = eacCpfDAO.getFirstPublishedEacCpfByIdentifier(firstArgValue, true);
					}
				}

				if (eacCpf != null) {
					value = String.valueOf(eacCpf.getArchivalInstitution().getRepositorycode());
				}

				return StringValue.makeStringValue(value);
			} else {
				return StringValue.makeStringValue("ERROR");
			}
		}
	}

}
