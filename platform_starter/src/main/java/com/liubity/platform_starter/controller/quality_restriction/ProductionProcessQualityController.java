package com.liubity.platform_starter.controller.quality_restriction;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.PurchaseOrderStatus;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.quality_restriction.ProductionProcessQuality;
import com.liubity.platform_starter.model.quality_restriction.RawMaterialQuality;
import com.liubity.platform_starter.service.quality_restriction.ProductionProcessQualityService;
import com.liubity.platform_starter.service.quality_restriction.RawMaterialQualityService;
import com.liubity.platform_starter.service.raw_materials_purchase.PurchaseOrderService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-12-12
 */
@RestController
@RequestMapping("/production_process_quality")
public class ProductionProcessQualityController {
    @Autowired
    private ProductionProcessQualityService service;
    
    @Transactional
    @LogOperation("更新工序质检信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<ProductionProcessQuality> request, HttpServletRequest httpServletRequest) {
        ProductionProcessQuality param=request.getParam();
        
        return CommonResponse.build(Result.success());
    }
    
    @LogOperation("删除工序质检信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<ProductionProcessQuality> request) {
        ProductionProcessQuality param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ProductionProcessQuality> all=service.findAll((Specification<ProductionProcessQuality>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (StringUtil.isNotEmpty(param.getScOrderNo())) {
                predicates.add(criteriaBuilder.like(root.get("scOrderNo"), "%" + param.getScOrderNo() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getProductionName())) {
                predicates.add(criteriaBuilder.like(root.get("productionName"), "%" + param.getProductionName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getProductionType())) {
                predicates.add(criteriaBuilder.like(root.get("productionType"), "%" + param.getProductionType() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
