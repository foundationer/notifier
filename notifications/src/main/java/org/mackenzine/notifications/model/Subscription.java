package org.mackenzine.notifications.model;

import java.util.ArrayList;
import java.util.Collection;

public class Subscription {

	private String name;
	private Collection<Notification> notifications;
	private Collection<Customer> subscribers;

	public Subscription(final String name) {
		this.name = name;
		this.notifications = new ArrayList<Notification>();
		this.subscribers = new ArrayList<Customer>();
	}

	public boolean addSubscriber(final Customer customer) {
		return this.subscribers.add(customer);
	}

	public boolean addNotification(final Notification notification) {
		return this.notifications.add(notification);
	}

	public String getName() {
		return name;
	}

	public Collection<Notification> getNotifications() {
		return notifications;
	}

	public String getSubscribersEmails() {
		StringBuilder builder = new StringBuilder();
		for (Customer subscriber : subscribers) {
			builder.append(subscriber.getEmail());
			builder.append("; ");
		}
		return builder.toString();
	}
}
