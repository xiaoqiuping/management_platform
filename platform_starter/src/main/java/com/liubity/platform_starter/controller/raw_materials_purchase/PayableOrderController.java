package com.liubity.platform_starter.controller.raw_materials_purchase;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.OrderNoType;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.raw_materials_purchase.PayableOrder;
import com.liubity.platform_starter.model.warehouse_storage.WarehouseOrder;
import com.liubity.platform_starter.service.raw_materials_purchase.PayableOrderService;
import com.liubity.platform_starter.service.warehouse_storage.WarehouseOrderService;
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
 * @Date 2020-12-07
 */
@RestController
@RequestMapping("/payable_order")
public class PayableOrderController {
    
    @Autowired
    private PayableOrderService service;
    
    @LogOperation("新增应付单信息")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<PayableOrder> request, HttpServletRequest httpServletRequest) {
        PayableOrder param=request.getParam();
//        if(param.getType()==1){
//            WarehouseOrder byOrderNo=warehouseOrderService.findByOrderNo(param.getRkOrderNo());
//            if(Objects.isNull(byOrderNo)){
//                return  CommonResponse.build(ReturnCode.ERROR,"单号{}采购入库单不存在",param.getRkOrderNo());
//            }
//        }
        if(Objects.isNull(param.getStatus())){
            param.setStatus(0);
        }
        param.setOrderNo(OrderNoGenerator.orderNo(OrderNoType.YF.type));
        param.setDutyMan(AccountInfo.getAccount(httpServletRequest).getName());
        PayableOrder save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("更新应付单信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<PayableOrder> request) {
        PayableOrder param=request.getParam();
        PayableOrder save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("删除应付单信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<PayableOrder> request) {
        PayableOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<PayableOrder> all=service.findAll((Specification<PayableOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if(Objects.nonNull(param.getType())){
                predicates.add(criteriaBuilder.equal(root.get("type"), param.getType()));
            }
            if(Objects.nonNull(param.getStatus())){
                predicates.add(criteriaBuilder.equal(root.get("status"), param.getStatus()));
            }
            if (StringUtil.isNotEmpty(param.getOrderNo())) {
                predicates.add(criteriaBuilder.like(root.get("orderNo"), "%" + param.getOrderNo() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getPayableOrderName())) {
                predicates.add(criteriaBuilder.like(root.get("payableOrderName"), "%" + param.getPayableOrderName() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    @LogOperation("更改委外单状态信息")
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResponse updateStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status) {
        service.updateStatus(id,status);
        return CommonResponse.build(Result.success());
    }
}
