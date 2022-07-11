package com.liubity.platform_starter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Liubity
 * @Date 2020-11-12
 */
public class StringUtil {
    
    private static final String PATTERN = "\\{}";
    
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str.trim());
    }
    
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
    
    public static String  replaceArgs(String msg,String...param){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher=pattern.matcher(msg);
        int i = 0;
        while (matcher.find()){
            String p="{}";
            if(i<param.length){
                p = param[i++];
            }
            msg = msg.replaceFirst(PATTERN,p);
        }
        return msg;
    }
    
    public static void main(String[] args) {
        System.out.println(isNotEmpty(""));
    }

}
