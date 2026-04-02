package main.service;


import main.domain.entity.Cart;
import main.domain.entity.Category;
import main.domain.entity.Product;
import main.dto.NewProductDetail;
import main.utility.MembershipType;

import java.util.Arrays;
import java.util.List;

/**
 * 프로그램의 흐름을 총괄적으로 제어하는 클래스
 */
public class CommerceRunner {

    private final CommerceSystem sys;
    private final AdminAuthentication auth;
    private final IoController io;

    /**
     * CommerceRunner 클래스의 생성자
     *
     * @param sys 시스템의 데이터 처리를 담당하는 CommerceSystem 객체
     * @param auth 관리자 인증을 담당하는 AdminAuthentication 객체
     * @param io 입출력을 담당하는 IoController 객체
     */
    public CommerceRunner(CommerceSystem sys, AdminAuthentication auth, IoController io) {

        this.sys = sys;
        this.auth = auth;
        this.io = io;
    }

    /**
     * 재품 추가 흐름을 다루는 메소드
     *
     * @param optionValue 사용자가 선택한 1부터 시작하는 재품 번호 (인덱스 번호가 아님)
     */
    public void addCartSequence(int optionValue) {
        Category category = sys.getCategoriesList().get(--optionValue);
        int productIndex = io.getProductOption(category.getInfo(), category.getProducts()); // optionValue and productIndex is ordinal number
        if (productIndex == 0) return;
        Product product = sys.getProduct(optionValue, --productIndex);
        boolean addToCart = io.processAddToCart(product.getInfo(), product.getName());
        if (addToCart) sys.addProductToCart(product);
    }

    /**
     * 카트 내에 있는 재품들을 결제하는 흐름을 다루는 메소드
     */
    public void checkoutSequence() {
        Cart cart = sys.getCart();
        boolean proceedCheckout = io.processCheckout(cart.getTotalPrice(), cart.getCartList());
        if (proceedCheckout) {
            List<MembershipType> membership = Arrays.asList(MembershipType.values());
            int option = io.getMembershipOption(membership);
            int discountedPrice = sys.getDiscountedPrice(option);
            io.confirmOrder(discountedPrice, sys.getCart().getCartList());
            sys.processCheckout();
        }
    }

    /**
     * 카트 안의 재품들을 모두 삭제하는 흐름을 다루는 메소드
     */
    public void removeCartSequence() {
        sys.removeOrder();
        io.confirmRemoveOrder();
    }

    /**
     * 관리자 로그인 흐름을 다루는 메서드
     *
     * @return 로그인 완료 여부
     */
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

    /**
     * 상품 정보를 추가하는 흐름을 다루는 메서드
     */
    public void addProductDetail() {
        List<Category> categories = sys.getCategoriesList();
        int categoryIndex = io.getCategory(categories);
        if (categoryIndex == 0) return; // move backward if categoryIndex is 0
        String categoryName = categories.get(--categoryIndex).getInfo();   // categoryIndex is ordinal number not index number
        NewProductDetail productDetail = io.addProductDetail(categoryName);
        boolean isUnique = sys.addProductDetail(categoryIndex, productDetail);
        io.printDuplicationResult(isUnique);
    }

    /**
     * 상품 정보를 수정하는 흐름을 다루는 메서드
     */
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

    /**
     * 상품을 삭제하는 흐름을 다루는 메서드
     */
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

    /**
     * 재품 전체를 조회하는 메서드
     */
    private void viewAllProduct() {

        sys.getCategoriesList().forEach(category -> io.printProductWithCategoryName(category.getInfo(), category.getProducts()));
    }

    /**
     * 관리자 메뉴 흐름을 다루는 메서드
     */
    private void adminSequence() {
        boolean admin = this.adminLogin();
        if (!admin) return;
        while (true) {
            int adminOptionValue = io.printAdminOption();
            if (adminOptionValue == 0) break; // move backward if adminOptionValue is 0
            this.adminOption(adminOptionValue);
        }
    }

    /**
     * 관리자 메뉴에서 사용자가 선택한 옵션에 따라 해당 메소드를 호출하는 메서드
     *
     * @param optionValue 사용자가 선택한 1부터 시작하는 옵션 번호 (인덱스 번호가 아님)
     */
    private void adminOption(int optionValue) {

        switch (optionValue) {
            case 1 -> this.addProductDetail();
            case 2 -> this.editProductDetail();
            case 3 -> this.removeProduct();
            case 4 -> this.viewAllProduct();
        }
    }

    /**
     * 사용자가 선택한 옵션번호에 따라 호출할 메소드를 분류하는 메소드
     *
     * @param optionValue 사용자가 선택한 1부터 시작하는 옵션 번호 (인덱스 번호가 아님)
     * @param cartSize 카트사이즈 분류: 4, 5번 옵션은 카트가 비어있을 때 선택할 수 없도록 예외처리 하기위한 매개변수
     */
    private void navigateOption(int optionValue, int cartSize) {
        if (cartSize == 0 && (optionValue == 4 || optionValue == 5))
            throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.\n");

        switch (optionValue) {
            case 1, 2, 3 -> this.addCartSequence(optionValue);
            case 4 -> this.checkoutSequence();
            case 5 -> this.removeCartSequence();
            case 6 -> this.adminSequence();
            default -> throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.\n");
        }
    }

    /**
     * 실행 메소드: 프로그램의 메인 루프를 담당하는 메소드
     */
    public void start() {

        while (true) {
            int mainOption = io.getMainOption(sys.getCategoriesList(), sys.getCart().getCartSize());
            if (mainOption == 0) break;
            this.navigateOption(mainOption, sys.getCart().getCartSize());
        }
    }
}
