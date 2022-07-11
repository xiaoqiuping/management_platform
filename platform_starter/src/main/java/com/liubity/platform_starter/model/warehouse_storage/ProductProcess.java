package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * 生产订单关联工艺
 * @Author Liubity
 * @Date 2020-11-29
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_ws_product_process")
public class ProductProcess {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    /**
     * 步骤顺序
     */
    @Column(name = "process_step",nullable=false)
    @JsonProperty("process_step")
    private Integer processStep;
    
    
    /**
     * 工艺ID
     */
    @Column(name = "process_id",nullable=false)
    @JsonProperty("process_id")
    private Long processId;
    
    /**
     * 工艺名称
     */
    @Column(name = "process_name",nullable=false)
    @JsonProperty("process_name")
    private String processName;
    
    /**
     * 质检数量
     */
    @Column(name = "quality_num")
    @JsonProperty("quality_num")
    private Integer qualityNum;
    
    /**
     * 通过数量
     */
    @Column(name = "access_num")
    @JsonProperty("access_num")
    private Integer accessNum;
    
    /**
     * 质检状态 0 通过，1：不通过
     */
    @Column(name = "quality_status")
    @JsonProperty("quality_status")
    private Integer qualityStatus;
    
    /**
     * 质检时间
     */
    @Column(name = "quality_time")
    @JsonProperty("quality_time")
    private Date qualityTime;
    
    /**
     * 质检人
     */
    @Column(name = "quality_man")
    @JsonProperty("quality_man")
    private String qualityMan;
    
    /**
     * 备注
     */
    @Column(name = "remarks")
    @JsonProperty("remarks")
    private String remarks;
    
    /**
     * 生产订单号
     */
    @Transient
    @JsonProperty("sc_order_no")
    private String scOrderNo;
    
    /**
     * 质检ID
     */
    @Transient
    @JsonProperty("product_process_quality_id")
    private Long productProcessQualityId;
}
