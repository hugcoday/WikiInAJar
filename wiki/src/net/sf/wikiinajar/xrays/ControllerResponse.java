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

import java.io.InputStream;
import java.util.Hashtable;

/**
 * Represents the response of a controller handling a request. A response is
 * basically a mime type and either an {@link Xml} object or an
 * {@link InputStream}.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ControllerResponse {

	private static final String MIME_DEFAULT = "text/plain";

	public static final String MIME_XML = "text/xml";

	public static final String MIME_HTML = "text/html";

	public static final int HTML_NOT_FOUND = 404;

	private static final int HTML_JUST_FINE = 0;

	public static final int HTTP_REDIRECT_FOUND = 302;
	public static final int HTTP_EXTERNAL_REDIRECT_FOUND = -302;

	private static Hashtable mimeTypeByExtension = new Hashtable();
	static {
		mimeTypeByExtension.put(".htm", "text/html");
		mimeTypeByExtension.put(".html", "text/html");
		mimeTypeByExtension.put(".xml", "text/xml");
		mimeTypeByExtension.put(".css", "text/css");
		mimeTypeByExtension.put(".xsl", "text/xsl+xml");
		mimeTypeByExtension.put(".txt", "text/plain");
		mimeTypeByExtension.put(".asc", "text/plain");
		mimeTypeByExtension.put(".gif", "image/gif");
		mimeTypeByExtension.put(".jpg", "image/jpeg");
		mimeTypeByExtension.put(".jpeg", "image/jpeg");
		mimeTypeByExtension.put(".png", "image/png");
		mimeTypeByExtension.put(".mp3", "audio/mpeg");
		mimeTypeByExtension.put(".m3u", "audio/mpeg-url");
		mimeTypeByExtension.put(".pdf", "application/pdf");
		mimeTypeByExtension.put(".doc", "application/msword");
		mimeTypeByExtension.put(".ogg", "application/x-ogg");
		mimeTypeByExtension.put(".zip", "application/octet-stream");
		mimeTypeByExtension.put(".exe", "application/octet-stream");
		mimeTypeByExtension.put(".class", "application/octet-stream");
	}

	private Xml xml;

	private String mime;

	private InputStream stream;

	private String templateFile;

	private int errorCode;

	private String resName;

	private String controller;

    private Class redirectController;

    private String redirectAction;

	protected ControllerResponse(String mime, Xml xml, String templateFile,
			String controller) {
		this(mime, xml, null, templateFile, controller);
	}

	protected ControllerResponse(String mime, InputStream stream) {
		this(mime, null, stream, null, null);
	}

	protected ControllerResponse(String mime, Xml xml, InputStream stream,
			String templateFile, String controller) {
		this.mime = mime;
		this.xml = xml;
		this.stream = stream;
		this.templateFile = templateFile;
		this.controller = controller;
		this.redirectController = null;
	}

	public ControllerResponse(int errorCode, String resName) {
		this.errorCode = errorCode;
		this.resName = resName;
	}

	public ControllerResponse(Class controllerClass, String redirectAction) {
	    this.redirectController = controllerClass;
	    this.redirectAction = redirectAction;
    }

    public String getMime() {
		return mime;
	}

	public InputStream getStream() {
		return stream;
	}

	public Xml getXml() {
		return xml;
	}

	public boolean hasStream() {
		return stream != null;
	}

	/**
	 * Returns mime type for given file.
	 * 
	 * @param fileName
	 * @return The mimetype or 'text/plain' if unknown.
	 */
	public static String getMimeType(String fileName) {
		int index = fileName.lastIndexOf('.');
		String ext = index != -1 ? fileName.substring(index) : fileName;
		String type = (String) mimeTypeByExtension.get(ext);
		return type != null ? type : MIME_DEFAULT;

	}

	protected void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public boolean wasError() {
		return errorCode != HTML_JUST_FINE;
	}

	public String getResourceName() {
		return resName;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getController() {
		return controller;
	}

    /**
     * Checks if this request should be redirected to another controller.
     * 
     * @return <code>true</code> if request needs to be redirected.
     * 
     * @see #getRedirectController()
     */
    public boolean isRedirect() {
        return redirectController != null;
    }

    /**
     * Returns the controller to redirect the request to, if set.
     * 
     * @return the controller or <code>null</code> if not set.
     * 
     * @see #isRedirect()
     */
    public Class getRedirectController() {
        return redirectController;
    }

    /**
     * If this response is a redirect, returns the action to invoke for the
     * redirected controller.
     * 
     * @return action or <code>null</code> if response is not a redirect.
     * 
     * @see #isRedirect()
     * @see #getRedirectController()
     */
    public String getRedirectAction() {
        return redirectAction;
    }
}
