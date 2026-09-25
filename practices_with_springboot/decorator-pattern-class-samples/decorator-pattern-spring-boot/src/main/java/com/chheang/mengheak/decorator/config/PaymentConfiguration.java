package com.chheang.mengheak.decorator.config;

import com.chheang.mengheak.decorator.payment.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfiguration {

    @Bean
    public PaymentGateway paymentGateway() {
        PaymentGateway core = new AbaPaymentAdapter(new AbaSdk());

        return new MetricsPaymentDecorator(
                new RetryPaymentDecorator(
                        new LoggingPaymentDecorator(core),
                        3
                )
        );
    }
}
