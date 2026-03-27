package Main;

import Main.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Product> products = new ArrayList<>(List.of(
                new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 50),
                new Product("iPhone 16", 1500000, "apple의 최신 스마트폰", 30),
                new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재 노트북", 35),
                new Product("AirPods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 70)
        ));

        for(Product product : products){
            System.out.printf("%d. %s", products.indexOf(product) + 1, product.toString());
        }
    }
}
