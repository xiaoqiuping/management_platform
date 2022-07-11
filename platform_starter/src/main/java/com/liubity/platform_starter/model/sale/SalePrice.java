package com.liubity.platform_starter.model.sale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 销售价目表
 * @Author Liubity
 * @Date 2020-11-26
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_sale_sale_price")
public class SalePrice extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //商品名称
    @Column(name = "goods_name",nullable=false,length=100)
    @JsonProperty("goods_name")
    private String goodsName;
    
    //商品类型
    @Column(name = "goods_type",nullable=false,length=100)
    @JsonProperty("goods_type")
    private String goodsType;
    
    //价格
    @Column(name = "price",nullable=false,length=10)
    @JsonProperty("price")
    private BigDecimal price;
    
}
