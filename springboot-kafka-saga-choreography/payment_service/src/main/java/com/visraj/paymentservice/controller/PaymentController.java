package com.visraj.paymentservice.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visraj.domainobjects.dto.CustomerOrder;
import com.visraj.domainobjects.events.dto.OrderEvent;
import com.visraj.domainobjects.events.dto.PaymentEvent;
import com.visraj.paymentservice.entity.Payment;
import com.visraj.paymentservice.repository.PaymentRepository;

@Controller
public class PaymentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;
	
	@Autowired
	KafkaTemplate<String, OrderEvent> orderKafkaTemplate;

	@KafkaListener(topics = "new-orders", groupId = "orders-group")
	public void processPayment(String event) throws Exception {
		
		LOGGER.info(String.format("Process payment event : %s", event));
		
		OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
		CustomerOrder order = orderEvent.getOrder();
		
		Payment payment = new Payment();
		payment.setOrderId(order.getOrderId());
		payment.setAmount(order.getAmount());
		payment.setMode(order.getPaymentMode());
		payment.setStatus("Success");
		
		try {
			if(!"online".equalsIgnoreCase(order.getPaymentMode())) {
				throw new RuntimeException(String.format("Payment method %s is not supported!", payment.getMode()));
			}
			
			paymentRepository.save(payment);
			
			PaymentEvent paymentEvent = new PaymentEvent();
			paymentEvent.setOrder(order);
			paymentEvent.setType("PAYMENT_CREATED");
			
			paymentKafkaTemplate.send("new-payments", paymentEvent);
			
			LOGGER.info("New payment is placed in topic : %s" , paymentEvent);

		} catch (Exception e) {
			
			LOGGER.error(String.format("Exception during processPayment() : %s", e.getMessage()));
			
			payment.setStatus("Failed");
			paymentRepository.save(payment);
			
			OrderEvent orderEvt = new OrderEvent();
			orderEvt.setOrder(order);
			orderEvt.setType("ORDER_REVERSED");
			orderKafkaTemplate.send("reversed-orders", orderEvt);
		}	
		
	}
}
