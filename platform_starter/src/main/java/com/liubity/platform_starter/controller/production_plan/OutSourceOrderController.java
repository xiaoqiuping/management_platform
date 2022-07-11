package com.liubity.platform_starter.controller.production_plan;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.OutSourceOrderStatus;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.production_management.MiscellaneousMaterial;
import com.liubity.platform_starter.model.production_plan.OutSourceOrder;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseOrder;
import com.liubity.platform_starter.service.production_management.MiscellaneousMaterialService;
import com.liubity.platform_starter.service.production_plan.OutSourceOrderService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.OrderNoGenerator;
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
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-12-09
 */
@RestControllerAdvice
@RequestMapping("outsource_order")
public class OutSourceOrderController {
    @Autowired
    private OutSourceOrderService service;
    
    @PostMapping("/save")
    @LogOperation("新增委外单信息")
    public CommonResponse save(@RequestBody CommonRequest<OutSourceOrder> request) {
        OutSourceOrder param=request.getParam();
        param.setOrderNo(OrderNoGenerator.orderNo("WW"));
        param.setStatus(OutSourceOrderStatus.NORMAL.status);
        OutSourceOrder save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/update")
    @LogOperation("更新委外单信息")
    public CommonResponse update(@RequestBody CommonRequest<OutSourceOrder> request) {
        OutSourceOrder param=request.getParam();
        OutSourceOrder save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("删除委外单信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<OutSourceOrder> request) {
        OutSourceOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<OutSourceOrder> all=service.findAll(filter(param), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    public static Specification<OutSourceOrder> filter(OutSourceOrder param) {
        return (Specification<OutSourceOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (Objects.nonNull(param.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), param.getStatus()));
            }
            if (StringUtil.isNotEmpty(param.getProductionName())) {
                predicates.add(criteriaBuilder.like(root.get("productionName"), "%" + param.getProductionName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getDutyMan())) {
                predicates.add(criteriaBuilder.like(root.get("dutyMan"), "%" + param.getDutyMan() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
    
    
    @LogOperation("更改委外单状态信息")
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResponse updateStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status) {
        service.updateStatus(id,status);
        return CommonResponse.build(Result.success());
    }
    
}
