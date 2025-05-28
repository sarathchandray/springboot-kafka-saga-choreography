package com.visraj.paymentservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {


	@Value("${spring.kafka.producer.payment.topic.name}")
	private String paymentTopicName;
	
	@Bean
	public NewTopic paymentTopic() {
		return TopicBuilder.name(paymentTopicName).build();
	}
	
	@Value("${spring.kafka.producer.order.reverse.topic.name}")
	private String orderReverseTopicName;
	
	@Bean
	public NewTopic reverseOrderTopic() {
		return TopicBuilder.name(orderReverseTopicName).build();
	}
	
}
