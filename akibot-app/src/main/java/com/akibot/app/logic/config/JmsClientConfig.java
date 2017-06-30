package com.akibot.app.logic.config;

import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableJms
public class JmsClientConfig {

	/*
	 * @Bean ActiveMQConnectionFactory connectionFactory() {
	 * System.out.println("- connectionFactory"); ActiveMQConnectionFactory
	 * factory = new ActiveMQConnectionFactory(
	 * "vm://localhost?broker.persistent=false&broker.useJmx=false&create=true")
	 * ; factory.setTrustedPackages(Arrays.asList("com.akibot", "java.util"));
	 * return factory; }
	 * 
	 * @Bean JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
	 * System.out.println("- jmsTemplate"); JmsTemplate jmsTemplate = new
	 * JmsTemplate(); jmsTemplate.setConnectionFactory(connectionFactory);
	 * jmsTemplate.setReceiveTimeout(3000L);
	 * jmsTemplate.setSessionTransacted(false);
	 * jmsTemplate.setDeliveryPersistent(false); return jmsTemplate; }
	 * 
	 * @Bean JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate)
	 * { System.out.println("- jmsMessagingTemplate"); JmsMessagingTemplate
	 * jmsMessagingTemplate = new JmsMessagingTemplate(jmsTemplate); return
	 * jmsMessagingTemplate; }
	 * 
	 * @Bean DefaultJmsListenerContainerFactory
	 * jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
	 * System.out.println("- jmsListenerContainerFactory");
	 * DefaultJmsListenerContainerFactory factory = new
	 * DefaultJmsListenerContainerFactory();
	 * factory.setConnectionFactory(connectionFactory);
	 * factory.setSessionTransacted(false); return factory; }
	 */
}
