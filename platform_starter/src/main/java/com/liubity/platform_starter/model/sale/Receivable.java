package com.liubity.platform_starter.model.sale;

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
 * 应收款
 * @Author Liubity
 * @Date 2020-11-26
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_sale_receivable")
public class Receivable extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //订单号
    @Column(name = "order_no", nullable = false, length = 50)
    @JsonProperty("order_no")
    private String orderNo;
    
    //应收款
    @Column(name = "receive_money", nullable = false)
    @JsonProperty("receive_money")
    private BigDecimal receiveMoney;
    
    /**
     * 应收款类别
     * 1：合同应收款
     * 2：零件加工款
     * 3：手表销售款
     * 4：保内维修应收款
     * 5:保外维修应收款
     */
    @Column(name = "receive_money_type", nullable = false)
    @JsonProperty("receive_money_type")
    private Integer receiveMoneyType;
    
    //责任人
    @Column(name = "duty_man")
    @JsonProperty("duty_man")
    private String dutyMan;
    
    //备注
    @Column(name = "remarks",length=300)
    @JsonProperty("remarks")
    private String remarks;
    
    //1:销售，2，售后
    @Column(name = "type")
    @JsonProperty("type")
    private Integer type;
    
    //0：未收，1，已收
    @Column(name = "status")
    @JsonProperty("status")
    private Integer status;
    
    /**
     * 收款日期
     */
    @Column(name="receivable_date")
    @JsonProperty("receivable_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date receivableDate;
    
    /**
     * 收款备注
     */
    @Column(name = "financial_remarks",length=300)
    @JsonProperty("financial_remarks")
    private String financialRemarks;
    
    
    /**
     * 总金额
     */
    @Transient
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    /**
     * 日期
     */
    @Transient
    @JsonProperty("date")
    private String date;
}
