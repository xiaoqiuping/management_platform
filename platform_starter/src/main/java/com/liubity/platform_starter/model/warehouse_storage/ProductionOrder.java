package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.backstage.Material;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 生产订单
 * @Author Liubity
 * @Date 2020-11-29
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_ws_production_order")
public class ProductionOrder extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 生产订单号
     */
    @Column(name = "order_no",nullable=false,length=50)
    @JsonProperty("order_no")
    private String orderNo;
    
    /**
     * 产品名称
     */
    @Column(name = "production_name",nullable=false,length=50)
    @JsonProperty("production_name")
    private String productionName;
    
    /**
     * 产品型号
     */
    @Column(name = "production_type")
    @JsonProperty("production_type")
    private String productionType;
    
    /**
     * 类型，1，成品，2，零件
     */
    @Column(name = "type",nullable=false,length=1)
    @JsonProperty("type")
    private Integer type;
    
    
    /**
     * 生产总数量
     */
    @Column(name = "total_num")
    @JsonProperty("total_num")
    private Integer totalNum;
    
    /**
     * 单价
     */
    @Column(name = "price")
    @JsonProperty("price")
    private BigDecimal price;
    
    /**
     * 总金额
     */
    @Column(name = "total_price")
    @JsonProperty("total_price")
    private BigDecimal totalPrice;
    
    /**
     * 订单周期
     */
    @Column(name = "order_cycle")
    @JsonProperty("order_cycle")
    private Integer orderCycle;
    
//    /**
//     * 负责人
//     */
//    @Column(name = "duty_man")
//    @JsonProperty("duty_man")
//    private String dutyMan;
    
    /**
     * 完成日期
     */
    @Column(name = "production_finished_date")
    @JsonProperty("production_finished_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date productionFinishedDate;
    
    /**
     * 发货形式
     */
    @Column(name = "send_goods_type")
    @JsonProperty("send_goods_type")
    private String sendGoodsType;
    
    /**
     * 订单要求
     */
    @Column(name = "order_requirement")
    @JsonProperty("order_requirement")
    private String orderRequirement;
    
    /**
     * 订单状态
     */
    @Column(name = "status")
    @JsonProperty("status")
    private Integer status;
    
    /**
     * 订单生成时间
     */
    @Column(name = "order_create_time")
    @JsonProperty("order_create_time")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderCreateTime;
    
    /**
     * 销售单号
     */
    @Column(name = "sale_order_no")
    @JsonProperty("sale_order_no")
    private String saleOrderNo;
    
    /**
     * 备注
     */
    @Column(name = "remarks")
    @JsonProperty("remarks")
    private String remarks;
    
    
    /**
     * 审核状态
     */
    @Column(name = "verify_status")
    @JsonProperty("verify_status")
    private Integer verifyStatus;
    /**
     * 审核人
     */
    @Column(name = "verify_man")
    @JsonProperty("verify_man")
    private String verifyMan;
    /**
     * 审核意见
     */
    @Column(name = "verify_remarks")
    @JsonProperty("verify_remarks")
    private String verifyRemarks;
    
    /**
     * 质检人
     */
    @Column(name = "examine_man")
    @JsonProperty("examine_man")
    private String examineMan;
    /**
     * 质检意见
     */
    @Column(name = "examine_remarks")
    @JsonProperty("examine_remarks")
    private String examineRemarks;
    
    
    
    @JsonProperty("process_list")
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "t_ws_ref_po_pp", joinColumns = {@JoinColumn(name = "po_id", referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name = "pp_id", referencedColumnName = "id")})
    List<ProductProcess> processList;
    
    
//    @JsonProperty("material_list")
//    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy="productionOrder")
//    private List<ProductMaterial> materialList;
    
    
    /**
     * 从哪个部门发起的请求
     */
    @Transient
    @JsonProperty("from")
    private String from;
}
