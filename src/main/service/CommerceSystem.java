package main.service;

import main.domain.entity.Cart;
import main.domain.entity.Category;
import main.domain.IterableOptions;
import main.domain.entity.Product;
import main.dto.NewProductDetail;

import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class CommerceSystem {

    private final List<Category> categories;
    private final Cart cart;
    private final Scanner sc;

    public CommerceSystem (List<Category> categories, Cart cart, Scanner sc) {

        this.categories = categories;
        this.cart = cart;
        this.sc = sc;
    }

    public <T> T loopMethod(Supplier<T> actionMethod) {

        while(true) {
            try{
                return actionMethod.get();
            } catch(NumberFormatException | IndexOutOfBoundsException e) {

                System.err.println(e.getMessage());
                System.out.println();
            }
        }
    }

    private void printList(List<? extends IterableOptions> lists) {
        if(!lists.isEmpty()) {
            for (IterableOptions element : lists) {
                System.out.printf("%d. %s%n", lists.indexOf(element) + 1, element.getInfo());
            }
        }

        System.out.println("0. " + ((lists.getFirst() instanceof Category) ? "종료" : "뒤로가기"));
    }

    public boolean inspectProductDuplication(int index, String productName) {

        List<Product> products = categories.get(index).getProducts();

        return products.stream().noneMatch(product -> product.getName().equals(productName));
    }

    public void addProductDetail(int index, NewProductDetail productDetail) {

        List<Product> products = categories.get(index).getProducts();
        products.add(
                new Product(
                productDetail.name(),
                productDetail.price(),
                productDetail.description(),
                productDetail.stockAmount()));
    }

    public List<Category> getCategoriesList() {

        return this.categories;
    }

    @Deprecated
    public int getOption(String regex) {

        String value = sc.nextLine();
        if(!value.matches(regex))
            throw new NumberFormatException("올바른 번호를 입력해주세요.\n");

        return Integer.parseInt(value);
    }

    public boolean viewCartDetail() {

        Supplier<Boolean> func = () -> {
            System.out.println("아래와 같이 주문 하시겠습니까?\n");
            System.out.println("[ 장바구니 내역 ]");
            this.printList(cart.getCartList());
            System.out.println("[ 총 주문 금액 ]");
            System.out.printf("%,d원%n%n", cart.getTotalPrice());
            System.out.println("1.주문 확정             0.취소");
            return this.getOption("0|1") == 1;
        };

        return this.loopMethod(func);
    }

    public void processCheckout() {
        System.out.printf("주문이 완료되었습니다! 총 금액: %,d원%n", cart.getTotalPrice());
        for(Product product : this.cart.getCartList()) {
            int stockAmount = product.getStockAmount();
            System.out.printf("%s 재고가 %d개 -> %d로 업데이트 되었습니다.%n", product.getInfo(), stockAmount, stockAmount - 1);
            product.updateStock(1);
        }

        cart.clearCart();
    }

    public void removeOrder() {
        this.cart.clearCart();
        System.out.println("주문이 취소되었습니다.\n");
    }


    public void addProductToCart(int categoryIndex, int productIndex) {
        Supplier<Boolean> func = () -> {
            System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
            System.out.println("1.확인             0.취소");

            return this.getOption("0|1") == 1;
        };

        Product product = this.categories.get(categoryIndex).getProducts().get(productIndex);
        System.out.println(product.getInfo());

        boolean addProduct = this.loopMethod(func);

        if (addProduct) cart.addProduct(product);
    }

    public int getProduct(int optionValue) {

        Category category = this.categories.get(optionValue);
        List<Product> products = category.getProducts();

        Supplier<Integer> func = () -> {
            System.out.println("[ " + category.getInfo() + " 카테고리 ]");
            this.printList(products);
            String acceptableRange = String.format("[0-%d]", products.size());
            return this.getOption(acceptableRange);
        };

        return this.loopMethod(func);
    }

    public int getMainOption () {

        Supplier<Integer> func = () -> {
            System.out.println("[ 실시간 커머스 플랫폼 ]");

            this.printList(this.categories);
            int optionSize = this.categories.size();

            if (this.cart.getCartSize() > 0) {
                System.out.println("4. 장바구니 확인");
                System.out.println("5. 주문 취소");

                optionSize += 2;    // input upbound value must be updated due to 2 more options are available
            }

            System.out.println("6. 관리자 모드");
            String acceptableRange = String.format("[0-%d]", ++optionSize); //add 1 for admin option
            return this.getOption(acceptableRange);
        };

        return this.loopMethod(func);
    }
}
