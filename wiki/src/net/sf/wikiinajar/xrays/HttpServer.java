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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class HttpServer extends NanoHTTPD {

	private static final String ACTION_SUFFIX = "Action";

	private static final String CONTENT_MATCHER = "@content@";

	private static final String NO_CACHE = "no-cache";

	private static final String HEADER_NO_CACHE = "cache-directive";

	private static final String DUMP_DIR = System.getProperty("xrays.dump.dir");

	private ActionMapping mapping;

	public HttpServer(ActionMapping mapping, int port, IServerSettings settings)
			throws IOException {
		super(port, settings);
		this.mapping = mapping;
	}

	public Response serve(String uri, String method, Properties header,
			Properties parms) {
		UriPath path = new UriPath(uri);

		Class controller = mapping.getController(path.getController());

		if (controller == null) {
			return getErrorResponse("No controller for uri: " + uri);
		} else {
			return getControllerResponse(controller, path, parms);
		}
	}

	/**
	 * Dynamically creates controller instance and lets it handle the request.
	 * 
	 * @param controller
	 * @param path
	 * @param parms
	 * @return
	 */
	private Response getControllerResponse(Class controller, UriPath path,
			Properties parms) {

		String action = path.getAction() + ACTION_SUFFIX;
		String fullName = ControllerResponse.class.getName() + " "
				+ controller.getName() + "." + action + "("
				+ Request.class.getName() + ")";

		try {
			Object instance = controller.newInstance();
			Method method = controller.getMethod(action,
					new Class[] { Request.class });

			if (!method.getReturnType().equals(ControllerResponse.class)) {
				throw new NoSuchMethodException();
			}
			Request request = new Request(path, parms);
			Object result = method.invoke(instance, new Object[] { request });

			if (result != null) {
				ControllerResponse response = (ControllerResponse) result;

				if (response.wasError()) {
					return handleError(response);
				} else if (response.isRedirect()) {
                    return getControllerResponse(response.getRedirectController(), makeRedirectPath(response.getRedirectController(), response
                            .getRedirectAction(), path.getIds()), parms);
                } else if (response.hasStream()) {
					return new Response(HTTP_OK, response.getMime(), response
							.getStream());
				} else {
					return embedInView(response, path);
				}
			} else {
				return getErrorResponse("Null response from controller: "
						+ fullName);
			}
		} catch (Exception e) {
			return getErrorResponse("Trouble invoking action: " + fullName
					+ " Message: " + e.getMessage()
					+ "\n<h2>Stack Trace</h2>\n<pre>" + stackTrace(e)
					+ "</pre>");
		}
	}

	private UriPath makeRedirectPath(Class redirectController, String redirectAction, String ids) {
        return new UriPath(mapping.getActionName(redirectController), redirectAction, ids);
    }

    private String stackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

	private Response handleError(ControllerResponse response) {
		if (response.getErrorCode() == ControllerResponse.HTTP_REDIRECT_FOUND) {
			Response r = new Response(HTTP_REDIRECT_FOUND, MIME_HTML,
					(InputStream) null);
			r.header.put("Location", encodeUri(response.getResourceName()));
			return r;
		} else if (response.getErrorCode() == ControllerResponse.HTTP_EXTERNAL_REDIRECT_FOUND) {
			Response r = new Response(HTTP_REDIRECT_FOUND, MIME_HTML,
					(InputStream) null);
			r.header.put("Location", response.getResourceName());
			return r;
		} else {
			return getErrorResponse(response.getResourceName());
		}
	}

	/**
	 * Loads the view for the specific controller and action and embeds the
	 * controllers resulting xml.
	 * 
	 * @param response
	 * @return
	 */
	private Response embedInView(ControllerResponse response, UriPath path) {
		String viewName = "./public/views/" + response.getController() + "/"
				+ response.getTemplateFile() + ".xml";
		InputStream is = null;
		try {
			is = new FileInputStream(viewName);
		} catch (FileNotFoundException e1) {
		}

		if (is == null) {
			return getErrorResponse("View not found: " + viewName);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		StringBuffer buffer = new StringBuffer();
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			String fullFile = buffer.toString().replaceFirst(CONTENT_MATCHER,
					Matcher.quoteReplacement(response.getXml().toString()));
			dump(fullFile, path);
			return newNoChacheResponse(HTTP_OK, response.getMime(), fullFile);
		} catch (IOException e) {
			return getErrorResponse("Error reading view file: " + viewName
					+ " Message: " + e.getMessage());
		}
	}

	/**
	 * If a dump directory has been specified, dumps the given content to a
	 * file.
	 * 
	 * @param content
	 * @param path
	 */
	private void dump(String content, UriPath path) {
		if (DUMP_DIR == null || path.getIds() == null) {
			return;
		}
		File file = new File(DUMP_DIR + File.separatorChar
				+ path.getController() + File.separatorChar + path.getAction()
				+ File.separatorChar + path.getIds());
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
			out.println(content);
			out.close();
		} catch (IOException e) {
			// ignore, it's for testing anyway
			e.printStackTrace();
		}
	}

	private Response newNoChacheResponse(String httpStatus, String mime,
			String fullFile) {
		Response response = new Response(httpStatus, mime, fullFile);
		response.header.put(HEADER_NO_CACHE, NO_CACHE);
		return response;
	}

	private Response getErrorResponse(String message) {
		return new Response(HTTP_NOTFOUND, MIME_HTML, message);
	}

	public static class UriPath {

		public static final String DEFAULT_ACTION = "default";

		private String controller;

		private String action;

		private String id;

		private String ids;

		private List idsAsList;

		public UriPath(String uri) {
			if (uri.startsWith("/")) {
				uri = uri.substring(1);
			}
			String[] tokens = uri.split("/");

			this.controller = tokens.length >= 1 ? tokens[0] : null;
			this.action = tokens.length >= 2 ? tokens[1] : DEFAULT_ACTION;
			this.id = tokens.length >= 3 ? tokens[2] : null;
			this.ids = id;

			this.idsAsList = new LinkedList();
			if (id != null) {
				this.idsAsList.add(id);
			}
			for (int i = 3; i < tokens.length; i++) {
				this.ids += "/" + tokens[i];
				idsAsList.add(tokens[i]);
			}

			// reset if empty controller
			this.controller = this.controller.length() == 0 ? null
					: this.controller;
		}

		public UriPath(String controller, String action, String ids) {
		    this(controller + "/" + action + "/" + ids);
        }

        /**
		 * Returns the mapping for the controller (the first element after the
		 * leading slash in a URI.
		 * 
		 * @return The controller mapping as specified in the URI or
		 *         <code>null</code> if root (slash only) was specified.
		 */
		public String getController() {
			return controller;
		}

		/**
		 * Returns the action as specified as the second URI element.
		 * 
		 * @return The action as specified in the URI or {@link #DEFAULT_ACTION}
		 *         if no action was specified.
		 */
		public String getAction() {
			return action;
		}

		/**
		 * Returns the id specified as third element of the URI.
		 * 
		 * @return The id or <code>null</code> if not specified.
		 */
		public String getId() {
			return id;
		}

		/**
		 * Returns all specified ids concatenated by '/';
		 * 
		 * @return All ids or <code>null</code> if no ids specified
		 */
		public String getIds() {
			return ids;
		}

		/**
		 * Sets a new value for the path id (the 3rd component of the url).
		 * 
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * Returns list of ids specified.
		 * 
		 * @return A list of ids. List can be empty, never <code>null</code>.
		 */
		public List getIdsAsList() {
			return idsAsList;
		}
	}

}
