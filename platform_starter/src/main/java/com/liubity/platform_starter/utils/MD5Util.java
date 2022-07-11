package com.liubity.platform_starter.utils;

import org.springframework.util.DigestUtils;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public class MD5Util {
    
    private static final String slat = "com.liubity.common.util";
    
    public static String md5(String str) {
        return DigestUtils.md5DigestAsHex((str+slat).getBytes()).toUpperCase();
    }
    
    public static void main(String[] args) {
        System.out.println(MD5Util.md5("123456789"));
    }
}
