package main.service;

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

    public int getOption(String regex) {

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
}
