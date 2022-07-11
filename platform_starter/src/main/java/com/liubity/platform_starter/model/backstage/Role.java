package com.liubity.platform_starter.model.backstage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_backstage_role")
public class Role extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 角色名称
     */
    @Column(name = "name", nullable = false, length = 50)
    @JsonProperty("name")
    private String name;
    
    /**
     * 部门
     */
    @Column(name = "department_name", nullable = false)
    @JsonProperty("department_name")
    private String departmentName;
    
    
    /**
     * 备注
     */
    @Column(name = "remarks", length = 300)
    @JsonProperty("remarks")
    private String remarks;

    /**
     * 是否禁用
     */
    @Column(name = "enable_flag", length = 1)
    @JsonProperty("enable_flag")
    private Integer enableFlag = 0;
    
 
    
}
