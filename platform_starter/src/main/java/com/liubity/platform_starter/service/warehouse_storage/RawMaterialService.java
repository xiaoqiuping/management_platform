package com.liubity.platform_starter.service.warehouse_storage;

import com.liubity.platform_starter.model.warehouse_storage.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-11-29
 */
public interface RawMaterialService  extends JpaRepository<RawMaterial,Long>, JpaSpecificationExecutor<RawMaterial> {
}
