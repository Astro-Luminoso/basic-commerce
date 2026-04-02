package main.domain.entity;

/**
 * 고객(Customer) 클래스는 고객의 이름, 이메일, 멤버십 정보를 포함한다.
 * 필수과제해서 만들라고 해서 만들었다. 하지만 실질적으로 과제에서 사용한적은 단 한번도 없는 비운의 클래스다.
 */
public class Customer {

    private String name;
    private String email;
    private String membership;


    public Customer (String name, String email, String membership) {

        this.name = name;
        this.email = email;
        this.membership = membership;
    }
}
