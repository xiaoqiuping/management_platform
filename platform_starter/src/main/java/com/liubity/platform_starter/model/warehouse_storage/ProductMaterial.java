package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 生产订单关联材料
 *
 * @Author Liubity
 * @Date 2020-11-29
 */
//@Data
//@Accessors(chain=true)
//@Entity
//@Table(name="t_ws_product_material")
//@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
//@JsonIgnoreProperties(ignoreUnknown=true, value={"productionOrder"})
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class ProductMaterial {
//
//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Column(name="id")
//    private Long id;
//
//    @ManyToOne()
//    @JoinColumn(name="production_order_id", nullable=false, insertable=false, updatable=false)
//    private ProductionOrder productionOrder;
//    @Column(name="production_order_id", nullable=false)
//    private Long productionOrderId;
//
//    /**
//     * 物料名称
//     */
//    @Column(name="material_name")
//    @JsonProperty("material_name")
//    private String materialName;
//
//    /**
//     * 物料型号
//     */
//    @Column(name="material_type")
//    @JsonProperty("material_type")
//    private String materialType;
//
//    /**
//     * 类型 1：原料，2，零部件，3，半成品
//     */
//    @Column(name="type")
//    @JsonProperty("type")
//    private Integer type;
//
//    /**
//     * 数量
//     */
//    @Column(name="total_num")
//    @JsonProperty("total_num")
//    private Integer totalNum;
//
//}
