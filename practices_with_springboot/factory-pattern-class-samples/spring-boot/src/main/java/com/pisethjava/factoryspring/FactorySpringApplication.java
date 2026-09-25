package com.pisethjava.factoryspring;

import com.pisethjava.factoryspring.export.ExportService;
import com.pisethjava.factoryspring.payment.PaymentService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class FactorySpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(FactorySpringApplication.class, args);
    }
}

@Component
class DemoRunner implements CommandLineRunner {
    private final PaymentService paymentService;
    private final ExportService exportService;

    public DemoRunner(PaymentService paymentService, ExportService exportService) {
        this.paymentService = paymentService;
        this.exportService = exportService;
    }

    @Override
    public void run(String... args) {
        paymentService.pay("ABA", BigDecimal.valueOf(25));
        paymentService.pay("KHQR", BigDecimal.valueOf(40));
        exportService.export("PDF", "Student list");
        exportService.export("JSON", "Student list");
    }
}
