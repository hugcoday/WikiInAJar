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

package org.rgse.wikiinajar.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper methods related to text based articles.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class TextUtils {

	private static final String LINE_SEP = System.getProperty("line.separator",
			"\n");

	public static String readTextFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		StringBuffer buffer = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line).append(LINE_SEP);
		}
		reader.close();
		return buffer.toString();
	}

	public static void writeTextFile(String file, String articleContent)
			throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(file));
		out.println(articleContent);
		out.close();
		if (out.checkError()) {
			throw new IOException("Error saving " + file);
		}
	}

	public static Set extractTags(String text) {
		Set results = new HashSet();
		Pattern p = Pattern.compile("\\[\\[tags:(.+)\\]\\]", Pattern.MULTILINE);
		Matcher m = p.matcher(text);

		while (m.find()) {
			String match = m.group(1);
			StringTokenizer tokens = new StringTokenizer(match, ", ");
			while (tokens.hasMoreTokens()) {
				results.add(tokens.nextToken().toLowerCase());
			}
		}
		return results;

	}

	/**
	 * Replaces '<', '>', and '&' with their HTML escape sequence.
	 * 
	 * @param string
	 * @return
	 */
	public static String escapeHtmlChars(String string) {
		return string.replaceAll("<", "&lt;").replace(">", "&gt;").replace("&",
				"&amp;");
	}

	/**
	 * Encodes given string for safe url characters. Note that this will also
	 * escape the slash, do not use this to encode complete urls.
	 * 
	 * @param url
	 * @return The encoded url or the original url in case of an encoding error.
	 */
	public static String urlEncode(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}

}
