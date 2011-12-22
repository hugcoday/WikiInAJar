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

package org.rgse.wikiinajar.models;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rgse.wikiinajar.helpers.DateUtils;
import org.rgse.wikiinajar.helpers.TextUtils;
import org.rgse.wikiinajar.helpers.wiki.render.RenderEngine;

/**
 * Simple wiki article based on a text file.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class WikiArticle implements ITaggable {

	public static final String WIKI_SUFFIX = ".wiki";

	private static final String DOC_ROOT = System.getProperty(
			"wikiinajar.docroot", "./public/docs");

	public static final String WIKI_BASE_DIR = DOC_ROOT + "/wiki/";

	private static final int MAX_HISTORY = 3;

	private boolean exists;

	private String fileName;

	private String rawContent;

	private String title;

	private Set tags;

	/**
	 * If the specified file exists, tries to read the contents.
	 * 
	 * @param id
	 * @throws IOException
	 *             If the file exists, but could not be read.
	 */
	public WikiArticle(String id) throws IOException {
		if (id == null || id.length() == 0) {
			throw new NullPointerException();
		}
		this.fileName = WIKI_BASE_DIR + id + WIKI_SUFFIX;
		this.title = id;
		this.rawContent = "";
		this.tags = new HashSet();
		// default tags:
		tags.add("all");
		tags.add("wiki");
		tags.add(id.substring(0, 1).toLowerCase());

		File file = new File(fileName);
		if (file.canRead()) {
			this.rawContent = TextUtils.readTextFile(file);
			this.exists = true;
			this.tags.addAll(TextUtils.extractTags(rawContent));
			this.tags.addAll(extractEventTags());
		} else {
			this.exists = false;
		}
	}

	/**
	 * If this article is an event, the month and year are used as tags
	 * automatically.
	 * 
	 * @return
	 */
	private Collection extractEventTags() {
		List result = new LinkedList();
		Date dueDate = getDueDate();
		if (dueDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dueDate);
			result.add(DateUtils.MONTHS[cal.get(Calendar.MONTH)].toLowerCase());
			result.add(cal.get(Calendar.YEAR) + "");
		}
		return result;
	}

	/**
	 * Open the article with new content. This will overwrite the existing
	 * article after backing up the original version.
	 * 
	 * @param id
	 * @param articleContent
	 * @throws IOException
	 */
	public WikiArticle(String id, String articleContent) throws IOException {
		if (id == null) {
			throw new NullPointerException();
		}
		this.fileName = WIKI_BASE_DIR + id + WIKI_SUFFIX;
		this.title = id;
		this.rawContent = articleContent;
		this.exists = true;

		//rollover(new File(fileName), 0, 1);
		TextUtils.writeTextFile(fileName, rawContent);

	}

	/**
	 * Copies this file's content to a file called 'name'.wiki.1. If the file
	 * exists already it first rolls over this one to '2', etc.
	 * 
	 * @param fromFile
	 * @param fromIndex
	 *            The from index, 0 for article file.
	 * @param toIndex
	 *            to The index to roll over to.
	 * @throws IOException
	 * 
	 */
	private void rollover(File fromFile, int fromIndex, int toIndex)
			throws IOException {
		if (toIndex > MAX_HISTORY || !fromFile.exists()) {
			return;
		}
		File toFile = new File(fromFile + "." + toIndex);
		if (toFile.exists()) {
			rollover(fromFile, toIndex, toIndex + 1);
		}
		TextUtils.writeTextFile(toFile.getAbsolutePath(), TextUtils
				.readTextFile(new File(fromFile.getAbsoluteFile()
						+ (fromIndex > 0 ? "." + fromIndex : ""))));
	}

	public String getContent() {
		return "<div>"
				+ RenderEngine.getWikiRenderer().render(
						TextUtils.escapeHtmlChars(rawContent)) + "</div>";
	}

	public boolean doesExist() {
		return exists;
	}

	public String getFileName() {
		return fileName;
	}

	public String getRawContent() {
		return rawContent;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * Lists all wiki articles.
	 * 
	 * @return A list of instances of this class. List may be empty.
	 * 
	 * @throws IOException
	 */
	public static List list() throws IOException {
		File[] files = new File(WIKI_BASE_DIR).listFiles();
		List result = new LinkedList();
		if (files == null) {
			return result;
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String name = file.getName();
			if (name.endsWith(WIKI_SUFFIX)) {
				result.add(new WikiArticle(name.substring(0, name.length()
						- WIKI_SUFFIX.length())));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.webapp.models.ITaggable#isTagged(java.util.List, boolean)
	 */
	public boolean isTagged(List tagList, boolean matchFullText) {
		for (Iterator iter = tagList.iterator(); iter.hasNext();) {
			String tag = (String) iter.next();
			if (isTagged(tag, matchFullText)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.webapp.models.ITaggable#isTagged(java.lang.String, boolean)
	 */
	public boolean isTagged(String tag, boolean matchFullText) {
		tag = tag.toLowerCase();
		return tags.contains(tag)
				|| (matchFullText && (rawContent.toLowerCase().contains(
						tag) || title.toLowerCase().contains(tag)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.webapp.models.ITaggable#getIdentifier()
	 */
	public String getIdentifier() {
		return getTitle();
	}

	/**
	 * Returns <code>true</code> if a wiki article with the given name exists.
	 * 
	 * @param id
	 * @return
	 */
	public static boolean exists(String id) {
		String fullName = WIKI_BASE_DIR + id + WIKI_SUFFIX;
		return new File(fullName).isFile();
	}

	/**
	 * Returns list with all tags for this article.
	 * 
	 * @return
	 */
	public Set listTags() {
		return tags;
	}

	/**
	 * Determines if the title contains a date and returns it.
	 * 
	 * @return the date or <code>null</code> if no date could be extracted.
	 */
	public Date getDueDate() {
		return extractDueDate(title);
	}

	/**
	 * Determines if the title contains a date and returns it.
	 * 
	 * @param title
	 *            the title
	 * @return the date or <code>null</code> if no date could be extracted.
	 */
	protected Date extractDueDate(String title) {
		if (title == null) {
			return null;
		}
		Pattern p = Pattern.compile(".*(\\d{4})-(\\d{2})-(\\d{2}).*");
		Matcher m = p.matcher(title);

		Date result = null;
		if (m.find()) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, toInt(m.group(1)));
			cal.set(Calendar.MONTH, toInt(m.group(2)) - 1);
			cal.set(Calendar.DAY_OF_MONTH, toInt(m.group(3)));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			result = cal.getTime();
		}
		return result;
	}

	private int toInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

}
