package com.liubity.platform_starter.model.after_sale_repairing;

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
 * 维修单
 * @Author Liubity
 * @Date 2020-12-12
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_asr_repair_order")
public class RepairOrder extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 维修单号
     */
    @Column(name = "order_no")
    @JsonProperty("order_no")
    private String orderNo;
    
    /**
     * 手表名称
     */
    @Column(name = "watch_name",length=100)
    @JsonProperty("watch_name")
    private String watchName;
    
    /**
     * 手表型号
     */
    @Column(name = "watch_type",length=100)
    @JsonProperty("watch_type")
    private String watchType;
    
    /**
     * 购买日期
     */
    @Column(name = "buy_date")
    @JsonProperty("buy_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date buyDate;
    
    /**
     * 保修截止日期
     */
    @Column(name = "guarantee_date")
    @JsonProperty("guarantee_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date guaranteeDate;
    
    /**
     * 客户名称
     */
    @Column(name = "buyer_name")
    @JsonProperty("buyer_name")
    private String buyerName;
    
    /**
     * 客户联系电话
     */
    @Column(name = "buyer_phone")
    @JsonProperty("buyer_phone")
    private String buyerPhone;
    
    /**
     * 客户地址
     */
    @Column(name = "buyer_address")
    @JsonProperty("buyer_address")
    private String buyerAddress;
    
    /**
     * 故障描述
     */
    @Column(name = "fault_description",length=300)
    @JsonProperty("fault_description")
    private String faultDescription;
    
    /**
     * 维修金额
     */
    @Column(name = "repair_amount")
    @JsonProperty("repair_amount")
    private BigDecimal repairAmount;
    
    /**
     * 维修人
     */
    @Column(name = "repair_man")
    @JsonProperty("repair_man")
    private String repairMan;
    
    /**
     * 维修日期
     */
    @Column(name = "repair_date")
    @JsonProperty("repair_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date repairDate;
    
    /**
     * 维修分析
     */
    @Column(name = "repair_analysis",length=300)
    @JsonProperty("repair_analysis")
    private String repairAnalysis;
    
    /**
     * 送回日期
     */
    @Column(name = "send_back_date")
    @JsonProperty("send_back_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date sendBackDate;
    
    /**
     * 备注
     */
    @Column(name = "remarks",length=300)
    @JsonProperty("remarks")
    private String remarks;
    
    /**
     * 维修类别 1：保内维修，2，保外维修
     */
    @Column(name = "type")
    @JsonProperty("type")
    private Integer type;
    
    /**
     * 订单状态 1：新建，2，审核不通过，3，审核通过，4，维修完成
     */
    @Column(name = "status")
    @JsonProperty("status")
    private Integer status;
    
    /**
     * 审核备注
     */
    @Column(name = "examine_remarks",length=300)
    @JsonProperty("examine_remarks")
    private String examineRemarks;
    
    /**
     * 审核人
     */
    @Column(name = "examine_man",length=300)
    @JsonProperty("examine_man")
    private String examineMan;
    
    
    
    /**
     * 应收单号
     */
    @Column(name = "ys_order_no",length=300)
    @JsonProperty("ys_order_no")
    private String ysOrderNo;
    
    /**
     * 请求来源 1：维修，2，质检
     */
    @Transient
    @JsonProperty("from")
    private Integer from;
    
}
