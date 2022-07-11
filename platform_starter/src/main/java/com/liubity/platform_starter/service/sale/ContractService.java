package com.liubity.platform_starter.service.sale;

import com.liubity.platform_starter.model.sale.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
public interface ContractService  extends JpaRepository<Contract,Long>, JpaSpecificationExecutor<Contract> {
    
    /**
     * 根据合同名称查找
     * @param contractName 合同名称
     * @return Contract findByContractName(String contractName);
     */
    Contract findByContractName(String contractName);
    
}
