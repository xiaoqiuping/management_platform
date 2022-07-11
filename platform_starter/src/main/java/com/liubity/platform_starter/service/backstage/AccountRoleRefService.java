package com.liubity.platform_starter.service.backstage;


import com.liubity.platform_starter.model.backstage.AccountRoleRef;
import com.liubity.platform_starter.model.backstage.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:48
 */
public interface AccountRoleRefService extends JpaRepository<AccountRoleRef,Long> {
    
    /**
     * 根据角色ID删除用户和角色关系
     * @param id 角色ID
     */
    void deleteByRoleId(Long id);
    
    /**
     * 根据角色ID删除用户和角色关系
     * @param id 角色ID
     */
    void deleteByAccountId(Long id);
    
    @Query(value="select role from Role role left join AccountRoleRef arr on role.id = arr.roleId where arr.accountId = :accountId ")
    List<Role> getRoleByAccount(@Param("accountId") Long accountId);
}
