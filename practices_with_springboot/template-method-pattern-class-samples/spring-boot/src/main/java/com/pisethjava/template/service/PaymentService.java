package com.pisethjava.template.service;

import com.pisethjava.template.dto.*;
import com.pisethjava.template.template.PaymentProcessingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentProcessorFactory factory;

	public PaymentResponse process(PaymentRequest request) {
		PaymentProcessingTemplate processor = factory.getProcessor(request.getMethod());
		return processor.process(request);
	}
}
