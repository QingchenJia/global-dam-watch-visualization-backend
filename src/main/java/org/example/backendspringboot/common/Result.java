package org.example.backendspringboot.common;

import lombok.Data;

@Data
public class Result<T> {

    private int code;

    private String message;

    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.message = "success";
        result.data = data;
        return result;
    }

    public static Result<String> error(String data) {
        Result<String> result = new Result<>();
        result.code = 0;
        result.message = "error";
        result.data = data;
        return result;
    }
}
