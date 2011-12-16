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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rgse.wikiinajar.models.ITaggable;
import org.rgse.wikiinajar.models.TagTree;
import org.rgse.wikiinajar.models.TagTree.TreeItem;


import junit.framework.TestCase;

public class TagTreeTest extends TestCase {

	private static final String tagTree = "number/small/1/--/2/--/--/big/100";

	private static final List tagList = Arrays.asList(tagTree.split("/"));

	public void testInitTree() throws IOException {
		TagTree fixture = new TagTree(Collections.EMPTY_LIST);
		assertEquals("/" + tagTree + "/", fixture.initTree(
				Arrays.asList(tagTree.split("/")), false).toString());
	}

	public void testInsertIntoTree() {

		TagTree fixture = new TagTree();
		TreeItem root = fixture.initTree(tagList, false);

		Taggable t = new Taggable("tagItem1", "number");
		fixture.insertIntoTree(root, t);

		assertEquals("/number!tagItem1/small/1/--/2/--/--/big/100/", root.toString());
		
		t = new Taggable("tagItem2", new String[] {"number", "small", "1"});
		fixture.insertIntoTree(root, t);
		assertEquals("/number!tagItem1/small/1!tagItem2/--/2/--/--/big/100/", root.toString());
		
		t = new Taggable("tagItem3", "no_match");
		fixture.insertIntoTree(root, t);
		assertEquals("/number!tagItem1/small/1!tagItem2/--/2/--/--/big/100/", root.toString());
		
		fixture = new TagTree();
		root = fixture.initTree(tagList, false);
		t = new Taggable("double", new String[] {"number", "small", "big"});
		fixture.insertIntoTree(root, t);
		assertEquals("/number/small!double/1/--/2/--/--/big!double/100/", root.toString());
	}

	private static class Taggable implements ITaggable {

		private Set tags;

		private String name;

		public Taggable(String name, String tag) {
			this(name, new String[] {tag});
		}

		public Taggable(String name, String[] strings) {
			this.name = name;
			this.tags = new HashSet();
			tags.addAll(Arrays.asList(strings));
		}

		public boolean isTagged(List tags, boolean fullText) {
			for (Iterator iter = tags.iterator(); iter.hasNext();) {
				String tag = (String) iter.next();
				if (isTagged(tag, fullText)) {
					return true;
				}
			}
			return false;
		}

		public boolean isTagged(String tag, boolean fullText) {
			return tags.contains(tag);
		}

		public String getIdentifier() {
			return name;
		}

	}
}
