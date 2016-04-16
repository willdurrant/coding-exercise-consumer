package com.codingexercise.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

import com.codingexercise.consumer.controller.EventController;
import com.codingexercise.consumer.repository.EventRepository;

@SpringBootApplication
public class ConsumerApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(ConsumerApplication.class);

	@Autowired
	private EventRepository eventRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {

		log.debug("Preparing the repository deleting any previous documents in the customer_events collection");
		eventRepository.deleteAll();
		Assert.isTrue(eventRepository.count() == 0, "Event repository should have 0 documents in the customer_events collection!");
	}
	
}
