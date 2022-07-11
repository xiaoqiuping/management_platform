package com.liubity.platform_starter.service.raw_materials_purchase;

import com.liubity.platform_starter.model.raw_materials_purchase.PayableOrder;
import com.liubity.platform_starter.model.sale.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-12-07
 */
public interface PayableOrderService extends JpaRepository<PayableOrder, Long>, JpaSpecificationExecutor<PayableOrder> {
    
    @Transactional
    @Modifying
    @Query(value="update PayableOrder po set po.status = :status where po.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    List<PayableOrder> findAllByCreateTimeBetween(Date startDate, Date endDate);
    
    
    @Query(value="select po.type, sum(po.totalAmount) as total_amount from PayableOrder po where po.createTime between :startDate and :endDate group by po.type")
    List<Object[]> statisticByCreateTimeGroupType(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value="select function('date_format',po.createTime,'%Y-%m-%d') as date, sum(po.totalAmount) as total_amount from PayableOrder po where po.createTime between :startDate and :endDate group by function('date_format',po.createTime,'%Y-%m-%d')")
    List<Object[]> statisticByCreateTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
