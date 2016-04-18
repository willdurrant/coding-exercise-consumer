package com.codingexercise.consumer.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codingexercise.consumer.domain.Event;
import com.codingexercise.consumer.domain.EventAttribute;
import com.codingexercise.consumer.repository.EventRepository;

/**
 * Spring web controller class for the Restful Events service.
 * 
 * @author wdurrant
 */
@Controller
@RequestMapping("/events")
public class EventController {

	private static final Logger log = LoggerFactory.getLogger(EventController.class);
	
	private static final String HTTP_RESTFUL = "HTTP_RESTFUL";

	@Autowired
	private EventRepository eventRepository;

	/**
	 * Restful endpoint for accepting HTTP post requests containing Event data as JSON.
	 * 
	 * @param event as RequestBody of a HTTP post request
	 * @return the persisted Event via the HTTP response
	 */
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Event> persistEvent(@RequestBody Event event) {
		Assert.notNull(event, "Event cannot be null when posting to /events");
		List<EventAttribute> eventAttributes = event.getEventAttributes();
		for (EventAttribute eventAttribute : eventAttributes) {
			log.debug(
					"Recieved new via Restful API an event for account number {} and tx amount of {} . About to persist it to the repository.",
					eventAttribute.getAccountNum(), eventAttribute.getTxAmount());
			event.setApiType(HTTP_RESTFUL);
		}
		//persist to Mongo
		Event persistedEvent = eventRepository.save(event);
		//return the persisted event complete with new id field
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new ResponseEntity<Event>(persistedEvent, headers, HttpStatus.CREATED);
	}

}
