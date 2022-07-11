package com.liubity.platform_starter.service.sale;

import com.liubity.platform_starter.model.sale.SendGoodsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Liubity
 * @Date 2020-12-18
 */
public interface SendGoodsOrderService extends JpaRepository<SendGoodsOrder,Long>, JpaSpecificationExecutor<SendGoodsOrder> {
    
    @Transactional
    @Modifying
    @Query(value = "update SendGoodsOrder sgo set sgo.status = :status where sgo.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
