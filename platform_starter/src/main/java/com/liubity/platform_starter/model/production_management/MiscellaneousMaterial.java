package com.liubity.platform_starter.model.production_management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-12-09
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_pm_miscellaneous_material")
public class MiscellaneousMaterial extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 领料人
     */
    @Column(name = "duty_man")
    @JsonProperty("duty_man")
    private String dutyMan;
    
    /**
     * 领料时间
     */
    @Column(name = "operation_time")
    @JsonProperty("operation_time")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date operationTime;
    
    @Transient
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date operationStartTime;
    @Transient
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date operationEndTime;
    
    /**
     * 动作：1，领料，2，退料，3:补料
     */
    @Column(name = "operation_type")
    @JsonProperty("operation_type")
    private Integer operationType;
    
    /**
     * 物料名称
     */
    @Column(name = "material_name",nullable=false)
    @JsonProperty("material_name")
    private String materialName;
    
    
    /**
     * 数量
     */
    @Column(name = "operation_num")
    @JsonProperty("operation_num")
    private Integer operationNum;
    
    
    /**
     * 操作人
     */
    @Column(name = "operation_man")
    @JsonProperty("operation_man")
    private String operationMan;
    
    /**
     * 备注
     */
    @Column(name="remarks")
    @JsonProperty("remarks")
    private String remarks;
    
}
