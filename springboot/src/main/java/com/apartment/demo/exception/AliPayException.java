package com.apartment.demo.exception;

public class AliPayException extends RuntimeException {
    private String code;
    private String msg;

    public AliPayException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
