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

/**
 * Filter for certain HTML tags.
 * @author rico_g AT users DOT sourceforge DOT net
 *
 */
public class HtmlTagsFilter implements Filter {

    private final static String[] ALLOWED_HTML_TAGS = { "code", "tt", "pre"};
    
    public String filter(String text) {
        for (int i = 0; i < ALLOWED_HTML_TAGS.length; i++) {
            text = text.replaceAll("&lt;" + ALLOWED_HTML_TAGS[i] + "&gt;", "<" + ALLOWED_HTML_TAGS[i] + ">");
            text = text.replaceAll("&lt;/" + ALLOWED_HTML_TAGS[i] + "&gt;", "</" + ALLOWED_HTML_TAGS[i] + ">");
        }
        return text;
    }

}
