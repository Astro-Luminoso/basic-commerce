package main.config;

import main.domain.entity.Product;
import main.service.AdminAuthentication;

import java.util.ArrayList;
import java.util.List;

public class AppConfig {

    
    public List<Product> getElectronicProducts() {

        return new ArrayList<>(List.of(
                new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 50),
                new Product("iPhone 16", 1500000, "apple의 최신 스마트폰", 30),
                new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재 노트북", 35),
                new Product("AirPods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 70)
        ));
    }

    public List<Product> getClothsProducts() {

        return new ArrayList<>(List.of(
                new Product("Air Jordan One Mid", 170000, "편안한 착용감의 운동화", 100),
                new Product("Adidas Original Samba", 120000, "대중적인 운동화", 80),
                new Product("Levi's 501 Jeans original", 90000, "클래식한 생지 청바지", 120),
                new Product("Uniqlo Heat-tech Cashmere blended T-Shirt", 25000, "캐시미어 블랜드로 따뜻한 보온성을 제공하는 티셔츠", 200),
                new Product("Canada Goose Puffer Jacket", 530000, "캐나다 구스 패딩", 100)
        ));
    }

    public List<Product> getFoodsProducts() {

        return new ArrayList<>(List.of(
                new Product("비비고 고기가득만두 900g", 19800, "비비고 지지고 볶는 혜자 만두", 300),
                new Product("도드람 직화 돼지곱창", 14900, "믿고 못먹는(?) 도드람 돼지곱창", 300),
                new Product("초밥 50pcs", 34900, "가성비 하나 없는(?)초밥 50피스", 500)
        ));
    }

    public AdminAuthentication getAdminAuthentication() {

        return new AdminAuthentication("admin1234");
    }
}
