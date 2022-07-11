package com.liubity.platform_starter.model.raw_materials_purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购合同
 * @Author Liubity
 * @Date 2020-12-07
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_rmp_purchase_contract")
public class PurchaseContract extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //合同名称
    @Column(name = "contract_name",nullable=false)
    @JsonProperty("contract_name")
    private String contractName;
    
    //供应商
    @Column(name = "supplier")
    @JsonProperty("supplier")
    private String supplier;
    
    //总金额
    @Column(name = "total_amount")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    
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
    @Column(name="remarks",length=500)
    @JsonProperty("remarks")
    private String remarks;
    
    @JsonProperty("contract_file_list")
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "t_rmp_ref_pc_pcf", joinColumns = {@JoinColumn(name = "pc_id", referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name = "pcf_id", referencedColumnName = "id")})
    List<PurchaseContractFile> processList;
}
