package com.liubity.platform_starter.controller.raw_materials_purchase;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchasePrice;
import com.liubity.platform_starter.service.raw_materials_purchase.PurchasePriceService;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-12-07
 */
@RestController
@RequestMapping("/purchase_price")
public class PurchasePriceController {
    @Autowired
    private PurchasePriceService service;
    
    @LogOperation("新增采购价目信息")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<PurchasePrice> request){
        PurchasePrice param=request.getParam();
        PurchasePrice byMaterialName=service.findByMaterialName(param.getMaterialName());
        if(Objects.nonNull(byMaterialName)){
            return CommonResponse.build(ReturnCode.ERROR,"{}已存在",param.getMaterialName());
        }
        PurchasePrice save=service.save(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("更新采购价目信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<PurchasePrice> request){
        PurchasePrice param=request.getParam();
        PurchasePrice save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("删除采购价目信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<PurchasePrice> request){
        PurchasePrice param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<PurchasePrice> all=service.findAll((Specification<PurchasePrice>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (StringUtil.isNotEmpty(param.getMaterialName())) {
                predicates.add(criteriaBuilder.like(root.get("materialName"), "%" + param.getMaterialName() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
