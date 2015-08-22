package org.mackenzine.notifications.transform;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.activemq.ScheduledMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mackenzine.notifications.model.Notification;
import org.mackenzine.notifications.model.Schedule;
import org.mackenzine.notifications.transform.NotificationTransformer;
import org.mackenzine.notifications.transform.ScheduledNotificationTransformer;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ScheduledNotificationTransformerTest {

	private ScheduledNotificationTransformer transformer;
	private Schedule schedule;

	@Mock private NotificationTransformer decoratedTransformer;
	@Mock private Notification notification;
	@Mock private TextMessage textMessage;

	@Rule public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		transformer = new ScheduledNotificationTransformer(decoratedTransformer);
		schedule = new Schedule();
		schedule.setCronEntry("*0");
		schedule.setDelay(1L);
		schedule.setPeriod(2L);
		schedule.setRepeat(3);
	}

	@Test
	public void shouldTransformEmailNotificationIntoTextMessage() throws JMSException {
		when(decoratedTransformer.transform(notification)).thenReturn(textMessage);
		when(notification.getSchedule()).thenReturn(schedule);

		Message message = transformer.transform(notification);

		verify(message).setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "*0");
		verify(message).setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1L);
		verify(message).setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 2L);
		verify(message).setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);
	}

	//TODO test exceptions
}
