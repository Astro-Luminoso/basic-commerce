package main.service;


import main.dto.NewProductDetail;

public class CommerceRunner {

    private final CommerceSystem sys;
    private final AdminAuthentication auth;
    private final IoController cli;

    public CommerceRunner (CommerceSystem sys, AdminAuthentication auth, IoController cli) {

        this.sys = sys;
        this.auth = auth;
        this.cli = cli;
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
        int value = cli.getCategory(sys.getCategoriesList());
        if (value == 0) return;
        String categoryName = sys.getCategoriesList().get(--value).getInfo();   // value is ordinal number not index number
        NewProductDetail productDetail = cli.addProductDetail(categoryName);
        boolean isUnique = sys.inspectProductDuplication(value, productDetail.name());
        cli.printDuplicationResult(isUnique);
        if (isUnique) sys.addProductDetail(value, productDetail);
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
            case 4:
                // TODO: List Product Detail
                break;
            default:
                break;

        }
    }

    private void navigateOption(int optionValue) {

        switch (optionValue){
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
            default:
                int categoryIndex = optionValue - 1;
                int productIndex = sys.getProduct(categoryIndex) - 1;
                sys.addProductToCart(categoryIndex, productIndex);
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
