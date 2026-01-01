package com.syos.infrastructure;

import com.syos.domain.Item;

import java.util.HashMap;
import java.util.Map;

public class StoreData {
    private static StoreData instance;
    private final Map<String, Item> inventory = new HashMap<>();
    private StoreData(){}

    public static StoreData getInstance(){
        if (instance == null){
            instance = new StoreData();
        }
        return instance;
    }
    public void addIem(Item item){
        inventory.put(item.getCode(), item);
    }
    public Item getItem (String code){
        return inventory.get(code);
    }

    public Map<String, Item> getInventory() {
        return inventory;
    }
    public void reset(){
        inventory.clear();
    }
}
