package com.liubity.platform_starter.controller.quality_restriction;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.PurchaseOrderStatus;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.quality_restriction.RawMaterialQuality;
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
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-12-10
 */
@RestController
@RequestMapping("/raw_material_quality")
public class RawMaterialQualityController {
    
    @Autowired
    private RawMaterialQualityService service;
    @Autowired
    private PurchaseOrderService orderService;
    
    @Transactional
    @LogOperation("更新原料质检信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<RawMaterialQuality> request, HttpServletRequest httpServletRequest) {
        RawMaterialQuality param=request.getParam();
        param.setQualityMan(AccountInfo.getAccount(httpServletRequest).getName());
        RawMaterialQuality save=service.saveAndFlush(param);
        
        List<RawMaterialQuality> allByCgOrderNo=service.findAllByCgOrderNo(param.getCgOrderNo());
        int count=(int) allByCgOrderNo.stream().filter(a -> Objects.nonNull(a.getStatus()) && 1 == a.getStatus()).count();
        if (allByCgOrderNo.size() == count) {
            orderService.updateStatusByOrderNo(PurchaseOrderStatus.EXAMINE_SUCCESS.status, param.getCgOrderNo());
        } else if (count == 0) {
            orderService.updateStatusByOrderNo(PurchaseOrderStatus.EXAMINE_FAILED.status, param.getCgOrderNo());
        } else {
            orderService.updateStatusByOrderNo(PurchaseOrderStatus.EXAMINEING.status, param.getCgOrderNo());
        }
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("删除原料质检信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<RawMaterialQuality> request) {
        RawMaterialQuality param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<RawMaterialQuality> all=service.findAll((Specification<RawMaterialQuality>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (StringUtil.isNotEmpty(param.getCgOrderNo())) {
                predicates.add(criteriaBuilder.like(root.get("cgOrderNo"), "%" + param.getCgOrderNo() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getRawMaterialName())) {
                predicates.add(criteriaBuilder.like(root.get("rawMaterialName"), "%" + param.getRawMaterialName() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
