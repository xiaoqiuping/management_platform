package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.model.backstage.Menu;
import com.liubity.platform_starter.model.backstage.MenuDTO;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.service.backstage.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:50
 */

@RestController
@RequestMapping("/menu")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<Menu> request) {
        Menu param=request.getParam();
        Menu menu=menuService.findByNameAndCode(param.getName(),param.getCode());
        if (Objects.nonNull(menu)) {
            return CommonResponse.build(500, "该菜单{}/{}已存在",param.getName(),param.getCode());
        }
        Menu save=menuService.save(request.getParam());
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<Menu> request) {
        Menu update=menuService.saveAndFlush(request.getParam());
        return CommonResponse.build(Result.success(), update);
    }
    
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        menuService.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    
    @PostMapping("/updateEnableFlag")
    public CommonResponse updateEnableFlag(@RequestBody CommonRequest<Menu> request) {
        menuService.updateEnableFlag(request.getParam().getEnableFlag(), request.getParam().getId());
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse list(@RequestBody(required=false) CommonRequest<Menu> request) {
            if (Objects.nonNull(request) && Objects.nonNull(request.getPage())) {
            Menu menu=request.getParam();
            PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage());
            Page<Menu> all=menuService.findAll((Specification<Menu>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates=new ArrayList<>();
                if (Objects.nonNull(menu.getName())) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%"+menu.getName()+"%"));
                }
                if (Objects.nonNull(menu.getLevel())) {
                    predicates.add(criteriaBuilder.equal(root.get("level"), menu.getLevel()));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }, pageRequest);
            
            return CommonResponse.build(Result.success(), all);
        } else {
            List<Menu> list=menuService.findAll();
            return CommonResponse.build(Result.success(), list);
        }
    }
    @PostMapping("/listAll/{roleId}")
    public CommonResponse listAll(@PathVariable("roleId") Long roleId) {
    
        //角色已拥有的菜单权限
        List<Menu> menuByRoleId=menuService.findMenuByRoleId(roleId);
        
        //所有菜单
        List<MenuDTO> result = new ArrayList<>();
        List<Menu> parentMenu=menuService.findAllByLevel(0);
        parentMenu.forEach(p->{
            MenuDTO menuVO = new MenuDTO();
            if(menuByRoleId.contains(p)){
                menuVO.setChecked(true);
            }
            BeanUtils.copyProperties(p,menuVO);
            List<Menu> secondChild=menuService.findByParentId(p.getId());
            List<MenuDTO> temp = new ArrayList<>();
            secondChild.forEach(s->{
                MenuDTO menuVO1 = new MenuDTO();
                if(menuByRoleId.contains(s)){
                    menuVO1.setChecked(true);
                }
                BeanUtils.copyProperties(s,menuVO1);
                List<Menu> thirdChild=menuService.findByParentId(s.getId());
                List<MenuDTO> temp1 = new ArrayList<>();
                thirdChild.forEach(t->{
                    MenuDTO menuVO2 = new MenuDTO();
                    if(menuByRoleId.contains(t)){
                        menuVO2.setChecked(true);
                    }
                    BeanUtils.copyProperties(t,menuVO2);
                    temp1.add(menuVO2);
                });
                menuVO1.setChilds(temp1);
                temp.add(menuVO1);
            });
            menuVO.setChilds(temp);
            result.add(menuVO);
        });
        return CommonResponse.build(Result.success(), result);
    }
    @PostMapping("/listByLevel/{level}")
    public CommonResponse listByLevel(@PathVariable("level") String level) {
        List<Menu> allByLevel=menuService.findAllByLevel(Integer.valueOf(level));
        return CommonResponse.build(Result.success(), allByLevel);
    }
}
