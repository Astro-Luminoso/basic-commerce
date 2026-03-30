package main.service;


public class CommerceRunner {

    private final CommerceSystem sys;


    public CommerceRunner (CommerceSystem sys) {

        this.sys = sys;
    }



    private void navigateOption(int optionValue) {

        switch (optionValue){
            case 4:
                boolean proceedCheckout = sys.viewCartDetail();
                if (proceedCheckout)
                    sys.processCheckout();
                break;
            case 5:
                sys.removeOrder();
                break;
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
