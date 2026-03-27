package main.service;

import main.domain.entity.Category;
import main.domain.IterableOptions;
import main.domain.entity.Product;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class CommerceSystem {

    private List<Category> categories;
    private Scanner sc;


    public CommerceSystem (List<Category> categories, Scanner sc) {

        this.categories = categories;
        this.sc = sc;
    }

    private <T> T loopMethod(Supplier<T> actionMethod) {

        while(true) {
            try{
                return actionMethod.get();
            } catch(NumberFormatException | IndexOutOfBoundsException e) {

                System.err.println(e.getMessage());
                System.out.println();
            }

        }
    }

    private void printList(List<IterableOptions> lists) {

        if(!lists.isEmpty()) {
            for (IterableOptions element : lists) {
                System.out.printf("%d. %s%n", lists.indexOf(element) + 1, element.getInfo());
            }
        }

        System.out.println("0. " + ((lists.getFirst() instanceof Category) ? "종료" : "뒤로가기"));
    }


    private int getProductIndex(String categoryName, List<Product> products) {

        System.out.println("[ " + categoryName + " 카테고리 ]");

        return this.getIndex(List.copyOf(products));

    }

    private int getOption () {

        return Integer.parseInt(sc.nextLine());
    }

    private boolean indexIsInbound (int inputValue, int collectionLength) {

        return inputValue < 0 || inputValue > collectionLength;
    }

    private int getIndex (List<IterableOptions> lists) {
        this.printList(lists);
        int value = this.getOption();

        if (indexIsInbound(value, lists.size()))
            throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.");

        return value -1;
    }

    public void start() {

        // Should I keep this??? or Should I pick this away to outside of start method?
        Supplier<Integer> func = () -> {
            System.out.println("[ 실시간 커머스 플랫폼 ]");

            return this.getIndex(List.copyOf(this.categories));
        };


        while (true){

            int categoryIndex = this.loopMethod(func);
            if (categoryIndex == -1) break;
            Category category = this.categories.get(categoryIndex);

            int productIndex = this.loopMethod(() -> this.getProductIndex(category.getInfo(), category.getProducts()));
            if (productIndex == -1) continue;
            Product product = category.getProducts().get(productIndex);

            System.out.println(product.getInfo());
        }
    }

}
