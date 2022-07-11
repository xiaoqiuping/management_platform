package com.liubity.platform_starter.service.quality_restriction;

import com.liubity.platform_starter.model.quality_restriction.RawMaterialQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-12-10
 */
public interface RawMaterialQualityService extends JpaRepository<RawMaterialQuality,Long>, JpaSpecificationExecutor<RawMaterialQuality> {
    
    List<RawMaterialQuality> findAllByCgOrderNo(String cgOrderNo);

}
