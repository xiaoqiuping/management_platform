package com.liubity.platform_starter.enums;

/**
 * @Author Liubity
 * @Date 2020-12-12
 */
public enum Departments {
    
    SCJH("SCJH","生产计划"),
    SCGL("SCGL","生产管理"),
    CWGL("CWGL","财务管理"),
    XSGL("XSGL","销售管理"),
    CKGL("CKGL","仓库管理"),
    YLCG("YLCG","原料采购"),
    PZJY("PZJY","品质检验"),
    SHWX("SHWX","售后维修");
    
    //类别
    public String type;
    //描述
    public String desc;
    private Departments(String type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
