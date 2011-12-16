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

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ListFilter implements Filter {
   
    /*
     * (non-Javadoc)
     * 
     * @see org.rgse.render.filter.Filter#filter(java.lang.String)
     */
    public String filter(String text) {

        String result = text;

        String regex = "(^([-#*]+|[-#*]*\\.)([^\r\n]+)[\r\n]*)+";
        // String regex = "^==.*";

        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);

        Matcher m = p.matcher(text);

        while (m.find()) {
            String plainList = m.group();
            String formattedList = generateList(plainList);
            int listStart = result.indexOf(plainList);
            String first = result.substring(0, listStart);
            String end = result.substring(listStart + plainList.length());

            result = first + formattedList + end;
        }
        return result;

    }

    /**
     * @param string
     * @return
     */
    private static String generateList(String text) {
        String[] lines = text.split("\n");
        StringBuffer buffer = new StringBuffer();
        Stack openTypes = new Stack();
        int level = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            int lineLevel = extractLevel(line);
            String curOpenType = extractOpenType(line);
            if (lineLevel == 0) {
                // not a list line
                if (level > 0) {
                    multiAppend(buffer, "</" + openTypes.peek() + "l>\n",
                            openTypes, null, level);
                    level = 0;
                }
            } else if (lineLevel > level) {
                multiAppend(buffer, "<" + curOpenType + "l>\n", openTypes,
                        curOpenType, lineLevel - level);
                level = lineLevel;
                // openTypes.push(curOpenType);
            } else if (lineLevel < level) {
                multiAppend(buffer, "</" + openTypes.peek() + "l>\n",
                        openTypes, null, level - lineLevel);
                level = lineLevel;
            }
            if (lineLevel > 0) {
                if (!curOpenType.equals(openTypes.peek())) {
                    buffer.append("\t</" + openTypes.pop() + "l><"
                            + curOpenType + "l>\n");
                    openTypes.push(curOpenType);
                }
                buffer.append("\t<li>" + line.substring(level).trim()
                        + "</li>\n");
            }
        }
        // close all open lists:
        if (openTypes.size() > 0)
            multiAppend(buffer, "</" + openTypes.peek() + "l>\n", openTypes,
                    null, level);

        return buffer.toString();
    }

    /**
     * @param line
     * @return
     */
    private static String extractOpenType(String line) {
        if (line.length() == 0)
            return "";
        switch (line.charAt(0)) {
        case '*':
            return "u";
        case '-':
            return "u";
        case '#':
            return "o";
        default:
            return "u";
        }
    }

    /**
     * @param buffer
     * @param string
     * @param i
     */
    private static void multiAppend(StringBuffer buffer, String string,
            Stack openTypes, String curOpenType, int count) {
        for (int i = 0; i < count; i++) {
            buffer.append(string);
            if (curOpenType != null)
                openTypes.push(curOpenType);
            else
                openTypes.pop();
        }
    }

    /**
     * @param line
     * @return
     */
    private static int extractLevel(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (!isListChar(line.charAt(i)) || i == line.length() - 1)
                return i;
        }
        return 0;
    }

    /**
     * @param c
     * @return
     */
    private static boolean isListChar(char c) {
        return c == '*' || c == '#' || c == '-';
    }

}
