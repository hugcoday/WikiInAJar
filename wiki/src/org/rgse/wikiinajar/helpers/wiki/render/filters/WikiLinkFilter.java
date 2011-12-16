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

package org.rgse.wikiinajar.helpers.wiki.render.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rgse.wikiinajar.helpers.wiki.render.WikiLink;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class WikiLinkFilter implements Filter {

	private String defNamespace;

	public WikiLinkFilter(String defaultNamespace) {
		this.defNamespace = defaultNamespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rgse.render.filter.Filter#filter(java.lang.String)
	 */
	public String filter(String text) {

		String result = text;

		// can't use groups here, did not catch two links on same line
		String regex = "\\[\\[.+?\\]\\]";

		Pattern p = Pattern.compile(regex);

		Matcher m = p.matcher(text);

		int addedChars = 0;
		while (m.find()) {
			String plainLink = m.group();
			WikiLink link = new WikiLink(plainLink.substring(2, plainLink
					.length() - 2), defNamespace);
			String formattedLink = link.toHtml();
			int linkStart = m.start() + addedChars;
			String first = result.substring(0, linkStart);
			String end = result.substring(m.end() + addedChars);

			addedChars += formattedLink.length() - plainLink.length() ;

			result = first + formattedLink + end;
		}
		return result;

	}
}
