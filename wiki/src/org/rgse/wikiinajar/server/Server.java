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

package org.rgse.wikiinajar.server;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import net.sf.wikiinajar.xrays.ActionMapping;
import net.sf.wikiinajar.xrays.HttpServer;
import net.sf.wikiinajar.xrays.PublicController;

import org.rgse.wikiinajar.controllers.AdminController;
import org.rgse.wikiinajar.controllers.FindController;
import org.rgse.wikiinajar.controllers.ImageController;
import org.rgse.wikiinajar.controllers.IndexController;
import org.rgse.wikiinajar.controllers.TagController;
import org.rgse.wikiinajar.controllers.VcardController;
import org.rgse.wikiinajar.controllers.WikiController;
import org.rgse.wikiinajar.models.Settings;

/**
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class Server {

	private static final String ERROR_LOG = "error.log";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Before we start, let's check if the docroot is avalable:
			if (!PublicController.publicDocrootExists()) {
				System.err.println("The directory does not exist: "
						+ PublicController.PUBLIC_ROOT);
				System.err.println("Current working directory is: "
						+ new File(".").getAbsolutePath());
				System.err.println("Wiki server stopped.");
				System.exit(1);
			}
			ActionMapping mapping = new ActionMapping();
			mapping.register(WikiController.class);
			mapping.register(VcardController.class);
			mapping.register(ImageController.class);
			mapping.register(TagController.class);
			mapping.register(FindController.class);
			mapping.register(IndexController.class);
			mapping.register(AdminController.class);

			int port = getPort(args);
			port = port != -1 ? port : 3003;

			Settings settings = Settings.getSettings();

			HttpServer server = new HttpServer(mapping, port, settings);

			// TODO Rico: fix this problem
			server.thread.join();
		} catch (Throwable t) {
			String msg = "An error was encountered at: " + new Date()
					+ "\nServer is shutting down. Error details:\n";
			logError(msg, t, ERROR_LOG);
			System.exit(1);
		}

	}

	/**
	 * Logs throwable to error log. If that fails, just prints to stderr.
	 * 
	 * @param msg
	 *            The message
	 * @param t
	 * @param file
	 */
	public static void logError(String msg, Throwable t, String file) {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(file));
			p.println(msg);
			t.printStackTrace(p);
			p.close();
			if (p.checkError()) {
				t.printStackTrace();
			}
		} catch (Throwable t2) {
			// error logging, print original cause:
			t.printStackTrace();
		}
	}

	private static int getPort(String[] args) {
		int port = -1;
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
				if (port <= 1024) {
					port = -1;
				}
			} catch (NumberFormatException e) {
				port = -1;
			}
		}
		return port;
	}

}
