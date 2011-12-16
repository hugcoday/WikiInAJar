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

package org.rgse.wikiinajar.views.tab;

import java.io.IOException;
import java.util.Iterator;

import org.rgse.wikiinajar.models.ITaggable;
import org.rgse.wikiinajar.models.Settings;
import org.rgse.wikiinajar.models.TagTree;
import org.rgse.wikiinajar.models.WikiArticle;
import org.rgse.wikiinajar.models.TagTree.TreeItem;

import net.sf.wikiinajar.xrays.View;
import net.sf.wikiinajar.xrays.Xml;

/**
 * Combines article view with list of matching articles.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 *
 */
public class TabTagView implements View {

    private Xml articleXml;
    private TagTree tagTree;
    private String articleId;
    private String tag;

    public TabTagView(TagTree tagTree, String tag, Xml articleXml, String articleId) {
        this.articleXml = articleXml;
        this.tagTree = tagTree;
        this.articleId = articleId;
        this.tag = tag;
    }

    /* (non-Javadoc)
     * @see net.sf.wikiinajar.xrays.View#toXml()
     */
    public Xml toXml() {
        addTabMenu(articleXml);
        Xml articleList = articleXml.tag("tag-article-list");
        for (Iterator iterator = tagTree.getRoot().getChildren().iterator(); iterator.hasNext();) {
            TreeItem item = (TreeItem) iterator.next();
            for (Iterator iterator2 = item.getLeafs().iterator(); iterator2.hasNext();) {
                ITaggable taggable = (ITaggable) iterator2.next();
                
                if (taggable instanceof WikiArticle) {
                    WikiArticle article = (WikiArticle) taggable;
                    Xml listEntry = articleList.tag("list-entry").cdata(article.getIdentifier());
                    if (article.getIdentifier().equals(articleId)) {
                        listEntry.attr("selected", "yes");
                    }
                }
            }
        }
        return articleXml;
    }

    private void addTabMenu(Xml articleXml) {
        Xml tagMenu = articleXml.tag("tab-menu");
        try {
            Settings settings = Settings.getSettings();
            for (Iterator iterator = settings.getTagsForMainMenu().iterator(); iterator.hasNext();) {
                String tag = (String) iterator.next();
                
                Xml entry = tagMenu.tag("tab-menu-entry");
                entry.cdata(tag);
                entry.attr("link", "/tab/tag/" + tag);
                if (this.tag.equals(tag)) {
                    entry.attr("selected", "yes");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
