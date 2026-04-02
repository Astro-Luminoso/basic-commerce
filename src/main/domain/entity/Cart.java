package main.domain.entity;

import java.util.Collections;
import java.util.List;

/**
 * 사용자가 구매할 상품을 관리하기 위한 장바구니 역할을 해줄 클래스
 */
public class Cart {

    /**
     * 실질적 장바구니 역할을 하는 Product 클래스를 담는 리스트
     */
    private List<Product> products;

    public Cart(List<Product> products) {
        this.products = products;
    }

    /**
     * 장바구니에 상품을 추가하는 메서드
     *
     * @param product 장바구니에 추가할 상품 객체
     */
    public void addProduct(Product product){

        this.products.add(product);
    }

    /**
     * 장바구니에 담긴 상품의 갯수를 반환
     *
     * @return Cart 클래스의 속성, products 리스트의 크기
     */
    public int getCartSize() {

        return this.products.size();
    }

    /**
     * 카트에 담긴 상품을 변경불가능한 형태의 리스트로 반환해주는 메서드
     *
     * @return Product 객체를 담은 변경 불가능한 리스트
     */
    public List<Product> getCartList() {

        return Collections.unmodifiableList(this.products);
    }

    /**
     * 장바구니에 담긴 상품들의 총 금액을 반환해주는 메서드
     *
     * @return Cart 클래스의 속성, products 리스트에 담긴 상품들의 가격을 모두 더한 값
     */
    public int getTotalPrice() {

        // reference:
        // https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Stream.html#mapToInt(java.util.function.ToIntFunction)
        return this.products.stream().mapToInt(Product::getPrice).sum();
    }

    /**
     * 장바구니에 담긴 상품들을 모두 제거하는 메서드
     */
    public void clearCart() {

        this.products.clear();
    }
}
