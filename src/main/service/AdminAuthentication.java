package main.service;

/**
 * 관리자 인증을 관리하는 클래스
 */
public class AdminAuthentication {

    private final String pwd;

    public AdminAuthentication(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 입력된 비밀번호가 관리자 비밀번호와 일치하는지 확인하는 메소드
     *
     * @param pwd 사용자가 입력한 비밀번호
     * @return 동일하면 true, otherwise false
     */
    public boolean isAuthenticated(String pwd) {

        return this.pwd.equals(pwd);
    }
}
