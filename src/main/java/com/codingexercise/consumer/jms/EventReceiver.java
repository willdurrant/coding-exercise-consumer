package com.codingexercise.consumer.jms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.codingexercise.consumer.domain.Event;
import com.codingexercise.consumer.domain.EventAttribute;
import com.codingexercise.consumer.repository.EventRepository;

@Component
public class EventReceiver {

	private static final String JMS_BROKER = "JMS_BROKER";

	private static final String MAILBOX_EVENTS_DESTINATION = "mailbox-events-destination";

	private static final Logger log = LoggerFactory.getLogger(EventReceiver.class);

	@Autowired
	private EventRepository eventRepository;

	/**
	 * JMS Listener to receive the Events as individual string messages. These
	 * are parsed into Event objects are persisted in the repository.
	 * 
	 * @param message
	 *            as String
	 */
	@JmsListener(destination = MAILBOX_EVENTS_DESTINATION)
	public void receiveMessage(String message) {
		log.debug("EventReceiver received new message from JMS broker {}", message);
		try {
			persistEvent(convertToEvent(message));
		} catch (RuntimeException e) {
			// log any RuntimeExceptions but the application can continue
			log.error(e.getMessage(), e);
		}
	}

	/*
	 * Persists an Event to the repository.
	 */
	private void persistEvent(Event event) {
		Assert.notNull(event, "Event cannot be null persisting received JMS message");
		List<EventAttribute> eventAttributes = event.getEventAttributes();
		for (EventAttribute eventAttribute : eventAttributes) {
			log.debug(
					"Recieved new via JMS Broker API an event for account number {} and tx amount of {} . About to persist it to the repository.",
					eventAttribute.getAccountNum(), eventAttribute.getTxAmount());
			event.setApiType(JMS_BROKER);
		}
		eventRepository.save(event);
	}

	/*
	 * Converts string JSON message to an Event object.
	 */
	private Event convertToEvent(String message) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject) parser.parse(message);
		} catch (ParseException e) {
			throw new RuntimeException("ParseException caught trying to parse message text into JSON object");
		}
		JSONArray events = (JSONArray) jsonObject.get("events");
		List<EventAttribute> eventAttributes = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = events.iterator();
		while (iterator.hasNext()) {
			JSONObject jsonT = (JSONObject) iterator.next();
			JSONObject jsonEventAttribute = (JSONObject) jsonT.get("attributes");
			String accountNum = (String) jsonEventAttribute.get("Account Number");
			String txAmount = (String) jsonEventAttribute.get("Transaction Amount");
			String cardMemberName = (String) jsonEventAttribute.get("Name");
			String product = (String) jsonEventAttribute.get("Product");
			EventAttribute eventAttribute = new EventAttribute(accountNum, txAmount, cardMemberName, product);
			eventAttributes.add(eventAttribute);
		}

		return new Event(eventAttributes);
	}

}
