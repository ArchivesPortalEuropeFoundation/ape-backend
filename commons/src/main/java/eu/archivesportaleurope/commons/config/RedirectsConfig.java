package eu.archivesportaleurope.commons.config;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.apenet.commons.utils.APEnetUtilities;

import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

public class RedirectsConfig extends ApePortalAndDashboardConfig {

    private String configPropertiesPath;

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

}
