package org.mackenzine.notifications.rest;

import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="org.mackenzine.notifications")
public class TestContext extends WebMvcConfigurerAdapter {

	private static final String BROKER_URL = "tcp://localhost:61616";

	@Bean
	public BrokerService broker() throws Exception {
		BrokerService broker = new BrokerService();
		broker.addConnector(BROKER_URL);
		broker.setPersistent(false);
		broker.start();
		return broker;
	}
}
