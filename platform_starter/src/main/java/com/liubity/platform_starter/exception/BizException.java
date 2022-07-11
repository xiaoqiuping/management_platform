package com.liubity.platform_starter.exception;

import com.liubity.platform_starter.utils.StringUtil;


/**
 * @Author Liubity
 * @Date 2020-12-02
 */
public class BizException extends RuntimeException {
    public BizException(String msg){
        super(msg);
    }
    public BizException(String msg,String...param){
        super(StringUtil.replaceArgs(msg,param));
    }
}
