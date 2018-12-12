package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 封装返回前端的高复用对象
 * @param <T>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
    //状态码
    private int status;
    //返回接口数据
    private T   data;
    //接口提示信息
    private String msg;

    //构造方法
    private ServerResponse() {

    }

    private ServerResponse(int status, String msg,T data) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(int status,T data) {
        this.status = status;
        this.data = data;

    }

    /**
     * 判断接口是否调用成功
     * @return
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==Const.SUCCESS_CODE;
    }

    /**
     * 成功
     * @return
     */

    public static ServerResponse createServerResponseBySuccess(){

        return new ServerResponse(Const.SUCCESS_CODE);
    }
    public static ServerResponse createServerResponseBySuccess(String msg){
        return new ServerResponse(Const.SUCCESS_CODE,msg);
    }


    public static <T> ServerResponse createServerResponseBySuccess(String msg,T data){
        return new ServerResponse(Const.SUCCESS_CODE,msg,data);
    }
    public static <T> ServerResponse createServerResponseBySuccess(T data){
        return new ServerResponse(Const.SUCCESS_CODE,data);
    }


    /**
     * 失败
     * @return
     */

    public static ServerResponse createServerResponseByError(){
        return new ServerResponse(Const.SUCCESS_ERROR);
    }
    public static ServerResponse createServerResponseByError(String msg){
        return new ServerResponse(Const.SUCCESS_ERROR,msg);
    }

    public static ServerResponse createServerResponseByError(int status){

        return new ServerResponse(status);
    }

    public static ServerResponse createServerResponseByError(int status,String msg){
        return new ServerResponse(status,msg);
    }




    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

