package com.synex.config;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;

@Configuration
public class JmsConfig {

	@Bean
	public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter conv = new MappingJackson2MessageConverter();
		conv.setTargetType(MessageType.TEXT);
		conv.setTypeIdPropertyName("_type");
		return conv;
	}

	@Bean
	public DefaultJmsListenerContainerFactory myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer, MappingJackson2MessageConverter converter) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		configurer.configure(factory, connectionFactory);
		// then tweak as needed
		factory.setSessionTransacted(true);
		factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		factory.setConcurrency("1-5");
		factory.setMessageConverter(converter);
		return factory;
	}
}
