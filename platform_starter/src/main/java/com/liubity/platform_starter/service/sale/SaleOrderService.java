package com.liubity.platform_starter.service.sale;

import com.liubity.platform_starter.model.sale.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public interface SaleOrderService  extends JpaRepository<SaleOrder,Long>, JpaSpecificationExecutor<SaleOrder> {
    
    @Transactional
    @Modifying
    @Query(value = "update SaleOrder so set so.status = :status where so.id = :id")
    void updateStatus(@Param("status") Integer status,@Param("id") Long id);
    
    SaleOrder findByOrderNo(String orderNo);
    
    @Transactional
    @Modifying
    @Query(value = "update SaleOrder so set so.status = :status where so.orderNo = :saleOrderNo")
    void updateStatusByOrderNo(@Param("status") Integer status, @Param("saleOrderNo")String saleOrderNo);
}
