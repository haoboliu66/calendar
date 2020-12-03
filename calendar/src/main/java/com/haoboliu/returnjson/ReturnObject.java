package com.haoboliu.returnjson;

import java.io.Serializable;

public class ReturnObject implements Serializable {

    private Integer code;
    private String message;
    private Object result;
    private String token;

    {
        code = 200;
        message = "";
        token = "";
    }

    public ReturnObject() {
    }

    public ReturnObject(String message, Object result) {
        this.message = message;
        this.result = result;
    }

    public ReturnObject(Object result) {
        this(200, "", result);
    }

    public ReturnObject(Integer code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public ReturnObject setCode(Integer code){
        this.code = code;
        return this;
    }

    public ReturnObject setMessage(String message){
        this.message = message;
        return this;
    }

    public ReturnObject setResult(Object result){
        this.result = result;
        return this;
    }

    public ReturnObject setToken(String token) {
        this.token = token;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }

    public String getToken() {
        return token;
    }
}
