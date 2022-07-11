package com.liubity.platform_starter.controller.financial_management;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.financial_management.FixedAssets;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseContract;
import com.liubity.platform_starter.service.financial_management.FixedAssetsService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.DateUtil;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-12-15
 */
@RestController
@RequestMapping("/fixed_assets")
public class FixedAssetController {
    @Autowired
    private FixedAssetsService service;
    
    @LogOperation("新增固定资产信息")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<FixedAssets> request, HttpServletRequest httpServletRequest){
        FixedAssets param=request.getParam();
        FixedAssets byName=service.findByName(param.getName());
        if(Objects.nonNull(byName)){
            return CommonResponse.build(ReturnCode.ERROR,"{}已存在",param.getName());
        }
        FixedAssets save=service.save(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("更新固定资产信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<FixedAssets> request){
        FixedAssets param=request.getParam();
        FixedAssets save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("删除固定资产信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<FixedAssets> request){
        FixedAssets param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<FixedAssets> all=service.findAll((Specification<FixedAssets>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            //名称
            if (StringUtil.isNotEmpty(param.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
