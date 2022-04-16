package eu.apenet.commons.xslt.eac;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import eu.apenet.commons.xslt.extensions.*;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.ClasspathURIResolver;


public final class EacXslt {
    private static final Logger LOG = Logger.getLogger(EacXslt.class);

	private static XsltExecutable getXsltExecutable(String xslUrl, String searchTerms, List<SolrField> highlightFields,
			ResourceBundleSource resourceBundleSource, Integer aiId, boolean isPreview, String solrStopwordsUrl) throws SaxonApiException{
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Source xsltSource = new StreamSource(classLoader.getResourceAsStream(xslUrl));	
        Processor processor = new Processor(false);
        HighlighterExtension highLighter = new HighlighterExtension (solrStopwordsUrl, searchTerms, highlightFields);
        ResourcebundleExtension  resourcebundleRetriever = new ResourcebundleExtension (resourceBundleSource);
        EadidCheckerExtension  eadidChecker = new EadidCheckerExtension (aiId, isPreview);
        SpecialCharactersEncoderExtension specialCharactersEncoder = new SpecialCharactersEncoderExtension();
        RetrieveRepositoryCodeFromEacIdExtension repositoryCodeEac = new RetrieveRepositoryCodeFromEacIdExtension();
        RetrieveRepositoryCodeFromEadIdExtension repositoryCodeEad = new RetrieveRepositoryCodeFromEadIdExtension();
        RetrieveTitleFromEadIdExtension titleEad = new RetrieveTitleFromEadIdExtension();
        CheckAgencyCodeExtension checkAgencyCode = new CheckAgencyCodeExtension();
        RetrieveCountryNameExtension countryName = new RetrieveCountryNameExtension();
        HrefCheckerExtension hrefChecker = new HrefCheckerExtension();
        TypeOfEacCpfExtension typeOfEacCpf = new TypeOfEacCpfExtension();
        processor.registerExtensionFunction(highLighter);
        processor.registerExtensionFunction(resourcebundleRetriever);
        processor.registerExtensionFunction(eadidChecker);
        processor.registerExtensionFunction(specialCharactersEncoder);
        processor.registerExtensionFunction(repositoryCodeEac);
        processor.registerExtensionFunction(repositoryCodeEad);
        processor.registerExtensionFunction(checkAgencyCode);
        processor.registerExtensionFunction(countryName);
        processor.registerExtensionFunction(hrefChecker);
        processor.registerExtensionFunction(typeOfEacCpf);
        processor.registerExtensionFunction(titleEad);
        XsltCompiler compiler = processor.newXsltCompiler();
        compiler.setURIResolver(new ClasspathURIResolver(xslUrl));
        return compiler.compile(xsltSource);
	}

    public static void convertEacToHtml(String xslUrl, Writer writer, Source xmlSource, String searchTerms,
    		List<SolrField> highlightFields, ResourceBundleSource resourceBundleSource,String secondDisplayUrl,
    		Integer aiId,boolean isPreview, String solrStopwordsUrl, String translationLanguage, String aiCodeUrl,
    		String eacUrlBase, String eadUrl, String langNavigator) throws SaxonApiException{
    	
		String language = resourceBundleSource.getLocale().getLanguage().toLowerCase();
		String languageIso3 = "eng";
		Map<String, String> langMap = APEnetUtilities.getIso2ToIso3LanguageCodesMap();
		
		//recover the iso3 language in the Map
		if (langMap.get(language)!= null && !langMap.get(language).isEmpty()){
			languageIso3 = langMap.get(language);
		}
		String langNavigatorIso3 = "eng";
		if (langMap.get(langNavigator)!= null && !langMap.get(langNavigator).isEmpty()){
			langNavigatorIso3 = langMap.get(langNavigator);
		}
		convertEacToHtml(xslUrl, writer, xmlSource, searchTerms, highlightFields, resourceBundleSource, languageIso3,
				secondDisplayUrl, aiId, isPreview, solrStopwordsUrl, translationLanguage, aiCodeUrl, eacUrlBase,
				eadUrl, langNavigatorIso3);
    }

    public static void convertEacToHtml(String xslUrl, Writer writer, Source xmlSource, String searchTerms,
    		List<SolrField> highlightFields, ResourceBundleSource resourceBundleSource, String language,
    		String secondDisplayUrl, Integer aiId,boolean isPreview, String solrStopwordsUrl,
    		String translationLanguage, String aiCodeUrl, String eacUrlBase, String eadUrl,
    		String langNavigator) throws SaxonApiException {
    	XsltExecutable executable = getXsltExecutable(xslUrl, searchTerms, highlightFields,resourceBundleSource, aiId, isPreview, solrStopwordsUrl);
        XsltTransformer transformer = executable.load();
        transformer.setParameter(new QName("eaccontent.extref.prefix"), new XdmAtomicValue(secondDisplayUrl));
        transformer.setParameter(new QName("language.selected"), new XdmAtomicValue(language));
        transformer.setParameter(new QName("translationLanguage"), new XdmAtomicValue(translationLanguage));
        transformer.setParameter(new QName("aiCodeUrl"), new XdmAtomicValue(aiCodeUrl));
        transformer.setParameter(new QName("eacUrlBase"), new XdmAtomicValue(eacUrlBase));
        transformer.setParameter(new QName("eadUrl"), new XdmAtomicValue(eadUrl));
        transformer.setParameter(new QName("lang.navigator"), new XdmAtomicValue(langNavigator));
        transformer.setParameter(new QName("searchTerms"), new XdmAtomicValue(searchTerms));
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
    }

}
