package com.visraj.paymentservice.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.visraj.domainobjects.dto.CustomerOrder;
import com.visraj.paymentservice.client.OrderClient;
import com.visraj.paymentservice.entity.Payment;
import com.visraj.paymentservice.repository.PaymentRepository;

@RestController
@RequestMapping("/api")
class PaymentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private OrderClient orderClient;
	
	@Autowired
	private WebClient webClient;
	
	@GetMapping("/payments/{payId}")
	public ResponseEntity<CustomerOrder> getOrderDetailsByPaymentId(@PathVariable("payId") Long payId) {
		
		CustomerOrder customerOrder = new CustomerOrder();
		
		Payment payment = paymentRepository .findById(payId).get();
		if(payment != null) {
			LOGGER.info("payment.getOrderId() " + payment.getOrderId());
			//customerOrder = orderClient.getOrderById(String.valueOf(payment.getOrderId()));
			
			// Using WebClient
	        customerOrder = webClient.get().uri("/orders/" + String.valueOf(payment.getOrderId())).retrieve()
	        		.bodyToMono(CustomerOrder.class).block();
		}
		
		return ResponseEntity.ok(customerOrder);
		
	}
}
 