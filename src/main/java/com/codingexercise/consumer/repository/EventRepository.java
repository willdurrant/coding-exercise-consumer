package com.codingexercise.consumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.codingexercise.consumer.domain.Event;

public interface EventRepository extends MongoRepository<Event, String> {

}
