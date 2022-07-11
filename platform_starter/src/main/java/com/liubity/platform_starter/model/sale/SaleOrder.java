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
import java.util.List;

/**
 * 销售单
 * @Author Liubity
 * @Date 2020-11-26
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_sale_sale_order")
public class SaleOrder  extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //订单号
    @Column(name = "order_no", nullable = false, length = 50)
    @JsonProperty("order_no")
    private String orderNo;
    
    //手表型号
    @Column(name = "watch_name", nullable = false, length = 50)
    @JsonProperty("watch_name")
    private String watchName;
    
    //手表型号
    @Column(name = "watch_type", nullable = false, length = 50)
    @JsonProperty("watch_type")
    private String watchType;
    
    //销售数量
    @Column(name = "sale_num", nullable = false)
    @JsonProperty("sale_num")
    private Integer saleNum;
    
    //成交价格
    @Column(name = "tran_price", nullable = false)
    @JsonProperty("tran_price")
    private BigDecimal tranPrice;
    
    //购买商
    @Column(name = "buyer")
    @JsonProperty("buyer")
    private String buyer;
    
    //销售人
    @Column(name = "salesman")
    @JsonProperty("salesman")
    private String salesman;
    
    
    /**
     * 应收单号
     */
    @Column(name = "ys_order_no",length=300)
    @JsonProperty("ys_order_no")
    private String ysOrderNo;
    
    //销售日期
    @Column(name = "sale_date")
    @JsonProperty("sale_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date saleDate;
    
    @Transient
    @JsonProperty("sale_start_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date saleStartDate;
    @Transient
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @JsonProperty("sale_end_date")
    private Date saleEndDate;
    
    
    //销售状态
    @Column(name = "status")
    @JsonProperty("status")
    private Integer status;
    @Transient
    @JsonProperty("status_list")
    private List<Integer> statusList;
    
    //销售变更
    @Column(name = "change_flag")
    @JsonProperty("change_flag")
    private Boolean changeFlag;
}
