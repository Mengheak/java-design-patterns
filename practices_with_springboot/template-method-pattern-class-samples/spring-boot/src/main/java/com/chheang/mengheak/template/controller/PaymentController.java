package com.chheang.mengheak.template.controller;

import com.chheang.mengheak.template.dto.*;
import com.chheang.mengheak.template.service.PaymentService;
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
