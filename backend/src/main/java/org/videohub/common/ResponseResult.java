package org.videohub.common;

import lombok.Data;

@Data
public class ResponseResult<T> {

    private int code;
    private String message;
    private T data;

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}

