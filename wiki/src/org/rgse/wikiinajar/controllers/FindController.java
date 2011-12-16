/*
 * Wiki in a jar
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

package org.rgse.wikiinajar.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.rgse.wikiinajar.models.TagTree;
import org.rgse.wikiinajar.views.tagtree.ShowTagTreeView;

import net.sf.wikiinajar.xrays.ControllerResponse;
import net.sf.wikiinajar.xrays.Request;

/**
 * Deals with full text search.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class FindController {

	private static final String GOOGLE_QUERY = "http://www.google.com/search?q=";
	private static final String QUERY = "query";

	public ControllerResponse pagesAction(Request request) throws IOException {
		String query = request.getProperty(QUERY);
		if (query == null) {
			return request.errorResponse("Empty query");
		}
		query = query.trim();
		if (query.startsWith(".")) {
			return request.externalRedirect(constructExternalQuery(query.substring(1)));
		}
		List searchTokens = split(query, ", /");
		if (searchTokens.isEmpty()) {
			return request.errorResponse("Empty query");
		}
		// Now just handle as tag tree
		TagTree tree = new TagTree(searchTokens, true);
		ShowTagTreeView view = new ShowTagTreeView(tree);
		return request.xmlResponse(view.toXml());
	}

	private String constructExternalQuery(String query) {
		try {
			return GOOGLE_QUERY + URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return GOOGLE_QUERY + query;
		}
	}

	private List split(String text, String delim) {
		StringTokenizer tokens = new StringTokenizer(text, delim);
		List results = new LinkedList();
		while (tokens.hasMoreTokens()) {
			results.add(tokens.nextToken());
		}
		return results;
	}

}
