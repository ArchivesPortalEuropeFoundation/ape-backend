package eu.apenet.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.ScriptsConfig;
import hthurow.tomcatjndi.TomcatJNDI;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import javax.naming.NamingException;

/**
 * A DSpace script launcher.
 *
 * @author Stuart Lewis
 * @author Mark Diggory
 */
public class ScriptLauncher
{
   /**
     * Execute the APE script launcher
     *
     * @param args Any parameters required to be passed to the scripts it executes
     * @throws IOException if IO error
     * @throws FileNotFoundException if file doesn't exist
     */
    public static void main(String[] args)
            throws FileNotFoundException, IOException, NamingException {

        //Configuration
        ScriptsConfig scriptsConfig = new ScriptsConfig();

        String configProperties = System.getProperty("script.properties");
        if (!StringUtils.isBlank(configProperties)) {
            scriptsConfig.setConfigPropertiesPath(configProperties);
        }

        File configFile = new File(configProperties);
        if (configFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(configFile);
                Properties scriptProperties = new Properties();
                scriptProperties.load(inputStream);
                inputStream.close();

                if (scriptProperties.containsKey("REPOSITORY_DIR_PATH")){
                    scriptsConfig.setRepoDirPath(scriptProperties.getProperty("REPOSITORY_DIR_PATH"));
                }
                if (scriptProperties.containsKey("CONTEXT_XML_FILE")){
                    scriptsConfig.setContextXmlPath(scriptProperties.getProperty("CONTEXT_XML_FILE"));
                }
            } catch (IOException ioe) {
            }
        }
        scriptsConfig.finalizeConfigPhase();
        APEnetUtilities.setConfig(scriptsConfig);



        // Initialise the datasource
//        DatabaseConfigurator.getInstance().init();
//        DataSource dataSource = DatabaseConfigurator.getInstance().getCurrentDataSource();
//        DatabaseConfigurator.getInstance().getCurrenJdbcTemplate();
//        JpaUtil.init();
        //for the above to work, the following 3 pom.xml dependencies need to be declared for runtime and not test
        //spring-jdbc
        //spring-test
        //database-mock

        TomcatJNDI tomcatJNDI = new TomcatJNDI();
        tomcatJNDI.processContextXml(new File(((ScriptsConfig)APEnetUtilities.getConfig()).getContextXmlPath()));
        tomcatJNDI.start();



        // Load up the ScriptLauncher's configuration
        Document commandConfigs = getConfig();

        // Check that there is at least one argument (if not display command options)
        if (args.length < 1)
        {
            System.err.println("You must provide at least one command argument");
            display(commandConfigs);
            System.exit(1);
        }

        // Look up command in the configuration, and execute.
        int status;
        status = runOneCommand(commandConfigs, args);

        System.exit(status);
    }

    /**
     * Recognize and execute a single command.
     * @param doc Document
     * @param args arguments
     */
    static int runOneCommand(Document commandConfigs, String[] args)
    {
        String request = args[0];
        Element root = commandConfigs.getRootElement();
        List<Element> commands = root.getChildren("command");
        Element command = null;
        for (Element candidate : commands)
        {
            if (request.equalsIgnoreCase(candidate.getChild("name").getValue()))
            {
                command = candidate;
                break;
            }
        }

        if (null == command)
        {
            // The command wasn't found
            System.err.println("Command not found: " + args[0]);
            display(commandConfigs);
            return 1;
        }

        // Run each step
        List<Element> steps = command.getChildren("step");
        for (Element step : steps)
        {
            // Instantiate the class
            Class target = null;

            // Is it the special case 'dsrun' where the user provides the class name?
            String className;
            if ("dsrun".equals(request))
            {
                if (args.length < 2)
                {
                    System.err.println("Error in launcher.xml: Missing class name");
                    return 1;
                }
                className = args[1];
            }
            else {
                className = step.getChild("class").getValue();
            }
            try
            {
                target = Class.forName(className,
                        true,
                        Thread.currentThread().getContextClassLoader());
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Error in launcher.xml: Invalid class name: " + className);
                return 1;
            }

            // Strip the leading argument from the args, and add the arguments
            // Set <passargs>false</passargs> if the arguments should not be passed on
            String[] useargs = args.clone();
            Class[] argTypes = {useargs.getClass()};
            boolean passargs = true;
            if ((step.getAttribute("passuserargs") != null) &&
                    ("false".equalsIgnoreCase(step.getAttribute("passuserargs").getValue())))
            {
                passargs = false;
            }
            if ((args.length == 1) || (("dsrun".equals(request)) && (args.length == 2)) || (!passargs))
            {
                useargs = new String[0];
            }
            else
            {
                // The number of arguments to ignore
                // If dsrun is the command, ignore the next, as it is the class name not an arg
                int x = 1;
                if ("dsrun".equals(request))
                {
                    x = 2;
                }
                String[] argsnew = new String[useargs.length - x];
                for (int i = x; i < useargs.length; i++)
                {
                    argsnew[i - x] = useargs[i];
                }
                useargs = argsnew;
            }

            // Add any extra properties
            List<Element> bits = step.getChildren("argument");
            if (step.getChild("argument") != null)
            {
                String[] argsnew = new String[useargs.length + bits.size()];
                int i = 0;
                for (Element arg : bits)
                {
                    argsnew[i++] = arg.getValue();
                }
                for (; i < bits.size() + useargs.length; i++)
                {
                    argsnew[i] = useargs[i - bits.size()];
                }
                useargs = argsnew;
            }

            // Run the main() method
            try
            {
                Object[] arguments = {useargs};

                // Useful for debugging, so left in the code...
                /**System.out.print("About to execute: " + className);
                 for (String param : useargs)
                 {
                 System.out.print(" " + param);
                 }
                 System.out.println("");**/

                Method main = target.getMethod("main", argTypes);
                main.invoke(null, arguments);

            }
            catch (Exception e)
            {
                // Exceptions from the script are reported as a 'cause'
                Throwable cause = e.getCause();
                System.err.println("Exception: " + cause.getMessage());
                cause.printStackTrace();
                return 1;
            }
        }

        // Everything completed OK
        return 0;
    }

    /**
     * Load the launcher configuration file
     *
     * @return The XML configuration file Document
     */
    protected static Document getConfig()
    {
        // Load the launcher configuration file
        String config = "launcher.xml";
        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc = null;
        try
        {
            doc = saxBuilder.build(config);
        }
        catch (Exception e)
        {
            System.err.println("Unable to load the launcher configuration file: launcher.xml");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return doc;
    }

    /**
     * Display the commands that the current launcher config file knows about
     * @param commandConfigs configs as Document
     */
    private static void display(Document commandConfigs)
    {
        // List all command elements
        List<Element> commands = commandConfigs.getRootElement().getChildren("command");

        // Sort the commands by name.
        // We cannot just use commands.sort() because it tries to remove and
        // reinsert Elements within other Elements, and that doesn't work.
        TreeMap<String, Element> sortedCommands = new TreeMap<>();
        for (Element command : commands)
        {
            sortedCommands.put(command.getChild("name").getValue(), command);
        }

        // Display the sorted list
        System.out.println("Usage: dspace [command-name] {parameters}");
        for (Element command : sortedCommands.values())
        {
            System.out.println(" - " + command.getChild("name").getValue() +
                    ": " + command.getChild("description").getValue());
        }
    }
}
