package com.pisethjava.adapter.payment.target;

import com.pisethjava.adapter.payment.domain.PaymentRequest;
import com.pisethjava.adapter.payment.domain.PaymentResult;

/**
 * Target interface expected by the application's business layer.
 *
 * Each third-party payment provider is adapted to this interface.
 */
public interface PaymentGateway {

	PaymentResult pay(PaymentRequest request);
}
