package org.mackenzine.notifications.activemq;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

public class ExceptionListener implements javax.jms.ExceptionListener{

	static Logger LOGGER  = Logger.getLogger(ExceptionListener.class);
	
	@Override
	public void onException(JMSException exception) {
		LOGGER.error(exception.getMessage(), exception);
	}
}
