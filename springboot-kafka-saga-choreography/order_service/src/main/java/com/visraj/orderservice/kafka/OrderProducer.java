package com.visraj.orderservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.visraj.domainobjects.events.dto.OrderEvent;

@Service
public class OrderProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);
	
	@Autowired
	private NewTopic topic;
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	public OrderProducer(NewTopic topic, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendMessage(OrderEvent event) {
		
		LOGGER.info(String.format("Order Event => %s", event.toString()));
		
		//create message
		
		Message<OrderEvent> message = MessageBuilder
										.withPayload(event)
										.setHeader(KafkaHeaders.TOPIC, topic.name())
										.build();
		
		// send message asynchronously
		kafkaTemplate.send(message).whenComplete((sendResult, throwable) -> {
			
			if(throwable != null) {
				onFailure(throwable);
			} else {
				onSuccess(sendResult);
			}
			
		});
	}

	private void onSuccess(SendResult<String, OrderEvent> sendResult) {

		LOGGER.info(String.format("Topic : %s \nPartition : %s \nOffset : %s \nTimestamp : %s",
			sendResult.getRecordMetadata().topic(),
			sendResult.getRecordMetadata().partition(),
			sendResult.getRecordMetadata().offset(),
			sendResult.getRecordMetadata().timestamp()
		));
		
		
	}

	private void onFailure(Throwable throwable) {

		LOGGER.info(String.format("Exception while sending message : %s " , throwable));
		
	}
	
	
}
