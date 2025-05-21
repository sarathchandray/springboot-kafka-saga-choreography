package com.visraj.orderservice.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visraj.domainobjects.events.dto.OrderEvent;
import com.visraj.orderservice.entity.Order;
import com.visraj.orderservice.repository.OrderRepository;

@Service
public class ReverseOrder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReverseOrder.class);
	
	@Autowired
	OrderRepository orderRepository;
	
	@KafkaListener(topics = "reversed-orders", groupId = "orders-group")
	public void reverseOrder(String event) throws Exception {
		
		LOGGER.info(String.format("Reverse order event %s", event));
		
		try {
			OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
			Optional<Order> order = orderRepository.findById(orderEvent.getOrder().getOrderId());
			
			order.ifPresent(o -> { 
				
				o.setStatus("Failed"); 
				orderRepository.save(o);
			});
		} catch (Exception e) {

			LOGGER.info("Exception occured while reverting order details");
		}	
	}
}
