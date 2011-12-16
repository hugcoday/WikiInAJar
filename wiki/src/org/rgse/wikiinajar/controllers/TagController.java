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
import java.util.List;

import org.rgse.wikiinajar.models.TagTree;
import org.rgse.wikiinajar.views.tagtree.ShowTagTreeView;


import net.sf.wikiinajar.xrays.ControllerResponse;
import net.sf.wikiinajar.xrays.Request;
import net.sf.wikiinajar.xrays.HttpServer.UriPath;

/**
 * Deals with tags and trees.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class TagController {

	public ControllerResponse treeAction(Request request) throws IOException {
		UriPath path = request.getPath();

		List tags = path.getIdsAsList();
		if (tags.isEmpty()) {
			return request.errorResponse("No tags specified");
		}

		TagTree tree = new TagTree(tags);
		ShowTagTreeView view = new ShowTagTreeView(tree);
		return request.xmlResponse(view.toXml());
	}

}
