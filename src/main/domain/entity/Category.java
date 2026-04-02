package main.domain.entity;

import main.domain.IterableOptions;

import java.util.List;

/**
 * 재품(Product)을 카테고리별로 분리하고 관리하기 위한 메소드
 */
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
