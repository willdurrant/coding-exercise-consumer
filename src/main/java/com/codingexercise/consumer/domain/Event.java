package com.codingexercise.consumer.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "customer_events")
public class Event {

	@Id
	private String id;

	@Field("attributes")
	private List<EventAttribute> eventAttributes;

	public Event() {}

	public Event(List<EventAttribute> eventAttributes) {
		this.eventAttributes = eventAttributes;
	}

	public String getId() {
		return id;
	}

	public List<EventAttribute> getEventAttributes() {
		return eventAttributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventAttributes == null) ? 0 : eventAttributes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (eventAttributes == null) {
			if (other.eventAttributes != null)
				return false;
		} else if (!eventAttributes.equals(other.eventAttributes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
