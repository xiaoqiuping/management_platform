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

/**
 * 应收单名称，应收金额，应收单单号，创建时间，创建人
 * 根据采购入库的单号，生成付款单给财务  是的
 *
 * 应付单管理
 * @Author Liubity
 * @Date 2020-12-07
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_rmp_payable_order")
public class PayableOrder extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //应付单单号
    @Column(name="order_no",nullable=false,length=50)
    @JsonProperty("order_no")
    private String orderNo;
    
    //应付单名称
    @Column(name="payable_order_name",nullable=false,length=100)
    @JsonProperty("payable_order_name")
    private String payableOrderName;
    
    //应付金额
    @Column(name="total_amount",nullable=false,length=10)
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    
    //创建人
    @Column(name="duty_man")
    @JsonProperty("duty_man")
    private String dutyMan;
    
    //原料入库单单号
    @Column(name="rk_order_no")
    @JsonProperty("rk_order_no")
    private String rkOrderNo;
    
    //备注
    @Column(name="remarks",length=300)
    @JsonProperty("remarks")
    private String remarks;
    
    //类别 1：原料应付，2，杂料应付
    @Column(name="type",length=1)
    @JsonProperty("type")
    private Integer type;
    
    //0：未收，1，已收
    @Column(name = "status")
    @JsonProperty("status")
    private Integer status;
    
    /**
     * 付款日期
     */
    @Column(name="payable_date")
    @JsonProperty("payable_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date payableDate;
    
    /**
     * 付款备注
     */
    @Column(name = "financial_remarks",length=300)
    @JsonProperty("financial_remarks")
    private String financialRemarks;
    
}
