package eu.apenet.commons.view.jsp;

import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class EnvironmentTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		String dashboardEnv = PropertiesUtil.get(PropertiesKeys.APE_DASHBOARD_ENVIRONMENT);
		String result = "Local development";
		if ("prod".equalsIgnoreCase(dashboardEnv) || "production".equalsIgnoreCase(dashboardEnv)){
			result = "Production";
		}else if ("acc".equalsIgnoreCase(dashboardEnv) || "acceptance".equalsIgnoreCase(dashboardEnv)){
			result = "Acceptance";
		}else if ("test".equalsIgnoreCase(dashboardEnv)){
			result = "Test";
		}else if ("cc".equalsIgnoreCase(dashboardEnv) || "contentchecker".equalsIgnoreCase(dashboardEnv)){
			result = "Content checker";
		}

		this.getJspContext().getOut().write(result);
	}

}
