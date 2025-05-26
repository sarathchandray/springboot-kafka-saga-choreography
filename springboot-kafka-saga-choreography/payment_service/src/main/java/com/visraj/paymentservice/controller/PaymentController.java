package com.visraj.paymentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.visraj.domainobjects.dto.CustomerOrder;
import com.visraj.paymentservice.client.OrderClient;
import com.visraj.paymentservice.repository.PaymentRepository;
import com.visraj.paymentservice.service.PaymentService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payments")
class PaymentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private OrderClient orderClient;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/{payId}")
	public ResponseEntity<CustomerOrder> getOrderDetailsByPaymentId(@PathVariable("payId") Long payId) {
		return paymentRepository.findById(payId).map(p -> {
			LOGGER.info("payment.getOrderId() " + p.getOrderId());
			ResponseEntity<CustomerOrder> customerOrder = orderClient.getOrderById(p.getOrderId());
			return customerOrder;
		}).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/order/{id}")
	public Mono<CustomerOrder> getOrderDetails(@PathVariable Long id) {
		return paymentService.getOrder(id);
	}

	@GetMapping("/orders/{payId}")
	public ResponseEntity<CustomerOrder> getOrderDtails(@PathVariable("payId") Long payId) {
		return paymentRepository.findById(payId).map(p -> {
			LOGGER.info("payment.getOrderId() " + p.getOrderId());
			CustomerOrder customerOrder = restTemplate.getForObject("http://order-service/orders/" + p.getOrderId(),
					CustomerOrder.class);
			return ResponseEntity.ok(customerOrder);
		}).orElse(ResponseEntity.notFound().build());
	}
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@GetMapping("/orders/greet")
	public String greetingFromOrder() {
		
		return webClientBuilder
	    //.baseUrl("http://order_service")
	    .build()
	    .get()
	    .uri("http://order-service/orders/greet")
	    .retrieve()
	    .bodyToMono(String.class).block();
		
	}

}
