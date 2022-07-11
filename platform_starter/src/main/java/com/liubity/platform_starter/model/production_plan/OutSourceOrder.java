package com.liubity.platform_starter.model.production_plan;

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
 * 委外单
 * @Author Liubity
 * @Date 2020-12-09
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_pp_outsource_order")
public class OutSourceOrder extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //委外订单号
    @Column(name="order_no",nullable=false,length=50)
    @JsonProperty("order_no")
    private String orderNo;
    
    //产品名称
    @Column(name="production_name",nullable=false,length=100)
    @JsonProperty("production_name")
    private String productionName;
    
    //产品数量
    @Column(name="production_num")
    @JsonProperty("production_num")
    private Integer productionNum;
    
    //支付费用
    @Column(name="cost")
    @JsonProperty("cost")
    private BigDecimal cost;
    
    //完工日期
    @Column(name="finished_date")
    @JsonProperty("finished_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date finishedDate;
    
    //委外厂家
    @Column(name="outsource_unit")
    @JsonProperty("outsource_unit")
    private String outSourceUnit;
    
    //委外厂家联系人
    @Column(name="contact")
    @JsonProperty("contact")
    private String contact;
    
    //联系人电话
    @Column(name="phone")
    @JsonProperty("phone")
    private String phone;
    
    //负责人
    @Column(name="duty_man")
    @JsonProperty("duty_man")
    private String dutyMan;
    
    //订单状态
    @Column(name="status")
    @JsonProperty("status")
    private Integer status;
    
    //备注
    @Column(name="remarks")
    @JsonProperty("remarks")
    private String remarks;
    
}
