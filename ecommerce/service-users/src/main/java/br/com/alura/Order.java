package br.com.alura;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class Order {
    private final String userId, orderId;
    private final BigDecimal amount;
    
    public Order(String userId, String orderId, BigDecimal amount) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getEmail() {
        return "email";
    }
    
}
