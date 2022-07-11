package com.liubity.platform_starter.model.common;

import lombok.Data;

/**
 * @Author: Liubity
 * @Date: 2020/11/15 16:13
 */
@Data
public class ReturnCode {
    /**
     * 成功
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败
     */
    public static final Integer ERROR = 500;

    /**
     * 未登录
     */
    public static final Integer NOT_LOGIN = 401;
}
