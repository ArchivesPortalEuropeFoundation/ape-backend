package eu.apenet.commons.xslt.ead;

import java.io.Writer;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import eu.apenet.commons.xslt.extensions.*;
import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.xslt.ClasspathURIResolver;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;


public final class EadXslt {

    private static XsltExecutable getXsltExecutable(String xslUrl, String searchTerms, List<SolrField> highlightFields, ResourceBundleSource resourceBundleSource,
                                                    Integer aiId, boolean isDashboard, String solrStopwordsUrl, String typeOfDisplay, XmlType xmlType) throws SaxonApiException {
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Source xsltSource = new StreamSource(classLoader.getResourceAsStream(xslUrl));
        Processor processor = new Processor(false);
        HighlighterExtension highLighter = new HighlighterExtension(solrStopwordsUrl, searchTerms, highlightFields);
        ResourcebundleExtension resourcebundleRetriever = new ResourcebundleExtension(resourceBundleSource);
        EadidCheckerExtension eadidChecker = new EadidCheckerExtension(aiId, isDashboard);
        EacRecordidCheckerExtension eacRecordidChecker = new EacRecordidCheckerExtension(aiId, isDashboard);
        CheckAgencyCodeExtension getAiCode1 = new CheckAgencyCodeExtension();
        RetrieveRepositoryCodeFromEacIdExtension getAiCode2 = new RetrieveRepositoryCodeFromEacIdExtension();
        processor.registerExtensionFunction(highLighter);
        processor.registerExtensionFunction(resourcebundleRetriever);
        processor.registerExtensionFunction(eadidChecker);
        processor.registerExtensionFunction(eacRecordidChecker);
        processor.registerExtensionFunction(getAiCode1);
        processor.registerExtensionFunction(getAiCode2);
        processor.registerExtensionFunction(new SpecialCharactersEncoderExtension());
        processor.registerExtensionFunction(new TypeOfDisplayExtension(typeOfDisplay));
        processor.registerExtensionFunction(new OtherFindingAidExtension(resourceBundleSource, xmlType));
        XsltCompiler compiler = processor.newXsltCompiler();
        compiler.setURIResolver(new ClasspathURIResolver(xslUrl));
        return compiler.compile(xsltSource);
    }

    public static void convertEadToHtml(String xslUrl, Writer writer, Source xmlSource, String searchTerms, List<SolrField> highlightFields,
            ResourceBundleSource resourceBundleSource, String secondDisplayUrl, Integer aiId, boolean isDashboard,
            String solrStopwordsUrl, String typeOfDisplay, XmlType xmlType, String eacUrl) throws SaxonApiException {
        XsltExecutable executable = getXsltExecutable(xslUrl, searchTerms, highlightFields, resourceBundleSource, aiId, isDashboard, solrStopwordsUrl, typeOfDisplay, xmlType);
        XsltTransformer transformer = executable.load();
        transformer.setParameter(new QName("eadcontent.extref.prefix"), new XdmAtomicValue(secondDisplayUrl));
        transformer.setParameter(new QName("eacUrl"), new XdmAtomicValue(eacUrl));
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
    }

}
