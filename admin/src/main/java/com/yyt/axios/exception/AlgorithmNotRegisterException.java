package com.yyt.axios.exception;

public class AlgorithmNotRegisterException extends RuntimeException {
    private String msg;

    public AlgorithmNotRegisterException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
