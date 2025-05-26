package com.visraj.paymentservice.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    @Autowired
//    private WebClient.Builder loadBalancedWebClientBuilder;
//
//    @GetMapping("/payments/{payId}")
//    public Mono<String> testLoadBalancer(@PathVariable("payId") Long payId) {
//        return loadBalancedWebClientBuilder
//            .build()
//            .get()
//            .uri("http://ORDER_SERVICE/orders/{id}", payId)
//            .retrieve()
//            .bodyToMono(String.class)
//            .onErrorResume(WebClientResponseException.class, ex -> {
//                if (ex.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
//                    return Mono.just("Internal Server Error: " + ex.getMessage());
//                } else {
//                    return Mono.error(ex);
//                }
//            })
//            .onErrorResume(Exception.class, ex -> Mono.just("An unexpected error occurred: " + ex.getMessage()));
//    }

}