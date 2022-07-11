package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * 成品管理
 * @Author Liubity
 * @Date 2020-11-29
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_ws_manufuture")
public class Manufacture extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 成品名称
     */
    @Column(name = "manufacture_name",nullable=false,length=50)
    @JsonProperty("manufacture_name")
    private String manufactureName;
    
    /**
     * 物料Id
     */
    @Column(name="material_id", nullable=false, length=50)
    @JsonProperty("material_id")
    private Long materialId;
    
    /**
     * 成品型号
     */
    @Column(name = "manufacture_model",nullable=false,length=50)
    @JsonProperty("manufacture_model")
    private String manufactureModel;
    
    /**
     * 操作日期
     */
    @Column(name = "operation_time")
    @JsonProperty("operation_time")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date operationTime;
    
    /**
     * 操作数量
     */
    @Column(name = "operation_num")
    @JsonProperty("operation_num")
    private Integer operationNum;
    
    /**
     * 操作类型 1：入库，2：出库
     */
    @Column(name = "operation_type")
    @JsonProperty("operation_type")
    private Integer operationType;
    
    /**
     * 入库人
     */
    @Column(name = "operation_man")
    @JsonProperty("operation_man")
    private String operationMan;
    
    /**
     * 质检人
     */
    @Column(name = "quality_man")
    @JsonProperty("quality_man")
    private String qualityMan;
    
    /**
     * 销售订单号
     */
    @Column(name = "sale_order_no")
    @JsonProperty("sale_order_no")
    private String saleOrderNo;
    
    /**
     * 备注
     */
    @Column(name="remarks")
    @JsonProperty("remarks")
    private String remarks;
    
    /**
     * 类别  1 零件，2：机芯，3：壳，4：表
     */
    @Column(name = "type")
    @JsonProperty("type")
    private Integer type;
}
