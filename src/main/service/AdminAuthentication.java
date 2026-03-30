package main.service;

public class AdminAuthentication {

    private final String pwd;

    public AdminAuthentication(String pwd) {
        this.pwd = pwd;
    }

    public boolean isAuthenticated(String pwd) {

        return this.pwd.equals(pwd);
    }
}
