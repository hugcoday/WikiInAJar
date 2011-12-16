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

package org.rgse.wikiinajar.views.admin;

import net.sf.wikiinajar.xrays.Xml;

import org.rgse.wikiinajar.models.Settings;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ShowAdminPageView {

	private Settings settings;

	public ShowAdminPageView(Settings settings) {
		this.settings = settings;

	}

	public Xml toXml() {
		Xml xml = new Xml("admin-page");
		xml.tag("only-local-connections").attr("value",
				settings.allowOnlyLocalConnections() ? "yes" : "no");
		return xml;
	}

}
