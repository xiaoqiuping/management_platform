package com.liubity.platform_starter.service.backstage;

import com.liubity.platform_starter.model.backstage.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:48
 */
public interface MenuService extends JpaRepository<Menu,Long>, JpaSpecificationExecutor<Menu> {

    /**
     * 根据CODE查找菜单
     * @param code code
     * @return Menu
     */
    Menu findByNameAndCode(String name,String code);
    
    /**
     * 根据ParentId查找菜单
     * @param parentId parentId
     * @return Menu
     */
    List<Menu> findByParentId(Long parentId);
    
    
    /**
     * 查找所以层级的菜单
     */
    List<Menu> findAllByLevel(Integer level);
    
    @Transactional
    @Modifying
    @Query(value = "update Menu m set m.enableFlag = :enableFlag where m.id = :id")
    void updateEnableFlag(@Param("enableFlag") Integer enableFlag,@Param("id") Long id);
    
    @Query(value="select m from Menu m left join RoleMenuRef rmr on m.id=rmr.menuId where rmr.roleId = :roleId")
    List<Menu> findMenuByRoleId(@Param("roleId") Long roleId);
    
    @Query(value="select m from Menu m left join RoleMenuRef rmr on m.id=rmr.menuId where rmr.roleId in (:roleIds)")
    Set<Menu> findMenuInRoleIds(@Param("roleIds") List<Long> roleIds);
}
