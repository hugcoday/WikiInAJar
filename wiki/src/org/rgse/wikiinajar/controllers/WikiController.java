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

import org.rgse.wikiinajar.models.WikiArticle;
import org.rgse.wikiinajar.views.wiki.EditWikiArticleView;
import org.rgse.wikiinajar.views.wiki.ShowWikiArticleView;

import net.sf.wikiinajar.xrays.ControllerResponse;
import net.sf.wikiinajar.xrays.Request;

/**
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class WikiController {

	private static final String HOME_PAGE = "Main Page";
	private static final String ARTICLE_CONTENT = "article-content";

	public ControllerResponse defaultAction(Request request) throws Exception {
		// Forward to show main page:
		request.getPath().setId(HOME_PAGE);
		return request.forward(showAction(request), "home");
	}

	public ControllerResponse showAction(Request request) throws IOException {

		WikiArticle article = new WikiArticle(request.getPath().getId());
		if (article.doesExist()) {
			ShowWikiArticleView view = new ShowWikiArticleView(article);
			if (article.getTitle().equals(HOME_PAGE)) {
				return request.xmlResponse(view.toXml(), "home");
			} else {
				return request.xmlResponse(view.toXml());
			}
		} else {
			return request.errorResponse("Page not found "
					+ request.getPath().getId());
		}
	}

	public ControllerResponse editAction(Request request) throws IOException {
		WikiArticle article = new WikiArticle(request.getPath().getId());
		EditWikiArticleView view = new EditWikiArticleView(article);
		return request.xmlResponse(view.toXml());
	}

	public ControllerResponse saveAction(Request request) throws IOException {
		String articleContent = request.getProperty(ARTICLE_CONTENT);
		if (articleContent == null) {
			return request.errorResponse("No content posted.");
		} else {
			WikiArticle article = new WikiArticle(request.getPath().getId(),
					articleContent.trim());
			return request
					.redirect(article.getTitle().equals(HOME_PAGE) ? "/wiki/"
							: "/wiki/show/" + request.getPath().getId());
		}
	}

}
