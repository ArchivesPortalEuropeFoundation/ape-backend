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
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * Class for check if the repository code exits in the system.
 */
public class CheckAgencyCodeExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 1797212368432283188L;

	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"checkAICode");

	private CheckAgencyCodeCall checkAgencyCodeCall;

	/**
	 * Constructor.
	 */
	public CheckAgencyCodeExtension() {
		this.checkAgencyCodeCall = new CheckAgencyCodeCall();
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return CheckAgencyCodeExtension.funcname;
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return this.checkAgencyCodeCall;
	}

	public int getMinimumNumberOfArguments() {
		return 3;
	}

	public int getMaximumNumberOfArguments() {
		return 3;
	}

	class CheckAgencyCodeCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = -1267376165135936320L;

		/**
		 * Constructor.
		 */
		public CheckAgencyCodeCall() {
			super();
		}

		/**
		 * Method to checks the agencyCode and agencyName values.
		 *
		 *     If exists arg[0] the method checks if the passed "agencyCode" exists in the system and returns the "repositoryCode".
		 *     If exists arg[1] the method checks if the passed "agencyNode" exists in the system and returns the "repositoryCode".
		 *     If exists arg[2] the method checks if the passed "agencyCode" (in arg[0]) exists in the system and returns the "repositoryName".
		 */
		@Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			if (sequences.length == 3) {
				String firstArgValue = sequences[0].head().getStringValue();
				String secondArgValue = sequences[1].head().getStringValue();
				String thirdArgValue = sequences[2].head().getStringValue();
				String value = "";

				// Recover the archival institutions.
				ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
				ArchivalInstitution archivalInstitution = null;

				if (firstArgValue != null && !firstArgValue.isEmpty()) {
					archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(firstArgValue);
				} else if (secondArgValue != null && !secondArgValue.isEmpty()) {
					archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByAiName(secondArgValue);
				}

				if (archivalInstitution != null) {
					if (thirdArgValue != null && !thirdArgValue.isEmpty() && thirdArgValue.equalsIgnoreCase("true")) {
						value = archivalInstitution.getRepositorycode();
					} else {
						value = archivalInstitution.getAiname();
					}
				}

				return StringValue.makeStringValue(value);
			} else {
				return StringValue.makeStringValue("ERROR");
			}
		}
	}

}
