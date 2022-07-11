package com.liubity.platform_starter.model.warehouse_storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 零部件耗费记录
 *
 * @Author Liubity
 * @Date 2021-02-28
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_ws_expend_record")
public class ExpendRecord extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    /**
     * 物料名称
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
     * 图号
     */
    @Column(name = "tu_hao")
    @JsonProperty("tu_hao")
    private String tuHao;
    
    /**
     * 装配数量
     */
    @Column(name = "assemble_num")
    @JsonProperty("assemble_num")
    private Integer assembleNum;
    
    /**
     * 损耗数量
     */
    @Column(name = "loss_num")
    @JsonProperty("loss_num")
    private Integer lossNum;
    
    /**
     * 报废数量
     */
    @Column(name = "scrap_num")
    @JsonProperty("scrap_num")
    private Integer scrapNum;
    
    /**
     * 记录数据日期
     */
    @Column(name="record_date")
    @JsonProperty("record_date")
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date recordDate;
    
    /**
     * 记录人
     */
    @Column(name="record_man")
    @JsonProperty("record_man")
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    private String recordMan;
    
    
    /**
     * 备注
     */
    @Column(name = "remarks")
    @JsonProperty("remarks")
    private String remarks;
    
    /**
     * 日期查询条件
     */
    @Transient
    @JsonProperty("record_date_list")
    private List<String> recordDateList;
    
}
