package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.backstage.SupplierManagement;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.service.backstage.SupplierManagementService;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
@RestController
@RequestMapping("/supplier")
public class SupplierManagementController {
    
    @Autowired
    private SupplierManagementService supplierManagementService;
    
    @LogOperation("新增供应商信息")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<SupplierManagement> request){
        SupplierManagement save=supplierManagementService.save(request.getParam());
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("更新供应商信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<SupplierManagement> request){
        supplierManagementService.deleteById(request.getParam().getId());
        SupplierManagement save=supplierManagementService.save(request.getParam());
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("删除供应商信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        supplierManagementService.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<SupplierManagement> request){
        List<SupplierManagement> result = new ArrayList<>();
        if(Objects.nonNull(request.getPage())){
            SupplierManagement param=request.getParam();
            PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
            Page<SupplierManagement> all=supplierManagementService.findAll((Specification<SupplierManagement>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates=new ArrayList<>();
                if (StringUtil.isNotEmpty(param.getSupplierName())) {
                    predicates.add(criteriaBuilder.like(root.get("supplierName"), "%" + param.getSupplierName() + "%"));
                }
                if (Objects.nonNull(param.getCooperationStartDate()) && Objects.nonNull(param.getCooperationEndDate())) {
                    predicates.add(criteriaBuilder.between(root.get("cooperationDate").as(Date.class), param.getCooperationStartDate(),param.getCooperationEndDate()));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }, pageRequest);
            return CommonResponse.build(Result.success(),all);
        }else {
            List<SupplierManagement> supplierManagements=result=supplierManagementService.findAll();
            return CommonResponse.build(Result.success(),supplierManagements);
        }
    }
    
    @GetMapping("/get/{id}")
    public CommonResponse getById(@PathVariable("id") Long id){
        Optional<SupplierManagement> byId=supplierManagementService.findById(id);
        return CommonResponse.build(Result.success(),byId.get());
    }
    
}
