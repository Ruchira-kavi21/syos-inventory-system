package com.syos.infrastructure;

import com.syos.domain.Batch;
import com.syos.domain.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private final Connection connection;

    public ItemDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Item> loadAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int capacity = rs.getInt("shelf_capacity");

                //Create Object
                Item item = new Item(code, name, price, capacity);
                //Load its Batches
                loadBatchesForItem(item);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void loadBatchesForItem(Item item) {
        String sql = "SELECT * FROM batches WHERE item_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getCode());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Batch batch = new Batch(
                        rs.getString("batch_number"),
                        rs.getDate("purchase_date").toLocalDate(),
                        rs.getDate("expiry_date").toLocalDate(),
                        rs.getInt("quantity")
                );

                String location = rs.getString("location");

                //Sort into the correct list based on db
                if ("STORE".equalsIgnoreCase(location)) {
                    item.addBatchToStore(batch);
                } else if ("SHELF".equalsIgnoreCase(location)) {
                    item.addBatchToStore(batch);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
