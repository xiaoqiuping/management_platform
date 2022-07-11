package com.liubity.platform_starter.service.backstage;

import com.liubity.platform_starter.model.backstage.SupplierManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
public interface SupplierManagementService extends JpaRepository<SupplierManagement,Long>, JpaSpecificationExecutor<SupplierManagement> {
}
