package com.pisethjava.builder.lesson05_immutable;

import java.math.BigDecimal;
import java.util.UUID;

public final class Payment {
    private final UUID id;
    private final BigDecimal amount;
    private final String currency;
    private final String status;

    private Payment(Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.status = builder.status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private BigDecimal amount;
        private String currency;
        private String status = "PENDING";

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
