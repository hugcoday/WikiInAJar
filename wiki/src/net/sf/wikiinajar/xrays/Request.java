/*
 * XRays Web Framework
 * 
 * Copyright (C) 2007 rico_g AT users DOT sourceforge DOT net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License s published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 */

package net.sf.wikiinajar.xrays;

import java.io.FileInputStream;
import java.util.Properties;

import net.sf.wikiinajar.xrays.HttpServer.UriPath;

/**
 * Contains information about the client's request. Also acts as a factory class
 * to generate various controller responses.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class Request {

	private static final String ERROR_VIEW = "error";

	private static final String COMMON_CONTROLLER = "common";

	private Properties properties;

	private UriPath path;

	private String templateFile;

	protected Request(UriPath path, Properties properties) {
		this.path = path;
		this.properties = properties;
		this.templateFile = path.getAction();
	}

	public ControllerResponse forward(ControllerResponse response,
			String templateFile) {
		response.setTemplateFile(templateFile);
		return response;
	}

	public ControllerResponse xmlResponse(Xml xml) {
		return new ControllerResponse(ControllerResponse.MIME_XML, xml,
				templateFile, path.getController());
	}

	public ControllerResponse xmlResponse(Xml xml, String templateFile) {
		return new ControllerResponse(ControllerResponse.MIME_XML, xml,
				templateFile, path.getController());
	}

	public ControllerResponse streamResponse(String resName,
			FileInputStream stream) {
		return new ControllerResponse(ControllerResponse.getMimeType(resName),
				stream);
	}

	public UriPath getPath() {
		return path;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public Properties getProperties() {
		return properties;
	}

	public ControllerResponse resourceNotFoundResponse(String resName) {
		return new ControllerResponse(ControllerResponse.HTML_NOT_FOUND,
				resName);
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public ControllerResponse errorResponse(String message) {
		Xml xml = new Xml("error");
		xml.tag("message", message);
		return new ControllerResponse(ControllerResponse.MIME_XML, xml,
				ERROR_VIEW, COMMON_CONTROLLER);
	}

	/**
	 * Creates redirect to local page. Performs URL encoding on given url.
	 * 
	 * @param url
	 * @return
	 */
	public ControllerResponse redirect(String url) {
		return new ControllerResponse(ControllerResponse.HTTP_REDIRECT_FOUND,
				url);
	}

	/**
	 * Creates redirect to external web page. No URL encoding will be performed
	 * on the given URL.
	 * 
	 * @param url
	 * @return
	 */
	public ControllerResponse externalRedirect(String url) {
		return new ControllerResponse(
				ControllerResponse.HTTP_EXTERNAL_REDIRECT_FOUND, url);
	}

    /**
     * Creates a new redirect to a controller.
     * 
     * @param controllerClass
     *            The controller class.
     * @param action
     *            The name of the action to invoke for the redirected
     *            controller.
     * @return
     */
    public ControllerResponse redirect(Class controllerClass, String action) {
        return new ControllerResponse(controllerClass, action);
    }
}
