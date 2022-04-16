package eu.apenet.commons.xslt.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

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

public class HrefCheckerExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 5267663782767624711L;

	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"checkHrefValue");

	private HrefCheckerCall hrefCheckerCall;

	/**
	 * Constructor.
	 */
	public HrefCheckerExtension() {
		this.hrefCheckerCall = new HrefCheckerCall();
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return HrefCheckerExtension.funcname;
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return this.hrefCheckerCall;
	}

	public int getMinimumNumberOfArguments() {
		return 1;
	}

	public int getMaximumNumberOfArguments() {
		return 1;
	}

	class HrefCheckerCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = -2318337149350309746L;

		/**
		 * Constructor.
		 */
		public HrefCheckerCall() {
			super();
		}

		/**
		 * Checks the list of @xlink:href passed in order to test if at least
		 * one of them is a valid link.
		 *
		 *     arg[0] -> List of @xlink:href
		 */
		@Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			String splitValue = "_HREF_SEPARATOR_";

			if (sequences.length == 1) {
				String list = sequences[0].head().getStringValue();
				boolean value = false;

				if (list != null && !list.isEmpty()) {
					List<String> hrefs = new ArrayList<String>(Arrays.asList(list.split(splitValue)));

					for (int i = 0; !value && i < hrefs.size(); i++) {
						String currentVal = hrefs.get(i);

						if (currentVal != null && !currentVal.isEmpty()
								&& (currentVal.startsWith("http")
										|| currentVal.startsWith("https")
										|| currentVal.startsWith("ftp")
										|| currentVal.startsWith("www"))) {
							// Checks if the link is an external one.
							value = true;
						} else {
							// Checks if the link is an internal one.
							// First, checks if is a link with another
							// apeEAC-CPF in the system.
							if (!value) {
								EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
								EacCpf eacCpf = null;
								eacCpf = eacCpfDAO.getFirstPublishedEacCpfByIdentifier(currentVal, true);

								if (eacCpf != null) {
									value = true;
								}
							}

							// Second, checks if is a link with an apeEAD (FA,
							// HG or SG) in the system.
							if (!value) {
								EadDAO eadDAO = DAOFactory.instance().getEadDAO();
								Ead ead = null;
								// First, FA with identifier.
								ead = eadDAO.getFirstPublishedEadByEadid(FindingAid.class, currentVal);

								if (ead != null) {
									value = true;
								}

								// Second, HG with identifier.
								if (!value) {
									ead = eadDAO.getFirstPublishedEadByEadid(HoldingsGuide.class, currentVal);
	
									if (ead != null) {
										value = true;
									}
								}

								// Third, SG with identifier.
								if (!value) {
									ead = eadDAO.getFirstPublishedEadByEadid(SourceGuide.class, currentVal);
	
									if (ead != null) {
										value = true;
									}
								}
							}

							// Third, checks if is a link with an EAG 2012 in
							// the system.
							if (!value) {
								ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
								ArchivalInstitution archivalInstitution = null;
								archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(currentVal);

								if (archivalInstitution != null) {
									value = true;
								}
							}
						}
					}
				}

				return StringValue.makeStringValue(Boolean.toString(value));
			} else {
				return StringValue.makeStringValue("ERROR");
			}
		}
		
	}

}
