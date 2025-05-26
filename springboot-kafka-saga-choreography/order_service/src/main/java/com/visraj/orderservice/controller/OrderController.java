package com.visraj.orderservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visraj.domainobjects.dto.CustomerOrder;
import com.visraj.domainobjects.events.dto.OrderEvent;
import com.visraj.orderservice.entity.Order;
import com.visraj.orderservice.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	KafkaTemplate<String, OrderEvent> kafkaTemplate;
	
	@PostMapping("/create")
	public String createOrder(@RequestBody CustomerOrder customerOrder) {
		
		Order order = new Order();
		order.setAmount(customerOrder.getAmount());
		order.setItem(customerOrder.getItem());
		order.setQuantity(customerOrder.getQuantity());
		order.setStatus("Created");
		
		try {
			order = orderRepository.save(order);

			customerOrder.setOrderId(order.getId());
			
			//need to set order event to push data to kafka
			OrderEvent orderEvent = new OrderEvent();
			orderEvent.setOrder(customerOrder);
			orderEvent.setType("ORDER_CREATED");
			
			kafkaTemplate.send("new-orders", orderEvent);
			
			LOGGER.info(String.format("New Order is placed in topic : %s" , orderEvent));
			
			
		} catch (Exception e) {

			LOGGER.info(String.format("OrderController : %s" , e.getMessage()));

			order.setStatus("Failed");
			orderRepository.save(order);
		}	
		
		return "Order #"+customerOrder.getOrderId()+" is successfully created!";
		
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<CustomerOrder> getOrderById(@PathVariable("orderId") Long id) {
	    LOGGER.info("++ getOrderById() : " + id);
	    return orderRepository.findById(id)
	        .map(order -> {
	            CustomerOrder customerOrder = new CustomerOrder();
	            customerOrder.setOrderId(order.getId());
	            customerOrder.setItem(order.getItem());
	            customerOrder.setAmount(order.getAmount());
	            customerOrder.setQuantity(order.getQuantity());
	            return ResponseEntity.ok(customerOrder);
	        })
	        .orElse(ResponseEntity.notFound().build());
	}
	
	
	@GetMapping("/greet")
	public String greet() {
		return "Hello!!";
	}
	


}
