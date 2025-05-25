package com.visraj.paymentservice.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visraj.domainobjects.dto.CustomerOrder;
import com.visraj.paymentservice.client.OrderClient;
import com.visraj.paymentservice.repository.PaymentRepository;

@RestController
@RequestMapping("/api")
class PaymentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private OrderClient orderClient;
	
	@GetMapping("/payments/{payId}")
	public ResponseEntity<CustomerOrder> getOrderDetailsByPaymentId(@PathVariable Long payId) {
	    return paymentRepository.findById(payId)
	        .map(p -> {
	            LOGGER.info("payment.getOrderId() " + p.getOrderId());
	            CustomerOrder customerOrder = orderClient.getOrderById(String.valueOf(p.getOrderId()));
	            return ResponseEntity.ok(customerOrder);
	        })
	        .orElse(ResponseEntity.notFound().build());
	}

}
 