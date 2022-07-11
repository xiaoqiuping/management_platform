package com.liubity.platform_starter.enums;

import java.util.Arrays;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public enum SaleOrderStatus {
    
    NORMAL(1, "新建"),
    HAND_UP(2, "挂起"),
    DELIVER_GOODS(3,"发货"),
    RETURN_GOODS(4,"退货"),
    FINISHED(5, "成交"),
    PRODUCTING(6, "生产中"),
    PRODUCT_FINISHED(7, "生产完成"),
    UNKNOWN(-9999, "未知");
    public Integer status;
    public String desc;
    
    SaleOrderStatus(Integer status, String desc) {
        this.status=status;
        this.desc=desc;
    }
    
    public static String getSaleOrderDesc(Integer status){
        return Arrays.stream(SaleOrderStatus.values()).filter(sta -> sta.status.equals(status)).findFirst().orElse(SaleOrderStatus.UNKNOWN).desc;
    }
    
}
