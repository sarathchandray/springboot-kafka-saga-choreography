package com.visraj.paymentservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.visraj.domainobjects.events.dto.OrderEvent;

@Service
public class ReversedOrderProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReversedOrderProducer.class);
	
	@Autowired
	private NewTopic reverseOrderTopic;
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	public ReversedOrderProducer(NewTopic reverseOrderTopic, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
		this.reverseOrderTopic = reverseOrderTopic;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendMessage(OrderEvent event) {
		
		LOGGER.info(String.format("Order Event => %s", event.toString()));
		
		//create message
		
		Message<OrderEvent> message = MessageBuilder
										.withPayload(event)
										.setHeader(KafkaHeaders.TOPIC, reverseOrderTopic.name())
										.build();
		kafkaTemplate.send(message);
	}
	
	
}
