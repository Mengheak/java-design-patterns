package com.pisethjava.decorator.config;

import com.pisethjava.decorator.payment.*;
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
