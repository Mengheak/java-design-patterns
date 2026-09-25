package com.pisethjava.builder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient roomWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("X-App-Name", "Builder Pattern Demo")
                .build();
    }
}
