package com.liubity.platform_starter.service.raw_materials_purchase;

import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Liubity
 * @Date 2020-12-07
 */
public interface PurchaseOrderService extends JpaRepository<PurchaseOrder,Long>, JpaSpecificationExecutor<PurchaseOrder> {
    
    @Transactional
    @Modifying
    @Query(value = "update PurchaseOrder po set po.status = :status where po.id = :id")
    void updateStatus( @Param("id") Long id,@Param("status") Integer status);
    
    @Transactional
    @Modifying
    @Query(value = "update PurchaseOrder po set po.status = :status where po.orderNo = :orderNo")
    void updateStatusByOrderNo( @Param("status") Integer status,@Param("orderNo") String orderNo);
    
    PurchaseOrder findByOrderNo(String orderNo);
}
