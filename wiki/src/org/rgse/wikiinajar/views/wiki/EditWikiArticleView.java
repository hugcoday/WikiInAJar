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

import org.rgse.wikiinajar.helpers.TextUtils;
import org.rgse.wikiinajar.models.WikiArticle;

import net.sf.wikiinajar.xrays.View;
import net.sf.wikiinajar.xrays.Xml;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class EditWikiArticleView implements View {

	private WikiArticle article;

	public EditWikiArticleView(WikiArticle article) {
		this.article = article;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.wikiinajar.xrays.View#toXml()
	 */
	public Xml toXml() {
		Xml xml = new Xml("article");
		xml.tag("id").cdata(article.getTitle());
		xml.tag("edit-article").tag("editfield").attr("target",
				"/wiki/save/" + TextUtils.urlEncode(article.getTitle())).tag("content").cdata(
				article.getRawContent()).up().tag("content-id"
				).cdata(article.getTitle());
		return xml;
	}
	
	/**
	 * Returns link to edit given page.
	 * @param link
	 * @return
	 */
	public static String getLink(String link) {
		return "/wiki/edit/" + TextUtils.urlEncode(link);
	}

}
