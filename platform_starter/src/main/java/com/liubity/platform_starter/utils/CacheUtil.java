package com.liubity.platform_starter.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public class CacheUtil {
    
    private static Cache<String,Object> cache =Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(100000L)
            .build();
    
    public static Object get(String key){
        return cache.get(key,k->null);
    }
    
    public static void put(String key,Object obj){
        cache.put(key,obj);
    }
    public static void remove(String key){
        cache.invalidate(key);
    }
    public static void removeAll(){
        cache.invalidateAll();
    }
    
    public static void main(String[] args) throws InterruptedException {
        //CacheUtil.put("a","aaa");
        System.out.println(CacheUtil.get("a"));
        Thread.sleep(2000);
        System.out.println(CacheUtil.get("a"));
    }
    
}
