package com.liubity.platform_starter.model.financial_management;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 发票附件
 * @Author Liubity
 * @Date 2020-12-15
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_fm_invoice_file")
public class InvoiceFile extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //采购合同文件名
    @Column(name = "name",nullable=false)
    @JsonProperty("name")
    private String name;
    
    //采购合同存储名
    @Column(name = "file_name",nullable=false)
    @JsonProperty("file_name")
    private String fileName;
    
    //存储路径
    @Column(name = "url",nullable=false)
    @JsonProperty("url")
    private String url;
    
    //文件大小
    @Column(name = "size",nullable=false)
    @JsonProperty("size")
    private Long size;
    
    //文件类型
    @Column(name = "type",nullable=false)
    @JsonProperty("type")
    private String type;
}
