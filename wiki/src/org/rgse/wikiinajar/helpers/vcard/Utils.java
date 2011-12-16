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

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Helper for dealing with vCards.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class Utils {

	/**
	 * Splits a string. Similar to the split function but uses a single
	 * character delimiter and ignores delimiters in double quotes.
	 * 
	 * @param delim
	 *            The delimiter
	 * @param s
	 *            The string to split
	 * @param n
	 *            How many results should be found. If 0 string will be split
	 *            into all possible sub strings. If > 0 splitting stops after n
	 *            results.
	 * @return ArrayList containing strings.
	 */
	public static ArrayList splitQuotedString(char delim, String s, int n) {
		StringBuffer buffer = new StringBuffer(s);

		boolean quote = false;
		int len = buffer.length();
		for (int i = 0; i < len && (n == 0 || n > 1); i++) {
			char c = buffer.charAt(i);
			if (c == '"') {
				quote = !quote;
			} else if (!quote && c == delim) {
				buffer.setCharAt(i, (char) 0);
				if (n > 0) {
					n--;
				}
			}
		}
		StringTokenizer toky = new StringTokenizer(buffer.toString(),
				new String(new char[] { 0 }));
		ArrayList result = new ArrayList();
		while (toky.hasMoreElements())
			result.add(toky.nextToken());
		return result;
	}

}
