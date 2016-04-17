# coding-exercise-consumer

This is the Consumer component designed to be run with the Producer component from coding-exercise-producer. The function of the Consumer is to persist Customer Event data as received from the Producer. This data can be received either via a HTTP Restful service or via a JMS Broker.


## Prerequisites

- Java 8
- MongoDB version 3.x
- Apache ActiveMQ (optional if wanting to use the JMS Broker)
- Maven 3.x (for development)

## Building the project

The project is built using Spring 4 Boot.

Clone repository locally

```bash
cd coding-exercise-consumer
mvn clean install
```

Optional if importing into Eclipse/STS
```bash
mvn eclipse:eclipse 
```

## Running the project via command line

- Make sure Mongo is running on localhost with url of `mongodb://localhost/`
- Optional for using JMS broker make sure ActiveMQ is running on the URL 'tcp://localhost:61616'

```bash
cd coding-exercise-consumer
mvn clean package
java -jar target/consumer-0.0.1-SNAPSHOT.jar
```

## Verifying data has been persisted

- In the console logs you should see some indication of events being received either via HTTP or JMS and persisted in the database
- Within the Mongo CLI you should see the data by doing the following;

```javascript
> use customer_event_repository
> db.customer_events.find()
```

- Note: the additional attribute 'apiType' has been added to the customer_event document. This indicates the API type by which the Producer sent the data to the Consumer - either HTTP_RESTFUL or JMS_BROKER
