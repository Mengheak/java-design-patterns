package com.pisethjava.facade.order.domain;

public record PlaceOrderResult(String orderNumber,String paymentTransactionId,String shipmentNumber,String invoiceNumber,String status) {
}
