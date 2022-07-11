package com.liubity.platform_starter.model.warehouse_storage;

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
 * 原材料
 *
 * @Author Liubity
 * @Date 2020-11-29
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain=true)
@Entity
@Table(name="t_ws_raw_material")
public class RawMaterial extends BaseObject {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    /**
     * 原料名称
     */
    @Column(name="raw_material_name", nullable=false, length=50)
    @JsonProperty("raw_material_name")
    private String rawMaterialName;
    
    /**
     * 物料Id
     */
    @Column(name="material_id", nullable=false, length=50)
    @JsonProperty("material_id")
    private Long materialId;
    
    /**
     * 原料价格
     */
    @Column(name="raw_material_price")
    @JsonProperty("raw_material_price")
    private BigDecimal rawMaterialPrice;
    
    /**
     * 操作数量
     */
    @Column(name="operation_num")
    @JsonProperty("operation_num")
    private Integer operationNum;
    
    /**
     * 操作类型 1：入库，2：出库
     */
    @Column(name="operation_type")
    @JsonProperty("operation_type")
    private Integer operationType;
    
    /**
     * 操作日期
     */
    @Column(name="operation_time")
    @JsonProperty("operation_time")
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date operationTime;
    
    /**
     * 责任人
     */
    @Column(name="duty_man")
    @JsonProperty("duty_man")
    private String dutyMan;
    
    /**
     * 备注
     */
    @Column(name="remarks")
    @JsonProperty("remarks")
    private String remarks;
}
