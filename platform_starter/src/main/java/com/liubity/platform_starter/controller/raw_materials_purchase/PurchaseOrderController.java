package com.liubity.platform_starter.controller.raw_materials_purchase;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.PurchaseOrderStatus;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.quality_restriction.RawMaterialQuality;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseOrder;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseRawMaterial;
import com.liubity.platform_starter.service.quality_restriction.RawMaterialQualityService;
import com.liubity.platform_starter.service.raw_materials_purchase.PurchaseOrderService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.OrderNoGenerator;
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
import java.util.Optional;

/**
 * @Author Liubity
 * @Date 2020-12-07
 */
@RestController
@RequestMapping("/purchase_order")
public class PurchaseOrderController {
    
    @Autowired
    private PurchaseOrderService service;
    @Autowired
    private RawMaterialQualityService qualityService;
    
    @LogOperation("新增采购订单信息")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<PurchaseOrder> request, HttpServletRequest httpServletRequest) {
        PurchaseOrder param=request.getParam();
        param.setOrderNo(OrderNoGenerator.orderNo("CG"));
        param.setDutyMan(AccountInfo.getAccount(httpServletRequest).getName());
        param.setStatus(PurchaseOrderStatus.NEW.status);
        PurchaseOrder save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("更新采购订单信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<PurchaseOrder> request) {
        PurchaseOrder param=request.getParam();
        PurchaseOrder save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("删除采购订单信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @LogOperation("更新采购订单状态信息")
    @PostMapping("/updateStatus/{id}/{status}")
    @Transactional
    public CommonResponse updateStatus(@PathVariable("id") Long id,@PathVariable("status")Integer status) {
        service.updateStatus(id,status);
        //采购完成，生成质检待检单
        if(PurchaseOrderStatus.PURCHASED.status.equals(status)){
            Optional<PurchaseOrder> byId=service.findById(id);
            if(byId.isPresent()){
                PurchaseOrder order = byId.get();
                List<PurchaseRawMaterial> rawMaterialList=order.getRawMaterialList();
                List<RawMaterialQuality> qualities = new ArrayList<>();
                if(Objects.nonNull(rawMaterialList)){
                    rawMaterialList.forEach(rm->{
                        RawMaterialQuality  quality = new RawMaterialQuality();
                        quality.setCgOrderNo(order.getOrderNo())
                                .setPurchaseMan(order.getPurchaseMan())
                                .setRawMaterialName(rm.getRawMaterialName())
                                .setTotalNum(rm.getTotalNum());
                        qualities.add(quality);
                    });
                }
                qualityService.saveAll(qualities);
            }
        }
        //通知
        //todo
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<PurchaseOrder> request) {
        PurchaseOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<PurchaseOrder> all=service.findAll(filter(param), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    public static Specification<PurchaseOrder> filter(PurchaseOrder param) {
        return (Specification<PurchaseOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (StringUtil.isNotEmpty(param.getPurchaseMan())) {
                predicates.add(criteriaBuilder.like(root.get("purchaseMan"), "%" + param.getPurchaseMan() + "%"));
            }
            if (Objects.nonNull(param.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), param.getStatus()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
    
}
