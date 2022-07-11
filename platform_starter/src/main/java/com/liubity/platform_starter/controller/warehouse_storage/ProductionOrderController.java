package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.Departments;
import com.liubity.platform_starter.enums.OrderNoType;
import com.liubity.platform_starter.enums.SCOrderStatus;
import com.liubity.platform_starter.enums.SaleOrderStatus;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.quality_restriction.ProductionProcessQuality;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseOrder;
import com.liubity.platform_starter.model.sale.SaleOrder;
import com.liubity.platform_starter.model.warehouse_storage.ProductProcess;
import com.liubity.platform_starter.model.warehouse_storage.ProductionOrder;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.quality_restriction.ProductionProcessQualityService;
import com.liubity.platform_starter.service.sale.SaleOrderService;
import com.liubity.platform_starter.service.warehouse_storage.ProductProcessService;
import com.liubity.platform_starter.service.warehouse_storage.ProductionOrderService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.DateUtil;
import com.liubity.platform_starter.utils.OrderNoGenerator;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 生产订单管理
 *
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/production_order")
public class    ProductionOrderController {
    
    @Autowired
    private ProductionOrderService service;
    @Autowired
    private ProductProcessService processService;
    @Autowired
    private ProductionProcessQualityService qualityService;
    @Autowired
    private WSMaterialService wsMaterialService;
    @Autowired
    private SaleOrderService saleOrderService;
    
    
    @PostMapping("/save")
    @LogOperation("新增生产订单信息")
    @Transactional
    public CommonResponse save(@RequestBody CommonRequest<ProductionOrder> request) {
        ProductionOrder param=request.getParam();
        param.setOrderNo(OrderNoGenerator.orderNo(OrderNoType.SC.type));
        param.setStatus(SCOrderStatus.NEW.status);
        param.setOrderCreateTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        if(StringUtil.isNotEmpty(param.getSaleOrderNo())){
            SaleOrder byOrderNo=saleOrderService.findByOrderNo(param.getSaleOrderNo());
            if(Objects.isNull(byOrderNo)){
                throw new BizException("订单号{}不存在",param.getSaleOrderNo());
            }
            //更新订单到生产中状态
            saleOrderService.updateStatus(SaleOrderStatus.PRODUCTING.status,byOrderNo.getId());
        }
        ProductionOrder save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/update")
    @LogOperation("修改生产订单信息")
    @Transactional
    public CommonResponse update(@RequestBody CommonRequest<ProductionOrder> request) {
        ProductionOrder param=request.getParam();
        ProductionOrder save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除生产订单信息")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<ProductionOrder> request) {
        ProductionOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ProductionOrder> all=service.findAll(filter(param), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    
    public static Specification<ProductionOrder> filter(ProductionOrder param){
        return (Specification<ProductionOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            String from = param.getFrom();
            if (StringUtil.isNotEmpty(from)) {
                if (Departments.SCJH.type.equals(from)) {
                    predicates.add(criteriaBuilder.le(root.get("status"), 5));
                }
                if (Departments.SCGL.type.equals(from)) {
                    predicates.add(criteriaBuilder.ge(root.get("status"), 5));
                }
                if (Departments.PZJY.type.equals(from)) {
                    predicates.add(criteriaBuilder.between(root.get("status"), 8,10));
                }
            }
            if (StringUtil.isNotEmpty(param.getProductionName())) {
                predicates.add(criteriaBuilder.like(root.get("productionName"), "%" + param.getProductionName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getProductionType())) {
                predicates.add(criteriaBuilder.like(root.get("productionType"), "%" + param.getProductionType() + "%"));
            }
            if (Objects.nonNull(param.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), param.getStatus()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
    
    @PostMapping("/updateStatus/{id}/{status}")
    @LogOperation("更改生产订单状态")
    public CommonResponse updateStatus(@PathVariable("status") Integer status, @PathVariable("id") Long id) {
        service.updateStatus(status, id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/verifyOrder")
    @LogOperation("审核生产订单")
    public CommonResponse verifyOrder(@RequestBody CommonRequest<ProductionOrder> request, HttpServletRequest httpServletRequest) {
        ProductionOrder param=request.getParam();
        param.setVerifyMan(AccountInfo.getAccount(httpServletRequest).getName());
        param.setStatus(param.getVerifyStatus());
        ProductionOrder save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @Transactional
    @PostMapping("/examineOrder")
    @LogOperation("质检生产订单产品")
    public CommonResponse examineOrder(@RequestBody CommonRequest<ProductionOrder> request, HttpServletRequest httpServletRequest) {
        ProductionOrder param=request.getParam();
        param.setExamineMan(AccountInfo.getAccount(httpServletRequest).getName());
        
        if(param.getStatus().equals(SCOrderStatus.STORAGE_ING.status)){
            //入库
            WSMaterial byMaterialNameAndMaterialModel=wsMaterialService.findByMaterialNameAndMaterialModel(param.getProductionName(), param.getProductionType());
            if(Objects.isNull(byMaterialNameAndMaterialModel)){
                throw new BizException("产品{}/{}不存在，请联系管理员添加该产品",param.getProductionName(), param.getProductionType());
            }
            byMaterialNameAndMaterialModel.setTotalNum(byMaterialNameAndMaterialModel.getTotalNum()+param.getTotalNum());
            wsMaterialService.saveAndFlush(byMaterialNameAndMaterialModel);
            param.setStatus(SCOrderStatus.STORAGE_SUCCESS.status);
    
            if(StringUtil.isNotEmpty(param.getSaleOrderNo())){
                //更新销售订单到生产完成状态
                saleOrderService.updateStatusByOrderNo(SaleOrderStatus.PRODUCT_FINISHED.status,param.getSaleOrderNo());
            }
            
        }
        ProductionOrder save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    
    @PostMapping("/examineProcess")
    @LogOperation("送检生产订单工序流程")
    public CommonResponse examineProcess(@RequestBody CommonRequest<ProductionOrder> request) {
        ProductionOrder param=request.getParam();
        ProductProcess productProcess=param.getProcessList().get(0);
        ProductionProcessQuality quality=new ProductionProcessQuality();
        quality.setProductionName(param.getProductionName())
                .setProductionType(param.getProductionType())
                .setScOrderNo(param.getOrderNo())
                .setType(param.getType())
                .setTotalNum(param.getTotalNum())
                .setProductProcessId(productProcess.getId())
                .setProductProcessName(productProcess.getProcessName())
                .setProcessStep(productProcess.getProcessStep());
        qualityService.save(quality);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("updateProductionProcess")
    @LogOperation("更新工序质检信息")
    @Transactional
    public CommonResponse updateProductionProcess(@RequestBody CommonRequest<ProductProcess> request, HttpServletRequest httpServletRequest) {
        //更新工序信息
        ProductProcess param=request.getParam();
        Optional<ProductProcess> byId1=processService.findById(param.getId());
        ProductProcess productProcess=byId1.get();
        productProcess.setAccessNum(param.getAccessNum())
                .setQualityNum(param.getQualityNum())
                .setQualityMan(AccountInfo.getAccount(httpServletRequest).getName())
                .setQualityTime(DateUtil.localDateTime2Date(LocalDateTime.now()))
                .setQualityStatus(param.getQualityStatus())
                .setRemarks(param.getRemarks());
        processService.saveAndFlush(productProcess);
        //更新工序质检单信息
        Optional<ProductionProcessQuality> byId=qualityService.findById(param.getProductProcessQualityId());
        ProductionProcessQuality quality=byId.get().setQualityStatus(param.getQualityStatus());
        qualityService.saveAndFlush(quality);
        
        //更新订单信息
        ProductionOrder byOrderNo=service.findByOrderNo(param.getScOrderNo());
        List<ProductProcess> processList=byOrderNo.getProcessList();
        if (Objects.nonNull(processList) && processList.size() > 0) {
            int count=(int) processList.stream().filter(p -> Objects.nonNull(p.getQualityStatus()) && p.getQualityStatus() == 1).count();
            if (count > 0) {
                if (count == processList.size()) {
                    byOrderNo.setStatus(SCOrderStatus.FINISHED_PRODUCTION.status);
                } else {
                    byOrderNo.setStatus(SCOrderStatus.IN_PRODUCTION.status);
                }
                service.saveAndFlush(byOrderNo);
            }
        }
        return CommonResponse.build(Result.success());
    }
    
}
