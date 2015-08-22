package org.mackenzine.notifications.activemq;

import java.util.ArrayList;
import java.util.Collection;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.mackenzine.notifications.model.Notification;
import org.mackenzine.notifications.model.Subscription;
import org.mackenzine.notifications.transform.EmailNotificationTransformer;
import org.mackenzine.notifications.transform.NotificationTransformer;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


@Component
@DependsOn("broker")
public class NotificationPublisher {

	private String topicName;
	private final ConnectionFactory connectionFactory;
	private final Connection connection;
	private Session session;
	private MessageProducer producer;
	private NotificationTransformer transformer;
	private ActiveMQTopic topic;

	public NotificationPublisher() throws JMSException {
		this.connectionFactory = new ActiveMQConnectionFactory();
		this.connection = this.connectionFactory.createConnection();
	}

	public Topic createTopic(final Subscription subscription) throws JMSException {
		topicName = subscription.getName();
		topic = new ActiveMQTopic(topicName);
		return topic;
	}

	public TopicSubscriber registerSubscriber(final Subscription subscription, final MessageListener listener) throws JMSException {
		connection.setClientID("Mackenzine Notifications");
		session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		producer = session.createProducer(topic);
		connection.start();
		transformer = new EmailNotificationTransformer(session);
		TopicSubscriber subscriber = this.session.createDurableSubscriber(topic, subscription.getName(), "selector", false);
		subscriber.setMessageListener(listener);
		return subscriber;
	}

	public Collection<Message> process(final Subscription subscription) throws JMSException {
		Collection<Message> messagesSent = new ArrayList<Message>();
		for (Notification notification : subscription.getNotifications()) {
			Message message = transformer.transform(notification);
			message.setStringProperty("addressList", subscription.getSubscribersEmails());
			// Delivery mode is persistent by default
			producer.send(message);
		}
		return messagesSent;
	}
}
