package main.domain.entity;

import main.domain.IterableOptions;

public class Product implements IterableOptions {


    private String name;
    private int price;
    private String description;
    private int stockAmount;


    public Product (String name, int price, String description, int stockAmount) {

        this.name = name;
        this.price = price;
        this.description = description;
        this.stockAmount = stockAmount;
    }

    public String getName() {

        return this.name;
    }

    public int getStockAmount() {

        return this.stockAmount;
    }


    public int getPrice() {

        return this.price;
    }

    public void updateStock(int amount) {

        this.stockAmount -= amount;
    }

    @Override
    public String getInfo() {

        return String.format("%-42s|%,12d원| %-20s", this.name, this.price, this.description);
    }
}
