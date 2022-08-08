package eu.apenet.commons.listener;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.RedirectsConfig;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Locale;

public class RedirectsConfigListener extends ApePortalAndDashboardConfigListener {

    private static final String REDIRECT_CONFIG_PROPERTIES_PATH = "REDIRECT_CONFIG_PROPERTIES_PATH";

    @Override
    public void contextInitializedInternal(ServletContext servletContext) {
        try {
            Locale.setDefault(Locale.UK);
            RedirectsConfig apeConfig = new RedirectsConfig();
            init(servletContext, apeConfig);
            apeConfig.finalizeConfigPhase();
            APEnetUtilities.setConfig(apeConfig);
        } catch (RuntimeException e) {
            log.fatal("Fatal error while initializing: " + e.getMessage(), e);
            throw e;
        }
    }

    protected void init(ServletContext servletContext, RedirectsConfig config) {

        String configProperties = servletContext.getInitParameter(REDIRECT_CONFIG_PROPERTIES_PATH);
        if (StringUtils.isBlank(configProperties)) {
            config.setConfigPropertiesPath("/ape/liferay/tomcat-base/conf/redirect.properties");
        } else {
            config.setConfigPropertiesPath(configProperties);
        }
        RedirectsPropertiesUtil.reload(config);

        super.init(servletContext, config);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
