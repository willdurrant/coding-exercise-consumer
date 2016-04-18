package com.codingexercise.consumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.codingexercise.consumer.domain.Event;

/**
 * Spring Data's MongoRepository interface. As we don't have any custom
 * requirements it sufficient to have this minimal implementation.
 * 
 * @author wdurrant
 */
public interface EventRepository extends MongoRepository<Event, String> {
}
