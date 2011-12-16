/*
 * XRays Web Framework
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
package net.sf.wikiinajar.xrays;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class Xml {

	private String tag;

	private LinkedList children;

	private Xml parent;

	private String value;

	private HashMap attibutes;

	public Xml(String tag) {
		this(null, tag, "");
	}

	private Xml(Xml parent, String tag, String value) {
		this.tag = tag;
		this.value = value;
		this.children = new LinkedList();
		this.attibutes = new HashMap();
		this.parent = parent;
		if (parent != null)
			parent.children.add(this);
	}

	/**
	 * Returns the parent element.
	 * 
	 * @return The parent element or <code>null</code> if this is the root
	 *         element.
	 */
	public Xml up() {
		return parent;
	}

	/**
	 * Adds and returns a new child tag with the given value.
	 * 
	 * @param tag
	 * @param value
	 * @return The child tag.
	 * 
	 * @see #up()
	 */
	public Xml tag(String tag, String value) {
		return new Xml(this, tag, value);
	}

	/**
	 * Adds and returns a new child tag.
	 * 
	 * @param tag
	 * @return The child tag.
	 * @see #up()
	 */
	public Xml tag(String tag) {
		return new Xml(this, tag, "");
	}

	/**
	 * Returns this element and child elements as string.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("\n");
		toString(buffer, "");
		return buffer.toString();
	}

	private void toString(StringBuffer buffer, String spacer) {
		buffer.append("\n").append(spacer).append("<").append(tag).append(" ");
		for (Iterator iter = attibutes.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			buffer.append(entry.getKey()).append("='").append(entry.getValue())
					.append("' ");
		}
		buffer.append(">");

		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Xml child = (Xml) iter.next();
			child.toString(buffer, spacer + " ");
			// buffer.append("\n");
		}
		buffer.append("").append(value);
		buffer.append("</").append(tag).append(">\n");
	}

	/**
	 * Sets the value of this tag to the specified content as CDATA element.
	 * 
	 * @param content
	 * @return This xml tag.
	 */
	public Xml cdata(String content) {
		this.value = "<![CDATA[" + content + "]]>";
		return this;
	}

	/**
	 * Adds a new attribute to the tag.
	 * 
	 * @param name
	 * @param value
	 * @return This tag.
	 */
	public Xml attr(String name, String value) {
		attibutes.put(name, value);
		return this;
	}
}
