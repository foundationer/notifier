package org.mackenzine.notifications.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;

	private final DateTime from;
	private final DateTime to;
	private final String subject;
	private final String content;
	private Schedule schedule;

	public Notification(final DateTime from, final DateTime to, final String subject, final String content) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public DateTime getFrom() {
		return from;
	}

	public DateTime getTo() {
		return to;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(final Schedule schedule) {
		this.schedule = schedule;
	}
}
