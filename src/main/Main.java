package main;

import main.config.AppConfig;
import main.domain.entity.Category;
import main.service.CommerceSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AppConfig appConfig = new AppConfig();

        Category electronic = new Category("전자제품", appConfig.getElectronicProducts());
        Category cloths = new Category("의류", appConfig.getClothsProducts());
        Category foods = new Category("식품", appConfig.getFoodsProducts());

        List<Category> categories = new ArrayList<>(List.of(electronic, cloths, foods));
        CommerceSystem app = new CommerceSystem(categories, sc);

        app.start();

    }
}
