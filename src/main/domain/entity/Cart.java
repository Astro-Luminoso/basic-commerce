package main.domain.entity;

import java.util.Collections;
import java.util.List;

public class Cart {

    private List<Product> products;


    public Cart(List<Product> products) {
        this.products = products;
    }


    public void addProduct(Product product){

        this.products.add(product);
    }

    public int getCartSize() {

        return this.products.size();
    }

    public List<Product> getCartList() {

        return Collections.unmodifiableList(this.products);
    }

    public int getTotalPrice() {

        // reference:
        // https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Stream.html#mapToInt(java.util.function.ToIntFunction)
        return this.products.stream().mapToInt(Product::getPrice).sum();
    }

    public void clearCart() {

        this.products.clear();
    }
}
