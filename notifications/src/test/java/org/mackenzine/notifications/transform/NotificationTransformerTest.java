package org.mackenzine.notifications.transform;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mackenzine.notifications.model.Notification;
import org.mackenzine.notifications.transform.EmailNotificationTransformer;
import org.mackenzine.notifications.transform.NotificationTransformer;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NotificationTransformerTest {

	private static final String EXCEPTION_MESSAGE = "Ouch!";

	private NotificationTransformer transformer;
	private Notification notification;
	private final DateTime now = DateTime.now();

	@Mock private Session session;
	@Mock private TextMessage textMessage;

	@Rule public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		notification = new Notification(now, now, "Dear John", "Follow the white rabbit");
		transformer = new EmailNotificationTransformer(session);
	}

	public void shouldThrowExceptionWhenCannotCreateTextMessage() throws JMSException {
		when(session.createTextMessage()).thenThrow(new JMSException(EXCEPTION_MESSAGE));

		transformer.transform(notification);

		exception.expect(JMSException.class);
		exception.expectMessage(EXCEPTION_MESSAGE);
	}

	public void shouldThrowExceptionWhenCannotSetPropertyToTextMessage() throws JMSException {
		doThrow(new JMSException(EXCEPTION_MESSAGE)).when(textMessage).setStringProperty(anyString(), anyString());

		transformer.transform(notification);

		exception.expect(JMSException.class);
		exception.expectMessage(EXCEPTION_MESSAGE);
	}

	@Test
	public void shouldTransformEmailNotificationIntoTextMessage() throws JMSException {
		when(session.createTextMessage()).thenReturn(textMessage);

		Message message = transformer.transform(notification);

		verify(message).setStringProperty("subject", notification.getSubject());
		verify(textMessage).setStringProperty("body", notification.getContent());
	}
}
