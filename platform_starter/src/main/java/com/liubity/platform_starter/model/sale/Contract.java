package com.liubity.platform_starter.model.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 销售合同
 * @Author Liubity
 * @Date 2020-11-26
 */
//
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_sale_contract")
public class Contract  extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //合同名称
    @Column(name = "contract_name",nullable=false)
    @JsonProperty("contract_name")
    private String contractName;
    
    //上传人
    @Column(name = "operator")
    @JsonProperty("operator")
    private String operator;
 
    //上传时间
    @Column(name = "upload_time")
    @JsonProperty("upload_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
    
    @Transient
    @JsonProperty("upload_start_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadStartTime;
    @Transient
    @JsonProperty("upload_end_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadEndTime;
    
    //备注
    @Column(name = "remarks")
    @JsonProperty("remarks")
    private String remarks;
    
    @JsonProperty("file_list")
    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy="contract")
    List<ContractFile> fileList;
}
