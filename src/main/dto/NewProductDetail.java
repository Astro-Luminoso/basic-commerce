package main.dto;

/**
 * 제품의 상세정보를 담는 DTO 클래스입니다. 제품의 이름, 가격, 설명, 재고 수량을 포함합니다.
 *
 * @param name 상품의 이름
 * @param price 상품의 가격
 * @param description 상품 설명
 * @param stockAmount 상품 재고 수량
 */
public record NewProductDetail (String name, int price, String description, int stockAmount) {}
