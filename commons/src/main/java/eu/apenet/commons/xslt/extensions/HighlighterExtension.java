package eu.apenet.commons.xslt.extensions;

import java.util.List;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.solr.HighlightUtil;

public class HighlighterExtension extends ExtensionFunctionDefinition {
	/**
	 * 
	 */
	private static final long serialVersionUID = 654874379518388994L;
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"highlight");
	// private static final Logger LOG = Logger.getLogger(Highlighter.class);
	private HighlighterCall highlighterCall;

	public HighlighterExtension(String solrStopwordsUrl,String searchTerms, List<SolrField> highlightFields) {
		this.highlighterCall = new HighlighterCall(solrStopwordsUrl, searchTerms, highlightFields);
	}

	@Override
	public StructuredQName getFunctionQName() {
		return funcname;
	}

	@Override
	public int getMinimumNumberOfArguments() {
		return 2;
	}

	public int getMaximumNumberOfArguments() {
		return 2;
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public SequenceType getResultType(SequenceType[] sequenceTypes) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return highlighterCall;
	}

	class HighlighterCall extends ExtensionFunctionCall {
		private static final long serialVersionUID = 6761914863093344493L;
		private String searchTerms;
		private boolean hasSearchTerms;
		private List<SolrField> highlightFields;
		private HighlightUtil highlightUtil;

		public HighlighterCall(String solrStopwordsUrl, String searchTerms, List<SolrField> highlightFields) {
			this.searchTerms = searchTerms;
			this.highlightFields = highlightFields;
			hasSearchTerms = StringUtils.isNotBlank(searchTerms);
			highlightUtil =  HighlightUtil.getInstance(solrStopwordsUrl);
		}

        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			if (sequences.length == 2) {
				Item firstArg = sequences[0].head();
				String value = "";
				if (firstArg != null) {
					value = firstArg.getStringValue();
					value = value.replaceAll(">", "&#62;").replaceAll("<","&#60;");
					if (hasSearchTerms) {
						SolrField highlightField = SolrField.getSolrField(sequences[1].head().getStringValue());
						if (highlightField != null && highlightFields.contains(highlightField)) {
							value = highlightUtil.highlight(searchTerms, value, highlightField.getType());
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
