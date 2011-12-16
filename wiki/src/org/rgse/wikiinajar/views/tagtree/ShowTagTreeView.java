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

package org.rgse.wikiinajar.views.tagtree;

import java.util.Iterator;

import org.rgse.wikiinajar.helpers.TextUtils;
import org.rgse.wikiinajar.models.ITaggable;
import org.rgse.wikiinajar.models.TagTree;
import org.rgse.wikiinajar.models.Vcard;
import org.rgse.wikiinajar.models.WikiArticle;
import org.rgse.wikiinajar.models.TagTree.TreeItem;
import org.rgse.wikiinajar.views.vcard.ShowVcardView;
import org.rgse.wikiinajar.views.wiki.ShowWikiArticleView;

import net.sf.wikiinajar.xrays.View;
import net.sf.wikiinajar.xrays.Xml;

/**
 * View of a tag tree.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ShowTagTreeView implements View {

	private TagTree tree;

	public ShowTagTreeView(TagTree tree) {
		this.tree = tree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.wikiinajar.xrays.View#toXml()
	 */
	public Xml toXml() {
		Xml xml = new Xml("article");
		xml.tag("id", "tag tree");
		xml = xml.tag("tagtree");
		TreeItem root = tree.getRoot();
		addElements(xml, root, "/tag/tree");
		return xml.up();
	}

	/**
	 * Recursively adds elements to xml structure. The root element is not added
	 * as a separate tag, because it only contains child tags and no wiki
	 * elements.
	 * 
	 * @param xml
	 * @param root
	 */
	private void addElements(Xml xml, TreeItem root, String tagTreeLink) {
		Xml tagRoot = root.isRoot() ? xml : xml.tag("tag").attr("name",
				root.getTag()).attr("link", tagTreeLink);
		if (!root.isRoot()) {
			for (Iterator iter = root.getLeafs().iterator(); iter.hasNext();) {
				ITaggable taggable = (ITaggable) iter.next();
				String id = taggable.getIdentifier();
				String escapedId = TextUtils.escapeHtmlChars(id);
				if (taggable instanceof WikiArticle) {
					tagRoot.tag("taggable", escapedId).attr("link",
							ShowWikiArticleView.getLink(id));
				} else if (taggable instanceof Vcard) {
					tagRoot.tag("taggable", escapedId).attr("link",
							ShowVcardView.getLink(id));
				}
			}
		}
		for (Iterator iter = root.getChildren().iterator(); iter.hasNext();) {
			TreeItem child = (TreeItem) iter.next();
			addElements(tagRoot, child, tagTreeLink + "/" + child.getTag());
		}
	}

	/**
	 * Creates a link to the given serialize tag tree.
	 * 
	 * @param link
	 * @return
	 */
	public static String getLink(String link) {
		if (link.startsWith("/")) {
			return "/tag/tree" + link;
		}
		return "/tag/tree/" + link;
	}

}
