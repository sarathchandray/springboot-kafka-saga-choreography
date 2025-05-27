package com.visraj.paymentservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.visraj.domainobjects.dto.CustomerOrder;

@HttpExchange("/orders")
public interface OrderClient {

	@GetExchange("/{orderId}")
	public ResponseEntity<CustomerOrder> getOrderById(@PathVariable("orderId") Long id);
	
	@GetExchange("/greet")
	public String greet();


	
}
