package com.liubity.platform_starter.model.backstage;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain=true)
@Entity
@Table(name="t_backstage_ref_account_role")
public class AccountRoleRef {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    /**
     * 账号ID
     */
    @Column(name="account_id", nullable=false, length=50)
    @JsonProperty("account_id")
    private Long accountId;
    
    /**
     * 角色ID
     */
    @Column(name="role_id", nullable=false, length=50)
    @JsonProperty("role_id")
    private Long roleId;
    
}
