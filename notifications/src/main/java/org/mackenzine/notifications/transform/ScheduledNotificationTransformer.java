package org.mackenzine.notifications.transform;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.ScheduledMessage;
import org.mackenzine.notifications.model.Notification;
import org.mackenzine.notifications.model.Schedule;


public class ScheduledNotificationTransformer implements NotificationTransformer {

	private final NotificationTransformer decoratedTransformer;

	public ScheduledNotificationTransformer(final NotificationTransformer transformer) {
		this.decoratedTransformer = transformer;
	}

	@Override
	public Message transform(final Notification notification) throws JMSException {
		Message message = decoratedTransformer.transform(notification);

		Schedule schedule = notification.getSchedule();

		if (schedule.getCronEntry() != null)
			message.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, schedule.getCronEntry());

		if (schedule.getDelay() != null)
			message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, schedule.getDelay());

		if (schedule.getPeriod() != null)
			message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, schedule.getPeriod());

		if (schedule.getRepeat() != null)
			message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, schedule.getRepeat());

		return message;
	}
}
