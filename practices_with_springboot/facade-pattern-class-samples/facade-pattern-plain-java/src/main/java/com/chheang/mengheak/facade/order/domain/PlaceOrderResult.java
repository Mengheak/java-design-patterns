package com.chheang.mengheak.facade.order.domain;

public record PlaceOrderResult(String orderNumber,String paymentTransactionId,String shipmentNumber,String invoiceNumber,String status) {
}
