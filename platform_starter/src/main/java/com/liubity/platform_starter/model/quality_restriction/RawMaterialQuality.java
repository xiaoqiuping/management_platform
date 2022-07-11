package com.liubity.platform_starter.model.quality_restriction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @Author Liubity
 * @Date 2020-12-10
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_qr_raw_material_quality")
public class RawMaterialQuality extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //采购单号
    @Column(name = "cg_order_no",nullable=false,length=50)
    @JsonProperty("cg_order_no")
    private String cgOrderNo;
    
    //原料名称
    @Column(name = "raw_material_name",nullable=false,length=50)
    @JsonProperty("raw_material_name")
    private String rawMaterialName;
    
    //数量
    @Column(name = "total_num")
    @JsonProperty("total_num")
    private Integer totalNum;
    
    //采购人
    @Column(name = "purchase_man")
    @JsonProperty("purchase_man")
    private String purchaseMan;
    
    //质检人
    @Column(name = "quality_man")
    @JsonProperty("quality_man")
    private String qualityMan;
    
    //合格量
    @Column(name = "qualified_num")
    @JsonProperty("qualified_num")
    private String qualifiedNum;
    
    //质检状态
    @Column(name = "status")
    @JsonProperty("status")
    private Integer status;
    
    //备注
    @Column(name = "remarks",length=300)
    @JsonProperty("remarks")
    private String remarks;
    
}
