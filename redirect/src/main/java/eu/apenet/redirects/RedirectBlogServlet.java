package eu.apenet.redirects;

import eu.apenet.redirects.util.RedirectService;
import eu.apenet.redirects.util.Redirection;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class RedirectBlogServlet extends HttpServlet {

	private static Logger LOGGER = Logger.getLogger(RedirectBlogServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		LOGGER.info("test");
//		response.setContentType("text/html");
//		PrintWriter pw = response.getWriter();
//		pw.println(RedirectService.instance().handleRequest(request));
//		pw.close();

		Redirection redirection = RedirectService.instance().handleRequest(request, Redirection.REDIRECTION_TYPE_BLOG);

		if (redirection.isPassThrough()) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.sendRedirect(redirection.getNewUrl());
		}
		else {
			request.setAttribute("redirection", redirection);
			Locale locale = new Locale(redirection.getLocale() != null ? redirection.getLocale() : "en");
			request.setAttribute("locale", locale);
			request.getSession().setAttribute("javax.servlet.jsp.jstl.fmt.locale.session", locale.toString());
			request.getRequestDispatcher("/redirectInfo.jsp").forward(request, response);
		}
	}


}
