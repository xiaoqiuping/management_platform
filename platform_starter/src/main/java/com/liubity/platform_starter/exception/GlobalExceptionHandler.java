package com.liubity.platform_starter.exception;

import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.ReturnCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Liubity
 * @Date 2020-12-04
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public CommonResponse handleBizException(BizException bizException){
        return CommonResponse.build(ReturnCode.ERROR,bizException.getMessage());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public CommonResponse handleRuntimeException(RuntimeException runtimeException){
        return CommonResponse.build(ReturnCode.ERROR,runtimeException.getMessage());
    }
    
    @ExceptionHandler(NullPointerException.class)
    public CommonResponse handleNullPointerException(){
        return CommonResponse.build(ReturnCode.ERROR,"NPE错误");
    }
    
    @ExceptionHandler(Exception.class)
    public CommonResponse handleException(Exception exception){
        return CommonResponse.build(ReturnCode.ERROR,"系统错误");
    }
}
