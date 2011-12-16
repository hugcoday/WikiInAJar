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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import org.rgse.wikiinajar.helpers.DateUtils;
import org.rgse.wikiinajar.helpers.TextUtils;
import org.rgse.wikiinajar.helpers.vcard.MimeConverter;
import org.rgse.wikiinajar.helpers.vcard.Utils;
import org.rgse.wikiinajar.helpers.wiki.render.RenderEngine;

/**
 * Represents a single instance of a vCard. The vCard does not necessarily have
 * to exist.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class Vcard implements ITaggable {

	private static final String VCARD_SUFFIX = ".vcf";

	private static final String DOC_ROOT = System.getProperty(
			"wikiinajar.docroot", "./public/docs");

	private static final String VCARD_BASE_DIR = DOC_ROOT + "/vcard";

	private File file;

	private List properties;

	private Set tags;

	private String id;

	private String rawContent;

	public Vcard(String id) throws IOException {
		if (id == null || id.length() == 0) {
			throw new NullPointerException();
		}
		this.file = new File(VCARD_BASE_DIR + "/" + id + VCARD_SUFFIX);
		this.id = id;
		this.rawContent = "";
		this.properties = new ArrayList();
		this.tags = new HashSet();
		// default tags:
		tags.add("all");
		tags.add("vcard");
		tags.add(id.substring(0, 1).toLowerCase());

		if (doesExist()) {
			rawContent = TextUtils.readTextFile(file);
			parse(rawContent);
			tags.addAll(TextUtils.extractTags(rawContent));
			tags.addAll(categoriesAsTags());
			tags.addAll(extractBirthdayTags());
		}
	}

	private Collection extractBirthdayTags() {
		List result = new LinkedList();
		Date bDay = getBirthday();
		if (bDay != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(bDay);
			result.add(DateUtils.MONTHS[cal.get(Calendar.MONTH)].toLowerCase());
			result.add(cal.get(Calendar.YEAR) + "");
		}
		return result;
	}

	/**
	 * Splits the category property if it exists.
	 * 
	 * @return
	 */
	private Collection categoriesAsTags() {
		List result = new LinkedList();
		for (Iterator iter = properties.iterator(); iter.hasNext();) {
			VCardProperty prop = (VCardProperty) iter.next();
			if (prop.getName().equalsIgnoreCase("CATEGORIES")
					&& prop.getValue() != null) {
				StringTokenizer tokens = new StringTokenizer(prop.getValue(),
						", ");
				while (tokens.hasMoreTokens()) {
					result.add(tokens.nextToken().trim().toLowerCase());
				}
			}
		}
		return result;
	}

	/**
	 * Ignores the BEGIN tag. It just starts parsing until the END:VCARD line.
	 * 
	 * @param lines
	 *            Takes lines off the list.
	 * 
	 * @return False, if parsing failed, because the end of the data has been
	 *         reached.
	 */
	private boolean parse(Stack lines) {

		while (!lines.isEmpty()) {

			VCardProperty prop = new VCardProperty();

			if (prop.parse(lines)) {
				if (prop.getName().equals("END"))
					return true;
				// only add it, if it was valid:
				properties.add(prop);
			}
		}
		// there was no END tag, so this vcard is not valid.
		return false;
	}

	/**
	 * Ignores the BEGIN tag. It just starts parsing until the END:VCARD line.
	 * 
	 * @param content
	 *            The content of the vcard.
	 * 
	 * @return False, if parsing failed.
	 */
	private boolean parse(String content) {
		Stack lines = toStack(content);
		Collections.reverse(lines);
		return parse(lines);
	}

	/**
	 * Splits the lines and returns a stack.
	 * 
	 * @param cardContent
	 * @return
	 */
	private Stack toStack(String cardContent) {
		String[] lines = cardContent.split("\n");
		Stack result = new Stack();
		for (int i = 0; i < lines.length; i++) {
			result.add(lines[i]);
		}
		return result;
	}

	public static class VCardProperty implements Comparable {

		private String value;

		private String name;

		private HashMap parameter = new HashMap();

		/**
		 * @param lines
		 * @return
		 */
		public boolean parse(Stack lines) {
			String line = (String) lines.pop();

			while (!lines.isEmpty()
					&& (((String) lines.peek()).startsWith(" ") || ((String) lines
							.peek()).startsWith("\t"))) {
				line += ((String) lines.pop()).substring(1);
			}

			if (line.trim().length() == 0)
				return false;

			ArrayList paramsAndValue = Utils.splitQuotedString(':', line, 2);

			if (paramsAndValue.size() == 2) {
				this.value = (String) paramsAndValue.get(1);
				String params = (String) paramsAndValue.get(0);
				params = params.toUpperCase();
				ArrayList allParams = Utils.splitQuotedString(';', params, 0);
				// the first one is the name of the proptery:
				this.name = (String) allParams.get(0);
				if (name.equals("BEGIN"))
					return false;
				for (int i = 1; i < allParams.size(); i++) {
					parseParameter((String) allParams.get(i));
				}
				if (isQuotedPrintable()) {
					decodeQuotedPrintable(lines);
				}

				if (isUTF8()) {
					this.value = utf8Encode(this.value);
				}

				this.value = value.replaceAll("\\\\,", ",");
				this.value = value.replaceAll("\\\\;", ";");
				// this.value = value.replaceAll("\\\\", "\\");
				this.value = value.replaceAll("\\\\n", "\n");
				return true;
			} else {
			}

			return false;
		}

		/**
		 * @param string
		 * @return
		 */
		private String utf8Encode(String string) {
			return value;
		}

		/**
		 * @return
		 */
		private boolean isUTF8() {
			List tmp = (List) parameter.get("CHARSET");
			if (tmp != null && tmp.contains("UTF-8"))
				return true;
			else
				return false;
		}

		/**
		 * Checks if there are more lines.
		 * 
		 * @param lines
		 */
		private void decodeQuotedPrintable(Stack lines) {

			while (value.endsWith("=")) {
				value = value.substring(0, value.length() - 1);
				if (lines.isEmpty())
					break;
				value += ((String) lines.pop()).trim();
			}
			value = quotedPrintableDecode(value);
		}

		/**
		 * @param value
		 * @return
		 */
		private String quotedPrintableDecode(String value) {
			return MimeConverter.decodeQuotedPrintable(value);
		}

		/**
		 * @return
		 */
		private boolean isQuotedPrintable() {
			List tmp = (List) parameter.get("ENCODING");
			if (tmp != null && tmp.contains("QUOTED-PRINTABLE"))
				return true;
			else
				return false;
		}

		/**
		 * Parses a parameter string where the parameter string is either in the
		 * form "name=value[,value...]" such as "TYPE=WORK,CELL" or is a vCard
		 * 2.1 parameter value such as "WORK" in which case the parameter name
		 * is determined from the parameter value.
		 */
		private void parseParameter(String param) {
			ArrayList list = Utils.splitQuotedString('=', param, 2);

			if (list.size() == 1) {
				// vCard 2.1, no '=' found:
				String paramValue = (String) list.get(0);
				String paramName = getParamName(paramValue);
				addParameterValue(paramName, paramValue);
			} else {
				String paramName = (String) list.get(0);
				ArrayList values = Utils.splitQuotedString(',', (String) list
						.get(1), 0);
				for (Iterator iter = values.iterator(); iter.hasNext();) {
					String element = (String) iter.next();
					addParameterValue(paramName, element);
				}
			}
		}

		/**
		 * @param paramName
		 * @param paramValue
		 */
		private void addParameterValue(String paramName, String paramValue) {
			ArrayList list = (ArrayList) parameter.get(paramName);
			if (list == null) {
				list = new ArrayList(1);
				parameter.put(paramName, list);
			}
			list.add(paramValue);
		}

		/**
		 * The vCard 2.1 specification allows parameter values without a name.
		 * The parameter name is then determined from the unique parameter
		 * value.
		 */
		private String getParamName(String value) {
			String[] types = { "DOM", "INTL", "POSTAL", "PARCEL", "HOME",
					"WORK", "PREF", "VOICE", "FAX", "MSG", "CELL", "PAGER",
					"BBS", "MODEM", "CAR", "ISDN", "VIDEO", "AOL", "APPLELINK",
					"ATTMAIL", "CIS", "EWORLD", "INTERNET", "IBMMAIL",
					"MCIMAIL", "POWERSHARE", "PRODIGY", "TLX", "X400", "GIF",
					"CGM", "WMF", "BMP", "MET", "PMB", "DIB", "PICT", "TIFF",
					"PDF", "PS", "JPEG", "QTIME", "MPEG", "MPEG2", "AVI",
					"WAVE", "AIFF", "PCM", "X509", "PGP" };
			String[] values = { "INLINE", "URL", "CID" };
			String[] encodings = { "7BIT", "QUOTED-PRINTABLE", "BASE64" };
			String name = "UNKNOWN";
			if (in_array(value, types)) {
				name = "TYPE";
			} else if (in_array(value, values)) {
				name = "VALUE";
			} else if (in_array(value, encodings)) {
				name = "ENCODING";
			}
			return name;
		}

		/**
		 * @param value2
		 * @param types
		 * @return
		 */
		private boolean in_array(String string, String[] types) {
			for (int i = 0; i < types.length; i++) {
				if (types[i].equals(string))
					return true;
			}
			return false;
		}

		/**
		 * @return The name of this property.
		 */
		public String getName() {
			return name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object other) {
			if (!(other.getClass() == getClass()))
				return -1;
			return this.name.compareTo(((VCardProperty) other).name);
		}

		/**
		 * @return Returns the parameter.
		 */
		public HashMap getParameter() {
			return parameter;
		}

		/**
		 * @return Returns the value.
		 */
		public String getValue() {
			if ("NOTE".equals(name)) {
				return RenderEngine.getVcardRenderer().render(
						TextUtils.escapeHtmlChars(value));
			}
			return value;
		}
	}

	/**
	 * Returns a list with all <code>VCardProperties</code>.
	 * 
	 * @return List with all properties.
	 */
	public List getProperties() {
		return properties;
	}

	public String getIdentifier() {
		return id;
	}

	public boolean doesExist() {
		return file.canRead();
	}

	/**
	 * Lists all vCards.
	 * 
	 * @return List of all vCards as {@link Vcard} objects. List may be empty,
	 *         never <code>null</code>.
	 * @throws IOException
	 */
	public static List list() throws IOException {
		File[] files = new File(VCARD_BASE_DIR).listFiles();
		List result = new LinkedList();
		if (files == null) {
			return result;
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String name = file.getName();
			if (name.endsWith(VCARD_SUFFIX)) {
				result.add(new Vcard(name.substring(0, name.length()
						- VCARD_SUFFIX.length())));
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
				|| (matchFullText && (rawContent.toLowerCase().contains(tag) || id
						.toLowerCase().contains(tag)));
	}

	public Collection listTags() {
		return tags;
	}

	/**
	 * Determines if the contact's birthday is set and if it is in the specified
	 * month.
	 * 
	 * @param month
	 *            As defined in {@link Calendar}
	 * @return <code>true</code> if the contact's birthday is in the specified
	 *         month.
	 */
	public boolean isBirthdayInMonth(int month) {
		Date bday = getBirthday();
		if (bday != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(bday);
			return cal.get(Calendar.MONTH) == month;
		}
		return false;
	}

	/**
	 * Returns the birthday for this contact.
	 * 
	 * @return the birthday or <code>null</code> if the value is not set or
	 *         the date could not be parsed.
	 */
	public Date getBirthday() {
		for (Iterator iter = properties.iterator(); iter.hasNext();) {
			VCardProperty prop = (VCardProperty) iter.next();
			if (prop.getName().equals("BDAY")) {
				Date bday = DateUtils.parseDate(prop.getValue());
				if (bday != null) {
					return bday;
				}
			}
		}
		return null;
	}
}
