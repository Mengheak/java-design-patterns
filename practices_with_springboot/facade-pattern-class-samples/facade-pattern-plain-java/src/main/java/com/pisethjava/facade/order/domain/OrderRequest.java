package com.pisethjava.facade.order.domain;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(String orderNumber,String customerId,List<String> productIds,BigDecimal totalAmount,String paymentMethod) {
}
