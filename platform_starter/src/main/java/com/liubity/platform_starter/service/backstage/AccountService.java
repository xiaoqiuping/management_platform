package com.liubity.platform_starter.service.backstage;


import com.liubity.platform_starter.model.backstage.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-11-11
 */
public interface AccountService extends JpaRepository<Account,Long> {
    
    /**
     * 逻辑删除
     * @param id 账号
     * @param flag 状态
     */
    @Transactional
    @Modifying
    @Query(value = "update Account a set a.deleteFlag = :flag where a.id = :id")
    void updateDeleteFlag(@Param("id") Long id,@Param("flag") Integer flag);
    
    /**
     * 根据账号查询
     * @param account 账号
     * @param flag  是否被删除
     * @return Account
     */
    Account findAccountByAccountAndDeleteFlagEquals(String account,Integer flag);
    
    /**
     * 根据账号查询
     * @param account 账号
     * @return Account
     */
    Account findAccountByAccount(String account);
    
    
    
    @Query(value = "select arr.roleId from AccountRoleRef arr where arr.accountId = :id")
    List<Long> selectRoleIds(@Param("id") Long id);
    
    @Transactional
    @Modifying
    @Query(value = "update Account a set a.password = :password where a.account = :account")
    void updatePassword(@Param("account") String account, @Param("password") String password);
}
