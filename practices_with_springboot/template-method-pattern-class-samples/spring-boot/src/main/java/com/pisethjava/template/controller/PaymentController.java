package com.pisethjava.template.controller;

import com.pisethjava.template.dto.*;
import com.pisethjava.template.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<PaymentResponse> process(@Valid @RequestBody PaymentRequest request) {
		return ResponseEntity.ok(paymentService.process(request));
	}
}
