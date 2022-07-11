package com.liubity.platform_starter.service.sale;

import com.liubity.platform_starter.model.dto.Statistic;
import com.liubity.platform_starter.model.sale.Receivable;
import org.hibernate.annotations.NamedNativeQuery;
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
 * @Date 2020-11-26
 */
public interface ReceivableService extends JpaRepository<Receivable, Long>, JpaSpecificationExecutor<Receivable> {
    
    @Transactional
    @Modifying
    @Query(value="update Receivable r set r.status = :status where r.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    Receivable findByOrderNo(String orderNo);
    
    List<Receivable> findAllByCreateTimeBetween(Date startDate, Date endDate);
    
    @Query(value="select sr.type, sum(sr.receiveMoney) as total_amount from Receivable sr where sr.createTime between :startDate and :endDate group by sr.type")
    List<Object[]> statisticByCreateTimeGroupType(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value="select function('date_format',sr.createTime,'%Y-%m-%d') as date ,sum(sr.receiveMoney) as total_amount from Receivable sr where sr.createTime between :startDate and :endDate group by function('date_format',sr.createTime,'%Y-%m-%d')")
    List<Object[]> statisticByCreateTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
