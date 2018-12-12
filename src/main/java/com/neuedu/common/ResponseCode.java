package com.neuedu.common;

public enum ResponseCode {
    PARAM_EMPTY(2,"参数为空"),
    EXISTS_USERNAME(3,"用户名已存在"),
    EXISTS_EMAIL(4,"邮箱已存在"),
    NOT_EXISTS_USERNAME(5,"用户名不存在"),
    USER_NOT_LOGIN(6,"用户未登入")
    ;
    private int status;
    private String msg;

    ResponseCode() {
    }

    ResponseCode(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
