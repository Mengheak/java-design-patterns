package com.pisethjava.decorator.api;

import com.pisethjava.decorator.payment.PaymentGateway;
import com.pisethjava.decorator.payment.PaymentRequest;
import com.pisethjava.decorator.payment.PaymentResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentGateway paymentGateway;

    public PaymentController(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResult pay(
            @Valid @RequestBody PaymentRequest request
    ) {
        return paymentGateway.pay(request);
    }
}
