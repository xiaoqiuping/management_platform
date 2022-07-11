package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库单
 *
 * @Author Liubity
 * @Date 2020-11-29
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain=true)
@Entity
@Table(name="t_ws_warehouse_order")
public class WarehouseOrder extends BaseObject {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    /**
     * 入库单单号
     */
    @Column(name="order_no", nullable=false, length=50)
    @JsonProperty("order_no")
    private String orderNo;
    
    /**
     * 采购单单号
     */
    @NotNull(message="采购单号不能为空")
    @Column(name="cg_order_no", nullable=false, length=50)
    @JsonProperty("cg_order_no")
    private String cgOrderNo;
    
    /**
     * 采购名称
     */
    @NotNull(message="采购名称不能为空")
    @Column(name="purchasing_name", nullable=false, length=50)
    @JsonProperty("purchasing_name")
    private String purchasingName;
    
    /**
     * 采购单位
     */
    @Column(name="purchasing_unit")
    @JsonProperty("purchasing_unit")
    private String purchasingUnit;
    
    /**
     * 联系人
     */
    @Column(name="contact")
    @JsonProperty("contact")
    private String contact;
    
    /**
     * 联系人电话
     */
    @Column(name="phone")
    @JsonProperty("phone")
    private String phone;
    
    /**
     * 采购日期
     */
    @Column(name="purchase_date")
    @JsonProperty("purchase_date")
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date purchaseDate;
    
    @Transient
    @JsonProperty("purchase_start_date")
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date purchaseStartDate;
    @Transient
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @JsonProperty("purchase_end_date")
    private Date purchaseEndDate;
    
    /**
     * 采购价格
     */
    @Column(name="purchase_price")
    @JsonProperty("purchase_price")
    private BigDecimal purchasePrice;
    
    /**
     * 采购总价
     */
    @Column(name="purchase_total_price")
    @JsonProperty("purchase_total_price")
    private BigDecimal purchaseTotalPrice;
    
    
    /**
     * 备注
     */
    @Column(name="remarks")
    @JsonProperty("remarks")
    private String remarks;
}
