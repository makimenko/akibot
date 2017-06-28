package com.akibot.app.logic.config;

import java.util.Arrays;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsClientConfig {

	@Bean
	ActiveMQConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
		factory.setTrustedPackages(Arrays.asList("com.akibot", "java.util"));
		return factory;
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
