package com.liubity.platform_starter.service.production_management;

import com.liubity.platform_starter.model.production_management.MiscellaneousMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-12-09
 */
public interface MiscellaneousMaterialService extends JpaRepository<MiscellaneousMaterial,Long>, JpaSpecificationExecutor<MiscellaneousMaterial> {

}
