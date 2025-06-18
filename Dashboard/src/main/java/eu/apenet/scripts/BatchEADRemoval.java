package eu.apenet.scripts;

import com.google.gson.JsonObject;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.scripts.utils.Statistics;
import eu.archivesportaleurope.commons.config.ScriptsConfig;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class BatchEADRemoval {

    private final Logger log = Logger.getLogger(getClass());

    public static void main (String[] args) throws IOException, ParseException {
        System.out.println("Hello from CLI world!");

        CommandLineParser commandLineParser = new GnuParser();
        Options options = makeOptions();

        CommandLine commandLine = commandLineParser.parse(options, args);
        System.out.println("Params: " + args.toString());

        String fileWithIDs = null;
        if (commandLine.hasOption('f')){
            fileWithIDs = commandLine.getOptionValue('f');
        }
        Integer aiId = null;
        if (commandLine.hasOption('i')){
            aiId = Integer.parseInt(commandLine.getOptionValue('i'));
        }

        BatchEADRemoval batchEADRemoval = new BatchEADRemoval();
        batchEADRemoval.doTheJob(aiId, fileWithIDs);

    }

    private static Options makeOptions() {
        Options options = new Options();
        options.addOption("f", "file", true,
                "The file with the IDs to be deleted");

        options.addOption("i", "aiId", true,
                "The ID of the archival institution");

        return options;
    }

    private List<Integer> readBatchIDs(String fileWithIDs){

        List<Integer> validIDs = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileWithIDs));
            String line = reader.readLine();

            while (line != null) {
                // read next line
                if (line.trim().length()>0) {
//                    System.out.println("Adding:SSS" + line.trim()+"SSS --> " + line.trim().length());
                    validIDs.add(Integer.parseInt(line.trim()));
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return validIDs;
    }

    private void doTheJob(Integer aiId, String fileWithIDs) throws IOException {
        List<Integer> ids = readBatchIDs(fileWithIDs);

        EadService.addBatchToQueue(ids, aiId, XmlType.EAD_FA, QueueAction.DELETE, new Properties());
    }
}
