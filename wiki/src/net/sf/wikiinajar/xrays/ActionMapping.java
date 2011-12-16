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

/**
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ActionMapping {

	private static final String CONTROLLER_SUFFIX = "Controller".toLowerCase();

	private HashMap mappings;

	/**
	 * Creates a new mapping instance. The only predefined mapping is the
	 * mapping for "public", serving public content (xslt, css, etc.). The
	 * public mappings are handled by {@link PublicController}.
	 * 
	 */
	public ActionMapping() {
		this.mappings = new HashMap();
		register(PublicController.class);
	}

	/**
	 * Registers a new controller mapping. As by naming convention, the class
	 * name is expected to be of the scheme <code>"Action"Controller</code>.
	 * This will be mapped to the URL <code>http://localhost/action/</code>.
	 * If the class name does not follow the naming convention, the whole class
	 * name (all lowercase) is used as action name.
	 * 
	 * @param class1
	 */
	public void register(Class class1) {

		String actionName = getActionName(class1);
		mappings.put(actionName, class1);
	}
	
	
	/**
	 * Workaround because class1.getSimpleName() returned empty string
	 * when runnnig from the command line.
	 * @param class1 The class
	 * @return the class name without the package.
	 */
	private String getClassName(Class class1) {
		String name = class1.getName();
		return name.substring(name.lastIndexOf(".") + 1);
	}

	/**
	 * Derives the action name from the given controller class.
	 * @param class1
	 * @return
	 */
	protected String getActionName(Class class1) {
	    return getActionName(getClassName(class1));
	}
	
	protected String getActionName(String class1) {
		String name = class1.toLowerCase();
		if (name.endsWith(CONTROLLER_SUFFIX)) {
			return name
					.substring(0, name.length() - CONTROLLER_SUFFIX.length());
		} else {
			return name;
		}
	}

	/**
	 * Returns the controller registered for the specified mapping.
	 * 
	 * @param controllerMapping
	 * 
	 * @return The controller for the mapping or <code>null</code> if no
	 *         controller was registered for that mapping.
	 */
	public Class getController(String controllerMapping) {
		Class mappedController = (Class) mappings.get(controllerMapping);
		return mappedController;
	}
}
