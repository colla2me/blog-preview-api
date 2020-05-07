package com.github.entity;

public class Result {
    private String status;
    private String msg;
    private Object data;

    public static Result failure(String msg) {
        return new Result("fail", msg, null);
    }

    public static Result success(Object data, String msg) {
        return new Result("ok", msg, data);
    }

    public Result(String status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
