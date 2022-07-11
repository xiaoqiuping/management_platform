package com.liubity.platform_starter.model.raw_materials_purchase;

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
 * （生产订单号？还是原材料采购订单号？）单号，原材料名称，型号，单价，数量，创建人，创建时间，采购人，采购时间，订单状态
 * 采购订单
 * @Author Liubity
 * @Date 2020-12-07
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_rmp_purchase_order")
public class PurchaseOrder extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //采购单单号
    @Column(name = "order_no",nullable=false,length=50)
    @JsonProperty("order_no")
    private String orderNo;
    
    //创建人
    @Column(name = "duty_man",nullable=false,length=50)
    @JsonProperty("duty_man")
    private String dutyMan;
    
    //采购人
    @Column(name = "purchase_man",nullable=false,length=50)
    @JsonProperty("purchase_man")
    private String purchaseMan;
    
    //采购时间
    @Column(name = "purchase_date",nullable=false,length=50)
    @JsonProperty("purchase_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date purchaseDate;
    
    //订单状态
    @Column(name = "status",nullable=false,length=50)
    @JsonProperty("status")
    private Integer status;
    
    //备注
    @Column(name="remarks",length=500)
    @JsonProperty("remarks")
    private String remarks;
    
    
    /**
     * 应付单号
     */
    @Column(name = "yf_order_no",length=300)
    @JsonProperty("yf_order_no")
    private String yfOrderNo;
    
    @JsonProperty("raw_material_list")
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "t_rmp_ref_po_prm", joinColumns = {@JoinColumn(name = "po_id", referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name = "prm_id", referencedColumnName = "id")})
    List<PurchaseRawMaterial> rawMaterialList;
}
