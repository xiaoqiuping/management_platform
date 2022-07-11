package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.backstage.Role;
import com.liubity.platform_starter.model.backstage.RoleMenuRef;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.service.backstage.AccountRoleRefService;
import com.liubity.platform_starter.service.backstage.RoleMenuRefService;
import com.liubity.platform_starter.service.backstage.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:52
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountRoleRefService accountRoleRefService;
    @Autowired
    private RoleMenuRefService roleMenuRefService;
    
    
    @LogOperation("新增角色")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<Role> request) {
        Role param=request.getParam();
        Role role=roleService.findByName(param.getName());
        if (Objects.nonNull(role)) {
            return CommonResponse.build(500, "该角色【" + param.getName() + "】已存在");
        }
        Role save=roleService.save(request.getParam());
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("更新角色信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<Role> request) {
        Role update=roleService.saveAndFlush(request.getParam());
        return CommonResponse.build(Result.success(), update);
    }
    
    @LogOperation("更新角色状态")
    @PostMapping("/updateEnableFlag")
    public CommonResponse updateEnableFlag(@RequestBody CommonRequest<Role> request) {
        roleService.updateEnableFlag(request.getParam().getEnableFlag(), request.getParam().getId());
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<Role> request) {
        if (Objects.isNull(request.getPage())) {
            return CommonResponse.build(ReturnCode.ERROR, "请传递页面对象");
        }
        if (Objects.nonNull(request.getParam())) {
            Role role=request.getParam();
            ExampleMatcher matcher=ExampleMatcher.matching();
            //角色名称模糊匹配
            if (Objects.nonNull(role.getName())) {
                matcher=matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            //根据部门匹配
            if (Objects.nonNull(role.getDepartmentName())) {
                matcher=matcher.withMatcher("department_name", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            //根据禁用状态匹配
            if (Objects.nonNull(role.getEnableFlag())) {
                matcher=matcher.withMatcher("enable_flag", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            Example<Role> example=Example.of(role, matcher);
            Page<Role> pageList=roleService.findAll(example, PageHelper.buildPageRequest(request.getPage()));
            return CommonResponse.build(Result.success(), pageList);
        } else {
            PageHelper page=request.getPage();
            Page<Role> list=roleService.findAll(PageHelper.buildPageRequest(page));
            return CommonResponse.build(Result.success(), list);
        }
    }
    
    @PostMapping("/listAll")
    public CommonResponse listAll() {
        List<Role> all=roleService.findAll();
        return CommonResponse.build(Result.success(), all);
    }
    
    /**
     *
     * @param id
     * @return
     */
    @Transactional
    @LogOperation("删除角色")
    @PostMapping("/delete/{id}")
    public CommonResponse remove(@PathVariable("id") Long id) {
        //删除主表
        roleService.deleteById(id);
        //删除角色和用户的关系表
        accountRoleRefService.deleteByRoleId(id);
        //删除角色和菜单的关系表
        roleMenuRefService.deleteByRoleId(id);
        return CommonResponse.build(Result.success());
    }
    
    /**
     * 分配菜单
     */
    @LogOperation("给角色分配菜单权限")
    @PostMapping("/allocMenu/{id}")
    @Transactional
    public CommonResponse allocMenu(@PathVariable("id") Long id, @RequestParam("menuIds") List<Long> menuIds) {
        //先删除原来的关系
        roleMenuRefService.deleteByRoleId(id);
        //再保存关系
        List<RoleMenuRef> refs=new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuRef ref=new RoleMenuRef();
            ref.setRoleId(id).setMenuId(menuId);
            refs.add(ref);
        }
        roleMenuRefService.saveAll(refs);
        return CommonResponse.build(Result.success());
    }
    
    @GetMapping("/listWithDepartment")
    public CommonResponse listWithDepartment(){
    
        return CommonResponse.build(Result.success());
    }


}
