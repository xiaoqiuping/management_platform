package com.liubity.platform_starter.service.financial_management;

import com.liubity.platform_starter.model.financial_management.FixedAssets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-12-15
 */
public interface FixedAssetsService extends JpaRepository<FixedAssets,Long>, JpaSpecificationExecutor<FixedAssets> {
    FixedAssets findByName(String name);
}
