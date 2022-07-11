package com.liubity.platform_starter.enums;

/**
 * @Author Liubity
 * @Date 2020-11-27
 */
public enum OrderNoType {
    
    YS("YS", "应收单"),
    YF("YF", "应付单"),
    WX("WX", "维修单"),
    SC("SC", "生产单"),
    XS("XS", "销售单"),
    FH("FH", "发货单");
    //类别
    public String type;
    //描述
    public String desc;
    
    OrderNoType(String type, String desc) {
        this.type=type;
        this.desc=desc;
    }
    
}
