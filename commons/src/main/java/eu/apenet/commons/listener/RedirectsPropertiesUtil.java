package eu.apenet.commons.listener;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.RedirectsConfig;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class RedirectsPropertiesUtil {

    private static final Logger LOGGER = Logger.getLogger(RedirectsPropertiesUtil.class);
    private static Properties dashboardProperties = new Properties();
    private static Properties defaultProperties = new Properties();

    static {
        try {
            InputStream inputStream = RedirectsPropertiesUtil.class.getClassLoader()
                    .getResourceAsStream("/default.properties");
            defaultProperties.load(inputStream);
            inputStream.close();
        } catch (IOException ioe) {
            LOGGER.error(APEnetUtilities.generateThrowableLog(ioe));
        }
    }

    public static void reload(RedirectsConfig dashboardConfig) {
        String configFilename = dashboardConfig.getConfigPropertiesPath();
        File configFile = new File(configFilename);
        if (configFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(configFile);
                dashboardProperties.load(inputStream);
                inputStream.close();
            } catch (IOException ioe) {
                LOGGER.error(APEnetUtilities.generateThrowableLog(ioe));
            }

        }
    }

    public static String get(String key) {
        String value = getExternal(key);
        if (value == null) {
            value = defaultProperties.getProperty(key);
        }
        return value;
    }

    public static Integer getInt(String key) {
        String value = getExternal(key);
        if (value == null) {
            value = defaultProperties.getProperty(key);
        }
        return Integer.parseInt(value);
    }

    public static Long getLong(String key) {
        String value = getExternal(key);
        if (value == null) {
            value = defaultProperties.getProperty(key);
        }
        return Long.parseLong(value);
    }

    private static String getExternal(String key) {
        return dashboardProperties.getProperty(key);
    }
    
    public static Properties readProperties(String string) throws IOException {
        Properties properties;
        try (StringReader stringReader = new StringReader(string)) {
            properties = new Properties();
            properties.load(stringReader);
        }
        return properties;
    }

    public static String writeProperties(Properties properties) throws IOException {
        String result;
        try (StringWriter stringWriter = new StringWriter()) {
            properties.store(stringWriter, "");
            result = stringWriter.toString();
            stringWriter.flush();
        }
        return result;
    }
}
