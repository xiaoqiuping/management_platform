package com.liubity.platform_starter.service.warehouse_storage;

import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
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
public interface WSMaterialService extends JpaRepository<WSMaterial,Long>, JpaSpecificationExecutor<WSMaterial> {
    
    List<WSMaterial> findByMaterialName(String materialName);
    
    WSMaterial findByMaterialNameAndMaterialModel(String materialName,String materialModel);
    
    @Transactional
    @Modifying
    @Query(value = "update WSMaterial m set m.totalNum = m.totalNum + :totalNum where m.id = :id")
    void updateTotalNum(@Param("totalNum") Integer totalNum, @Param("id") Long id);
    
    @Transactional
    @Modifying
    @Query(value = "update WSMaterial m set m.totalNum = m.totalNum + :totalNum where m.materialName = :materialName")
    void updateTotalNumByName(@Param("totalNum") Integer totalNum, @Param("materialName") String materialName);
}
