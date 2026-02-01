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
    public void syncItemInventory(Item item) {
        String deleteSql = "DELETE FROM batches WHERE item_code = ?";
        String insertSql = "INSERT INTO batches (batch_number, item_code, purchase_date, expiry_date, quantity, location) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            // 1. Wipe the old state for this item
            PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
            deleteStmt.setString(1, item.getCode());
            deleteStmt.executeUpdate();

            // 2. Prepare to save the NEW state
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);

            // 3. Save Back Store Batches
            for (Batch b : item.getStoreBatches()) {
                insertStmt.setString(1, b.getBatchNumber());
                insertStmt.setString(2, item.getCode());
                insertStmt.setDate(3, java.sql.Date.valueOf(b.getDateOfPurchase()));
                insertStmt.setDate(4, java.sql.Date.valueOf(b.getExpiryDate()));
                insertStmt.setInt(5, b.getQuantity());
                insertStmt.setString(6, "STORE");
                insertStmt.addBatch();
            }

            // 4. Save Front Shelf Batches
            for (Batch b : item.getShelfBatches()) {
                insertStmt.setString(1, b.getBatchNumber());
                insertStmt.setString(2, item.getCode());
                insertStmt.setDate(3, java.sql.Date.valueOf(b.getDateOfPurchase()));
                insertStmt.setDate(4, java.sql.Date.valueOf(b.getExpiryDate()));
                insertStmt.setInt(5, b.getQuantity());
                insertStmt.setString(6, "SHELF");
                insertStmt.addBatch();
            }

            // Execute all updates
            insertStmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error syncing inventory: " + e.getMessage());
        }
    }
}
