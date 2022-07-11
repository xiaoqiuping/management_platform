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
 * 零部件
 *
 * @Author Liubity
 * @Date 2020-11-29
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain=true)
@Entity
@Table(name="t_ws_semi_manufacture")
public class SemiManufacture extends BaseObject {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    /**
     * 零部件名称
     */
    @Column(name="semi_manufacture_name", nullable=false, length=50)
    @JsonProperty("semi_manufacture_name")
    private String semiManufactureName;
    
    /**
     * 物料Id
     */
    @Column(name="material_id", nullable=false, length=50)
    @JsonProperty("material_id")
    private Long materialId;
    
    /**
     * 零部件型号
     */
    @Column(name="semi_manufacture_model")
    @JsonProperty("semi_manufacture_model")
    private String semiManufactureModel;
    
    /**
     * 生产线
     */
    @Column(name="production_line")
    @JsonProperty("production_line")
    private String productionLine;
    
    /**
     * 操作日期
     */
    @Column(name="operation_time")
    @JsonProperty("operation_time")
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date operationTime;
    
    /**
     * 操作数量
     */
    @Column(name="operation_num")
    @JsonProperty("operation_num")
    private Integer operationNum;
    
    /**
     * 操作类型 0：入库，1：出库
     */
    @Column(name="operation_type")
    @JsonProperty("operation_type")
    private Integer operationType;
    
    /**
     * 责任人
     */
    @Column(name="duty_man")
    @JsonProperty("duty_man")
    private String dutyMan;
    
    /**
     * 质检人
     */
    @Column(name="quality_man")
    @JsonProperty("quality_man")
    private String qualityMan;
    
    /**
     * 备注
     */
    @Column(name="remarks")
    @JsonProperty("remarks")
    private String remarks;
    
    /**
     * 工序
     */
    @Column(name="processess")
    @JsonProperty("processess")
    private String processess;
    
    /**
     * 类别  1 零件，2：机芯，3，壳
     */
    @Column(name="type")
    @JsonProperty("type")
    private Integer type;
    
}
