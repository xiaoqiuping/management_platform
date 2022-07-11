package com.liubity.platform_starter.enums;

/**
 * @Author Liubity
 * @Date 2020-12-01
 */
public enum OperationType {
    PUT(0 ),
    TAKE(1 );
    public Integer status;
    private OperationType(Integer status) {
        this.status=status;
    }
}
