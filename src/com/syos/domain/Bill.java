package com.syos.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Bill {
    private final String billNumber;
    private final LocalDateTime date;
    private final double totalAmount;
    private final double discount;
    private final double cashReceived;
    private final double changeAmount;
    private final List<BillItem> items;

    public Bill (String billNumber, LocalDateTime date, double totalAmount, double discount,
                double cashReceived, double changeAmount, List<BillItem> items) {
        this.billNumber = billNumber;
        this.date = date;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.cashReceived = cashReceived;
        this.changeAmount = changeAmount;
        this.items = items;
    }
    public String getBillNumber() {
        return billNumber;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public double getDiscount() {
        return discount;
    }
    public double getCashReceived() {
        return cashReceived;
    }
    public double getChangeAmount() {
        return changeAmount;
    }
    public List<BillItem> getItems() {
        return items;
    }
}
