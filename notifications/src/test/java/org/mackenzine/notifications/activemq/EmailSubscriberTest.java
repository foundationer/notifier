package org.mackenzine.notifications.activemq;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mackenzine.notifications.activemq.EmailSubscriber;


public class EmailSubscriberTest {

	private EmailSubscriber subscriber;
	private Message message;

	@Before
	public void setUp() throws IOException {
		subscriber = new EmailSubscriber();
	}

	/**
	 * Warning: this is a very-low expectation test.
	 * It also needs to have access to a working email account.
	 * But it should be useful to send a test mail to a controlled mail account.
	 * @throws JMSException
	 */
	@Ignore("Configure email account before running this test")
	@Test
	public void shouldSendMessage() throws JMSException {
		createWellFormedMessage();

		try {
			subscriber.onMessage(message);
		} catch (RuntimeException e) {
			fail("Exception thrown");
		}
		assertTrue(true);
	}

	@Test
	public void shouldThrowExceptionWhenMessageIsMalformed() throws JMSException {
		message = new ActiveMQTextMessage();
		message.setStringProperty("addressList", "Chimichanga!");

		try {
			subscriber.onMessage(message);
		} catch (RuntimeException e) {
			// TODO check exception message
			assertTrue(true);
			return;
		}
		fail("No exception thrown");
	}

	private void createWellFormedMessage() throws JMSException {
		message = new ActiveMQTextMessage();
		message.setStringProperty("addressList", "godoy.lucas@gmail.com");
		message.setStringProperty("subject", "Hello world!");
		message.setStringProperty("body", "Message body");
	}
}
