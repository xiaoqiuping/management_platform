package com.liubity.platform_starter.service.backstage;

import com.liubity.platform_starter.model.backstage.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:48
 */
public interface RoleService extends JpaRepository<Role,Long>, JpaSpecificationExecutor<Role> {

    /**
     *
     * @param enableFlag 禁用标志
     * @param id id
     * @return Role
     */
    @Transactional
    @Modifying
    @Query(value = "update Role r set r.enableFlag = :enableFlag where r.id = :id")
    void updateEnableFlag(@Param("enableFlag") Integer enableFlag, @Param("id") Long id);
    
    /**
     * 根据角色名称匹配查询
     *
     * @param name name
     * @return Role
     */
    Role findByName(String name);
    
}
