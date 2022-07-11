package com.liubity.platform_starter.service.quality_restriction;

import com.liubity.platform_starter.model.quality_restriction.ProductionProcessQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-12-12
 */
public interface ProductionProcessQualityService extends JpaRepository<ProductionProcessQuality,Long>, JpaSpecificationExecutor<ProductionProcessQuality> {
}
