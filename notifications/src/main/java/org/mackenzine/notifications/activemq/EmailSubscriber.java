package org.mackenzine.notifications.activemq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("broker")
public class EmailSubscriber implements MessageListener {

	static private Logger LOGGER = Logger.getLogger(EmailSubscriber.class);

	private Session emailSession;
	private Properties accountPorperties;

	public EmailSubscriber() throws IOException {
		Properties emailServerPorperties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("email_server.properties");
		emailServerPorperties.load(stream);

		getAccountProperties(loader);

		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(accountPorperties.getProperty("sender.user"), accountPorperties.getProperty("sender.password"));
			}
		};
		emailSession = Session.getInstance(emailServerPorperties, authenticator);
	}

	@Override
	public void onMessage(final Message message) {
		try {

			javax.mail.Message emailMessage = new MimeMessage(emailSession);
			emailMessage.setFrom(new InternetAddress(accountPorperties.getProperty("sender.address")));

			//TODO: check for undefined activeMQ message properties
			emailMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(message.getStringProperty("addressList")));
			emailMessage.setSubject(message.getStringProperty("subject"));
			emailMessage.setText(message.getStringProperty("body"));

			Transport.send(emailMessage);
		} catch (AddressException e) {
			LOGGER.error("Error parsing mail recipients", e);
			throw new RuntimeException(e.getMessage());
		} catch (MessagingException e) {
			LOGGER.error("Error sending mail", e);
			throw new RuntimeException(e.getMessage());
		} catch (JMSException e) {
			LOGGER.error("Error reading properties from message", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	private void getAccountProperties(final ClassLoader loader) throws IOException {
		accountPorperties = new Properties();
		InputStream stream = loader.getResourceAsStream("email_account.properties");
		accountPorperties.load(stream);
	}
}
