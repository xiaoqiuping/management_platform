package com.liubity.platform_starter.model.common;

import lombok.Data;

/**
 * @Author Liubity
 * @Date 2020-11-12
 */
@Data
public class Result {
    
    private Integer code;
    
    private String msg;
    
    public Result(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    
    public static Result success() {
        return new Result(200,"OK");
    }
    public static Result fail() {
        return new Result(500,"fail");
    }
}
