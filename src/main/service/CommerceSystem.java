package main.service;

import main.domain.entity.Cart;
import main.domain.entity.Category;
import main.domain.entity.Product;
import main.dto.NewProductDetail;
import main.utility.MembershipType;

import java.util.Arrays;
import java.util.List;

public class CommerceSystem {

    private final List<Category> categories;
    private final Cart cart;

    public CommerceSystem(List<Category> categories, Cart cart) {

        this.categories = categories;
        this.cart = cart;
    }

    public List<Category> getCategoriesList() {

        return this.categories;
    }

    public Cart getCart() {
        return this.cart;
    }

    private boolean inspectProductDuplication(int index, String productName) {

        List<Product> products = categories.get(index).getProducts();

        return products.stream().noneMatch(product -> product.getName().equals(productName));
    }

    public boolean addProductDetail(int index, NewProductDetail productDetail) {

        boolean isUnique = this.inspectProductDuplication(index, productDetail.name());
        if (isUnique) {
            List<Product> products = categories.get(index).getProducts();
            products.add(
                    new Product(
                            productDetail.name(),
                            productDetail.price(),
                            productDetail.description(),
                            productDetail.stockAmount()));
        }

        return isUnique;
    }

    public Product getProduct(int categoryIndex, int productIndex) {

        return this.categories.get(categoryIndex).getProducts().get(productIndex);
    }

    public Product getProduct(String productName) {

        return this.categories.stream()
                .flatMap(category -> category.getProducts().stream())
                .filter(product -> product.getName().equals(productName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    }

    public void addProductToCart(Product product) {

        cart.addProduct(product);
    }

    public List<String> getMembershipOption() {

        return Arrays.stream(MembershipType.values()).map(Enum::name).toList();
    }

    public int getDiscountedPrice(int membershipOption) {
        MembershipType membership = MembershipType.values()[--membershipOption];

        return membership.calculateDiscountedPrice(cart.getTotalPrice());
    }

    public void processCheckout() {
        this.cart.getCartList().forEach(product -> product.updateStock(1));
        this.removeOrder();
    }

    public void removeOrder() {
        this.cart.clearCart();
    }

    public void updateProduct(Product product, NewProductDetail updateDetail) {

        product.update(updateDetail);
    }

    public void removeProduct(Product product) {

        this.getCategoriesList().stream()
                .filter(category -> category.getProducts().contains(product))
                .findFirst()
                .ifPresent(category -> category.getProducts().remove(product));
    }
}
