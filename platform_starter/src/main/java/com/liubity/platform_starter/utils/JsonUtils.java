package com.liubity.platform_starter.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * @Author Liubity
 * @Date 2020-11-22
 */
public class JsonUtils {
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static <T> T jsonToBean(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType=MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static <K, V> Map<K, V> jsonToMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        JavaType javaType=MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
