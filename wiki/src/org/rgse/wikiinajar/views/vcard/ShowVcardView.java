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

package org.rgse.wikiinajar.views.vcard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.rgse.wikiinajar.helpers.TextUtils;
import org.rgse.wikiinajar.models.Vcard;
import org.rgse.wikiinajar.models.Vcard.VCardProperty;

import net.sf.wikiinajar.xrays.View;
import net.sf.wikiinajar.xrays.Xml;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ShowVcardView implements View {

	private static final String TYPE_KEY = "TYPE";

	private Vcard vcard;

	private static HashMap nicePropertyNames = new HashMap();

	static {
		nicePropertyNames = new HashMap();
		nicePropertyNames.put("N", "Name");
		nicePropertyNames.put("FN", "Full name");
		nicePropertyNames.put("TITLE", "Title");
		nicePropertyNames.put("ORG", "Organisation");
		nicePropertyNames.put("TEL", "Phone");
		nicePropertyNames.put("EMAIL", "Email");
		nicePropertyNames.put("URL", "Url");
		nicePropertyNames.put("ADR", "Address");
		nicePropertyNames.put("BDAY", "Birthday");
		nicePropertyNames.put("NOTE", "Notes");
		nicePropertyNames.put("LABEL", "Shipping label");
		nicePropertyNames.put("CATEGORIES", "Categories");
	}

	public ShowVcardView(Vcard vcard) {
		this.vcard = vcard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.wikiinajar.xrays.View#toXml()
	 */
	public Xml toXml() {
		Xml xml = new Xml("article");
		addTags(xml.tag("tag-list"), vcard);
		xml.tag("id").cdata(vcard.getIdentifier());
		xml = xml.tag("vcard");
		List properties = vcard.getProperties();
		for (Iterator iter = properties.iterator(); iter.hasNext();) {
			VCardProperty property = (VCardProperty) iter.next();
			Xml prop = null;
			if (property.getValue().trim().length() == 0) {
				// skip empty properties
				continue;
			}
			if (property.getName().equals("NOTE")) {
				prop = xml.tag("formatted-property").attr("name",
						getNiceName(property.getName())).tag("value",
						property.getValue()).up();
				addPropertyTypes(prop, property.getParameter());
			} else if (property.getName().equals("ADR")) {
				prop = xml.tag("formatted-property").attr("name",
						getNiceName(property.getName())).tag("value",
						formatAddress(property.getValue())).up();
				addPropertyTypes(prop, property.getParameter());
			} else {
				prop = xml.tag("property").attr("name",
						getNiceName(property.getName())).tag("value").cdata(
						property.getValue()).up();
			}
			addPropertyTypes(prop, property.getParameter());
		}
		return xml.up();
	}

	private String formatAddress(String value) {
		return value.replaceAll(";", "<br/>\n").replaceAll("^<br/>","");
	}

	private void addTags(Xml xml, Vcard article) {
		List sorted = new LinkedList(article.listTags());
		Collections.sort(sorted);
		for (Iterator iter = sorted.iterator(); iter.hasNext();) {
			String tag = (String) iter.next();
			xml.tag("tag", tag);
		}
	}

	private void addPropertyTypes(Xml prop, HashMap parameter) {
		if (parameter == null) {
			return;
		}
		List types = (List) parameter.get(TYPE_KEY);
		if (types == null || types.isEmpty()) {
			return;
		}
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			String type = (String) iter.next();
			prop.tag("type", type.toLowerCase());
		}
	}

	private String getNiceName(String name) {
		String niceName = (String) nicePropertyNames.get(name);
		return niceName != null ? niceName : name;
	}

	/**
	 * Returns link to page to view specified vCard.
	 * 
	 * @param identifier
	 * @return
	 */
	public static String getLink(String identifier) {
		return "/vcard/show/" + TextUtils.urlEncode(identifier);
	}

}
