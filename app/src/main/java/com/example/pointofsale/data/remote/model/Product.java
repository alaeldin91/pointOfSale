package com.example.pointofsale.data.remote.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Define the entity and specify the table name
@Entity(tableName = "productTable")
public class Product {

    // Define the primary key and auto-generate the IDs
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Define a column for the product name
    private String productName;

    // Define a column for the product price
    private double productPrice;

    // Getter and setter for the ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for the product name
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter and setter for the product price
    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
