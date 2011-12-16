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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class TagTree {

	public static final String PARENT_DIRECTIVE = "--";

	private TreeItem root;

	/**
	 * Creates a new tag tree and populates it with all elements that match the
	 * given tags.
	 * 
	 * @param tags
	 * @throws IOException
	 */
	public TagTree(List tags) throws IOException {
		this(tags, false);
	}

	/**
	 * Creates a new tag tree and populates it.
	 * 
	 * @param tags
	 *            The tags to match.
	 * @param matchFullText
	 *            If <code>true</code>, matches given tags against full text
	 *            of articles instead of only their tags.
	 *            
	 * @throws IOException
	 */
	public TagTree(List tags, boolean matchFullText) throws IOException {
		this.root = initTree(tags, matchFullText);

		loadMatchingElements(WikiArticle.list());
		loadMatchingElements(Vcard.list());
	}

	/**
	 * No-op constructor for testing only.
	 * 
	 */
	protected TagTree() {

	}

	protected TreeItem initTree(List tagList, boolean matchFullText) {
		TreeItem root = new TreeItem("", matchFullText);

		TreeItem curRoot = root;
		for (Iterator iter = tagList.iterator(); iter.hasNext();) {
			String tag = (String) iter.next();
			if (isParentDirective(tag)) {
				curRoot = curRoot.getParent();
				if (curRoot == null) {
					// invalid tag list (can't go higher than root)
					return root;
				}
			} else {
				TreeItem child = new TreeItem(tag, curRoot, matchFullText);
				curRoot = child;
			}
		}
		return root;
	}

	private boolean isParentDirective(String tag) {
		return tag.equals(PARENT_DIRECTIVE);
	}

	private void loadMatchingElements(List taggables) throws IOException {
		for (Iterator iter = taggables.iterator(); iter.hasNext();) {
			ITaggable taggable = (ITaggable) iter.next();
			insertIntoTree(root, taggable);
		}
	}

	/**
	 * Inserts the given article into the tag tree if it matches the right tags.
	 * If not, the article is not inserted.
	 * 
	 * @param root
	 * @param article
	 * @return
	 */
	protected boolean insertIntoTree(TreeItem root, ITaggable article) {
		if (root.isRoot() || root.matchesTag(article)) {
			boolean addedToSubTree = false;
			for (Iterator iter = root.children.iterator(); iter.hasNext();) {
				TreeItem child = (TreeItem) iter.next();
				if (insertIntoTree(child, article)) {
					addedToSubTree = true;
				}
			}
			if (!addedToSubTree && !root.isRoot()) {
				root.addLeaf(article);
				return true;
			} else {
				return true;
			}
		}
		return false;
	}

	public static class TreeItem {

		private String tag;

		private TreeItem parent;

		private ArrayList children;

		/** List of taggable elements matching this tree item. */
		private LinkedList leafs;

		private boolean matchFullText;

		/**
		 * Creates a new root node.
		 * 
		 * @param tag
		 */
		public TreeItem(String tag, boolean matchFullText) {
			this(tag, null, matchFullText);
		}

		public boolean isRoot() {
			return parent == null;
		}

		/**
		 * Creates a new child node.
		 * 
		 * @param tag
		 * @param parent
		 * @param matchFullText 
		 */
		public TreeItem(String tag, TreeItem parent, boolean matchFullText) {
			this.parent = parent;
			this.matchFullText = matchFullText;
			this.tag = tag;
			this.leafs = new LinkedList();
			this.children = new ArrayList();
			if (this.parent != null)
				this.parent.children.add(this);
		}

		public boolean matchesTag(ITaggable article) {
			return article.isTagged(tag, matchFullText);
		}

		public void addLeaf(ITaggable article) {
			leafs.add(article);
		}

		public TreeItem getParent() {
			return parent;
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			toString(buffer);
			// remove tailing "../":
			String result = buffer.toString().replaceAll(
					"(" + PARENT_DIRECTIVE + "/)*$", "");
			return result;
		}

		private void toString(StringBuffer buffer) {
			buffer.append(tag).append(leafsToString()).append("/");
			for (int i = 0; i < children.size(); i++) {
				TreeItem child = (TreeItem) children.get(i);
				child.toString(buffer);
				buffer.append(PARENT_DIRECTIVE).append("/");
			}
		}

		private String leafsToString() {
			if (leafs.isEmpty()) {
				return "";
			}
			StringBuffer buffer = new StringBuffer();
			for (Iterator iter = leafs.iterator(); iter.hasNext();) {
				ITaggable taggable = (ITaggable) iter.next();
				buffer.append("!").append(taggable.getIdentifier());
			}
			return buffer.toString();
		}

		public String getTag() {
			return tag;
		}

		public List getLeafs() {
			return leafs;
		}

		public List getChildren() {
			return children;
		}
	}

	public TreeItem getRoot() {
		return root;
	}
}
