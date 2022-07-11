package com.liubity.platform_starter.controller.after_sale_repairing;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.Departments;
import com.liubity.platform_starter.enums.OrderNoType;
import com.liubity.platform_starter.enums.RepairOrderStatus;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.after_sale_repairing.RepairOrder;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.sale.Receivable;
import com.liubity.platform_starter.model.warehouse_storage.ProductionOrder;
import com.liubity.platform_starter.service.after_sale_repairing.RepairOrderService;
import com.liubity.platform_starter.service.sale.ReceivableService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.OrderNoGenerator;
import com.liubity.platform_starter.utils.StringUtil;
import org.checkerframework.checker.units.qual.A;
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
import java.util.Optional;

/**
 * @Author Liubity
 * @Date 2020-12-12
 */
@RestController
@RequestMapping("/repair_order")
public class RepairOrderController {
    
    @Autowired
    private RepairOrderService service;
    @Autowired
    private ReceivableService receivableService;
    
    
    @PostMapping("/save")
    @LogOperation("新增维修单信息")
    public CommonResponse save(@RequestBody CommonRequest<RepairOrder> request, HttpServletRequest httpServletRequest) {
        RepairOrder param=request.getParam();
        //生成应收单 1保内维修，2，保外维修
        Receivable receivable=new Receivable();
        String ysOrderNo=OrderNoGenerator.orderNo(OrderNoType.YS.type);
        receivable.setOrderNo(ysOrderNo)
                .setReceiveMoney(param.getRepairAmount())
                .setDutyMan(AccountInfo.getAccount(httpServletRequest).getName())
                .setType(2)
                .setStatus(0);
        if (param.getType().equals(1)) {
            receivable.setReceiveMoneyType(4);
        } else if (param.getType().equals(2)) {
            receivable.setReceiveMoneyType(5);
        } else {
            throw new BizException("未知的单据类型");
        }
        receivableService.save(receivable);
        
        param.setOrderNo(OrderNoGenerator.orderNo(OrderNoType.WX.type));
        param.setStatus(RepairOrderStatus.NEW.status);
        param.setYsOrderNo(ysOrderNo);
        RepairOrder save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/update")
    @LogOperation("更新维修单信息")
    public CommonResponse update(@RequestBody CommonRequest<RepairOrder> request) {
        RepairOrder param=request.getParam();
        RepairOrder save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("删除维修单信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        Optional<RepairOrder> byId=service.findById(id);
        if(byId.isPresent()){
            String ysOrderNo=byId.get().getYsOrderNo();
            Receivable byOrderNo=receivableService.findByOrderNo(ysOrderNo);
            //财务已收款
            if(Objects.nonNull(byOrderNo)){
                if(byOrderNo.getStatus().equals(1)){
                    throw new BizException("该笔应收款已收款，不能进行删除，请联系财务平账");
                }else {
                    receivableService.deleteById(byOrderNo.getId());
                }
            }
        }
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<RepairOrder> request) {
        RepairOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<RepairOrder> all=service.findAll(filter(param), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    public static Specification<RepairOrder> filter(RepairOrder param) {
        return (Specification<RepairOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (Objects.nonNull(param.getFrom())) {
                if (2 == param.getFrom()) {
                    predicates.add(criteriaBuilder.le(root.get("status"), 3));
                }
            }
            predicates.add(criteriaBuilder.equal(root.get("type"), param.getType()));
            if (Objects.nonNull(param.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), param.getStatus()));
            }
            if (StringUtil.isNotEmpty(param.getBuyerName())) {
                predicates.add(criteriaBuilder.like(root.get("buyerName"), "%" + param.getBuyerName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getWatchName())) {
                predicates.add(criteriaBuilder.like(root.get("watchName"), "%" + param.getWatchName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getWatchType())) {
                predicates.add(criteriaBuilder.like(root.get("watchType"), "%" + param.getWatchType() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
    
    
    @LogOperation("删除维修单信息")
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResponse updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        service.updateStatus(id, status);
        return CommonResponse.build(Result.success());
    }
    
    @LogOperation("审核维修单信息")
    @PostMapping("/examineOrder")
    public CommonResponse examineOrder(@RequestBody CommonRequest<RepairOrder> request, HttpServletRequest httpServletRequest) {
        RepairOrder param=request.getParam();
        param.setExamineMan(AccountInfo.getAccount(httpServletRequest).getName());
        service.saveAndFlush(param);
        return CommonResponse.build(Result.success());
    }
}
