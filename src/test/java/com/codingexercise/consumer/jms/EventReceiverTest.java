package com.codingexercise.consumer.jms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.codingexercise.consumer.domain.Event;
import com.codingexercise.consumer.repository.EventRepository;

public class EventReceiverTest {

	private EventReceiver classUnderTest = new EventReceiver();

	@Mock
	private EventRepository mockEventRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(classUnderTest, "eventRepository", mockEventRepository);
	}

	@Test
	public void testReceiveMessage() {
		String message = "{\"events\":[{\"attributes\":{\"Account Number\":\"370000000000000\",\"Transaction Amount\":\"USD 25000.00\",\"Name\":\"Lewis Hamilton\",\"Product\":\"Centurion\"}}]}";
		classUnderTest.receiveMessage(message);
		// verify interaction with repository and that the attribute apiType has
		// been set to JMS_BROKER
		ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
		Mockito.verify(mockEventRepository).save(argument.capture());
		Assert.assertEquals("JMS_BROKER", argument.getValue().getApiType());
	}

	@Test
	public void testReceiveMessage_inValidMessage() {
		String message = "{someIncorrectEventMessageFormat}";
		classUnderTest.receiveMessage(message);
		// verify no interaction with repository as message isn't convertable to an event
		Mockito.verify(mockEventRepository, Mockito.never()).save(Mockito.any(Event.class));
	}

}
