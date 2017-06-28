package com.akibot.web;

import static org.junit.Assert.assertEquals;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.akibot.web.config.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("fake")
@Import({ WebConfig.class, AkiBotWebApplicationTests.TestConfig.class })
@WebAppConfiguration
public class AkiBotWebApplicationTests {

	static class TestConfig {

		@Bean
		BrokerService brokerService() throws Exception {
			System.out.println("*** brokerService");
			BrokerService brokerService = new BrokerService();
			brokerService.setPersistent(false);
			return brokerService;
		}

		@Bean
		ActiveMQConnectionFactory connectionFactory() {
			return new ActiveMQConnectionFactory("vm://localhost");
		}

		@Bean
		JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
			JmsTemplate jmsTemplate = new JmsTemplate();
			jmsTemplate.setConnectionFactory(connectionFactory);
			jmsTemplate.setReceiveTimeout(3000L);
			jmsTemplate.setSessionTransacted(false);
			jmsTemplate.setDeliveryPersistent(false);
			return jmsTemplate;
		}

		@Bean
		JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
			JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate(jmsTemplate);
			return jmsMessagingTemplate;
		}

		@Bean
		DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
			DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
			factory.setConnectionFactory(connectionFactory);
			factory.setSessionTransacted(false);
			return factory;
		}

	}

	private TestRestTemplate restTemplate = new TestRestTemplate();
	private String BASE_URL = "http://localhost:8080/";

	@Test
	public void test() {
		//ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "scene/index.html", String.class);
		//assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
