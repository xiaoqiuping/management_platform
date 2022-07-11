package com.liubity.platform_starter.service.sale;

import com.liubity.platform_starter.model.sale.SalePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public interface SalePriceService extends JpaRepository<SalePrice,Long>, JpaSpecificationExecutor<SalePrice> {
    
    SalePrice findByGoodsNameAndGoodsType(String goodsName,String goodsType);

}
