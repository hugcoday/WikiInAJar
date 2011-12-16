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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class TextUtilsTest extends TestCase {

	public void testGetAllMatches() {
		assertTagsMatch(new String[] { "tag1", "tag2", "tag3" }, TextUtils
				.extractTags("a very long text\n"
						+ "Next line [[tags: tag1, tag2   tag3]] more"));

		assertTagsMatch(new String[] { "tag1", "tag2", "tag3" }, TextUtils
				.extractTags("a very long [[tags: tag1]] text\n"
						+ "Next line [[tags: tag2   tag3]] more"));

		assertTagsMatch(new String[] { "tag1", "tag2", "tag3" }, TextUtils
				.extractTags("line [[tags: tag1,tag2 tag3]] more"));

		assertTagsMatch(new String[] {}, TextUtils
				.extractTags("line [[tags: ]] more"));

		assertTagsMatch(new String[] { "tag" }, TextUtils
				.extractTags("line [[tags:tag]] more"));
		assertTagsMatch(new String[] { "tag" }, TextUtils
				.extractTags("line [[tags:tag, tag]] more"));
	}

	private void assertTagsMatch(String[] strings, Set list) {
		List l = new LinkedList(list);
		Collections.sort(l);
		assertEquals(strings.length, l.size());
		for (int i = 0; i < strings.length; i++) {
			assertEquals(strings[i], l.get(i));
		}
	}

}
