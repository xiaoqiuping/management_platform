package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.backstage.Account;
import com.liubity.platform_starter.model.backstage.AccountRoleRef;
import com.liubity.platform_starter.model.backstage.Role;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.service.backstage.AccountRoleRefService;
import com.liubity.platform_starter.service.backstage.AccountService;
import com.liubity.platform_starter.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-11-11
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRoleRefService accountRoleRefService;
    
    
    
    @PostMapping("/list")
    public CommonResponse list(@RequestBody CommonRequest<Account> request) {

        if (Objects.isNull(request.getPage())) {
            return CommonResponse.build(ReturnCode.ERROR, "请传递页面对象");
        }
        if (Objects.nonNull(request.getParam())) {
            Account account = request.getParam();

            ExampleMatcher matcher = ExampleMatcher.matching();
            //账号模糊匹配
            if (Objects.nonNull(account.getAccount())) {
                matcher = matcher.withMatcher("account", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            //姓名模糊匹配
            if (Objects.nonNull(account.getName())) {
                matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            //工号模糊匹配
            if (Objects.nonNull(account.getJobNumber())) {
                matcher = matcher.withMatcher("job_number", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            //部门精准匹配
            if (Objects.nonNull(account.getDepartment())) {
                matcher = matcher.withMatcher("department", ExampleMatcher.GenericPropertyMatchers.exact());
            }
            //账号状态精准匹配
            if (Objects.nonNull(account.getDeleteFlag())) {
                matcher = matcher.withMatcher("delete_flag", ExampleMatcher.GenericPropertyMatchers.exact());
            }

            Example<Account> example = Example.of(account, matcher);
            Page<Account> pageList = accountService.findAll(example, PageHelper.buildPageRequest(request.getPage()));
            return CommonResponse.build(Result.success(), pageList);
        } else {
            PageHelper page = request.getPage();
            Page<Account> list = accountService.findAll(PageHelper.buildPageRequest(page));
            return CommonResponse.build(Result.success(), list);
        }
    }

    @LogOperation("新增用户")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<Account> request) {
        Account param = request.getParam();
        Account accountByAccount = accountService.findAccountByAccount(param.getAccount());
        if (Objects.nonNull(accountByAccount)) {
            return CommonResponse.build(500, "该用户【" + param.getAccount() + "】以存在");
        }
        param.setPassword(MD5Util.md5(param.getPassword()));
        Account save = accountService.save(request.getParam());
        return CommonResponse.build(Result.success(), save);
    }

    @LogOperation("修改用户信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<Account> request) {
        Account update = accountService.saveAndFlush(request.getParam());
        return CommonResponse.build(Result.success(), update);
    }
    
    @PostMapping("/updatePassword")
    public CommonResponse updatePassword(@RequestBody CommonRequest<Account> request) {
        Account param=request.getParam();
        accountService.updatePassword(param.getAccount(),MD5Util.md5(param.getPassword()));
        return CommonResponse.build(Result.success());
    }
    
    @LogOperation("删除用户")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable Long id) {
        accountService.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @LogOperation("更新用户状态")
    @PostMapping("/updateDeleteFlag")
    public CommonResponse updateDeleteFlag(@RequestBody CommonRequest<Account> request) {
        accountService.updateDeleteFlag(request.getParam().getId(),request.getParam().getDeleteFlag());
        return CommonResponse.build(Result.success());
    }
    
    
    /**
     * 分配菜单
     */
    @LogOperation("给用户分配角色")
    @PostMapping("/allocRole/{accountId}")
    @Transactional
    public CommonResponse allocRole(@PathVariable("accountId") Long id, @RequestParam("roleIds") List<Long> roleIds) {
        //先删除原来的关系
        accountRoleRefService.deleteByAccountId(id);
        //再保存关系
        List<AccountRoleRef> refs=new ArrayList<>();
        for (Long roleId : roleIds) {
            AccountRoleRef ref=new AccountRoleRef();
            ref.setAccountId(id).setRoleId(roleId);
            refs.add(ref);
        }
        accountRoleRefService.saveAll(refs);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/getRoleByAccount/{accountId}")
    public CommonResponse getRoleByAccount(@PathVariable("accountId") Long accountId){
        List<Role> roleByAccount=accountRoleRefService.getRoleByAccount(accountId);
        return CommonResponse.build(Result.success(),roleByAccount);
    }
    
}
