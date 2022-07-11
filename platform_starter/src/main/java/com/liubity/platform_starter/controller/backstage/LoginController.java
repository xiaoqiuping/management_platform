package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.backstage.Account;
import com.liubity.platform_starter.model.backstage.AccountDTO;
import com.liubity.platform_starter.model.backstage.Menu;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.service.backstage.AccountService;
import com.liubity.platform_starter.service.backstage.MenuService;
import com.liubity.platform_starter.utils.CacheUtil;
import com.liubity.platform_starter.utils.MD5Util;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Liubity
 * @Date 2020-11-12
 */
@RestController
public class LoginController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private MenuService menuService;
    
    @PostMapping("/login")
    //@LogOperation("登录系统")
    public CommonResponse login(@RequestBody CommonRequest<Account> request) {
        Account param=request.getParam();
        String account = param.getAccount();
        String password=param.getPassword();
        if(StringUtil.isEmpty(account)){
            return CommonResponse.build(500,"账号不能为空");
        }
        if(StringUtil.isEmpty(password)){
            return CommonResponse.build(500,"密码不能为空");
        }
    
        Account accountByAccount=accountService.findAccountByAccount(account);
        if(Objects.isNull(accountByAccount)){
            return CommonResponse.build(500,"该用户不存在");
        }
        password = MD5Util.md5(password);
        if(!password.equals(accountByAccount.getPassword())){
            return CommonResponse.build(500,"密码错误");
        }
        if(!accountByAccount.getDeleteFlag().equals(0)){
            return CommonResponse.build(500,"账号已被禁用，请联系管理员");
        }
        //缓存用户名称
        accountByAccount.setPassword("******");
        String authorization =MD5Util.md5(account+System.currentTimeMillis());
        CacheUtil.put(authorization,accountByAccount);
        return CommonResponse.build(Result.success(),authorization);
    }
    
    //@LogOperation("登出系统")
    @PostMapping("/loginOut")
    public CommonResponse loginOut(HttpServletRequest httpServletRequest) {
        String authorization=httpServletRequest.getHeader("Authorization");
        CacheUtil.remove(authorization);
        return CommonResponse.build(Result.success());
    }
    
    @GetMapping("/getUserDetail/{account}")
    public CommonResponse getUserDetail(@PathVariable("account") String account) {
    
        Account accountByAccount=accountService.findAccountByAccountAndDeleteFlagEquals(account,0);
        AccountDTO dto = new AccountDTO();
        BeanUtils.copyProperties(accountByAccount,dto);
    
        List<Long> roleIds=accountService.selectRoleIds(accountByAccount.getId());
        Set<Menu> menuByRoleId=menuService.findMenuInRoleIds(roleIds);
        List<Menu> topMenus=menuByRoleId.stream().filter(m -> m.getLevel() == 0).collect(Collectors.toList());
        
        dto.setTopMenus(topMenus);
        return CommonResponse.build(Result.success(),dto);
    }
    
    @PostMapping("/getSiderMenus/{account}")
    public CommonResponse getSiderMenus(@PathVariable("account") String account,@RequestParam("menuId") Long menuId) {
    
        Account accountByAccount=accountService.findAccountByAccountAndDeleteFlagEquals(account,0);
        AccountDTO dto = new AccountDTO();
        BeanUtils.copyProperties(accountByAccount,dto);
        List<Long> roleIds=accountService.selectRoleIds(accountByAccount.getId());
        Set<Menu> menuByRoleId=menuService.findMenuInRoleIds(roleIds);
        
        List<Menu> byParentId=menuService.findByParentId(menuId);
        List<Menu> list=new ArrayList<>(byParentId);
        for(Menu menu:byParentId){
            List<Menu> byParentId1=menuService.findByParentId(menu.getId());
            list.addAll(byParentId1);
        }
        
        List<Menu> collect=list.stream().filter(menuByRoleId::contains).collect(Collectors.toList());
        return CommonResponse.build(Result.success(),collect);
    }
}
