package com.liubity.platform_starter.enums;

import java.util.Arrays;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public enum PurchaseOrderStatus {
    
    NEW(1, "新建"),
    PURCHASING(2, "采购中"),
    PURCHASED(3, "采购完成"),
    EXAMINE_SUCCESS(4, "审核通过"),
    EXAMINEING(5, "审核中"),
    EXAMINE_FAILED(6, "审核未通过"),
    STORAGE_SUCCESS(7, "入库成功"),
    UNKNOWN(-9999, "未知");
    public Integer status;
    public String desc;
    PurchaseOrderStatus(Integer status, String desc) {
        this.status=status;
        this.desc=desc;
    }
    public static String getPurchaseOrderDesc(Integer status){
        return Arrays.stream(PurchaseOrderStatus.values()).filter(sta -> sta.status.equals(status)).findFirst().orElse(PurchaseOrderStatus.UNKNOWN).desc;
    }
}
