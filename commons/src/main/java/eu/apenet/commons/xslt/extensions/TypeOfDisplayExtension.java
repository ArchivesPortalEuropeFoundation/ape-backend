package eu.apenet.commons.xslt.extensions;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.BooleanValue;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class TypeOfDisplayExtension extends ExtensionFunctionDefinition {
	/**
	 * 
	 */
	private static final long serialVersionUID = 654874379518388994L;
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"typeOfDisplay");
	// private static final Logger LOG = Logger.getLogger(Highlighter.class);
	private TypeOfDisplayCall typeOfDisplayCall;

	public TypeOfDisplayExtension(String type) {
		this.typeOfDisplayCall = new TypeOfDisplayCall(type);
	}

	@Override
	public StructuredQName getFunctionQName() {
		return funcname;
	}

	@Override
	public int getMinimumNumberOfArguments() {
		return 1;
	}

	public int getMaximumNumberOfArguments() {
		return 1;
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public SequenceType getResultType(SequenceType[] sequenceTypes) {
		return SequenceType.OPTIONAL_BOOLEAN;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return typeOfDisplayCall;
	}

	class TypeOfDisplayCall extends ExtensionFunctionCall {
		private static final long serialVersionUID = 6761914863093344493L;
		private String type;

		public TypeOfDisplayCall(String type) {
			this.type = type;
		}

        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			if (sequences.length == 1) {
				Item firstArg = sequences[0].head();
				boolean value = false;
				if (firstArg != null) {
					value = type.equalsIgnoreCase(firstArg.getStringValue());
				}
				return BooleanValue.get(value);
			} else {
				return BooleanValue.FALSE;
			}
		}
	}
}
