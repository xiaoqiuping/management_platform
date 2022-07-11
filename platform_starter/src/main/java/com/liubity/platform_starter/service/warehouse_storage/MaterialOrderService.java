package com.liubity.platform_starter.service.warehouse_storage;

import com.liubity.platform_starter.model.warehouse_storage. MaterialOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-11-30
 */
public interface MaterialOrderService extends JpaRepository< MaterialOrder,Long>, JpaSpecificationExecutor< MaterialOrder> {

}
