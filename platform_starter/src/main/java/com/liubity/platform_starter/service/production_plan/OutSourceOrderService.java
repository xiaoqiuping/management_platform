package com.liubity.platform_starter.service.production_plan;

import com.liubity.platform_starter.model.production_plan.OutSourceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Liubity
 * @Date 2020-12-09
 */
public interface  OutSourceOrderService extends JpaRepository<OutSourceOrder,Long>, JpaSpecificationExecutor<OutSourceOrder> {
    
    @Transactional
    @Modifying
    @Query(value = "update OutSourceOrder oo set oo.status = :status where oo.id = :id")
    void updateStatus(@Param("id")Long id, @Param("status")Integer status);
}
