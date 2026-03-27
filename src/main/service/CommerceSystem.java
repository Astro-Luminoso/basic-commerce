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
            } catch(InputMismatchException | IndexOutOfBoundsException e) {

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


    private int getProduct(String categoryName, List<Product> products) {

        System.out.println("[ " + categoryName + " 카테고리 ]");
        this.printList(List.copyOf(products));

        int value = this.getOption();
        if(indexIsInbound(value, products.size()))
            throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.");

        return value - 1;

    }

    private int getOption () {

        return sc.nextInt();
    }

    private boolean indexIsInbound (int inputValue, int collectionLength) {

        return inputValue < 0 || inputValue > collectionLength;
    }
    public void start() {

        Supplier<Integer> func = () -> {
            System.out.println("[ 실시간 커머스 플랫폼 ]");
            this.printList(List.copyOf(this.categories));
            int value = this.getOption();

            if (indexIsInbound(value, this.categories.size()))
                throw new IndexOutOfBoundsException("올바른 번호를 입력해주세요.");

            return value -1;
        };

        while (true){

            int categoryIndex = this.loopMethod(func);
            if (categoryIndex == -1) break;
            Category category = this.categories.get(categoryIndex);

            int productIndex = this.loopMethod(() -> this.getProduct(category.getInfo(), category.getProducts()));
            if (productIndex == -1) continue;
            Product product = category.getProducts().get(productIndex);

            System.out.println(product.getInfo());
        }
    }

}
