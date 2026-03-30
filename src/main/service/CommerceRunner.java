package main.service;


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

    private void adminOption(int optionValue) {

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
                if (admin) this.adminOption(cli.getOption("^[0-4]$"));
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
