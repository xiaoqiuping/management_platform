package com.liubity.platform_starter.model.backstage;


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
@Table(name = "t_backstage_menu")
public class Menu extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    /**
     * 菜单名称
     */
    @Column(name = "code", nullable = false, length = 50)
    @JsonProperty("code")
    private String code;
    
    /**
     * 菜单名称
     */
    @Column(name = "name", nullable = false, length = 50)
    @JsonProperty("name")
    private String name;


    /**
     * 菜单级数
     */
    @Column(name = "level", nullable = false, length = 1)
    @JsonProperty("level")
    private Integer level;

    /**
     * 图标（ele-icon）
     */
    @Column(name = "icon", length = 20)
    @JsonProperty("icon")
    private String icon;

    /**
     * 是否禁用
     */
    @Column(name = "enable_flag", length = 1)
    @JsonProperty("enable_flag")
    private Integer enableFlag = 0;


    /**
     * 父级ID
     */
    @Column(name = "parent_id", length = 50)
    @JsonProperty("parent_id")
    private Long parentId;


    /**
     * 排序号
     */
    @Column(name = "sort_num", length = 50)
    @JsonProperty("sort_num")
    private String sortNum;
}
