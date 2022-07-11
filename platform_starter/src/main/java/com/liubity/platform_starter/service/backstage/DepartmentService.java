package com.liubity.platform_starter.service.backstage;

import com.liubity.platform_starter.model.backstage.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:48
 */
public interface DepartmentService extends JpaRepository<Department, Long> {
    
    /**
     * 根据部门名称查询列表
     *
     * @param name
     * @return
     */
    List<Department> findAllByNameLike(String name);
    
    
    /**
     * 根据部门名称匹配查询
     *
     * @param name
     * @return
     */
    Department findByName(String name);
}
