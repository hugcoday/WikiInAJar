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

package org.rgse.wikiinajar.helpers.wiki.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rgse.wikiinajar.helpers.wiki.render.filters.Filter;



/**
 * A filter transforms a particular text markup into something else.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public abstract class LineByLineFilter implements Filter {
    private static final Logger logger = Logger
            .getLogger(LineByLineFilter.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see org.rgse.render.filter.Filter#filter(java.lang.String)
     */
    public String filter(String text) {
        StringBuffer buffer = new StringBuffer();
        try {
            render(buffer, new BufferedReader(new StringReader(text)));
        } catch (IOException e) {
            // this should never happen:
            logger.log(Level.WARNING, "Caught exception. Should not happen...",
                    e);
            return text;
        }
        return buffer.toString();
    }

    /**
     * Transform the markup in the text into something filter specific.
     * 
     * @param buffer
     *            The buffer to modify.
     * @param reader
     *            The reader to read the text that contains the markup.
     * @throws IOException
     *             In case the reader encounters an error.
     */
    public abstract void render(StringBuffer buffer, BufferedReader reader)
            throws IOException;
}
