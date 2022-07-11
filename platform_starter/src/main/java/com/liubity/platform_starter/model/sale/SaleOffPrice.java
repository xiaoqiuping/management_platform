package com.liubity.platform_starter.model.sale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 销售折扣表
 * @Author Liubity
 * @Date 2020-11-26
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_sale_sale_off_price")
public class SaleOffPrice extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //商品名称
    @Column(name = "goods_name")
    @JsonProperty("goods_name")
    private String goodsName;
    
    //购买人
    @Column(name = "buyer")
    @JsonProperty("buyer")
    private String buyer;
    
    //折扣价格
    @Column(name = "price",nullable=false)
    @JsonProperty("price")
    private BigDecimal price;
    
    //类别 0：商品名称， 1：购买人
    @Column(name = "type")
    @JsonProperty("type")
    private Integer type;
}
