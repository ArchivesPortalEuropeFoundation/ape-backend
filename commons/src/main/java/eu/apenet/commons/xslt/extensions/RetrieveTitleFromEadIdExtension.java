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
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

/**
 * Class for retrieve the identifier of the Archival Institution from the
 * identifier of an apeEAD.
 */
public class RetrieveTitleFromEadIdExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -5996799246531865681L;

	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"titleFromEad");

	private RetrieveRepositoryCodeFromEadIdCall retrieveRepositoryCodeFromEadIdCall;

	public RetrieveTitleFromEadIdExtension() {
		this.retrieveRepositoryCodeFromEadIdCall = new RetrieveRepositoryCodeFromEadIdCall();
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return RetrieveTitleFromEadIdExtension.funcname;
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
			if (sequences!= null && sequences.length == 2) {
				String firstArgValue = sequences[0].head().getStringValue();
				String secondArgValue = sequences[1].head().getStringValue();
				String value = "";
				String type = "";

				// apeEAD.
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				Ead ead = null;

				if (firstArgValue != null && !firstArgValue.isEmpty()) {
					// Checks if exists a FA with the provided identifier.
					if (secondArgValue != null && !secondArgValue.isEmpty()) {
						// And repository code.
						ead = eadDAO.getEadByEadid(FindingAid.class, secondArgValue, firstArgValue,false);
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
							ead = eadDAO.getEadByEadid(HoldingsGuide.class, secondArgValue, firstArgValue,false);
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
							ead = eadDAO.getEadByEadid(SourceGuide.class, secondArgValue, firstArgValue,false);
							type = "sg";
						} else {
							// First SG with identifier.
							ead = eadDAO.getFirstPublishedEadByEadid(SourceGuide.class, firstArgValue);
							type = "sg";
						}
					}
				}

				if (ead != null) {
					if (secondArgValue != null && !secondArgValue.isEmpty()) {
						value = secondArgValue;
					} else {
						if(type.equals("fa")){
							value = ((FindingAid)ead).getTitle();
						}else if(type.equals("hg")){
							value = ((HoldingsGuide)ead).getTitle();
						}else if(type.equals("sg")){
							value = ((SourceGuide)ead).getTitle();
						}
					}
				}

				return StringValue.makeStringValue(value);
			} else {
				return StringValue.makeStringValue("ERROR");
			}
		}
		
	}

}
