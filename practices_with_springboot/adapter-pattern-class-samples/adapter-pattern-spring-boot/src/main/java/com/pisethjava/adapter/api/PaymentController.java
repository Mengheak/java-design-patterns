package com.pisethjava.adapter.api;

import com.pisethjava.adapter.domain.*;
import com.pisethjava.adapter.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
	private final PaymentService service;

	public PaymentController(PaymentService s) {
		service = s;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponse pay(@Valid @RequestBody PaymentRequest r) {
		PaymentResult x = service.pay(r.provider(),
				new PaymentCommand(r.account(), r.amount(), r.currency(), r.reference()));
		return new PaymentResponse(x.provider(), x.transactionId(), x.status(), x.message());
	}

	@GetMapping("/providers")
	public List<PaymentProvider> providers() {
		return service.providers();
	}
}
