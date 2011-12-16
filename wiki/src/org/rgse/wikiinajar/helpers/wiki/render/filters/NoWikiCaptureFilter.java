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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Filter for &lt;nowiki&gt; tags that captures and removes all content between nowiki tags.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 *
 */
public class NoWikiCaptureFilter implements Filter {

    protected static final String EMPTY_NOWIKI_TAGS  = "&lt;nowiki&gt;&lt;/nowiki&gt;";
    protected static final String CAPTURE_REGEX      = "&lt;nowiki&gt;(.*?)&lt;/nowiki&gt;";
    
    private Pattern pattern;
    private List    capturedContent;

    /**
     * Creates a new nowiki filter that captures all nowiki escaped text and
     * puts it in the given list.
     * 
     * @param capturedContent
     *            List will be cleared during this process.
     */
    public NoWikiCaptureFilter(List capturedContent) {
        this.pattern = Pattern.compile(CAPTURE_REGEX, Pattern.DOTALL);
        this.capturedContent = capturedContent;
    }
    
    public String filter(String text) {
        // start fresh:
        capturedContent.clear();
        Matcher matcher = pattern.matcher(text);
        
        while(matcher.find()) {
            String nowiki = matcher.group(1);
            capturedContent.add(nowiki);
        }
        text = matcher.replaceAll(EMPTY_NOWIKI_TAGS);
        return text;
    }
}
