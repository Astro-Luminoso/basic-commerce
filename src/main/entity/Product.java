package main.entity;

public class Product {


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


    @Override
    public String toString(){

        return String.format("%-13s|%,12d원| %-20s%n", this.name, this.price, this.description);
    }
}
