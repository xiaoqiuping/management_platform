package com.liubity.platform_starter.model.backstage;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;


@Data
@Accessors(chain=true)
@Entity
@Table(name="t_backstage_ref_role_menu")
public class RoleMenuRef{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 菜单ID
     */
    @Column(name="menu_id", nullable=false,length=50)
    @JsonProperty("menu_id")
    private Long menuId;

    /**
     * 角色ID
     */
    @Column(name="role_id", nullable=false,length=50)
    @JsonProperty("role_id")
    private Long roleId;

}
