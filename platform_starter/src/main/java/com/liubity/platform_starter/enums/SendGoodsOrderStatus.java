package com.liubity.platform_starter.enums;

import java.util.Arrays;

/**
 * @Author Liubity
 * @Date 2020-12-18
 */
public enum SendGoodsOrderStatus {
    
    NEW(1, "新建"),
    WAREHOUSE_OUT(2, "出库送检"),
    EXAMINE_FAIL(3, "质检不通过"),
    EXAMINE_SUCCESS(4, "质检通过"),
    FINISHED(5, "已发货"),
    
    UNKNOWN(-9999, "未知");
    public Integer status;
    public String desc;
    
    SendGoodsOrderStatus(Integer status, String desc) {
        this.status=status;
        this.desc=desc;
    }
    
    public static String getSaleOrderDesc(Integer status) {
        return Arrays.stream(SendGoodsOrderStatus.values()).filter(sta -> sta.status.equals(status)).findFirst().orElse(SendGoodsOrderStatus.UNKNOWN).desc;
    }
}
