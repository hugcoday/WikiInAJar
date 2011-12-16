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
import java.util.Arrays;
import java.util.Iterator;

import net.sf.wikiinajar.xrays.ControllerResponse;
import net.sf.wikiinajar.xrays.Request;

import org.rgse.wikiinajar.models.ITaggable;
import org.rgse.wikiinajar.models.TagTree;
import org.rgse.wikiinajar.models.WikiArticle;
import org.rgse.wikiinajar.models.TagTree.TreeItem;
import org.rgse.wikiinajar.views.tab.TabTagView;
import org.rgse.wikiinajar.views.wiki.ShowWikiArticleView;

/**
 * Handles requests from the main menu "tab" bar.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class TabController {

    private static final String WIKI_CONTROLLER = "wiki";

    public ControllerResponse wikiAction(Request request) {
       // return request.redirect(WikiController.class, "show");
    	return  request.redirect("/wiki/show/" + request.getPath().getId());
    }
    
    /**
     * <code>/tab/tag/wiki/Main+Page/1</code>
     * @param request
     * @return
     * @throws IOException 
     */
    public ControllerResponse tagAction(Request request) throws IOException {
        String tag = request.getPath().getId();
        
        TagTree tagTree = new TagTree(Arrays.asList(new String[] {tag}));
            
        WikiArticle article;
        if (request.getPath().getIdsAsList().size() > 1) {
            article = new WikiArticle((String) request.getPath().getIdsAsList().get(1));
        } else {
            String id = getFirstArticleId(tagTree);
            if (id == null) {
                return request.errorResponse("No matching articles for tag: " + tag);
            }
            article = new WikiArticle(id);
        }
        
        ShowWikiArticleView view = new ShowWikiArticleView(article);
        
        TabTagView ttView = new TabTagView(tagTree, tag, view.toXml(), article.getIdentifier());
        return request.xmlResponse(ttView.toXml());
    }

    private String getFirstArticleId(TagTree tagTree) {
        for (Iterator iterator = tagTree.getRoot().getChildren().iterator(); iterator.hasNext();) {
            TreeItem item = (TreeItem) iterator.next();
            for (Iterator iterator2 = item.getLeafs().iterator(); iterator2.hasNext();) {
                ITaggable taggable = (ITaggable) iterator2.next();
                
                if (taggable instanceof WikiArticle) {
                    return taggable.getIdentifier();
                }
            }
        }
        return null;
    }
}
