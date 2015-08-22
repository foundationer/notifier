package org.mackenzine.notifications.rest;

import java.util.Collection;
import java.util.Collections;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.mackenzine.notifications.activemq.EmailSubscriber;
import org.mackenzine.notifications.activemq.NotificationPublisher;
import org.mackenzine.notifications.model.Notification;
import org.mackenzine.notifications.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/notify")
public class NotificationController {

	private static Logger LOGGER = Logger.getLogger(NotificationController.class);

	@Autowired private NotificationPublisher publisher;
	@Autowired private EmailSubscriber emailSubscriber;

	@RequestMapping(value="/manual", method=RequestMethod.POST)
	@ResponseBody
	public Collection<Message> sendManualNotification(@RequestParam final String fromDate, @RequestParam final String toDate, @RequestParam final String updateType) {
		Subscription subscription = new Subscription(toDate);
		subscription.addNotification(new Notification(DateTime.parse(fromDate), DateTime.parse(toDate), "subject", "content"));

		try {
			publisher.createTopic(subscription);
			if("EMAIL".equals(updateType.toUpperCase())) {
				publisher.registerSubscriber(subscription, emailSubscriber);
			}
			return publisher.process(subscription);
		} catch (JMSException e) {
			LOGGER.error("Error processing subscription", e);
			return Collections.emptyList();
		}
	}

	@RequestMapping(value="/schedule", method=RequestMethod.POST)
	public Collection<Message> scheduleNotification(@RequestParam final String fromDate, @RequestParam final String toDate, @RequestParam final String updateType) {
		Subscription subscription = new Subscription(toDate);
		subscription.addNotification(new Notification(DateTime.parse(fromDate), DateTime.parse(toDate), "subject", "content"));

		try {
			publisher.createTopic(subscription);
			if("EMAIL".equals(updateType.toUpperCase())) {
				publisher.registerSubscriber(subscription, emailSubscriber);
			}
			// TODO refactor to use ScheduledNotificationTransformer here
			// The only difference between the two requests will be the call to process
			// So maybe we can use a template method here
			return publisher.process(subscription);
		} catch (JMSException e) {
			LOGGER.error("Error processing subscription", e);
			return Collections.emptyList();
		}
	}
}
