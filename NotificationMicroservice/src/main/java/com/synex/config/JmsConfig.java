package com.synex.config;

import jakarta.jms.ConnectionFactory;

import java.util.List;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {

	/** The broker URL, e.g. "tcp://localhost:61616" */
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	/** Username for the broker (if any) */
	@Value("${spring.activemq.user:}")
	private String username;

	/** Password for the broker (if any) */
	@Value("${spring.activemq.password:}")
	private String password;

	/**
	 * ActiveMQ connection factory with trusted packages.
	 */
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		cf.setBrokerURL(brokerUrl);
		if (!username.isBlank()) {
			cf.setUserName(username);
			cf.setPassword(password);
		}
		// allow sending/receiving POJOs from this package
		cf.setTrustedPackages(List.of("com.example.notification.dto", "java.util"));
		return cf;
	}

	/**
	 * JMS Template for producing messages.
	 */
	@Bean
	public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory);
	}

	/**
	 * Listener container factory for @JmsListener methods.
	 */
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setConcurrency("1-5");
		return factory;
	}
}
