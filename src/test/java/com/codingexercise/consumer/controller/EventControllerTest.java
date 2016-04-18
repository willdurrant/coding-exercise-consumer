package com.codingexercise.consumer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.codingexercise.consumer.domain.Event;
import com.codingexercise.consumer.domain.EventAttribute;
import com.codingexercise.consumer.repository.EventRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JUnit test using Spring web test framework in standalone mode.
 * 
 * @author wdurrant
 */
public class EventControllerTest {

	private EventController classUnderTest = new EventController();

	private MockMvc mockMvc;

	@Mock
	private EventRepository mockEventRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest).build();
		ReflectionTestUtils.setField(classUnderTest, "eventRepository", mockEventRepository);
	}

	/**
	 * Tests the HTTP Post interaction.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostEvent() throws Exception {
		// mock data
		List<EventAttribute> attributes = new ArrayList<>();
		EventAttribute attribute1 = new EventAttribute("000123", "$1000", "John Smith", "Platinum");
		attributes.add(attribute1);
		Event event1 = new Event(attributes);

		// set up the stubbed interaction for the EventRepository
		when(mockEventRepository.save(event1)).thenReturn(event1);

		mockMvc.perform(
				post("/events").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(event1)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andDo(print());

		// verify interaction with repository and that the attribute apiType has
		// been set to HTTP_RESTFUL
		ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
		Mockito.verify(mockEventRepository).save(argument.capture());
		Assert.assertEquals("HTTP_RESTFUL", argument.getValue().getApiType());
	}

	/**
	 * Tests the HTTP Post interaction when error occurs HTTP status of 400
	 * should be returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostEvent_Error() throws Exception {

		//for no event data passed in expect back a 400 error
		mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	/*
	 * Util method to convert JSON object into byte[].
	 * 
	 */
	private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

}
