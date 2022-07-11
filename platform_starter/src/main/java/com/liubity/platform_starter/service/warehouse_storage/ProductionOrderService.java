package com.liubity.platform_starter.service.warehouse_storage;

import com.liubity.platform_starter.model.warehouse_storage.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-11-29
 */
public interface ProductionOrderService  extends JpaRepository<ProductionOrder,Long>,JpaSpecificationExecutor<ProductionOrder>{
    
    @Transactional
    @Modifying
    @Query(value = "update ProductionOrder po set po.status = :status where po.id = :id")
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);
    
    
    @Transactional
    @Modifying
    @Query(value = "update ProductionOrder po set po.status = :status where po.orderNo = :orderNo")
    void updateStatusByOrderNo(@Param("status") Integer status, @Param("orderNo") String orderNo);
    
    ProductionOrder findByOrderNo(String orderNo);
}
