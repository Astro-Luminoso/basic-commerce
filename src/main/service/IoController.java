package main.service;

import main.domain.IterableOptions;
import main.domain.entity.Category;
import main.domain.entity.Product;
import main.dto.NewProductDetail;

import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class IoController {

    private final Scanner sc;

    public IoController(Scanner sc) {
        this.sc = sc;
    }

    private <T> T loopMethod(Supplier<T> actionMethod) {
        while (true) {
            try {
                return actionMethod.get();
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
                System.out.println();
            }
        }
    }

    public void printList(List<? extends IterableOptions> lists) {
        if (!lists.isEmpty()) {
            for (IterableOptions element : lists) {
                System.out.printf("%d. %s%n", lists.indexOf(element) + 1, element.getInfo());
            }
        }

        System.out.println("0. " + ((lists.getFirst() instanceof Category) ? "종료" : "뒤로가기"));
    }

    public int getIntValue(String regex) {
        String value = sc.nextLine();
        if (!value.matches(regex))
            throw new NumberFormatException("올바른 번호를 입력해주세요.\n");

        return Integer.parseInt(value);
    }

    public int getMainOption(List<Category> categories, int cartSize) {
        Supplier<Integer> func = () -> {
            System.out.println("[ 실시간 커머스 플랫폼 ]");
            this.printList(categories);
            int optionSize = categories.size();
            if (cartSize > 0) {
                System.out.println("4. 장바구니 확인");
                System.out.println("5. 주문 취소");
                optionSize += 2;    // input upbound value must be updated due to 2 more options are available
            }
            System.out.println("6. 관리자 모드");

            return this.getIntValue(String.format("[0-%d]", ++optionSize)); //add 1 for admin option
        };

        return this.loopMethod(func);
    }

    public int getProductOption(String categoryName, List<Product> products) {
        Supplier<Integer> func = () -> {
            System.out.printf("[ %s 카테고리 ]%n", categoryName);
            this.printList(products);
            return this.getIntValue(String.format("^[0-%d]$", products.size()));
        };

        return this.loopMethod(func);
    }

    public boolean processAddToCart(String productInfo, String productName) {
        Supplier<Boolean> func = () -> {
            System.out.println(productInfo);
            System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
            System.out.println("1.확인             0.취소");

            return this.getIntValue("0|1") == 1;
        };

        boolean result = this.loopMethod(func);
        if (result) {
            System.out.printf("%s가 장바구니에 추가되었습니다.%n", productName);
        }

        return result;
    }


    public boolean processCheckout(int totalPrice, List<Product> cartList) {
        Supplier<Boolean> func = () -> {
            System.out.println("아래와 같이 주문 하시겠습니까?\n");
            System.out.println("[ 장바구니 내역 ]");
            this.printList(cartList);
            System.out.println("[ 총 주문 금액 ]");
            System.out.printf("%,d원%n%n", totalPrice);
            System.out.println("1.주문 확정             0.취소");
            return this.getIntValue("0|1") == 1;
        };

        return this.loopMethod(func);
    }

    public void confirmOrder(int totalPrice, List<Product> cartList) {
        System.out.printf("주문이 완료되었습니다! 총 금액: %,d원%n", totalPrice);
        for (Product product : cartList) {
            int stockAmount = product.getStockAmount();
            System.out.printf("%s 재고가 %d개 -> %d로 업데이트 되었습니다.%n", product.getInfo(), stockAmount, --stockAmount);
        }
    }

    public void confirmRemoveOrder() {
        System.out.println("주문이 취소되었습니다.");
    }


    public int getCategory(List<Category> categories) {
        Supplier<Integer> func = () -> {
            System.out.println("카테고리를 선택해주세요: ");
            this.printList(categories);
            return this.getIntValue(String.format("^[0-%d]", categories.size()));
        };

        return this.loopMethod(func);
    }

    public String adminAuthorization() {

        System.out.println("관리자 비밀번호를 입력해주세요: ");
        return sc.nextLine();
    }

    public void isAuthorised(boolean isLoggedIn, short chanceLeft) {

        if (isLoggedIn) {
            System.out.println("관리자 로그인 성공!\n");

        } else {
            System.err.println("비밀번호가 틀렸습니다. 다시 입력해주세요.\n"
                    + ((chanceLeft == 0) ? "로그인 시도가 3회 실패하여 메인매뉴로 돌아갑니다.\n" : "")
            );
        }
    }

    public NewProductDetail addProductDetail(String categoryName) {
        Supplier<NewProductDetail> func = () -> {
            System.out.printf("[ %s 카테고리에 상품 추가 ]%n", categoryName);
            System.out.print("상품명을 입력해주세요: ");
            String name = sc.nextLine();
            System.out.print("상품 가격을 입력해주세요: ");
            int price = this.getIntValue("^\\d+0$");
            System.out.print("상품설명을 입력해주세요: ");
            String description = sc.nextLine();
            System.out.print("재고수량을 입력해주세요: ");
            int stockAmount = getIntValue("^\\d+$");

            return new NewProductDetail(name, price, description, stockAmount);
        };

        return this.loopMethod(func);
    }

    public void printDuplicationResult(boolean isUnique) {

        System.out.println((isUnique) ? "해당 항목이 추가됩니다." : "해당 항목은 이미 존재합니다. 품목 추가를 취소합니다.");
    }

    public String getProductName() {
        System.out.print("수정할 상품명을 입력해주세요: ");

        return sc.nextLine();
    }

    public int getEditOption(String productInfo) {
        Supplier<Integer> func = () -> {
            System.out.printf("현재 상품 정보: %s%n%n", productInfo);
            System.out.println("수정할 항목을 선택해주세요:\n1. 가격\n2. 설명\n3. 재고 수량\n0. 뒤로가기");

            return this.getIntValue("^[0-3]$");
        };

        return this.loopMethod(func);
    }

    public NewProductDetail editProductDetail(int editOption, Product product) throws NumberFormatException {
        Supplier<NewProductDetail> func;

        switch (editOption) {
            case 1 -> func = () -> {
                System.out.printf("현재 가격: %,d원%n", product.getPrice());
                System.out.print("새로운 가격을 입력해 주세요: ");
                int newPrice = this.getIntValue("^\\d+0$");
                System.out.printf("%s의 가격이 %,d원 → %,d원으로 수정되었습니다.%n", product.getName(), product.getPrice(), newPrice);
                return new NewProductDetail(product.getName(), newPrice, product.getDescription(), product.getStockAmount());
            };
            case 2 -> func = () -> {
                System.out.println("새로운 설명을 입력해 주세요: ");
                String newDescription = sc.nextLine();
                System.out.printf("%s의 설명이 수정되었습니다.%n", product.getName());
                return new NewProductDetail(product.getName(), product.getPrice(), newDescription, product.getStockAmount());
            };
            case 3 -> func = () -> {
                System.out.printf("현재 수량: %d%n", product.getStockAmount());
                System.out.print("새로운 가격을 입력해 주세요: ");
                int newStockAmount = this.getIntValue("^\\d+$");
                System.out.printf("%s의 수량이 %,d → %,d으로 수정되었습니다.%n", product.getName(), product.getStockAmount(), newStockAmount);
                return new NewProductDetail(product.getName(), product.getPrice(), product.getDescription(), newStockAmount);
            };
            default -> throw new IllegalArgumentException("올바른 번호를 입력해주세요.\n");
        }

        return this.loopMethod(func);
    }

    public boolean checkRemoveProduct(String productInfo) {
        Supplier<Boolean> func = () -> {
            System.out.printf("현재 상품 정보: %s%n%n", productInfo);
            System.out.println("해당 상품을 삭제하시겠습니까?");
            System.out.println("1.삭제             0.취소");
            return this.getIntValue("0|1") == 1;
        };

        return this.loopMethod(func);
    }

    public void confirmRemoveProduct(String productName) {
        System.out.printf("%s가 삭제되었습니다.%n", productName);
    }
}
