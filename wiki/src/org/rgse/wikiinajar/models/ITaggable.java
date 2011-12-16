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

import java.util.List;

/**
 * Represents an entity that supports tags.
 * 
 * @authorrico_g AT users DOT sourceforge DOT net
 * 
 */
public interface ITaggable {

	/**
	 * Returns a unique identifier for this element.
	 * 
	 * @return A unique identifier for this element.
	 */
	public String getIdentifier();

	/**
	 * Returns <code>true</code> if this element is tagged with at least one
	 * of the specified tags.
	 * 
	 * @param tags
	 *            A list of tags as Strings.
	 * @param matchFullText
	 *            If <code>true</code>, matches against full text instead of
	 *            only the defined tags.
	 * 
	 * @return <code>true</code> if this element is tagged.
	 */
	public boolean isTagged(List tags, boolean matchFullText);

	/**
	 * Returns <code>true</code> if this element it tagged with the specified
	 * tag.
	 * 
	 * @param tag
	 *            The tag to look for.
	 * @param matchFullText
	 *            If <code>true</code>, matches against full text instead of
	 *            only the defined tags.
	 * 
	 * @return <code>true</code> if this element is tagged.
	 */
	public boolean isTagged(String tag, boolean matchFullText);

}
