package eu.apenet.commons.xslt.extensions;

import java.util.Locale;
import java.util.Map;

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
import eu.apenet.commons.utils.APEnetUtilities;

public class RetrieveCountryNameExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 1996252993435226567L;

	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"countryName");

	private RetrieveCountryNameCall retrieveCountryNameCall;

	/**
	 * Constructor.
	 */
	public RetrieveCountryNameExtension() {
		this.retrieveCountryNameCall = new RetrieveCountryNameCall();
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return RetrieveCountryNameExtension.funcname;
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return this.retrieveCountryNameCall;
	}

	public int getMinimumNumberOfArguments() {
		return 2;
	}

	public int getMaximumNumberOfArguments() {
		return 2;
	}

	class RetrieveCountryNameCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = 4990688202417868669L;

		/**
		 * Constructor.
		 */
		public RetrieveCountryNameCall() {
			super();
		}

		/**
		 * Recovers the name of the country in the language passed from the country code.
		 *
		 *     arg[0] -> Three letter language code
		 *     arg[1] -> Two letter country code
		 */
		@Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			if (sequences.length == 2) {
				String lang = sequences[0].head().getStringValue();
				String countryCode = sequences[1].head().getStringValue();
				String value = countryCode;

				if (lang != null && !lang.isEmpty()
						&& countryCode != null && !countryCode.isEmpty()) {

					// Checks the language.
					Map<String, Locale> localeMap = APEnetUtilities.getIso3ToIso2LanguageCodesMap();
					Locale language = localeMap.get(lang);
					if (language == null) {
						language = Locale.ENGLISH;
					}

					Locale locale = new Locale("", countryCode);
					value = locale.getDisplayCountry(language);
				}

				return StringValue.makeStringValue(value);
			} else {
				return StringValue.makeStringValue("ERROR");
			}
		}
		
	}

}
