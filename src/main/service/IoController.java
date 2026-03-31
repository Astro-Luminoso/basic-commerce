package main.service;

import main.domain.IterableOptions;
import main.domain.entity.Category;
import main.dto.NewProductDetail;

import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class IoController {

    private final Scanner sc;

    public IoController (Scanner sc) {
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

    public void printList(List<? extends IterableOptions> lists) {
        if(!lists.isEmpty()) {
            for (IterableOptions element : lists) {
                System.out.printf("%d. %s%n", lists.indexOf(element) + 1, element.getInfo());
            }
        }

        System.out.println("0. " + ((lists.getFirst() instanceof Category) ? "종료" : "뒤로가기"));
    }

    public int getIntValue(String regex) {

        String value = sc.nextLine();
        if(!value.matches(regex))
            throw new NumberFormatException("올바른 번호를 입력해주세요.\n");

        return Integer.parseInt(value);
    }

    public String adminAuthorization() {

        System.out.println("관리자 비밀번호를 입력해주세요: ");
        return sc.nextLine();
    }

    public void isAuthorised(boolean isLoggedIn, short chanceLeft) {

        if (isLoggedIn){
            System.out.println("관리자 로그인 성공!\n");

        } else {
            System.err.println("비밀번호가 틀렸습니다. 다시 입력해주세요.\n"
                    + ((chanceLeft == 0) ? "로그인 시도가 3회 실패하여 메인매뉴로 돌아갑니다.\n" : "")
            );
        }
    }

    public int getCategory(List<Category> categories) {

        System.out.println("카테고리를 선택해주세요: ");
        this.printList(categories);
        return this.getIntValue(String.format("^[0-%d]", categories.size()));
    }

    public NewProductDetail addProductDetail(String categoryName) {
        System.out.printf("[ %s 카테고리에 상품 추가 ]%n", categoryName);
        System.out.print("상품명을 입력해주세요: ");
        String name = sc.nextLine();
        System.out.print("상품 가격을 입력해주세요: ");
        int price = this.getIntValue("^\\d+0$");
        System.out.print("상품설명을 입력해주세요: ");
        String description = sc.nextLine();
        System.out.print("재고수량을 입력해주세요: ");
        int stockAmount = getIntValue("^\\d+$");

        return new NewProductDetail(name, price, description, stockAmount);
    }

    public void printDuplicationResult(boolean isUnique) {

        System.out.println((isUnique) ? "해당 항목이 추가됩니다." : "해당 항목은 이미 존재하므로 실행을 취소합니다.");
    }
}
