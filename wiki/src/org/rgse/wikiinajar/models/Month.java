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

package org.rgse.wikiinajar.models;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.rgse.wikiinajar.helpers.DateUtils;

/**
 * A moth of a certain year. Each month has events (wiki articles or contact
 * birthdays).
 * 
 * @author rico_g AT users DOT sourceforge DOT net
 * 
 */
public class Month {

	private LinkedList days;

	private int year;

	private int month;

	private LinkedList events;

	public Month(int month, int year) {
		init(month, year);
	}

	public Month(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		init(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
	}

	private void init(int month, int year) {
		this.days = new LinkedList();
		this.month = month;
		this.year = year;
		this.events = new LinkedList();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);

		for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			calendar.set(Calendar.DAY_OF_MONTH, i);
			days.add(new Day(i, calendar.get(Calendar.DAY_OF_WEEK)));
		}
	}

	public String getMonth() {
		return DateUtils.MONTHS[month];
	}

	public int getMonthAsInt() {
		return month;
	}

	public LinkedList getDays() {
		return days;
	}

	public int getYear() {
		return year;
	}

	public String toString() {
		return DateUtils.MONTHS[month] + " " + year;
	}

	/**
	 * Loads events for this month (such as birthdays).
	 * 
	 * @throws IOException
	 * 
	 */
	public void loadEvents() throws IOException {
		List vcards = Vcard.list();

		for (Iterator iter = vcards.iterator(); iter.hasNext();) {
			Vcard vcard = (Vcard) iter.next();
			if (vcard.isBirthdayInMonth(month)) {
				events.add(new Event(vcard.getIdentifier(),
						vcard.getBirthday(), true));
			}
		}

		List articles = WikiArticle.list();
		for (Iterator iter = articles.iterator(); iter.hasNext();) {
			WikiArticle article = (WikiArticle) iter.next();
			Date dueDate = article.getDueDate();
			if (isThisMonth(dueDate)) {
				events.add(new Event(article.getIdentifier(), dueDate, false));
			}
		}
	}

	private boolean isThisMonth(Date dueDate) {
		if (dueDate == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dueDate);
		return cal.get(Calendar.MONTH) == this.month
				&& cal.get(Calendar.YEAR) == this.year;
	}

	/**
	 * Returns the events in this month. Events can be wiki articles or
	 * anniversaries of contacts.
	 * 
	 * @return events as {@link Event} objects.
	 */
	public List getEvents() {
		return events;
	}

	public static class Event {

		private boolean isBirthday;

		private Calendar date;

		private String identifier;

		public Event(String identifier, Date date, boolean isBirthday) {
			this.identifier = identifier;
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			this.date = cal;
			this.isBirthday = isBirthday;
		}

		public Date getDate() {
			return date.getTime();
		}

		public boolean isBirthday() {
			return isBirthday;
		}

		public String getIdentifier() {
			return identifier;
		}

		/**
		 * Returns <code>true</code> if this event is before today.
		 * 
		 * @return
		 */
		public boolean isBeforeToday() {
			return DateUtils.isBeforeDay(date, Calendar.getInstance(),
					isBirthday);
		}

		/**
		 * Returns <code>true</code> if the event is on that given day of the
		 * month.
		 * 
		 * @param dayOfMonth
		 * @return
		 */
		public boolean isOnDay(int dayOfMonth) {
			return date.get(Calendar.DAY_OF_MONTH) == dayOfMonth;
		}

		/**
		 * Returns <code>true</code> if this event is today.
		 * 
		 * @return
		 */
		public boolean isToday() {
			return DateUtils
					.isSameDay(date, Calendar.getInstance(), isBirthday);
		}
	}

	public static class Day {

		private int dayOfWeek;

		private int dayOfMonth;

		public Day(int dayOfMonth, int dayOfWeek) {
			this.dayOfMonth = dayOfMonth;
			this.dayOfWeek = dayOfWeek;
		}

		public String toString() {
			return DateUtils.WEEKDAYS[dayOfWeek] + " " + dayOfMonth;
		}

		public String getName() {
			return DateUtils.WEEKDAYS[dayOfWeek];
		}

		public int getDayOfMonth() {
			return dayOfMonth;
		}

		public int getDayOfWeek() {
			return dayOfWeek;
		}

		public boolean isSunday() {
			return this.dayOfWeek == Calendar.SUNDAY;
		}

		public boolean isWeekend() {
			return isSunday() || dayOfWeek == Calendar.SATURDAY;
		}

	}

	public boolean hasEvents(Day day) {
		for (Iterator iter = events.iterator(); iter.hasNext();) {
			Event event = (Event) iter.next();
			if (event.isOnDay(day.dayOfMonth)) {
				return true;
			}
		}
		return false;
	}
}
