package com.liubity.platform_starter.enums;

import java.util.Arrays;

/**
 * 生产订单状态
 * @Author Liubity
 * @Date 2020-11-26
 */
public enum SCOrderStatus {
    //仓库
    NEW(1, "新建"),
    //生产计划
    NO_SEND(2, "未发送"),
    NEED_VERIFY(3, "待审核"),
    VERIFY_FAIL(4, "审核未通过"),
    VERIFY_SUCCESS(5, "审核通过"),
    //生产管理
    IN_PRODUCTION(6, "生产中"),
    FINISHED_PRODUCTION(7, "生产完成"),
    
    
    //质检
    EXAMINING(8, "送检中"),
    EXAMINE_FAILED(9, "质检未通过"),
    EXAMINE_SUCCESS(10, "质检通过"),
    //仓库
    STORAGE_ING(11, "入库"),
    STORAGE_SUCCESS(12, "入库成功"),
    HAND_UP(13, "挂起"),
    UNKNOWN(-9999, "未知");
    public Integer status;
    public String desc;
    private SCOrderStatus(Integer status, String desc) {
        this.status=status;
        this.desc=desc;
    }
    
    public static String getSCOrderDesc(Integer status){
        return Arrays.stream(SCOrderStatus.values()).filter(sta -> sta.status.equals(status)).findFirst().orElse(SCOrderStatus.UNKNOWN).desc;
    }
}
