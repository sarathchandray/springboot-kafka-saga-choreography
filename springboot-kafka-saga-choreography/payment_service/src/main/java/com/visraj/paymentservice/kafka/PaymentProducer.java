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
import com.visraj.domainobjects.events.dto.PaymentEvent;

@Service
public class PaymentProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProducer.class);
	
	@Autowired
	private NewTopic paymentTopic;
	
	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

	public PaymentProducer(NewTopic paymentTopic, KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
		this.paymentTopic = paymentTopic;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendMessage(PaymentEvent event) {
		
		LOGGER.info(String.format("Payement Event => %s", event.toString()));
		
		//create message
		
		Message<PaymentEvent> message = MessageBuilder
										.withPayload(event)
										.setHeader(KafkaHeaders.TOPIC, paymentTopic.name())
										.build();
		kafkaTemplate.send(message);
	}
	
	
}
