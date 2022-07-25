package eu.apenet.redirects;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import eu.apenet.redirects.util.RedirectService;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;

public class RedirectServlet extends HttpServlet {

	private static Logger LOGGER = Logger.getLogger(RedirectServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		LOGGER.info("test");
//		response.setContentType("text/html");
//		PrintWriter pw = response.getWriter();
//		pw.println(RedirectService.instance().handleRequest(request));
//		pw.close();

		String newUrl = RedirectService.instance().handleRequest(request);

//		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
//		response.sendRedirect("http://www.in.gr");


		request.setAttribute("newUrl", newUrl);
		request.getRequestDispatcher("/redirectInfo.jsp").forward(request, response);
	}


}
