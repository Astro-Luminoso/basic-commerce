package main.service;

import main.domain.entity.Cart;
import main.domain.entity.Category;
import main.domain.entity.Product;
import main.dto.NewProductDetail;
import main.utility.MembershipType;

import java.util.List;

/**
 * 시스템 데이터 처리를 일괄적으로 관리하는 클래스
 */
public class CommerceSystem {

    private final List<Category> categories;
    private final Cart cart;

    public CommerceSystem(List<Category> categories, Cart cart) {

        this.categories = categories;
        this.cart = cart;
    }

    public List<Category> getCategoriesList() {

        return this.categories;
    }

    public Cart getCart() {
        return this.cart;
    }

    /**
     * 중복된 상품이 존재하는지 검사하는 메소드
     *
     * @param categoryIndex 상품이 추가될 카테고리의 인덱스
     * @param productName 상품 이름
     * @return 같은 이름의 상품이 없다면 true, 그 외 false
     */
    private boolean inspectProductDuplication(int categoryIndex, String productName) {

        List<Product> products = categories.get(categoryIndex).getProducts();

        return products.stream().noneMatch(product -> product.getName().equals(productName));
    }

    /**
     * 신규 재품 등록을 처리하는 메소드
     *
     * @param index 카테고리 인덱스
     * @param productDetail 신규상품 상세 정보가 담긴 record 객체
     * @return 상품이 성공적으로 등록되었다면 true, 그 외 false
     */
    public boolean addProductDetail(int index, NewProductDetail productDetail) {

        boolean isUnique = this.inspectProductDuplication(index, productDetail.name());
        if (isUnique) {
            List<Product> products = categories.get(index).getProducts();
            products.add(
                    new Product(
                            productDetail.name(),
                            productDetail.price(),
                            productDetail.description(),
                            productDetail.stockAmount()));
        }

        return isUnique;
    }

    /**
     *  카태고리 인덱스와 상품 인덱스를 받아 해당 상품 객체를 반환하는 메소드
     *
     * @param categoryIndex 카테고리 인덱스
     * @param productIndex 상품 인덱스
     * @return 상품 객체
     */
    public Product getProduct(int categoryIndex, int productIndex) {

        return this.categories.get(categoryIndex).getProducts().get(productIndex);
    }

    /**
     * 상품 이름으로 상품 객체를 반환하는 메소드
     *
     * @param productName 상품 이름
     * @return 상품 객체
     */
    public Product getProduct(String productName) {

        return this.categories.stream()
                .flatMap(category -> category.getProducts().stream())
                .filter(product -> product.getName().equals(productName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    }

    /**
     * 상품 객체를 받아 카트에 추가하는 메소드
     * @param product 카트에 추가할 메소드
     */
    public void addProductToCart(Product product) {

        cart.addProduct(product);
    }

    /**
     * 멤버십 옵션을 받아 카트의 총 가격에 대한 할인된 가격을 반환하는 메소드
     *
     * @param membershipOption 선택된 맴버십
     * @return 할인된 가격
     */
    public int getDiscountedPrice(int membershipOption) {
        MembershipType membership = MembershipType.values()[--membershipOption];

        return membership.calculateDiscountedPrice(cart.getTotalPrice());
    }

    /**
     * 재고 처리 후 카트 내의 상품들을 모두 제거하는 메소드
     */
    public void processCheckout() {
        this.cart.getCartList().forEach(product -> product.updateStock(1));
        this.removeOrder();
    }

    /**
     * 카트 내 상품들을 모두 제거하는 메소드
     */
    public void removeOrder() {
        this.cart.clearCart();
    }

    /**
     * 상품 정보를 수정하는 메소드
     *
     * @param product 수정 대상 상품 객체
     * @param updateDetail 수정된 상품의 상세정보가 담긴 record 형식의 DTO 객체
     */
    public void updateProduct(Product product, NewProductDetail updateDetail) {

        product.update(updateDetail);
    }

    /**
     * 상품객체를 받아 해당 상품이 담긴 카테고리를 찾은 후 카테고리 내에 있는 상품을 찾아 삭제하는 메소드
     *
     * @param product 삭제 대상 상품 객체
     */
    public void removeProduct(Product product) {

        this.getCategoriesList().stream()
                .filter(category -> category.getProducts().contains(product))
                .findFirst()
                .ifPresent(category -> category.getProducts().remove(product));
    }
}
