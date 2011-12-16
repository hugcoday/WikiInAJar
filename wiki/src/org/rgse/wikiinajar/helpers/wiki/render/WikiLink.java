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
package org.rgse.wikiinajar.helpers.wiki.render;

import org.rgse.wikiinajar.helpers.TextUtils;
import org.rgse.wikiinajar.models.WikiArticle;
import org.rgse.wikiinajar.models.Image;
import org.rgse.wikiinajar.views.tagtree.ShowTagTreeView;
import org.rgse.wikiinajar.views.vcard.ShowVcardView;
import org.rgse.wikiinajar.views.wiki.EditWikiArticleView;
import org.rgse.wikiinajar.views.wiki.ShowWikiArticleView;

/**
 * Represents a wiki style link. A link has two components: an optional
 * namespace and the required page name. Both are separated by a colon.
 * <p>
 * Additionally, a wiki link can be invisible, meaning it won't be embedded in
 * the actual page (e.g. tags).
 * </p>
 * 
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class WikiLink {

	private static final String NAMESPACE_SEP = ":";

	public static final String WIKI_NAMESPACE = "wiki";

	public static final String VCARD_NAMESPACE = "vcard";
	
	public static final String IMG_NAMESPACE = "img";

	public static final String TAGS_NAMESPACE = "tags";

	public static final String TAG_TREE_NAMESPACE = "tagtree";

	public static final String SIDEBAR_NAMESPACE = "sidebar";

	private String link;

	private String namespace;

	private String htmlLink;

	public WikiLink(String plainLink, String defaultNamespace) {
		//reverse html escaping
		plainLink = plainLink.replaceAll("&amp;", "&");
		String[] tokens = splitByNamespace(plainLink);
		this.namespace = tokens[0] != null && tokens[0].length() > 0 ? tokens[0]
				: defaultNamespace;
		this.link = tokens[1];
		this.htmlLink = determineHtmlLink();
	}

	/**
	 * Based on the namespace, figures out the link. No namespace results in a
	 * wiki link (since there is no link support for vcards yet).
	 * 
	 * @return The link or empty string for hidden links.
	 */
	private String determineHtmlLink() {
		String result = "";
		String encLink = TextUtils.escapeHtmlChars(link);
		if ((WIKI_NAMESPACE.equals(namespace) || VCARD_NAMESPACE
				.equals(namespace))
				&& (containsIllegalChars(namespace) || containsIllegalChars(link))) {
			result = "[[" + encLink
					+ "]] (Link must not contain: \\/:*?\"&lt;&gt;|) ";
		} else if (WIKI_NAMESPACE.equalsIgnoreCase(this.namespace)) {
			if (WikiArticle.exists(link)) {
				result = "<a href=\"" + ShowWikiArticleView.getLink(link)
						+ "\">" + encLink + "</a>";
			} else {
				result = "<a id=\"editlink\" href=\"" + EditWikiArticleView.getLink(link)
						+ "\">" + encLink + "</a>";
			}
		} else if (VCARD_NAMESPACE.equalsIgnoreCase(namespace)) {
			result = "<a href=\"" + ShowVcardView.getLink(link) + "\">" + link
					+ "</a>";

		}  else if (IMG_NAMESPACE.equalsIgnoreCase(namespace)) {
			result = "<IMG src=\"" + "/image/show/" + link + "\">" + link + "</IMG>";
 
        } else if (TAG_TREE_NAMESPACE.equalsIgnoreCase(namespace)) {
			result = "<a href=\"" + ShowTagTreeView.getLink(link) + "\">"
					+ encLink + "</a>";
		} else if (TAGS_NAMESPACE.equalsIgnoreCase(namespace)) {
			// hide tags
		} else if (SIDEBAR_NAMESPACE.equalsIgnoreCase(namespace)) {
			// hide sidebar
		} else {
			result = encLink + " (Unknown namespace: " + namespace + ") "+IMG_NAMESPACE;
		}
		return result;
	}

	/**
	 * Because we use the page title as file name, certain characters are not
	 * allowed. This checks for these characters.
	 * 
	 * @param path
	 * @return <code>true</code> if the path contains characters that are
	 *         illegal in file names.
	 */
	private boolean containsIllegalChars(String path) {
		for (int i = 0; i < path.length(); i++) {
			switch (path.charAt(i)) {
			case '\\':
			case '/':
			case ':':
			case '*':
			case '?':
			case '"':
			case '<':
			case '>':
			case '|':
				return true;
			}
		}
		return false;
	}

	/**
	 * No-op constructor for testing.
	 * 
	 */
	protected WikiLink() {
	}

	protected String[] splitByNamespace(String plainLink) {
		int index = plainLink.indexOf(NAMESPACE_SEP);
		if (index != -1) {
			return new String[] { plainLink.substring(0, index).trim(),
					plainLink.substring(index + 1).trim() };
		} else {
			return new String[] { null, plainLink.trim() };
		}
	}

	/**
	 * Returns the HTML code for this link. If the link is invisible this
	 * returns an empty string.
	 * 
	 * @return
	 */
	public String toHtml() {
		return htmlLink;
	}

	public String getPlainLink() {
		return link;
	}

	public String getNamespace() {
		return namespace;
	}

}
