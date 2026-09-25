package com.pisethjava.template.service;

import com.pisethjava.template.template.PaymentProcessingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {
	private final Map<String, PaymentProcessingTemplate> processors;

	public PaymentProcessingTemplate getProcessor(String method) {
		PaymentProcessingTemplate processor = processors.get(method);
		if (processor == null)
			throw new IllegalArgumentException("Unsupported payment method: " + method);
		return processor;
	}
}
