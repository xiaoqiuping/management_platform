package com.liubity.platform_starter.model.backstage;

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
 * 供应商管理
 * @Author Liubity
 * @Date 2020-11-19
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_backstage_supplier")
public class SupplierManagement extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 供应商名称
     */
    @JsonProperty("supplier_name")
    @Column(name="supplier_name",nullable=false,length=100)
    private String supplierName;
    
    /**
     * 供应商地址
     */
    @JsonProperty("supplier_address")
    @Column(name="supplier_address",length=300)
    private String supplierAddress;
    
    /**
     * 供应商联系人
     */
    @JsonProperty("contact")
    @Column(name="contact",length=50)
    private String contact;
    
    /**
     * 联系人职务
     */
    @JsonProperty("contact_duty")
    @Column(name="contact_duty",length=50)
    private String contactDuty;
    
    /**
     * 联系人电话
     */
    @JsonProperty("contact_phone")
    @Column(name="contact_phone",length=50)
    private String contactPhone;
    
    /**
     * 合作日期
     */
    @JsonProperty("cooperation_date")
    @Column(name="cooperation_date")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date cooperationDate;
    
    /**
     * 备注
     */
    @JsonProperty("remarks")
    @Column(name="remarks",length=50)
    private String remarks;
    
    @Transient
    @JsonProperty("cooperation_start_date")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date cooperationStartDate;
    @Transient
    @JsonProperty("cooperation_end_date")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date cooperationEndDate;
    
    @JsonProperty("materials")
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "t_backstage_ref_supplier_material", joinColumns = {@JoinColumn(name = "supplier_id", referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name = "material_id", referencedColumnName = "id")})
    List<Material> materials;
}
