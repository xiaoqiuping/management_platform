package com.liubity.platform_starter.model.backstage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @Author Liubity
 * @Date 2020-11-15
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_backstage_department")
public class Department extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 部门名称
     */
    @Column(name = "name", nullable = false, length = 50)
    @JsonProperty("name")
    private String name;
    
    /**
     * 部门负责人
     */
    @Column(name = "responsibility", length = 50)
    @JsonProperty("responsibility")
    private String responsibility;
    
    /**
     * 备注
     */
    @Column(name = "remarks",length = 300)
    @JsonProperty("remarks")
    private String remarks;
    
}
