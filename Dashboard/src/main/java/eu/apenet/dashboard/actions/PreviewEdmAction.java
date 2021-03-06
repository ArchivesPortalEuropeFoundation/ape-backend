/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.ead2edm.EAD2EDMConverter;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class PreviewEdmAction extends AbstractInstitutionAction{

    protected Logger logger = Logger.getLogger(getClass());
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String execute() throws Exception{
        Ese ese = getEse(getServletRequest());
        if (ese != null) {

            if (ese.getPathHtml() == null) {
                try {
                    EAD2EDMConverter.generateHtml(ese);
                } catch (TransformerException e) {
                    logger.error(e.getMessage(), e);
                    getServletResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create html files.");
                }
            }
            if (ese.getPathHtml() != null) {
                logger.info(APEnetUtilities.getConfig().getRepoDirPath() + ", " + ese.getPathHtml());
                File file = EdmFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPathHtml());
                if (!file.exists()) {
                    // Do your thing if the file appears to be non-existing.
                    // Throw an exception, or send 404, or show default/warning
                    // page, or just ignore it.
                	getServletResponse().sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                    return ERROR;
                } else {
                    String path = getServletRequest().getParameter("path");
                    logger.info(path);
                    if (StringUtils.isBlank(path)) {
                        write(file, getServletResponse());
                    } else {
                        File parentDir = file.getParentFile();
                        File subHtmlFile = EdmFileUtils.getFile(parentDir, path);
                        if (!subHtmlFile.exists()) {
                            // Do your thing if the file appears to be
                            // non-existing.
                            // Throw an exception, or send 404, or show
                            // default/warning page, or just ignore it.
                        	getServletResponse().sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                            return ERROR;
                        }
                        write(subHtmlFile, getServletResponse());
                    }
                }
            }

        } else {
        	getServletResponse().sendError(HttpServletResponse.SC_BAD_REQUEST, "No parameter id or eseId given.");
            return ERROR;
        }
        return SUCCESS;
    }

    private Ese getEse(HttpServletRequest request) {
        String eseId = request.getParameter("eseId");
        String id1 = request.getParameter("id");
        Ese ese = null;
        EseDAO dao = DAOFactory.instance().getEseDAO();
        if (NumberUtils.isNumber(id1)) {
			List<Ese> eses = dao.getEses(NumberUtils.toInt(getId()), getAiId());
			if (eses.size() > 0) {
				ese = eses.get(0);
			}
        } else if (NumberUtils.isNumber(eseId)) {
            ese = dao.findById(NumberUtils.toInt(eseId));
        }
        return ese;
    }

    protected void write(File file, HttpServletResponse response) throws IOException {
        // Check if file actually exists in filesystem.

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(file.getName());

        // If content type is unknown, then set the default value.
        // For all content types, see:
        // http://www.w3schools.com/media/media_mimeref.asp
        // To add new content types, add new mime-mapping entry in web.xml.
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Init servlet response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

        // Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            // Open streams.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } finally {
            // Gently close streams.
            close(output);
            close(input);
        }
    }

    // Helpers (can be refactored to public utility class)
    // ----------------------------------------
    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail
                // it.
            }
        }
    }
}
