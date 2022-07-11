package com.liubity.platform_starter.model.raw_materials_purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 序号 名称 单价 用途 供应商 采购人
 * 采购价目表
 * @Author Liubity
 * @Date 2020-12-07
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_rmp_purchase_price")
public class PurchasePrice extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //物料名称
    @Column(name = "material_name")
    @JsonProperty("material_name")
    private String materialName;
    
    //价格
    @Column(name = "price")
    @JsonProperty("price")
    private BigDecimal price;
    
    //用途
    @Column(name = "purpose")
    @JsonProperty("purpose")
    private String purpose;
    
    //供应商
    @Column(name = "supplier")
    @JsonProperty("supplier")
    private String supplier;
    
    //采购人
    @Column(name = "purchase_man")
    @JsonProperty("purchase_man")
    private String purchaseMan;
    
    //备注
    @Column(name="remarks",length=500)
    @JsonProperty("remarks")
    private String remarks;
}
