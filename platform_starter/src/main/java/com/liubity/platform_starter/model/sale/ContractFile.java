package com.liubity.platform_starter.model.sale;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @Author Liubity
 * @Date 2020-11-28
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown=true, value={"contract"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_sale_contract_file")
public class ContractFile extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //合同文件
    @ManyToOne()
    @JoinColumn(name = "contract_id",nullable=false, insertable=false, updatable=false)
    private Contract contract;
    @Column(name = "contract_id", nullable=false)
    private Long contractId;
    
    
    //合同文件名
    @Column(name = "name",nullable=false)
    @JsonProperty("name")
    private String name;
    
    //合同存储名
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
