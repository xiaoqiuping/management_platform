package com.liubity.platform_starter.service.sale;

import com.liubity.platform_starter.model.sale.SaleOffPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public interface SaleOffPriceService  extends JpaRepository<SaleOffPrice,Long>, JpaSpecificationExecutor<SaleOffPrice> {
    
    SaleOffPrice findByGoodsName(String goodsName);
    
    SaleOffPrice findByBuyer(String buyer);
}
