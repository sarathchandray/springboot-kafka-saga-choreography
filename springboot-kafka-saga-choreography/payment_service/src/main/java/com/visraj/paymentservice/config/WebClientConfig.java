package com.visraj.paymentservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.visraj.paymentservice.client.OrderClient;

@Configuration
public class WebClientConfig {


	@Autowired
	private LoadBalancedExchangeFilterFunction  loadBalancedExchangeFilterFunction;
	
	//configuration for webclient that will be pointing to order_service
	
	@Bean
	public WebClient orderWebClient() {
		
		return WebClient
			.builder()
			//lookup in service_registry
			.baseUrl("http://order_service")
			.filter(loadBalancedExchangeFilterFunction)
			.build();
	}
	
	
	@Bean
	public OrderClient orderClient(WebClient orderWebClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(orderWebClient)) // ✅ FIXED
                .build();

        return factory.createClient(OrderClient.class);
    }
	
	
}
