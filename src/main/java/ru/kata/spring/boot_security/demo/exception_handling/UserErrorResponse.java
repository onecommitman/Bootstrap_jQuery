package ru.kata.spring.boot_security.demo.exception_handling;

public class UserErrorResponse {
    private String info;

    public UserErrorResponse() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
