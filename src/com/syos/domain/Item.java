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

}
