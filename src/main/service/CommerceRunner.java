package main.service;


import main.domain.entity.Category;
import main.domain.entity.Product;
import main.dto.NewProductDetail;

import java.util.List;
import java.util.function.Supplier;

public class CommerceRunner {

    private final CommerceSystem sys;
    private final AdminAuthentication auth;
    private final IoController cli;

    public CommerceRunner (CommerceSystem sys, AdminAuthentication auth, IoController cli) {

        this.sys = sys;
        this.auth = auth;
        this.cli = cli;
    }

    public int getProduct(int optionValue) {

        Category category = this.category.get(optionValue);
        List<Product> products = category.getProducts();

        Supplier<Integer> func = () -> {
            System.out.println("[ " + category.getInfo() + " 카테고리 ]");
            cli.printList(products);
            String acceptableRange = String.format("[0-%d]", products.size());
            return this.getOption(acceptableRange);
        };

        return this.loopMethod(func);
    }

    private boolean adminLogin () {
        boolean isLoggedIn;
        short chancePoint = 3;
        do {
            String pwd = cli.adminAuthorization();
            isLoggedIn = auth.isAuthenticated(pwd);
            cli.isAuthorised(isLoggedIn, --chancePoint); // chance point is used after the login attempt

        } while(!isLoggedIn && chancePoint != 0);

        return isLoggedIn;
    }

    private void addProductDetail() {
        List<Category> categories = sys.getCategoriesList();
        int categoryIndex = sys.loopMethod(() -> cli.getCategory(categories));
        if (categoryIndex == 0) return; // move backward if categoryIndex is 0
        String categoryName = categories.get(--categoryIndex).getInfo();   // categoryIndex is ordinal number not index number
        NewProductDetail productDetail = cli.addProductDetail(categoryName);
        boolean isUnique = sys.inspectProductDuplication(categoryIndex, productDetail.name());
        cli.printDuplicationResult(isUnique);
        if (!isUnique) return;
        sys.addProductDetail(categoryIndex, productDetail);
    }

    private void adminOption(int optionValue) {

        switch (optionValue) {
            case 0:
                return;
            case 1:
                this.addProductDetail();
                break;
            case 2:
                //TODO: Edit Product Detail
                break;
            case 3:
                //TODO: Remove Product Detail
                break;
            case 4:
                // TODO: List Product Detail
                break;
            default:
                break;

        }
    }

    private void navigateOption(int optionValue) {
        switch (optionValue){
            case 1, 2, 3:
                int productIndex = sys.getProduct(--optionValue) - 1;   // optionValue is ordinal number
                sys.addProductToCart(optionValue, productIndex);
                break;
            case 4:
                boolean proceedCheckout = sys.viewCartDetail();
                if (proceedCheckout) sys.processCheckout();
                break;
            case 5:
                sys.removeOrder();
                break;
            case 6:
                boolean admin = this.adminLogin();
                if (admin) {
                    int adminOptionValue = sys.loopMethod(()-> cli.getIntValue("^[0-4]$"));
                    this.adminOption(adminOptionValue);
                }
                break;
            default:
                throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.\n");
        }
    }


    public void start() {

        while (true){
            int mainOption = sys.getMainOption();
            if (mainOption == 0) break;
            this.navigateOption(mainOption);
        }
    }
}
