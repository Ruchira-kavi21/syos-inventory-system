package com.syos.domain;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private final String code;
    private final String name;
    private final double price;
    private final List<Batch> batchList;

    public Item (String code, String name, double price){
        this.code = code;
        this.name = name;
        this.price = price;
        this.batchList = new ArrayList<>();
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public List<Batch> getBatchList() {
        return batchList;
    }
    public void addBatch(Batch batch) {
        this.batchList.add(batch);
    }
    public void reduceStock(int quantityNeeded){
        batchList.sort(new SmartBatchComparator());
        int remainingQuantity = quantityNeeded;

        for (Batch batch : batchList){
            if (remainingQuantity == 0) break;
            int currentStock = batch.getQuantity();
            if (currentStock > 0){
                if (currentStock>= remainingQuantity){
                    batch.decreaseQuantity(remainingQuantity);
                    remainingQuantity = 0;
                }else {
                    batch.decreaseQuantity(currentStock);
                    remainingQuantity = currentStock;
                }
            }
        }
        if (remainingQuantity > 0){
            throw new IllegalArgumentException("Not enough stock available for item: " + name);
        }
    }
}
