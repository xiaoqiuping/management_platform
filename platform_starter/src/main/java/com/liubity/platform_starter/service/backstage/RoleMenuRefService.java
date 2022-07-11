package com.liubity.platform_starter.service.backstage;


import com.liubity.platform_starter.model.backstage.RoleMenuRef;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:48
 */
public interface RoleMenuRefService extends JpaRepository<RoleMenuRef,Long> {
    
    /**
     * 删除角色的所有菜单
     * @param id
     */
    void deleteByRoleId(Long id);
}
