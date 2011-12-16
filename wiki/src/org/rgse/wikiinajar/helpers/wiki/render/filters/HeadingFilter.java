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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rgse.wikiinajar.helpers.wiki.render.LineByLineFilter;




/**
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 */
public class HeadingFilter extends LineByLineFilter {

    private Pattern pattern;

    public HeadingFilter() {
        pattern = Pattern.compile("^=+");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.rgse.render.Filter#render(java.lang.StringBuffer,
     *      java.io.BufferedReader)
     */
    public void render(StringBuffer buffer, BufferedReader reader)
            throws IOException {

        String line = null;
        Matcher matcher = null;
        while ((line = reader.readLine()) != null) {
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                String headingType = matcher.group();
                line = line.trim();
                if (line.endsWith(headingType)) {
                    line = line.replaceAll(headingType, "");
                    buffer.append("<h");
                    buffer.append(headingType.length());
                    buffer.append(">");
                    buffer.append(line);
                    buffer.append("</h");
                    buffer.append(headingType.length());
                    buffer.append(">");
                } else {
                    buffer.append(line);
                }
            } else {
                buffer.append(line);
            }
            buffer.append("\n");
        }
    }

}
