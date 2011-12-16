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

import java.util.ArrayList;

import org.rgse.wikiinajar.helpers.wiki.render.filters.Filter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.HeadingFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.HrFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.IntendedFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.ListFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.SectionFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.StrongFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.StrongerFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.StrongestFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.UrlFilter;
import org.rgse.wikiinajar.helpers.wiki.render.filters.WikiLinkFilter;


/**
 * Maintains a list of registered filters and is responsible for applying all
 * filters to a specified string to render the output.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class RenderEngine {
	private static RenderEngine wikiRenderer;

	private static RenderEngine vcardRenderer;

	private ArrayList filters;

	public static RenderEngine getWikiRenderer() {
		if (wikiRenderer == null) {
			wikiRenderer = new RenderEngine(WikiLink.WIKI_NAMESPACE);
		}
		return wikiRenderer;
	}

	public static RenderEngine getVcardRenderer() {
		if (vcardRenderer == null) {
			vcardRenderer = new RenderEngine(WikiLink.VCARD_NAMESPACE);
		}
		return vcardRenderer;
	}

	public RenderEngine(String defaultNamespace) {
		filters = new ArrayList();
		filters.add(new SectionFilter());
		filters.add(new HeadingFilter());
		filters.add(new HrFilter());
		filters.add(new IntendedFilter());
		filters.add(new UrlFilter());
		filters.add(new ListFilter());
		filters.add(new StrongestFilter()); // note the order
		filters.add(new StrongerFilter());
		filters.add(new StrongFilter());
		filters.add(new WikiLinkFilter(defaultNamespace));

	}

	public String render(String text) {
		for (int i = 0; i < filters.size(); i++) {
			Filter f = (Filter) filters.get(i);
			text = f.filter(text);
		}
		return text;
	}

}
