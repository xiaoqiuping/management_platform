package com.liubity.platform_starter.controller.production_management;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.production_management.MiscellaneousMaterial;
import com.liubity.platform_starter.service.production_management.MiscellaneousMaterialService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-12-09
 */
@RestControllerAdvice
@RequestMapping("miscellaneous_material")
public class MiscellaneousMaterialController {
    
    @Autowired
    private MiscellaneousMaterialService service;
    
    @PostMapping("/save")
    @LogOperation("新增杂料操作记录信息")
    public CommonResponse save(@RequestBody CommonRequest<MiscellaneousMaterial> request, HttpServletRequest httpServletRequest){
        MiscellaneousMaterial param=request.getParam();
        param.setOperationMan(AccountInfo.getAccount(httpServletRequest).getName());
        MiscellaneousMaterial save=service.save(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @PostMapping("/update")
    @LogOperation("更新杂料操作记录信息")
    public CommonResponse update(@RequestBody CommonRequest<MiscellaneousMaterial> request){
        MiscellaneousMaterial param=request.getParam();
        MiscellaneousMaterial save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("删除杂物操作记录信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<MiscellaneousMaterial> request) {
        MiscellaneousMaterial param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<MiscellaneousMaterial> all=service.findAll((Specification<MiscellaneousMaterial>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("operationType"),param.getOperationType()));
            if (StringUtil.isNotEmpty(param.getMaterialName())) {
                predicates.add(criteriaBuilder.like(root.get("materialName"), "%" + param.getMaterialName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getDutyMan())) {
                predicates.add(criteriaBuilder.like(root.get("dutyMan"), "%" + param.getDutyMan() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
