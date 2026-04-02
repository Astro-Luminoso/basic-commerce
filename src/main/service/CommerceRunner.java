package main.service;


import main.domain.entity.Cart;
import main.domain.entity.Category;
import main.domain.entity.Product;
import main.dto.NewProductDetail;

import java.util.List;

public class CommerceRunner {

    private final CommerceSystem sys;
    private final AdminAuthentication auth;
    private final IoController io;

    public CommerceRunner(CommerceSystem sys, AdminAuthentication auth, IoController io) {

        this.sys = sys;
        this.auth = auth;
        this.io = io;
    }

    public void addCartSequence(int optionValue) {
        Category category = sys.getCategoriesList().get(--optionValue);
        int productIndex = io.getProductOption(category.getInfo(), category.getProducts()); // optionValue and productIndex is ordinal number
        if (productIndex == 0) return;
        Product product = sys.getProduct(optionValue, --productIndex);
        boolean addToCart = io.processAddToCart(product.getInfo(), product.getName());
        if (addToCart) sys.addProductToCart(product);
    }

    public void checkoutSequence() {
        Cart cart = sys.getCart();
        boolean proceedCheckout = io.processCheckout(cart.getTotalPrice(), cart.getCartList());
        if (proceedCheckout) {
            List<String> membership = sys.getMembershipOption();
            int option = io.getMembershipOption(membership);
            int discountedPrice = sys.getDiscountedPrice(option);
            io.confirmOrder(discountedPrice, sys.getCart().getCartList());
            sys.processCheckout();
        }
    }

    public void removeCartSequence() {
        sys.removeOrder();
        io.confirmRemoveOrder();
    }

    public boolean adminLogin() {
        boolean isLoggedIn;
        short chancePoint = 3;
        do {
            String pwd = io.adminAuthorization();
            isLoggedIn = auth.isAuthenticated(pwd);
            io.isAuthorised(isLoggedIn, --chancePoint); // chance point is used after the login attempt

        } while (!isLoggedIn && chancePoint != 0);

        return isLoggedIn;
    }

    public void addProductDetail() {
        List<Category> categories = sys.getCategoriesList();
        int categoryIndex = io.getCategory(categories);
        if (categoryIndex == 0) return; // move backward if categoryIndex is 0
        String categoryName = categories.get(--categoryIndex).getInfo();   // categoryIndex is ordinal number not index number
        NewProductDetail productDetail = io.addProductDetail(categoryName);
        boolean isUnique = sys.addProductDetail(categoryIndex, productDetail);
        io.printDuplicationResult(isUnique);
    }

    private void editProductDetail() {
        try {
            String productName = io.getProductName();
            Product product = sys.getProduct(productName);
            int editOption = io.getEditOption(product.getInfo());
            if (editOption == 0) return;
            NewProductDetail productDetail = io.editProductDetail(editOption, product);
            sys.updateProduct(product, productDetail);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void removeProduct() {
        try {
            String productName = io.getProductName();
            Product product = sys.getProduct(productName);
            boolean confirmRemove = io.checkRemoveProduct(product.getInfo());
            if (!confirmRemove) return;
            sys.removeProduct(product);
            io.confirmRemoveProduct(productName);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void viewAllProduct() {

        sys.getCategoriesList().forEach(category -> io.printProductWithCategoryName(category.getInfo(), category.getProducts()));
    }

    private void adminSequence() {
        boolean admin = this.adminLogin();
        if (!admin) return;
        while (true) {
            int adminOptionValue = io.printAdminOption();
            if (adminOptionValue == 0) break; // move backward if adminOptionValue is 0
            this.adminOption(adminOptionValue);
        }
    }

    private void adminOption(int optionValue) {

        switch (optionValue) {
            case 1 -> this.addProductDetail();
            case 2 -> this.editProductDetail();
            case 3 -> this.removeProduct();
            case 4 -> this.viewAllProduct();
        }
    }

    private void navigateOption(int optionValue) {
        switch (optionValue) {
            case 1, 2, 3 -> this.addCartSequence(optionValue);
            case 4 -> this.checkoutSequence();
            case 5 -> this.removeCartSequence();
            case 6 -> this.adminSequence();
            default -> throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.\n");
        }
    }

    public void start() {

        while (true) {
            int mainOption = io.getMainOption(sys.getCategoriesList(), sys.getCart().getCartSize());
            if (mainOption == 0) break;
            this.navigateOption(mainOption);
        }
    }
}
