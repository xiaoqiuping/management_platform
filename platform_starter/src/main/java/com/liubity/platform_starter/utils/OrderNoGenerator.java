package com.liubity.platform_starter.utils;

import com.liubity.platform_starter.enums.OrderNoType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Liubity
 * @Date 2020-11-12
 */
public class OrderNoGenerator {
    
    private static final AtomicInteger SEQ=new AtomicInteger(0);
    private static final DateTimeFormatter DATE_TIME_FORMATTER=DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
    
    public static String generateOrderNo() {
        LocalDateTime localDateTime=LocalDateTime.now();
        int intValue=SEQ.incrementAndGet();
        String num;
        if (intValue < 10000) {
            num="00000".substring(String.valueOf(intValue).length()) + intValue;
        } else {
            num="" + intValue;
        }
        if (intValue > 99999) {
            SEQ.getAndSet(0);
        }
        return localDateTime.format(DATE_TIME_FORMATTER) + num;
    }
    
    public static String orderNo(String type){
        return type+generateOrderNo();
    }
    
    public static void main(String[] args) {
        System.out.println(orderNo("YS"));
        System.out.println(orderNo("XS"));
        System.out.println(OrderNoGenerator.orderNo(OrderNoType.YS.type));
    }
    
}
