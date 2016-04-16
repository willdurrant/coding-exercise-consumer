package com.codingexercise.consumer.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codingexercise.consumer.domain.Event;
import com.codingexercise.consumer.domain.EventAttribute;
import com.codingexercise.consumer.repository.EventRepository;

@Controller
@RequestMapping("/events")
public class EventController {

	private static final Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventRepository eventRepository;

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Event> persistEvent(@RequestBody Event event) {
		Assert.notNull(event, "Event cannot be null when posting to /events");
		List<EventAttribute> eventAttributes = event.getEventAttributes();
		for (EventAttribute eventAttribute : eventAttributes) {
			log.debug(
					"Recieved new event for account number {} and tx amount of {} . About to persist it to the repository.",
					eventAttribute.getAccountNum(), eventAttribute.getTxAmount());
		}
		eventRepository.save(event);
		
		return new ResponseEntity<Event>(event, HttpStatus.CREATED);
	}

}
