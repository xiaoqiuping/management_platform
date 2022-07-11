package com.liubity.platform_starter.model.warehouse_storage;

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
 * 物料领退补
 * @Author Liubity
 * @Date 2020-11-30
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_ws_material_order")
public class MaterialOrder extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    /**
     * 单号
     */
    @Column(name = "order_no")
    @JsonProperty("order_no")
    private String orderNo;
    
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
    @JsonFormat(timezone="GTM+8",pattern="yyyy-MM-dd")
    private Date operationTime;
    
    @Transient
    @JsonFormat(timezone="GTM+8",pattern="yyyy-MM-dd")
    private Date operationStartTime;
    @Transient
    @JsonFormat(timezone="GTM+8",pattern="yyyy-MM-dd")
    private Date operationEndTime;
    
    /**
     * 动作：1，领料，2，退料，3:补料
     */
    @Column(name = "operation_type")
    @JsonProperty("operation_type")
    private Integer operationType;
    
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
    
    @JsonProperty("material_operation_list")
    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy="materialOrder")
    private List<MaterialOperation> materialOperationList;
}
