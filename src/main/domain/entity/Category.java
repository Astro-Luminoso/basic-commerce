package main.domain.entity;

import main.domain.IterableOptions;

import java.util.List;

public class Category implements IterableOptions {

    private final String categoryName;
    private final List<Product> products;

    public Category(String name, List<Product> products) {

        this.categoryName = name;
        this.products = products;
    }

    public List<Product> getProducts() {

        return this.products;
    }

    @Override
    public String getInfo() {

        return this.categoryName;
    }
}
