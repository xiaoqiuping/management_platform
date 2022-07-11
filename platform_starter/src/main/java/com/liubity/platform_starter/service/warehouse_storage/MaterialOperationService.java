package com.liubity.platform_starter.service.warehouse_storage;

import com.liubity.platform_starter.model.warehouse_storage.MaterialOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * @Author Liubity
 * @Date 2020-11-29
 */
public interface MaterialOperationService extends JpaRepository<MaterialOperation,Long> {
    
    @Transactional
    @Modifying
    @Query(value="delete from MaterialOperation mo where mo.materialOrderId = :materialOrderId")
    void deleteAllByMaterialId(@Param("materialOrderId") Long materialOrderId);
}
