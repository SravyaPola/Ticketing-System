package com.synex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class TicketMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketMicroserviceApplication.class, args);
	}

}
