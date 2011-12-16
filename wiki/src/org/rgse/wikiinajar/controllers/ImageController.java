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

package org.rgse.wikiinajar.controllers;

import java.io.File;
import java.io.FileInputStream;

import org.rgse.wikiinajar.models.Image;
import org.rgse.wikiinajar.models.Vcard;
import org.rgse.wikiinajar.views.vcard.ShowVcardView;

import net.sf.wikiinajar.xrays.ControllerResponse;
import net.sf.wikiinajar.xrays.Request;

/**
 * Deals with vCards.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class ImageController {

	public ControllerResponse showAction(Request request) throws Exception {
		File img = new File(Image.getImageDir()+"/" + request.getPath().getId());
		if (img.exists()) {
			FileInputStream fis = new FileInputStream(Image.getImageDir()+"/" + request.getPath().getId());
			
			return request.streamResponse(request.getPath().getId(), fis);
		} else {
			return request.errorResponse("找不到！ "
					+ request.getPath().getId());
		}
	}

}
