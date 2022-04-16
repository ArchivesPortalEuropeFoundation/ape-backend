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

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.types.XmlType;

public class OtherFindingAidExtension extends ExtensionFunctionDefinition {
	/**
	 * 
	 */
	private static final long serialVersionUID = 654874379518388994L;
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"otherfindingaid");
	private static final Logger LOG = Logger.getLogger(OtherFindingAidExtension.class);
	private OtherFindingAidExtensionCall resourceCall;

	public OtherFindingAidExtension(ResourceBundleSource resourceBundleSource, XmlType xmlType) {
		this.resourceCall = new OtherFindingAidExtensionCall(resourceBundleSource, xmlType);
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
		return 2;
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING, SequenceType.OPTIONAL_STRING };
	}

	@Override
	public SequenceType getResultType(SequenceType[] sequenceTypes) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return resourceCall;
	}

	class OtherFindingAidExtensionCall extends ExtensionFunctionCall {
		private static final long serialVersionUID = 6761914863093344493L;
		private ResourceBundleSource resourceBundleSource;
		private XmlType xmlType;

		public OtherFindingAidExtensionCall(ResourceBundleSource resourceBundleSource,XmlType xmlType) {
			this.resourceBundleSource = resourceBundleSource;
			this.xmlType = xmlType;
		}

        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			String value="ERROR";
			if (sequences.length >= 1){
				String type = sequences[0].head().getStringValue();
				if (sequences.length == 1){
					
					if ("title".equals(type)){
						if (XmlType.EAD_FA.equals(xmlType)){
							value = resourceBundleSource.getString("eadcontent.otherfindaid");
						}else {
							value = resourceBundleSource.getString("eadcontent.otherfindaid.sghg2fa");
						}
					}
				}else if (sequences.length == 2){
					String suffix = sequences[1].head().getStringValue();
					String prefix = "seconddisplay.view.sghg2fa.";
					if ("link".equals(type)){
						if (XmlType.EAD_FA.equals(xmlType)){
							prefix = "seconddisplay.view.fa.";
							value = resourceBundleSource.getString(prefix + suffix);
						}else {
							
							value = resourceBundleSource.getString(prefix + suffix);
						}
					}				
				}
			}
			return StringValue.makeStringValue(value);
		}
	}
}
