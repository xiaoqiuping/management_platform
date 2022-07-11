package com.liubity.platform_starter.model.backstage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_backstage_supplier_material")
public class Material implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 材料名称
     */
    @JsonProperty("material_name")
    @Column(name = "material_name")
    private String materialName;
    
    /**
     * 材料价格
     */
    @JsonProperty("material_price")
    @Column(name = "material_price")
    private String materialPrice;
    
}
