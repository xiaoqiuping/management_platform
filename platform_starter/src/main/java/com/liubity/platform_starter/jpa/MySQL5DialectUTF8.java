package com.liubity.platform_starter.jpa;

import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQLStorageEngine;

/**
 * 解决JPA默认创建表的时候使用引擎和编码集的问题
 *
 * @Author: Liubity
 * @Date: 2020/11/14 12:04
 */
public class MySQL5DialectUTF8 extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }

    @Override
    public MySQLStorageEngine getDefaultMySQLStorageEngine(){
        return InnoDBStorageEngine.INSTANCE;
    }
}
