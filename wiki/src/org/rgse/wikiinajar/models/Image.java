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


/**
 * 定义图片常量
 * 
 * @author han.xx
 * 
 */
public class Image{

	private static final String DOC_ROOT = System.getProperty(
			"wikiinajar.docroot", "./public/docs");

	private static final String IMAGE_BASE_DIR = DOC_ROOT + "/image";

	public static String getImageDir() {
		return IMAGE_BASE_DIR;
	}
}
