package com.liubity.platform_starter.model.quality_restriction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 产品工序质检
 * @Author Liubity
 * @Date 2020-12-12
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_qr_production_process_quality")
public class ProductionProcessQuality extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //生产订单号
    @Column(name = "sc_order_no",nullable=false,length=50)
    @JsonProperty("sc_order_no")
    private String scOrderNo;
    
    /**
     * 产品名称
     */
    @Column(name = "production_name",nullable=false,length=50)
    @JsonProperty("production_name")
    private String productionName;
    
    /**
     * 产品型号
     */
    @Column(name = "production_type",nullable=false,length=50)
    @JsonProperty("production_type")
    private String productionType;
    
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
     * 关联工序ID
     */
    @Column(name = "product_process_id")
    @JsonProperty("product_process_id")
    private Long productProcessId;
    
    /**
     * 关联工序名称
     */
    @Column(name = "product_process_name")
    @JsonProperty("product_process_name")
    private String productProcessName;
    
    /**
     * 步骤顺序
     */
    @Column(name = "process_step",nullable=false)
    @JsonProperty("process_step")
    private Integer processStep;
    
    /**
     * 质检状态 0 通过，1：不通过
     */
    @Column(name = "quality_status")
    @JsonProperty("quality_status")
    private Integer qualityStatus;
    
}
