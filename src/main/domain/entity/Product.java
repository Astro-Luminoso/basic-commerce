package main.domain.entity;

import main.domain.IterableOptions;
import main.dto.NewProductDetail;

/**
 * 제품(Product) 클래스는 제품의 이름, 가격, 설명, 재고 수량을 포함한다.
 * 제품의 상태를 관리하는 메서드가 포함된다.
 */
public class Product implements IterableOptions {


    private String name;
    private int price;
    private String description;
    private int stockAmount;


    public Product(String name, int price, String description, int stockAmount) {

        this.name = name;
        this.price = price;
        this.description = description;
        this.stockAmount = stockAmount;
    }

    public String getName() {

        return this.name;
    }

    public int getStockAmount() {

        return this.stockAmount;
    }


    public int getPrice() {

        return this.price;
    }

    public String getDescription() {

        return this.description;
    }

    public void updateStock(int amount) {

        this.stockAmount -= amount;
    }

    /**
     * 재품의 상세정보를 일괄적으로 수정하는 메서드
     *
     * @param detail 수정된 제품의 상세정보를 담은 record 형식의 DTO 객체
     */
    public void update(NewProductDetail detail) {

        this.name = detail.name();
        this.price = detail.price();
        this.description = detail.description();
        this.stockAmount = detail.stockAmount();
    }

    /**
     * 괜히 만들었나 싶은 사실 toString() 메서드로 대체 가능한 메서드. 실제로 이 프로그램에서는 toString과 역할이 동일하다.
     *
     * @return 상품 객체의 정보를 특정 형식으로 반환하는 문자열
     */
    @Override
    public String getInfo() {

        return String.format("%-42s|%,12d원| %-20s", this.name, this.price, this.description);
    }
}
