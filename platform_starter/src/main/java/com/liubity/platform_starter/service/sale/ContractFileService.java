package com.liubity.platform_starter.service.sale;

import com.liubity.platform_starter.model.sale.ContractFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * @Author Liubity
 * @Date 2020-11-29
 */
public interface ContractFileService extends JpaRepository<ContractFile,Long>{
    
    
    @Transactional
    @Modifying
    @Query(value="delete from ContractFile cf where cf.contractId = :contractId")
    void deleteAllByContractId(@Param("contractId") Long contractId);
    
}
