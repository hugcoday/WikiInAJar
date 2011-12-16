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

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Filter for &lt;nowiki&gt; tags that reinserts previously captured nowiki text.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 *
 */
public class NoWikiInsertFilter implements Filter {

    private Pattern pattern;
    private List    noWikiContent;

    public NoWikiInsertFilter(List noWikiContent) {
        this.pattern       = Pattern.compile(NoWikiCaptureFilter.EMPTY_NOWIKI_TAGS);
        this.noWikiContent = noWikiContent;
    }

    public String filter(String text) {
        for (Iterator iterator = noWikiContent.iterator(); iterator.hasNext();) {
            String content = (String) iterator.next();
            Matcher m = pattern.matcher(text);
            text = m.replaceFirst(content);
        }
        return text;
    }

}
