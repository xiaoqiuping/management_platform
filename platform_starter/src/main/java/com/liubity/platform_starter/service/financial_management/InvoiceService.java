package com.liubity.platform_starter.service.financial_management;

import com.liubity.platform_starter.model.financial_management.Invoice;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-12-15
 */
public interface InvoiceService extends JpaRepository<Invoice,Long>, JpaSpecificationExecutor<Invoice> {
    
    Invoice findByInvoiceName(String invoiceName);
}
