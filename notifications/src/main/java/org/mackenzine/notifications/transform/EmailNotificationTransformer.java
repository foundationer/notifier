package org.mackenzine.notifications.transform;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.mackenzine.notifications.model.Notification;


public class EmailNotificationTransformer implements NotificationTransformer {

	private Session session;

	public EmailNotificationTransformer(final Session session) {
		this.session = session;
	}

	@Override
	public Message transform(final Notification notification) throws JMSException {
		TextMessage textMessage = session.createTextMessage();
		textMessage.setStringProperty("subject", notification.getSubject());
		textMessage.setStringProperty("body", notification.getContent());
		return textMessage;
	}
}
