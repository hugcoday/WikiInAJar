/*
 * Wiki_in_a_jar
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

package org.rgse.wikiinajar.views.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import net.sf.wikiinajar.xrays.View;
import net.sf.wikiinajar.xrays.Xml;

import org.rgse.wikiinajar.helpers.DateUtils;
import org.rgse.wikiinajar.models.Month;
import org.rgse.wikiinajar.models.Month.Day;
import org.rgse.wikiinajar.models.Month.Event;

/**
 * Page for a single month with its events.
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class MonthView implements View {

	private Month month;

	private SimpleDateFormat df;

	public MonthView(Month month) {
		this.month = month;
		this.df = new SimpleDateFormat("MM/dd/yyyy");
	}

	public Xml toXml() {
		Xml xml = new Xml("article");
		xml.tag("id", month.getMonth().toLowerCase() + " " + month.getYear());
		appendCalendar(xml);
		// add events:
		Xml events = xml.tag("events");
		Xml past = events.tag("past");
		Xml future = events.tag("future");
		Xml today = events.tag("today");
		for (Iterator iter = month.getEvents().iterator(); iter.hasNext();) {
			Event event = (Event) iter.next();
			Xml cur = event.isBeforeToday() ? past : (event.isToday() ? today
					: future);
			cur.tag("event").attr("id", event.getIdentifier()).attr(
					"isbirthday", event.isBirthday() + "").attr("date",
					formattedDate(event.getDate()));
		}
		return xml;
	}

	private String formattedDate(Date date) {
		return df.format(date);
	}

	/**
	 * Appends the calendar tag to the given xml element.
	 * 
	 * @param xml
	 *            The parent tag to add to.
	 */
	public void appendCalendar(Xml xml) {
		xml = xml.tag("calendar");
		Xml m = xml.tag("month").attr("name", month.getMonth()).attr("year",
				month.getYear() + "");
		m.attr("nextlink", nextLink(month));
		m.attr("prevlink", prevLink(month));

		Xml week = m.tag("week");
		boolean addedFiller = false;
		for (Iterator iter = month.getDays().iterator(); iter.hasNext();) {
			Day day = (Day) iter.next();
			if (!addedFiller) {
				addedFiller = true;
				addFiller(week, day);
			}
			Xml dayTag = week.tag("day").attr("dayofweek", day.getName()).attr(
					"dayofmonth", day.getDayOfMonth() + "").attr("weekend",
					day.isWeekend() ? "yes" : "no");
			if (month.hasEvents(day)) {
				dayTag.attr("eventlink", "/calendar/"
						+ DateUtils.MONTHS[month.getMonthAsInt()].toLowerCase()
						+ "/" + month.getYear());
			}
			if (day.getDayOfWeek() == Calendar.SATURDAY) {
				week = m.tag("week");
			}
		}

	}

	private void addFiller(Xml week, Day day) {
		if (!day.isSunday()) {
			int filler = day.getDayOfWeek() - 1;
			for (int i = 0; i < filler; i++) {
				week.tag("filler");
			}
		}
	}

	private String prevLink(Month month) {
		int year = month.getYear();
		int prevMonth = month.getMonthAsInt() - 1;
		if (prevMonth < Calendar.JANUARY) {
			prevMonth = Calendar.DECEMBER;
			year--;
		}
		return "/calendar/" + DateUtils.MONTHS[prevMonth].toLowerCase() + "/"
				+ year;
	}

	private String nextLink(Month month) {
		int year = month.getYear();
		int nextMonth = month.getMonthAsInt() + 1;
		if (nextMonth > Calendar.DECEMBER) {
			nextMonth = Calendar.JANUARY;
			year++;
		}
		return "/calendar/" + DateUtils.MONTHS[nextMonth].toLowerCase() + "/"
				+ year;
	}

}
