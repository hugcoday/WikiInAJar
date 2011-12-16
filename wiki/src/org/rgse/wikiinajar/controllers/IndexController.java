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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.rgse.wikiinajar.models.ITaggable;
import org.rgse.wikiinajar.models.TagTree;
import org.rgse.wikiinajar.models.Vcard;
import org.rgse.wikiinajar.models.WikiArticle;
import org.rgse.wikiinajar.views.tagtree.ShowTagTreeView;

import net.sf.wikiinajar.xrays.ControllerResponse;
import net.sf.wikiinajar.xrays.Request;

/**
 * Supports the indexes for the different content types.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class IndexController {

	public ControllerResponse wikiAction(Request request) throws IOException {
		List sortedInitials = getSortedInitials(WikiArticle.list());
		List tags = new LinkedList();
		tags.add("wiki");
		addInitials(tags, sortedInitials);

		TagTree tree = new TagTree(tags);
		ShowTagTreeView view = new ShowTagTreeView(tree);
		return request.xmlResponse(view.toXml());
	}

	public ControllerResponse vcardAction(Request request) throws IOException {
		List sortedInitials = getSortedInitials(Vcard.list());
		List tags = new LinkedList();
		tags.add("vcard");
		addInitials(tags, sortedInitials);

		TagTree tree = new TagTree(tags);
		ShowTagTreeView view = new ShowTagTreeView(tree);
		return request.xmlResponse(view.toXml());
	}

	public ControllerResponse allAction(Request request) throws IOException {
		List sortedInitials = getSortedInitials(WikiArticle.list());
		List tags = new LinkedList();
		tags.add("wiki");
		addInitials(tags, sortedInitials);
		
		tags.add(TagTree.PARENT_DIRECTIVE);
		tags.add("vcard");
		
		sortedInitials = getSortedInitials(Vcard.list());
		addInitials(tags, sortedInitials);

		TagTree tree = new TagTree(tags);
		ShowTagTreeView view = new ShowTagTreeView(tree);
		return request.xmlResponse(view.toXml());
	}

	private void addInitials(List tags, List sortedInitials) {
		for (Iterator iter = sortedInitials.iterator(); iter.hasNext();) {
			String initial = (String) iter.next();
			tags.add(initial);
			tags.add(TagTree.PARENT_DIRECTIVE);
		}
	}

	private List getSortedInitials(List list) {
		Set initials = new HashSet();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ITaggable taggable = (ITaggable) iter.next();
			initials
					.add(taggable.getIdentifier().substring(0, 1).toLowerCase());
		}
		List result = new LinkedList(initials);
		Collections.sort(result);
		return result;
	}

}
