package org.mackenzine.notifications.activemq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mackenzine.notifications.activemq.NotificationPublisher;
import org.mackenzine.notifications.model.Subscription;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NotificationPublisherTest {

	private static final String BROKER_URL = "tcp://localhost:61616";
	private static final String TOPIC_NAME = "test";

	private NotificationPublisher publisher;
	private Subscription subscription;
	private BrokerService broker;

	@Mock private MessageListener listener;

	@Before
	public void setUp() throws Exception {
		broker = new BrokerService();
		broker.addConnector(BROKER_URL);
		broker.setPersistent(false);
		broker.start();
		publisher = new NotificationPublisher();
		subscription = new Subscription(TOPIC_NAME);
	}

	@After
	public void tearDown() throws Exception {
		broker.stop();
	}

	@Test
	public void shouldCreateTopic() throws JMSException {
		Topic createdTopic = publisher.createTopic(subscription);

		assertNotNull(createdTopic);
		assertEquals(TOPIC_NAME, createdTopic.getTopicName());
	}

	@Test
	public void shouldRegisterSubscriber() throws JMSException {
		publisher.createTopic(subscription);
		TopicSubscriber subscriber = publisher.registerSubscriber(subscription, listener);

		assertNotNull(subscriber);
		assertEquals(listener, subscriber.getMessageListener());
	}
}
