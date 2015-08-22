package org.mackenzine.notifications.transform;

import javax.jms.JMSException;
import javax.jms.Message;

import org.mackenzine.notifications.model.Notification;



public interface NotificationTransformer {

	Message transform(Notification notification) throws JMSException;
}