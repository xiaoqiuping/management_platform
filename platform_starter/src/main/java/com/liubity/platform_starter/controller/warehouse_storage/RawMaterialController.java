package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.OperationType;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.warehouse_storage.RawMaterial;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.warehouse_storage.RawMaterialService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 原材料管理
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/raw_material")
public class RawMaterialController {
    
    @Autowired
    private RawMaterialService service;
    @Autowired
    private WSMaterialService materialService;
    
    
    @PostMapping("/save")
    @LogOperation("新增原材料信息")
    @Transactional
    public CommonResponse save(@RequestBody CommonRequest<RawMaterial> request, HttpServletRequest httpServletRequest){
        RawMaterial param=request.getParam();
        param.setDutyMan(AccountInfo.getAccount(httpServletRequest).getName());
        param.setOperationTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        RawMaterial save=service.save(param);
    
        Optional<WSMaterial> byId=materialService.findById(param.getMaterialId());
        WSMaterial wsMaterial = byId.get();
        if(OperationType.PUT.status.equals(param.getOperationType())){
            wsMaterial.setTotalNum(wsMaterial.getTotalNum()+param.getOperationNum());
        }else {
            int i=wsMaterial.getTotalNum() - param.getOperationNum();
            if(i<0){
                throw new BizException("{}库存不足",param.getRawMaterialName());
            }
            wsMaterial.setTotalNum(i);
        }
        materialService.save(wsMaterial);
        return CommonResponse.build(Result.success(),save);
    }
    
    @PostMapping("/update")
    @LogOperation("修改原材料信息")
    @Transactional
    public CommonResponse update(@RequestBody CommonRequest<RawMaterial> request,HttpServletRequest httpServletRequest){
        RawMaterial param=request.getParam();
        param.setDutyMan(AccountInfo.getAccount(httpServletRequest).getName());
        param.setOperationTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        RawMaterial save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除原材料信息")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<RawMaterial> request){
        RawMaterial param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<RawMaterial> all=service.findAll((Specification<RawMaterial>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if(StringUtil.isNotEmpty(param.getRawMaterialName())){
                predicates.add(criteriaBuilder.like(root.get("rawMaterialName"), "%" + param.getRawMaterialName() + "%"));
            }
            if(Objects.nonNull(param.getOperationType())){
                predicates.add(criteriaBuilder.equal(root.get("operationType"),  param.getOperationType() ));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
