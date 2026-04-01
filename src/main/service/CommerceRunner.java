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

    public int getProduct(int optionValue) {

        Category category = sys.getCategoriesList().get(optionValue);   // optionValue is ordinal number not index number

        return io.getProductOption(category.getInfo(), category.getProducts());
    }

    public void addProductToCart(int optionValue) {
        int productIndex = this.getProduct(--optionValue) - 1;   // optionValue and productIndex is ordinal number
        Product product = sys.getProduct(optionValue, productIndex);
        boolean addToCart = io.processAddToCart(product.getInfo(), product.getName());
        if (!addToCart) return;
        sys.addProductToCart(product);
    }

    public boolean viewCartDetail() {

        Cart cart = sys.getCart();

        return io.processCheckout(cart.getTotalPrice(), cart.getCartList());
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

        sys.getCategoriesList().forEach(category -> io.getProductOption(category.getInfo(), category.getProducts()));
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
            case 1, 2, 3:
                this.addProductToCart(optionValue);
                break;
            case 4:
                boolean proceedCheckout = this.viewCartDetail();
                if (proceedCheckout) {
                    io.confirmOrder(sys.getCart().getTotalPrice(), sys.getCart().getCartList());
                    sys.processCheckout();
                }
                break;
            case 5:
                sys.removeOrder();
                io.confirmRemoveOrder();
                break;
            case 6:
                boolean admin = this.adminLogin();
                if (admin) {
                    // TODO: update print feature
                    int adminOptionValue = io.getIntValue("^[0-4]$");
                    if (adminOptionValue == 0) return; // move backward if adminOptionValue is 0
                    this.adminOption(adminOptionValue);
                }
                break;
            default:
                throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.\n");
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
