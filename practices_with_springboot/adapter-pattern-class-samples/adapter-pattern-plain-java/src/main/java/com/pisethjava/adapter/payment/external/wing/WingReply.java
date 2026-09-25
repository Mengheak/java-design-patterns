package com.pisethjava.adapter.payment.external.wing;

public record WingReply(int code, String transactionReference, String text) {
}
