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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import net.sf.wikiinajar.xrays.IServerSettings;

/**
 * Server settings. Settings are kept in a properties file under public/config.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class Settings implements IServerSettings {

	private static final String CONFIG_FILE = "public/config";

	private static final String ONLY_LOCAL = "only.allow.local.connections";
	
	private static final Object lock = new Object();

	private static Settings instance;

	private boolean onlyLocalConnections;

	private Settings() throws IOException {
		Properties props = new Properties();
		try {
			InputStream in = new FileInputStream(CONFIG_FILE);
			props.loadFromXML(in);
			in.close();
		} catch (FileNotFoundException e) {
			// ignore
		}
		this.onlyLocalConnections = props.getProperty(ONLY_LOCAL, "yes")
				.equals("yes");
	}

	public void toggleAllowRemoteConnections() throws IOException {
		this.onlyLocalConnections = !onlyLocalConnections;
		this.save();
	}

	private void save() throws IOException {
		Properties props = new Properties();
		props.put(ONLY_LOCAL, onlyLocalConnections ? "yes" : "no");
		OutputStream os = new FileOutputStream(CONFIG_FILE);
		props.storeToXML(os, "");
		os.close();
	}

	public boolean allowOnlyLocalConnections() {
		return onlyLocalConnections;
	}

	public static Settings getSettings() throws IOException {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new Settings();
				}
			}
		}
		return instance;
	}

	/**
	 * Returns a list of tags to show as tab in the main menu bar.
	 * @return tags as strings or empty list.
	 */
    public List getTagsForMainMenu() {
        return Arrays.asList(new String[] {"wiki", "help", "l", "all", "foooo"});
    }

}
