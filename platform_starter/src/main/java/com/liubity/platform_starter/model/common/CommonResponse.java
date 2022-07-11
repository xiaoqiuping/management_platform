package com.liubity.platform_starter.model.common;

import com.liubity.platform_starter.utils.StringUtil;
import lombok.Data;

/**
 * @Author Liubity
 * @Date 2020-11-12
 */
@Data
public class CommonResponse {
    
    
    Object data;
    Result result;
    
    public CommonResponse() {
    }
    public CommonResponse(Result result) {
        this.result=result;
    }
    public CommonResponse(Object data, Result result) {
        this.data=data;
        this.result=result;
    }
    
//    public static CommonResponse build(Integer code, String msg, Object o){
//        return new CommonResponse(o,new Result(code,msg));
//    }
    public static CommonResponse build(Result result, Object o){
        return new CommonResponse(o,result);
    }
    
    public static CommonResponse build(Result result){
        CommonResponse commonResponse=new CommonResponse();
        commonResponse.setResult(result);
        return commonResponse;
    }
    
    public static CommonResponse build(Integer code, String msg){
        return new CommonResponse(new Result(code,msg));
    }
    public static CommonResponse build(Integer code, String msg,String...param){
        return new CommonResponse(new Result(code, StringUtil.replaceArgs(msg,param)));
    }

}
