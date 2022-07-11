package com.liubity.platform_starter.service.after_sale_repairing;

import com.liubity.platform_starter.model.after_sale_repairing.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Liubity
 * @Date 2020-12-12
 */
public interface RepairOrderService extends JpaRepository<RepairOrder,Long>, JpaSpecificationExecutor<RepairOrder> {
    
    @Transactional
    @Modifying
    @Query(value = "update RepairOrder ro set ro.status = :status where ro.id = :id")
    void updateStatus(@Param("id") Long id,@Param("status") Integer status);
    
    @Transactional
    @Modifying
    @Query(value = "update RepairOrder ro set ro.status = :status where ro.orderNo = :orderNo")
    void updateStatusByOrderNo(@Param("orderNo")String orderNo, @Param("status")Integer status);
}
