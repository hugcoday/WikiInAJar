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

package org.rgse.wikiinajar.helpers.wiki.render.filters;

import java.io.BufferedReader;
import java.io.IOException;

import org.rgse.wikiinajar.helpers.wiki.render.LineByLineFilter;

/**
 * Support for wiki tables.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 *
 */
public class TableFilter extends LineByLineFilter {

    private static final String TABLE_START = "{|";
    private static final String TABLE_END = "|}";
    private static final String TABLE_CELL_START = "|";
    private static final String TABLE_ROW_PREFIX = "|-";
    private static final String TABLE_HEADER_CELL_START = "!";
    
    private boolean isTableOpen  = false;
    private boolean isRowOpen    = false;
    private boolean isDataOpen   = false;
    private boolean isHeaderOpen = false;
    
    private int rowIndex = 0;

    public void render(StringBuffer buffer, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            
            if (line.startsWith(TABLE_START)) {
                closeTableIfOpen(buffer);
                
                openTable(buffer, line.substring(TABLE_START.length()).trim());

            } else if (isTableOpen) {

                if (line.startsWith(TABLE_END)) {

                    closeTableIfOpen(buffer);

                } else if (line.startsWith(TABLE_ROW_PREFIX)) {
                    if (isRowOpen) {
                        closeRowIfOpen(buffer);
                    }
                } else if (line.startsWith(TABLE_CELL_START)) {
//                    closeRowIfOpen(buffer);
                    closeDataIfOpen(buffer);
                    if (!isRowOpen) {
                        openRow(buffer);
                    }
                    
                    line = line.substring(TABLE_CELL_START.length());
                    String[] cells = splitCells(line, "\\|\\|", "|");
                    for (int i = 0; i < cells.length; i++) {
                        if (i == 0 && hasFormattingModifiers(line, "\\|\\|", "|")) {
                            openData(buffer, cells[0]);
                            continue;
                        } else if (!isDataOpen) {
                            openData(buffer);
                        }
                        buffer.append(cells[i]).append("\n");
                        if (i < cells.length - 1) {
                            closeDataIfOpen(buffer);
                        }
                    }

                } else if (line.startsWith(TABLE_HEADER_CELL_START)) {
                    closeHeaderIfOpen(buffer);
                    if (!isRowOpen) {
                        openRow(buffer);
                    }
                    
                    line = line.substring(TABLE_HEADER_CELL_START.length());
                    String[] cells = splitCells(line, "!!", "!");
                    for (int i = 0; i < cells.length; i++) {
                        if (i == 0 && hasFormattingModifiers(line, "!!", "!")) {
                            openHeader(buffer, cells[0]);
                            continue;
                        } else if (!isHeaderOpen) {
                            openHeader(buffer, "");
                        }
                        buffer.append(cells[i]).append("\n");
                        if (i < cells.length - 1) {
                            closeHeaderIfOpen(buffer);
                        }
                    }
                } else {
                    buffer.append(line);
                }
            } else {
                buffer.append(line);
            }
            
            buffer.append("\n");
        }
        closeTableIfOpen(buffer);
    }

    private void closeHeaderIfOpen(StringBuffer buffer) {
        if (isHeaderOpen) {
            buffer.append("</th>\n");
            isHeaderOpen = false;
        }
    }

    private void openHeader(StringBuffer buffer, String formatModifiers) {
        buffer.append("<th ").append(formatModifiers).append(" >\n");
        isHeaderOpen = true;
        
    }

    private boolean hasFormattingModifiers(String line, String regex, String sepChar) {
        String[] cells = line.split(regex);
        if (cells.length > 1 && cells[1].startsWith(sepChar)) {
            return true;
        } else if (cells.length == 1) {
            return line.indexOf(sepChar.charAt(0)) != -1;
        } else {
            return false;
        }
    }

    private String[] splitCells(String line, String regex, String sepChar) {
        String[] cells = line.split(regex);
        if (cells.length > 1 && cells[1].startsWith(sepChar)) {
            // remove leading "|" or "!" of first cell (cell 0 contains formatting modifiers)
            cells[1] = cells[1].substring(1);
        }
        if (cells.length == 1) {
            int pipeIndex = line.indexOf(sepChar.charAt(0));
            if (pipeIndex != -1) {
                cells = new String[] {line.substring(0, pipeIndex), line.substring(pipeIndex + 1)};
            } else {
                cells = new String[] {line};
            }
        }
        return cells;
    }

    private void openData(StringBuffer buffer, String formatModifiers) {
        buffer.append("<td ").append(formatModifiers).append(" >\n");
        isDataOpen = true;
    }

    private void closeTableIfOpen(StringBuffer buffer) {
        closeDataIfOpen(buffer);
        closeHeaderIfOpen(buffer);
        closeRowIfOpen(buffer);
        if (isTableOpen) {
            buffer.append("</table>\n");
            isTableOpen = false;
        }
    }

    private void openTable(StringBuffer buffer, String formattingModifiers) {
        buffer.append("<table ").append(formattingModifiers).append(">\n");
        isTableOpen = true;
        rowIndex = 0;
    }

    private void closeDataIfOpen(StringBuffer buffer) {
        if (isDataOpen) {
            buffer.append("\n</td>");
            isDataOpen = false;
        }
    }

    private void openData(StringBuffer buffer) {
        openData(buffer, "");
    }

    private void openRow(StringBuffer buffer) {
        buffer.append("<tr ").append(rowIndex % 2 == 0 ? "class='even'" : "class='odd'").append(">\n");
        isRowOpen = true;
        rowIndex++;
    }

    private void closeRowIfOpen(StringBuffer buffer) {
        closeDataIfOpen(buffer);
        closeHeaderIfOpen(buffer);
        if (isRowOpen) {
            buffer.append("</tr>\n");
            isRowOpen = false;
        }
    }
}
