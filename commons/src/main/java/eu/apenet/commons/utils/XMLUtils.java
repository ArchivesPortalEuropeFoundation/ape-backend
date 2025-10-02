package eu.apenet.commons.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

/**
 *
 * @author Eloy Date: 7th of Dec 2011
 *
 * This class gathers methods useful for managing XML files
 */
public final class XMLUtils {

    //Constants
    private static final Logger log = Logger.getLogger(XMLUtils.class);
    private static final String SEPARATOR = ";";
    private static final String START_ATTRIBUTE = "[";
    private static final String END_ATTRIBUTE = "]";
    //private static final String NO_ATTRIBUTE_FOUND = "This attribute has not been found within the XML";
    //private static final String NO_TAG_FOUND = "This tag has not been found within the XML";
    private static final String NO_ATTRIBUTE_FOUND = "";
    private static final String NO_TAG_FOUND = "";

    //Methods
    // This method extracts the content within a tag inside an EAG or
    // the content within an attribute for the tag.
    // If there are more than one elements with the same name or attributes, their values
    // will be added with a ; in between
    // In order to include a tag within the elements we want to search is necessary to follow this rules
    // 1- The input will be a HashMap(String, String)
    // 2- Every key will represent a tag
    // 3- Every value will represent an attribute or a set of attributes for this tag
    // 4- If the value "" is included for a key, that will mean that it is expected to be retrieved the text within the tag itself
    // 5- If the value is a set of attributes f.i.: "classcode; level", it will mean that it will be necessary to retrieve
    //    different values (one [or several if the attribute is repeated] per attribute defined)
    //
    // 6- The output will be a HashMap(String, String)
    // 7- Every key will represent a tag[attribute] if it is needed or simply a tag if no attribute has been defined
    // 8- Every value will be the value for the tag or the attribute depending of the element found
    //
    // Example: INPUT -> [("archguide/desc/organization/descunit","classcode;level") ; ("archguide/identity/autform","")]
    // Example. OUTPUT -> [("archguide/desc/organization/descunit[classcode]","classcode_value"); ("archguide/desc/organization/descunit[level]","level_value"); ("archguide/identity/autform","autform_value")]
    @SuppressWarnings("unchecked")
    public static Map<String, String> extractTagsAndAttributesFromXML(HashMap<String, String> elements, String XMLPath) {

        Map<String, String> results = new HashMap<String, String>();
        HashMap<String, String> remainingElements = (HashMap<String, String>) elements.clone();
        String currentTag = "";
        boolean tagFound = false;

        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {

            sfile = new FileInputStream(XMLPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

            if (elements.size() > 0) {

                String attributeInput = "";
                String valueInput = "";
                String keyOutput = "";
                String valueOutput = "";

                while (input.hasNext()) {

                    switch (input.getEventType()) {

                        // The event is a start element <
                        case XMLEvent.START_ELEMENT:

                            if (currentTag.isEmpty()) {
                                currentTag = input.getLocalName();
                            } else {
                                currentTag = currentTag + APEnetUtilities.FILESEPARATOR + input.getLocalName();
                            }

                            if (elements.containsKey(currentTag)) {

                                // The current tag is a tag which content  is needed to be retrieved
                                tagFound = true;
                                log.debug("Retrieving tag: " + currentTag);
                                valueInput = elements.get(currentTag);

                                if (!valueInput.isEmpty()) {

                                    // It is necessary to look for the attributes defined by the user in order to retrieve their content
                                    String[] attributeArray = valueInput.split(SEPARATOR);
                                    List<String> attributeList = new LinkedList<String>(Arrays.asList(attributeArray));

                                    // It is necessary to look for every tag's attribute within the list
                                    for (int x = 0; x < input.getAttributeCount(); x++) {

                                        attributeInput = input.getAttributeLocalName(x);

                                        if (attributeList.contains(input.getAttributeLocalName(x))) {
                                            // There is an attribute defined, then they HashMap key will be "pathToTheElement[nameOfTheAttribute]" 
                                            keyOutput = currentTag + START_ATTRIBUTE + attributeInput + END_ATTRIBUTE;
                                            // The HashMap value is the text within this attribute
                                            valueOutput = input.getAttributeValue(x);

                                            if (results.containsKey(keyOutput)) {

                                                // The results already contain the key which is going to be inserted
                                                // so it is necessary to retrieve the current value/s and add the new one
                                                valueOutput = results.get(keyOutput) + SEPARATOR + valueOutput;
                                            }

                                            results.put(keyOutput, valueOutput);
                                            keyOutput = "";
                                            valueOutput = "";
                                            attributeList.removeAll(Arrays.asList(input.getAttributeLocalName(x)));
                                        }

                                    }

                                    if (attributeList.size() > 0) {
                                        // There are attributes added by the user within the input HashMap elements
                                        // that don't exist in the XML
                                        for (int i = 0; i < attributeList.size(); i++) {
                                            keyOutput = currentTag + START_ATTRIBUTE + attributeList.get(i) + END_ATTRIBUTE;
                                            valueOutput = NO_ATTRIBUTE_FOUND;
                                            results.put(keyOutput, valueOutput);
                                            //log.info("Warning: Reading XML. Attribute " + attributeList.get(i) + " for tag " + currentTag + " has not been found within the XML");
                                            keyOutput = "";
                                            valueOutput = "";
                                        }
                                    }

                                    remainingElements.remove(currentTag);

                                } else {

                                    // It is necessary to retrieve only the tag's content
                                    attributeInput = null;
                                    keyOutput = currentTag;
                                }

                            }

                            break;

                        // The event is the beginning of the text for an element
                        case XMLEvent.CHARACTERS:

                            if (attributeInput == null && tagFound) {
                                // The HashMap value is the text within the element
                                valueOutput = input.getText();

                                if (results.containsKey(keyOutput)) {

                                    // The results already contain the key which is going to be inserted
                                    // so it is necessary to retrieve the current value/s and add the new one
                                    valueOutput = results.get(keyOutput) + SEPARATOR + valueOutput;
                                }

                                results.put(keyOutput, valueOutput);
                                keyOutput = "";
                                valueOutput = "";
                                tagFound = false;
                                remainingElements.remove(currentTag);

                            }

                            break;

                        // The event is the end of the element </
                        case XMLEvent.END_ELEMENT:

                            if (attributeInput == null && tagFound) {
                                // The HashMap value is empty
                                valueOutput = "";

                                if (results.containsKey(keyOutput)) {

                                    // The results already contain the key which is going to be inserted
                                    // so it is necessary to retrieve the current value/s and add the new one
                                    valueOutput = results.get(keyOutput) + SEPARATOR + valueOutput;
                                }

                                results.put(keyOutput, valueOutput);
                                keyOutput = "";
                                tagFound = false;
                                remainingElements.remove(currentTag);

                            }

                            if (currentTag.lastIndexOf(APEnetUtilities.FILESEPARATOR) > 0) {
                                currentTag = currentTag.substring(0, currentTag.lastIndexOf(APEnetUtilities.FILESEPARATOR));
                            }

                            break;

                    }
                    if (input.hasNext()) {
                        input.next();
                    }
                }

                if (remainingElements.size() > 0) {

                    @SuppressWarnings("rawtypes")
                    Collection collection = remainingElements.keySet();

                    // Obtain an Iterator for Collection
                    @SuppressWarnings("rawtypes")
                    Iterator iterator = collection.iterator();

                    if (collection.size() > 0) {

                        // Iterate through HashMap keys iterator
                        while (iterator.hasNext()) {

                            // There are tags that have not been found within the XML
                            keyOutput = (String) iterator.next();
                            valueOutput = NO_TAG_FOUND;
                            results.put(keyOutput, valueOutput);
                            //log.info("Warning: Reading XML. Tag " + keyOutput + " has not been found within the XML");

                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error parsing StAX for file " + XMLPath, e);
        } finally {
            try {
                input.close();
                sfile.close();
            } catch (Exception e) {
                log.error("Error closing streams" + e.getMessage(), e);
            }
        }

        return results;
    }

    public static String removeUnusedCharacters(String input) {
        if (input != null) {
            String result = input.replaceAll("[\t ]+", " ");
            result = result.replaceAll("[\n\r]+", " ");
            return result;
        } else {
            return null;
        }

    }

}
