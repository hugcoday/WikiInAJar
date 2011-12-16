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

package org.rgse.wikiinajar.helpers.vcard;

/**
 * Utilities for decoding a QUOTED-PRINTABLE strings.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class MimeConverter {

    /**
     * Decodes a QUOTED-PRINTABLE string.
     * @param text  The text to decode.
     * @return The decoded text.
     */
    public static String decodeQuotedPrintable(String text) {
        if (text.length() == 0) {
            return text;
        }
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < text.length();) {
            if (text.charAt(i) == '=') {
                if (i + 2 < text.length()
                        && isHex(text.substring(i + 1, i + 3))) {
                    out.append((char) Integer.parseInt(text.substring(i + 1,
                            i + 3), 16));
                    i += 3;
                } else {
                    int j = 1;
                    while (i + j < text.length()
                            && ((text.charAt(i + j) == 32) || (text.charAt(i
                                    + j) == 9))) {
                        // Ignore space and tab
                        j++;
                    }
                    if (i + j >= text.length()) {
                        i += j;
                    } else if ((text.charAt(i + j) == 13)
                            && (text.charAt(i + j + 1) == 10)) {
                        i += j + 2;
                    } else if ((text.charAt(i + j) == 13)
                            || (text.charAt(i + j) == 10)) {
                        i += j + 1;
                    } else {
                        out.append(text.charAt(i++));
                    }
                }
            } else
                out.append(text.charAt(i++));
        }
        return out.toString();
    }

    /**
     * Returns true if c is a hexadecimal character. 0-9 or a-f or A-F.
     * 
     * @param c
     *            The value to check
     * @return True if c is hex character.
     */
    public static boolean isHex(char c) {
        c = Character.toUpperCase(c);
        return (c >= '0' && c <= '9') || (c >= (int) 'A' && c <= (int) 'F');
    }

    public static boolean isHex(String hex) {
        if (hex.length() != 2)
            return false;
        return isHex(hex.charAt(0)) && isHex(hex.charAt(1));
    }

}
