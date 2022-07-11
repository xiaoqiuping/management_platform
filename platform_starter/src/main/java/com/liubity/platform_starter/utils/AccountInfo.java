package com.liubity.platform_starter.utils;

import com.liubity.platform_starter.model.backstage.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @Author Liubity
 * @Date 2020-12-01
 */
public class AccountInfo {
    
    public static Account getAccount(HttpServletRequest httpServletRequest){
        String authorization=httpServletRequest.getHeader("Authorization");
        return (Account) CacheUtil.get(authorization);
    }
}
