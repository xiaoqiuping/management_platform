package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.OperationType;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.warehouse_storage.SemiManufacture;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.warehouse_storage.SemiManufactureService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 半成品管理
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/semi_manufacture")
public class SemiManufactureController {
    @Autowired
    private SemiManufactureService service;
    @Autowired
    private WSMaterialService materialService;
    
    
    @PostMapping("/save")
    @LogOperation("新增半成品信息")
    @Transactional
    public CommonResponse save(@RequestBody CommonRequest<SemiManufacture> request,HttpServletRequest httpServletRequest){
        SemiManufacture param=request.getParam();
        param.setDutyMan(AccountInfo.getAccount(httpServletRequest).getName());
        param.setOperationTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        SemiManufacture save=service.save(param);
        Optional<WSMaterial> byId=materialService.findById(param.getMaterialId());
        WSMaterial wsMaterial = byId.get();
        if(OperationType.PUT.status.equals(param.getOperationType())){
            wsMaterial.setTotalNum(wsMaterial.getTotalNum()+param.getOperationNum());
        }else {
            int i=wsMaterial.getTotalNum() - param.getOperationNum();
            if(i<0){
                throw new BizException("{}/{}库存不足",param.getSemiManufactureName(),param.getSemiManufactureModel());
            }
            wsMaterial.setTotalNum(i);
        }
        materialService.save(wsMaterial);
        return CommonResponse.build(Result.success(),save);
    }
    
    @PostMapping("/update")
    @LogOperation("修改半成品信息")
    @Transactional
    public CommonResponse update(@RequestBody CommonRequest<SemiManufacture> request, HttpServletRequest httpServletRequest){
        SemiManufacture param=request.getParam();
        param.setDutyMan(AccountInfo.getAccount(httpServletRequest).getName());
        param.setOperationTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        SemiManufacture semiManufacture=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),semiManufacture);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除半成品信息")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<SemiManufacture> request){
        SemiManufacture param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<SemiManufacture> all=service.findAll((Specification<SemiManufacture>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if(StringUtil.isNotEmpty(param.getSemiManufactureName())){
                predicates.add(criteriaBuilder.like(root.get("semiManufactureName"), "%" + param.getSemiManufactureName() + "%"));
            }
            if(StringUtil.isNotEmpty(param.getSemiManufactureModel())){
                predicates.add(criteriaBuilder.like(root.get("semiManufactureModel"), "%" + param.getSemiManufactureModel() + "%"));
            }
            if(Objects.nonNull(param.getOperationType())){
                predicates.add(criteriaBuilder.equal(root.get("operationType"),  param.getOperationType() ));
            }
            if(Objects.nonNull(param.getType())){
                predicates.add(criteriaBuilder.equal(root.get("type"),  param.getType() ));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
