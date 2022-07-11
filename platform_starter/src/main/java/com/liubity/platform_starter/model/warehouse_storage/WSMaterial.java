package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 物料管理
 * @Author Liubity
 * @Date 2020-11-29
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_ws_material")
public class WSMaterial extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    /**
     * 物料类别
     */
    @Column(name = "material_type")
    @JsonProperty("material_type")
    private Integer materialType;
    
    /**
     * 子类别
     */
    @Column(name = "children_type")
    @JsonProperty("children_type")
    private Integer childrenType;
    
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
     * 图号
     */
    @Column(name = "tu_hao")
    @JsonProperty("tu_hao")
    private String tuHao;

    
    /**
     * 库存量
     */
    @Column(name = "total_num")
    @JsonProperty("total_num")
    private Integer totalNum = 0;
    
    /**
     * 价格
     */
    @Column(name = "price")
    @JsonProperty("price")
    private BigDecimal price = BigDecimal.ZERO;
    
    
    @Column(name = "remarks")
    @JsonProperty("remarks")
    private String remarks;
    
//    /**
//     * 最后操作时间
//     */
//    @Column(name = "last_operation_time")
//    @JsonProperty("last_operation_time")
//    @JsonFormat(timezone="GTM+8",pattern="yyyy-MM-dd HH:mm:ss")
//    private Date lastOperationTime;

}
