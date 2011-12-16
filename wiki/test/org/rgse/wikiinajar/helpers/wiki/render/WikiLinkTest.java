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

package org.rgse.wikiinajar.helpers.wiki.render;

import org.rgse.wikiinajar.helpers.wiki.render.WikiLink;

import junit.framework.TestCase;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class WikiLinkTest extends TestCase {

	/**
	 * Test method for
	 * {@link org.rgse.wikiinajar.helpers.wiki.render.WikiLink#WikiLink(java.lang.String)}.
	 */
	public void testWikiLink() {
		assertWikiLink("wiki", "Main Page","wiki:Main Page");

		assertWikiLink("wiki", "", "wiki:");
		
		assertWikiLink("", "", ":");
		
		assertWikiLink(null, "Main Page", "Main Page ");
		
		assertWikiLink("wiki", "Main Page", "wiki: Main Page ");
		
		assertWikiLink("wiki", "Main Page", " wiki : Main Page ");
		
	}

	private void assertWikiLink(String namespace, String link, String origLink) {
		WikiLink fixture = new WikiLink();
		String[] tokens = fixture.splitByNamespace(origLink);
		if (namespace == null) {
			assertNull(tokens[0]);
		} else {
			assertEquals(namespace, tokens[0]);
		}
		assertEquals(link, tokens[1]);

	}

}
