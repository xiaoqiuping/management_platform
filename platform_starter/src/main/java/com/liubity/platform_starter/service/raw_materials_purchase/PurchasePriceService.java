package com.liubity.platform_starter.service.raw_materials_purchase;

import com.liubity.platform_starter.model.raw_materials_purchase.PurchasePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-12-07
 */
public interface PurchasePriceService extends JpaRepository<PurchasePrice,Long>, JpaSpecificationExecutor<PurchasePrice> {
    PurchasePrice findByMaterialName(String materialName);
}
