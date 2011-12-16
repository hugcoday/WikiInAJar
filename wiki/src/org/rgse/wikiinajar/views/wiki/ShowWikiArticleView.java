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

package org.rgse.wikiinajar.views.wiki;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.rgse.wikiinajar.helpers.TextUtils;
import org.rgse.wikiinajar.models.WikiArticle;

import net.sf.wikiinajar.xrays.View;
import net.sf.wikiinajar.xrays.Xml;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ShowWikiArticleView implements View {

	private WikiArticle article;

	public ShowWikiArticleView(WikiArticle article) {
		this.article = article;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.wikiinajar.xrays.View#toXml()
	 */
	public Xml toXml() {
		Xml xml = new Xml("article");
		
		addTags(xml.tag("tag-list"), article);
		xml.tag("id").cdata(article.getTitle());
		xml.tag("show-article").tag("content", article.getContent());
		return xml;
	}
	
	private void addTags(Xml xml, WikiArticle article) {
		List sorted = new LinkedList(article.listTags());
		Collections.sort(sorted);
		for (Iterator iter = sorted.iterator(); iter.hasNext();) {
			String tag = (String) iter.next();
			xml.tag("tag", tag);
		}
	}

	/**
	 * Returns a link that points to the page to show the specified
	 * wiki article.
	 * @param identifier
	 * @return
	 */
	public static String getLink(String identifier) {
		return "/wiki/show/" + TextUtils.urlEncode(identifier);
	}

}
