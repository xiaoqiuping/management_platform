package com.liubity.platform_starter.enums;

import java.util.Arrays;

/**
 * @Author Liubity
 * @Date 2020-12-09
 */
public enum OutSourceOrderStatus {
    NORMAL(1, "新建"),
    OUT(2, "委外中"),
    FINISHED(3, "完成"),
    UNKNOWN(-9999, "未知");
    public Integer status;
    public String desc;
    
    OutSourceOrderStatus(Integer status, String desc) {
        this.status=status;
        this.desc=desc;
    }
    public static String getOutSourceOrderDesc(Integer status){
        return Arrays.stream(OutSourceOrderStatus.values()).filter(sta -> sta.status.equals(status)).findFirst().orElse(OutSourceOrderStatus.UNKNOWN).desc;
    }
}
