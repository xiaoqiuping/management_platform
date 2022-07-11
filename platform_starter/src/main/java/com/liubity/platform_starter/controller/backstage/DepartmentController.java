package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.backstage.Department;
import com.liubity.platform_starter.service.backstage.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-11-15
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @LogOperation("新增部门")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<Department> request) {
        Department department=departmentService.findByName(request.getParam().getName());
        if (Objects.nonNull(department)) {
            return CommonResponse.build(ReturnCode.ERROR, "该部门名【" + request.getParam().getName() + "】已存在");
        }
        Department save=departmentService.save(request.getParam());
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("修改部门信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<Department> request) {
        Department update=departmentService.saveAndFlush(request.getParam());
        return CommonResponse.build(Result.success(), update);
    }
    
    @LogOperation("修改部门")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        departmentService.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<Department> request) {
        if (Objects.isNull(request.getPage())) {
            return CommonResponse.build(ReturnCode.ERROR, "请传递页面对象");
        }
        if (Objects.nonNull(request.getParam())) {
            Department department=request.getParam();
            ExampleMatcher matcher=ExampleMatcher.matching();
            //部门名称模糊匹配
            if (Objects.nonNull(department.getName())) {
                matcher=matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            Example<Department> example=Example.of(department, matcher);
            Page<Department> pageList=departmentService.findAll(example, PageHelper.buildPageRequest(request.getPage()));
            return CommonResponse.build(Result.success(), pageList);
        } else {
            PageHelper page=request.getPage();
            Page<Department> list=departmentService.findAll(PageHelper.buildPageRequest(page));
            return CommonResponse.build(Result.success(), list);
        }
    }
    
    @PostMapping("/list")
    public CommonResponse listPage() {
        List<Department> all=departmentService.findAll();
        return CommonResponse.build(Result.success(), all);
    }
}
