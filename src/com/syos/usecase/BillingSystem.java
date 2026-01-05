package com.syos.usecase;

import com.syos.domain.Bill;
import com.syos.domain.BillItem;
import com.syos.domain.Cart;
import com.syos.domain.Item;
import com.syos.infrastructure.StoreData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillingSystem implements BillingService {
    private final StoreData storeData = StoreData.getInstance();
    private static int billCounter = 1;

    @Override
    public Bill processTransaction(Cart cart, double cashTendered){
        //validation
        if (cart.isEmpty()){
            throw new IllegalArgumentException("Cart is empty. Cannot process transaction.");
        }

        double totalAmount = cart.calculateTotal();
        if (cashTendered < totalAmount){
            throw new IllegalArgumentException("Insufficient cash. Total is: " + totalAmount);
        }

        List<BillItem> billItems = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : cart.getItems().entrySet()){
            Item item = entry.getKey();
            int qtyNeeded = entry.getValue();

            item.reduceStock(qtyNeeded);

            double lineTotal = item.getPrice() * qtyNeeded;
            billItems.add(new BillItem(item.getName(), qtyNeeded, item.getPrice(), lineTotal));
        }

        double change = cashTendered - totalAmount;
        double discount = 0.0;

        String billNumber = String.format("B%05d", billCounter++);
        return new Bill.BillBuilder()
                .setBillNumber(billNumber)
                .setDate(LocalDateTime.now())
                .setTotalAmount(totalAmount)
                .setDiscount(discount)
                .setCashReceived(cashTendered)
                .setChangeAmount(change)
                .addItemList(billItems)
                .build();
    }
}
