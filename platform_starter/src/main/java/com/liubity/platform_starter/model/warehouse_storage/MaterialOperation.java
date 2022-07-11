package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @Author Liubity
 * @Date 2020-11-29
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_ws_material_operation")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown=true, value={"materialOrder"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialOperation{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    @ManyToOne()
    @JoinColumn(name = "material_order_id",nullable=false, insertable=false, updatable=false)
    private MaterialOrder materialOrder;
    @Column(name = "material_order_id", nullable=false)
    @JsonProperty("material_order_id")
    private Long materialOrderId;
    
    /**
     * 物料id
     */
    @Column(name = "material_id",nullable=false)
    @JsonProperty("material_id")
    private Long materialId;
    
    
    /**
     * 物料名称
     */
    @Column(name = "material_name",nullable=false)
    @JsonProperty("material_name")
    private String materialName;
    
    /**
     * 物料型号
     */
    @Column(name = "material_model")
    @JsonProperty("material_model")
    private String materialModel;
    
    
    /**
     * 物料类型 1：原料，2，零部件，3，半成品
     */
    @Column(name = "material_type")
    @JsonProperty("material_type")
    private Integer materialType;
    
    /**
     *
     * 子类型
     */
    @Column(name = "children_type")
    @JsonProperty("children_type")
    private Integer childrenType;
    
    /**
     * 数量
     */
    @Column(name = "operation_num")
    @JsonProperty("operation_num")
    private Integer operationNum ;

    
}
