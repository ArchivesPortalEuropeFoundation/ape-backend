package eu.archivesportaleurope.commons.config;

import java.util.Properties;

public class ScriptsConfig extends ApePortalAndDashboardConfig {

    private String configPropertiesPath;
    private String contextXmlPath;
    private Properties scriptProperties;

    @Override
    protected void initBeforeFinalize() {
        super.initBeforeFinalize();
    }

    public String getConfigPropertiesPath() {
        return configPropertiesPath;
    }

    public void setConfigPropertiesPath(String configPropertiesPath) {
        checkConfigured();
        this.configPropertiesPath = configPropertiesPath;
    }

    public void setContextXmlPath(String contextXmlPath) {
        this.contextXmlPath = contextXmlPath;
    }

    public String getContextXmlPath() {
        return contextXmlPath;
    }

    public void setScriptProperties(Properties scriptProperties) {
        this.scriptProperties = scriptProperties;
    }

    public Properties getScriptProperties() {
        return scriptProperties;
    }
}
