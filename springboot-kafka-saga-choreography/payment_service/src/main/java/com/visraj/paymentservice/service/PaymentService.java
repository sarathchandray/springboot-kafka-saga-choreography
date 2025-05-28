package com.visraj.paymentservice.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visraj.domainobjects.dto.CustomerOrder;
import com.visraj.domainobjects.events.dto.OrderEvent;
import com.visraj.domainobjects.events.dto.PaymentEvent;
import com.visraj.paymentservice.entity.Payment;
import com.visraj.paymentservice.kafka.ReversedOrderProducer;
import com.visraj.paymentservice.kafka.PaymentProducer;
import com.visraj.paymentservice.repository.PaymentRepository;

import reactor.core.publisher.Mono;

@Controller
public class PaymentService {
	
	private final WebClient webClient;

    public PaymentService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private PaymentProducer paymentProducer;
	
	@Autowired
	private ReversedOrderProducer reversedOrderProducer;
	
//	@Autowired
//	KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;
	
//	@Autowired
//	KafkaTemplate<String, OrderEvent> orderKafkaTemplate;
	
	
	public Mono<CustomerOrder> getOrder(Long orderId) {
        return webClient.get()
                .uri("http://ORDER_SERVICE/orders/{id}", orderId)
                .retrieve()
                .bodyToMono(CustomerOrder.class);
    }

//  Consume as String using StringDeserializer
//	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
//	public void processPayment(String event) throws Exception {
//		
//		LOGGER.info(String.format("Process payment event : %s", event));
//		
//		OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
//		CustomerOrder order = orderEvent.getOrder();

	
	// Consume as Object using JsonDeserializer
	@KafkaListener(topics = "${spring.kafka.consumer.order.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void processPayment(OrderEvent orderEvent ) throws Exception {
		
		LOGGER.info(String.format("Process payment event : %s", orderEvent));
		
//		OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
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
			
			//paymentKafkaTemplate.send("new-payments", paymentEvent);
			
			paymentProducer.sendMessage(paymentEvent);
			
			LOGGER.info(String.format("New payment is placed in topic : %s" , paymentEvent));

		} catch (Exception e) {
			
			LOGGER.error(String.format("Exception during processPayment() : %s", e.getMessage()));
			
			payment.setStatus("Failed");
			paymentRepository.save(payment);
			
			OrderEvent orderEvt = new OrderEvent();
			orderEvt.setOrder(order);
			orderEvt.setType("ORDER_REVERSED");
			
			reversedOrderProducer.sendMessage(orderEvt);
			//orderKafkaTemplate.send("reversed-orders", orderEvt);
			
		}	
		
	}
}
