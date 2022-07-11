package com.liubity.platform_starter.model.financial_management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 固定资产
 * @Author Liubity
 * @Date 2020-12-15
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_fm_fixed_assets")
public class FixedAssets extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 资产名称
     */
    @Column(name="name")
    @JsonProperty("name")
    private String name;
    
    /**
     *型号
     */
    @Column(name="type")
    @JsonProperty("type")
    private String type;
    
    /**
     *购买时间
     */
    @Column(name="buy_date")
    @JsonProperty("buy_date")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date buyDate;
    
    /**
     *购买厂家
     */
    @Column(name="buy_supplier")
    @JsonProperty("buy_supplier")
    private String buySupplier;
    
    /**
     *价格
     */
    @Column(name="price")
    @JsonProperty("price")
    private BigDecimal price;
    
    /**
     *备注
     */
    @Column(name="remarks",length=300)
    @JsonProperty("remarks")
    private String remarks;
    
}
