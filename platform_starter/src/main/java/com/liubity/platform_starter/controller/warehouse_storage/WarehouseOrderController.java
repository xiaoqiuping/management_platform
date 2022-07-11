package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.PurchaseOrderStatus;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.production_plan.OutSourceOrder;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseOrder;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseRawMaterial;
import com.liubity.platform_starter.model.warehouse_storage.WarehouseOrder;
import com.liubity.platform_starter.service.raw_materials_purchase.PurchaseOrderService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.service.warehouse_storage.WarehouseOrderService;
import com.liubity.platform_starter.utils.OrderNoGenerator;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 入库单管理
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/warehouse_order")
public class WarehouseOrderController {
    
    @Autowired
    private WarehouseOrderService service;
    @Autowired
    private PurchaseOrderService orderService;
    @Autowired
    private WSMaterialService wsMaterialService;
    
    
    @PostMapping("/save")
    @LogOperation("新增入库单信息")
    @Transactional
    public CommonResponse save(@Validated @RequestBody CommonRequest<WarehouseOrder> request){
        WarehouseOrder param=request.getParam();
        PurchaseOrder byOrderNo=orderService.findByOrderNo(param.getCgOrderNo());
        if(Objects.isNull(byOrderNo)){
            return CommonResponse.build(ReturnCode.ERROR,"采购订单号{}不存在",param.getCgOrderNo());
        }
        if(!PurchaseOrderStatus.EXAMINE_SUCCESS.status.equals(byOrderNo.getStatus())){
            return CommonResponse.build(ReturnCode.ERROR,"采购订单号{}原料未全部通过质检，不能入库",param.getCgOrderNo());
        }
        //新增采购入库单
        param.setOrderNo(OrderNoGenerator.orderNo("RK"));
        WarehouseOrder warehouseOrder=service.save(param);
        //更新原材料数量
        List<PurchaseRawMaterial> rawMaterialList=byOrderNo.getRawMaterialList();
        rawMaterialList.forEach(rm->{
            wsMaterialService.updateTotalNumByName(rm.getTotalNum(),rm.getRawMaterialName());
        });
        //更新采购单状态
        orderService.updateStatus(byOrderNo.getId(),PurchaseOrderStatus.STORAGE_SUCCESS.status);
        return CommonResponse.build(Result.success(),warehouseOrder);
    }
    
    @PostMapping("/update")
    @LogOperation("修改入库单信息")
    public CommonResponse update(@RequestBody CommonRequest<WarehouseOrder> request){
        WarehouseOrder param=request.getParam();
        WarehouseOrder warehouseOrder=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),warehouseOrder);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除入库单信息")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<WarehouseOrder> request){
        WarehouseOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<WarehouseOrder> all=service.findAll(filter(param), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    public static Specification<WarehouseOrder> filter(WarehouseOrder param) {
        return (Specification<WarehouseOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if(StringUtil.isNotEmpty(param.getPurchasingName())){
                predicates.add(criteriaBuilder.like(root.get("purchasingName"), "%" + param.getPurchasingName() + "%"));
            }
            if(StringUtil.isNotEmpty(param.getPurchasingUnit())){
                predicates.add(criteriaBuilder.like(root.get("purchasingUnit"), "%" + param.getPurchasingUnit() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
