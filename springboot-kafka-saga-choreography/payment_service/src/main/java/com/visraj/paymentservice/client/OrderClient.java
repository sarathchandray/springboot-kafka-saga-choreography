package com.visraj.paymentservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.visraj.domainobjects.dto.CustomerOrder;

@HttpExchange
public interface OrderClient {

	@GetExchange("/orders/{orderId}")
	public CustomerOrder getOrderById(@PathVariable("orderId") String id);
	
}
