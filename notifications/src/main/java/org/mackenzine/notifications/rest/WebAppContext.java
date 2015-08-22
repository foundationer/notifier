package org.mackenzine.notifications.rest;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="org.mackenzine.notifications")
public class WebAppContext extends WebMvcConfigurerAdapter {

	private static final String BROKER_URL = "tcp://localhost:61616";

	@Bean
	public BrokerService broker() throws Exception {
		BrokerService broker = new BrokerService();
		JDBCPersistenceAdapter jdbc=new JDBCPersistenceAdapter();
		EmbeddedDataSource dataSource=new EmbeddedDataSource();
		dataSource.setDatabaseName(broker.getBrokerName());
		dataSource.setCreateDatabase("create");
		jdbc.setDataSource(dataSource);
		jdbc.deleteAllMessages();
		broker.setPersistenceAdapter(jdbc);
		broker.setUseVirtualTopics(false);
		broker.addConnector(BROKER_URL);
		return broker;
	}

	@Bean
	public MappingJackson2HttpMessageConverter messageConverter() {
		return new MappingJackson2HttpMessageConverter();
	}

	@Bean
	public RequestMappingHandlerAdapter handlingAdapter() {
		return new RequestMappingHandlerAdapter();
	}
}
