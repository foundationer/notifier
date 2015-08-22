package org.mackenzine.notifications.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.activemq.broker.BrokerService;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestContext.class})
@WebAppConfiguration
public class NotificationControllerTest {

	private MockMvc mock;

	@Autowired private WebApplicationContext webApplicationContext;
	@Autowired BrokerService broker;

	@Before
	public void setUp() {
		mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@After
	public void tearDown() throws Exception {
		broker.stop();
	}

	@Test
	public void shouldTriggerManualEmailUpdate() throws Exception {
		mock.perform(post("/notify/manual")
				.param("toDate", DateTime.now().toString())
				.param("fromDate", DateTime.now().plusMonths(1).toString())
				.param("updateType", "email"))
				.andExpect(status().isOk());
	}

	@Test
	@Ignore("Work in progress. The test pass, but the controller is not doing what we want yet")
	public void shouldTriggerScheduledEmailUpdate() throws Exception {
		mock.perform(post("/notify/schedule")
				.param("toDate", DateTime.now().toString())
				.param("fromDate", DateTime.now().plusMonths(1).toString())
				.param("updateType", "email"))
				.andExpect(status().isOk());
	}
}
