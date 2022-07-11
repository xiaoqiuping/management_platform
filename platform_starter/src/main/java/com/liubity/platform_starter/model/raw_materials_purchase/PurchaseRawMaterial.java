package com.liubity.platform_starter.model.raw_materials_purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 原材料名称，单价，数量
 * 采购原料
 * @Author Liubity
 * @Date 2020-12-07
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_rmp_purchase_raw_material")
public class PurchaseRawMaterial extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //原材料名称
    @Column(name = "raw_material_name",nullable=false,length=50)
    @JsonProperty("raw_material_name")
    private String rawMaterialName;
    
    //单价
    @Column(name = "price")
    @JsonProperty("price")
    private BigDecimal price;
    
    //数量
    @Column(name = "total_num")
    @JsonProperty("total_num")
    private Integer totalNum;
    
}
