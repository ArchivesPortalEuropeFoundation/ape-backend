package eu.apenet.commons.xslt.extensions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import net.sf.saxon.om.Sequence;
import org.apache.log4j.Logger;

import java.util.List;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;

/**
 * Class for check  the eacCpf type (person, corporateBody or family)
 */
public class TypeOfEacCpfExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 1797212368432283188L;
	protected final Logger log = Logger.getLogger(getClass());
	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"typeOfEacCpf");

	private TypeOfEacCpfCall typeOfEacCpfCall;

	/**
	 * Constructor.
	 */
	public TypeOfEacCpfExtension() {
		this.typeOfEacCpfCall = new TypeOfEacCpfCall();
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return TypeOfEacCpfExtension.funcname;
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return this.typeOfEacCpfCall;
	}

	public int getMinimumNumberOfArguments() {
		return 1;
	}

	public int getMaximumNumberOfArguments() {
		return 1;
	}

	class TypeOfEacCpfCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = -1267376165135936320L;

		/**
		 * Constructor.
		 */
		public TypeOfEacCpfCall() {
			super();
		}

		/**
		 * Method to checks the eac-cpf type throw the link value.
		 *
		 */
		@Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
			
			String identifier = sequences[0].head().getStringValue();
			String typeEacCpf = "";
			if (sequences.length ==1){
				
				EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
				EacCpf eacCpf = null;
				eacCpf = eacCpfDAO.getFirstPublishedEacCpfByIdentifier(identifier, false);
				if (eacCpf != null){
					String path = APEnetUtilities.getApePortalAndDashboardConfig().getRepoDirPath() + eacCpf.getPath();
					typeEacCpf = extractElementFromXML(path,"eac-cpf/cpfDescription/identity/entityType").trim();
				} 
				return StringValue.makeStringValue(typeEacCpf);
			}else{
				return StringValue.makeStringValue("ERROR");
			}
		
		}
		
		/**
		 * Method to extract an element from the xml file
		 * @param path
		 * @param element
		 * @return text
		 */
		public String extractElementFromXML(String path, String element) {
			String text = "";
	        XMLStreamReader input = null;
		    InputStream sfile = null;
	        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
	        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
	        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
	        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
	        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
	        try {
	            sfile = new FileInputStream(path);
	            input = xmlif.createXMLStreamReader(sfile);

	            boolean exit = false;
	            boolean found = false;
	            String[] pathElements = null;
	            if(input!=null && element!=null){
	            	if(element.contains("/")){ //check input parameters
	            		if(element.startsWith("/")){ 
	            			element = element.substring(1);
	            		}
	            		if(element.endsWith("/") && element.length()>2){
	            			element = element.substring(0,element.length()-2);
	            		}
	            		pathElements = element.split("/");
	            	}else{
	            		pathElements = new String[1];
	            		pathElements[0] = element;
	            	}
	                
	            	List<String> currentElement = new ArrayList<String>();
	                log.debug("Checking EAC file, looking for element " + element + ", path begins with " + pathElements[0]);
	                while (!exit && input.hasNext()) {
	                	switch (input.getEventType()) {
	                	case XMLEvent.START_ELEMENT:
	                		currentElement.add(input.getLocalName().toString());
	                		if(currentElement.size()==pathElements.length){
	                			found = true;
	                			for(int i=0;i<pathElements.length && found;i++){
	                				found = (pathElements[i].trim().equals(currentElement.get(i).trim()));
	                			}
	                			text = "";
	                		}
	                		break;
	                	case XMLEvent.CHARACTERS:
	                	case XMLEvent.CDATA:
	                		if(found){
	                			text = ((text!=null)?text:"")+input.getText();
	                		}
	                		break;
	                	case XMLEvent.END_ELEMENT:
	                		currentElement.remove(currentElement.size()-1);
	                		if(found){
	                			exit = true;
	                		}
	                		break;
	                	}
	                	if (input.hasNext()){
	    	                input.next();
	    	            }
	                }
	            }
	        }catch(Exception e){
	        	log.error("Exception getting "+element,e);
	        }
			return text;
		}
	}

}
