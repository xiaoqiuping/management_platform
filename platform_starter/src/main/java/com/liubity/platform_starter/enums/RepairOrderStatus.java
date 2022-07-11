package com.liubity.platform_starter.enums;

/**
 * @Author Liubity
 * @Date 2020-12-12
 */
public enum RepairOrderStatus {
    
    NEW(1, "新建"),
    EXAMINE_SUCCESS(2, "质检未通过"),
    EXAMINE_FAILED(3, "质检通过"),
    FINISHED_REPAIR(4, "维修完成");
    public Integer status;
    public String desc;
    private RepairOrderStatus(Integer status, String desc) {
        this.status=status;
        this.desc=desc;
    }
    
}
