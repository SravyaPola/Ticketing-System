package com.synex.config;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;

@Configuration
public class JmsConfig {

	@Bean
	public MappingJackson2MessageConverter jacksonJmsMessageConverter(ObjectMapper mapper) {
		MappingJackson2MessageConverter conv = new MappingJackson2MessageConverter();
		conv.setTargetType(MessageType.TEXT);
		conv.setTypeIdPropertyName("_type");
		conv.setObjectMapper(mapper);
		return conv;
	}

	@Bean
	public JmsTemplate jmsTemplate(ConnectionFactory cf, MappingJackson2MessageConverter converter) {
		JmsTemplate jt = new JmsTemplate(cf);
		jt.setMessageConverter(converter);
		jt.setReceiveTimeout(0L);
		return jt;
	}

	@Bean
	public DefaultJmsListenerContainerFactory myFactory(ConnectionFactory cf,
			DefaultJmsListenerContainerFactoryConfigurer configurer, MappingJackson2MessageConverter converter) {

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, cf);
		factory.setSessionTransacted(true);
		factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		factory.setConcurrency("1-5");
		factory.setMessageConverter(converter);
		return factory;
	}
}
