package com.liubity.platform_starter.model.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * 发货单
 *
 * @Author Liubity
 * @Date 2020-12-18
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain=true)
@Entity
@Table(name="t_sale_send_goods_order")
public class SendGoodsOrder extends BaseObject {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    /**
     * 发货单单号
     */
    @Column(name="order_no", nullable=false, length=50)
    @JsonProperty("order_no")
    private String orderNo;
    
    /**
     * 产品名称
     */
    @Column(name="production_name", nullable=false, length=100)
    @JsonProperty("production_name")
    private String productionName;
    
    /**
     * 产品型号
     */
    @Column(name="production_type")
    @JsonProperty("production_type")
    private String productionType;
    
    /**
     * 数量
     */
    @Column(name="total_num")
    @JsonProperty("total_num")
    private Integer totalNum;
    
    /**
     * 销售单单号
     */
    @Column(name="sale_order_no")
    @JsonProperty("sale_order_no")
    private String saleOrderNo;
    
    /**
     * 客户名称
     */
    @Column(name="buyer")
    @JsonProperty("buyer")
    private String buyer;
    
    /**
     * 客户电话
     */
    @Column(name="buyer_phone")
    @JsonProperty("buyer_phone")
    private String buyerPhone;
    
    /**
     * 客户地址
     */
    @Column(name="buyer_address")
    @JsonProperty("buyer_address")
    private String buyerAddress;
    
    /**
     * 快递方式
     */
    @Column(name="express_type")
    @JsonProperty("express_type")
    private String expressType;
    
    /**
     * 发送人
     */
    @Column(name="sender")
    @JsonProperty("sender")
    private String sender;
    
    /**
     * 发送日期
     */
    @Column(name="send_date")
    @JsonProperty("send_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date sendDate;
    
    /**
     * 快递单号
     */
    @Column(name="express_no")
    @JsonProperty("express_no")
    private String expressNo;
    
    /**
     * 备注
     */
    @Column(name="remarks")
    @JsonProperty("remarks")
    private String remarks;
    
    /**
     * 状态
     */
    @Column(name="status")
    @JsonProperty("status")
    private Integer status;
    
    @Transient
    private String from;
}
