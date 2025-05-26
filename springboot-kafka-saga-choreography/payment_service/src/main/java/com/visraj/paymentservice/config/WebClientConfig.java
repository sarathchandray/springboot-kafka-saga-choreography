package com.visraj.paymentservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.visraj.paymentservice.client.OrderClient;

@Configuration
public class WebClientConfig {

//	@Autowired
//	private LoadBalancedExchangeFilterFunction filterFunction;
	
    @Bean
    @LoadBalanced
    public WebClient orderWebClient() {
        return WebClient
        		.builder()
        		.baseUrl("http://order-service")
        		//.filter(filterFunction)
        		.build();
    }

    @Bean
    public OrderClient orderClient(WebClient.Builder builder) {
        
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(orderWebClient()))
                .build();

        return factory.createClient(OrderClient.class);
    }
    
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
