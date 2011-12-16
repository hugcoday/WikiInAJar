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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Serves files in the 'public' folder.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class PublicController {

	public static final String PUBLIC_ROOT = "./public";

	public ControllerResponse skinsAction(Request request) {
		String resName = PUBLIC_ROOT + "/skins/" + request.getPath().getIds();
		try {
			return request
					.streamResponse(resName, new FileInputStream(resName));
		} catch (FileNotFoundException e) {
			return request.resourceNotFoundResponse(resName);
		}
	}

	/**
	 * Returns <code>true</code> if the directory serving static files exists.
	 * 
	 * @return <code>true</code> if the doc root dir exists.
	 * 
	 * @see #PUBLIC_ROOT
	 */
	public static boolean publicDocrootExists() {
		File dir = new File(PUBLIC_ROOT);
		return dir.exists() && dir.isDirectory();
	}
}
