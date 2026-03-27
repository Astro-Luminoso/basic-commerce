package main.service;

import main.entity.Product;

import java.util.List;
import java.util.Scanner;

public class CommerceSystem {

    private List<Product> products;
    private Scanner sc;


    public CommerceSystem (List<Product> products, Scanner sc) {

        this.products = products;
        this.sc = sc;
    }


    public void start() {

        System.out.println("[ 실시간 커머스 플랫 - 전자제품 ]");

        for(Product product : products){
            System.out.printf("%d. %s", products.indexOf(product) + 1, product.toString());
        }

        System.out.println("0. 종료");

        while(true) {
            int value = sc.nextInt();
            if(value == 0) break;
        }

    }

}
