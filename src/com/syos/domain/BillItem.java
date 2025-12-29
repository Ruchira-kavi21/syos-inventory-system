package com.syos.domain;

public class BillItem {
    private final Item itemName;
    private final int quantity;
    private final double totalPrice;

    public BillItem(Item itemName, int quantity, double totalPrice) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Item getItem() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
