package com.example.pointofsale.data.remote.model;

public class ShoppingCart {
    private String productName;
    private double priceProduct;
    private int quantity;
    private double taxPercentage;
    private double discountAmount; // Discount amount in the item's currency

    public ShoppingCart(String productName, double priceProduct, int quantity, double taxPercentage, double discountAmount) {
        this.productName = productName;
        this.priceProduct = priceProduct;
        this.quantity = quantity;
        this.taxPercentage = taxPercentage;
        this.discountAmount = discountAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getSubtotal() {
        return (priceProduct * quantity) - discountAmount;
    }

    public double getTaxAmount() {
        return getSubtotal() * (taxPercentage / 100);
    }

    public double getTotalPriceWithTax() {
        return getSubtotal() + getTaxAmount();
    }
}
