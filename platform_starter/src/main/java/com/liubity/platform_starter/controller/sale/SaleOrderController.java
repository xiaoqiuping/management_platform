package com.liubity.platform_starter.controller.sale;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.annotation.MessageNotice;
import com.liubity.platform_starter.enums.OrderNoType;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.after_sale_repairing.RepairOrder;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.sale.Receivable;
import com.liubity.platform_starter.model.sale.SaleOffPrice;
import com.liubity.platform_starter.model.sale.SaleOrder;
import com.liubity.platform_starter.service.sale.ReceivableService;
import com.liubity.platform_starter.service.sale.SaleOrderService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.OrderNoGenerator;
import com.liubity.platform_starter.utils.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
@RestController
@RequestMapping("/sale_order")
public class SaleOrderController {
    
    @Autowired
    private SaleOrderService service;
    @Autowired
    private ReceivableService receivableService;
    
    
    @PostMapping("/save")
    @MessageNotice("createOrder")
    @LogOperation("??????????????????")
    public CommonResponse save(@RequestBody CommonRequest<SaleOrder> request,HttpServletRequest httpServletRequest) {
        SaleOrder param=request.getParam();
        
        Receivable receivable=new Receivable();
        String ysOrderNo=OrderNoGenerator.orderNo(OrderNoType.YS.type);
        receivable.setOrderNo(ysOrderNo)
                .setReceiveMoney(BigDecimal.valueOf(param.getTranPrice().doubleValue() * param.getSaleNum()))
                .setDutyMan(AccountInfo.getAccount(httpServletRequest).getName())
                .setType(1)
                .setReceiveMoneyType(3)
                .setStatus(0);
        receivableService.save(receivable);

        param.setOrderNo(OrderNoGenerator.orderNo(OrderNoType.XS.type));
        param.setStatus(1);
        param.setChangeFlag(false);
        param.setYsOrderNo(ysOrderNo);
        SaleOrder save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @MessageNotice("updateOrder")
    @LogOperation("??????????????????")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<SaleOrder> request) {
        SaleOrder param=request.getParam();
        SaleOrder update=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), update);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("??????????????????")
    public CommonResponse delete(@PathVariable("id") Long id) {
    
        Optional<SaleOrder> byId=service.findById(id);
        if(byId.isPresent()){
            String ysOrderNo=byId.get().getYsOrderNo();
            Receivable byOrderNo=receivableService.findByOrderNo(ysOrderNo);
            //???????????????
            if(Objects.nonNull(byOrderNo)){
                if(byOrderNo.getStatus().equals(1)){
                    throw new BizException("?????????????????????????????????????????????????????????????????????");
                }else {
                    receivableService.deleteById(byOrderNo.getId());
                }
            }
        }
        
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<SaleOrder> request) {
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SaleOrder> all=service.findAll(filter(request.getParam()), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    public static Specification<SaleOrder> filter(SaleOrder param){
        return (Specification<SaleOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (Objects.nonNull(param.getStatusList()) && param.getStatusList().size() > 0) {
                CriteriaBuilder.In<Integer> in=criteriaBuilder.in(root.get("status"));
                param.getStatusList().forEach(in::value);
                predicates.add(in);
            }
            if (Objects.nonNull(param.getChangeFlag())) {
                predicates.add(criteriaBuilder.equal(root.get("changeFlag"), param.getChangeFlag()));
            }
            //??????
            if (StringUtil.isNotEmpty(param.getOrderNo())) {
                predicates.add(criteriaBuilder.like(root.get("orderNo"), "%" + param.getOrderNo() + "%"));
            }
            //????????????
            if (StringUtil.isNotEmpty(param.getWatchName())) {
                predicates.add(criteriaBuilder.like(root.get("watchName"), "%" + param.getWatchName() + "%"));
            }
            //????????????
            if (StringUtil.isNotEmpty(param.getWatchType())) {
                predicates.add(criteriaBuilder.like(root.get("watchType"), "%" + param.getWatchType() + "%"));
            }
            //?????????
            if (StringUtil.isNotEmpty(param.getBuyer())) {
                predicates.add(criteriaBuilder.equal(root.get("buyer"), "%" + param.getBuyer() + "%"));
            }
            //?????????
            if (StringUtil.isNotEmpty(param.getSalesman())) {
                predicates.add(criteriaBuilder.equal(root.get("salesman"), "%" + param.getSalesman() + "%"));
            }
            //????????????
            if (Objects.nonNull(param.getSaleStartDate()) && Objects.nonNull(param.getSaleEndDate())) {
                predicates.add(criteriaBuilder.between(root.get("saleDate"), param.getSaleStartDate(), param.getSaleEndDate()));
            }
            //????????????
            if (Objects.nonNull(param.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), param.getStatus()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
    
    
    @MessageNotice("updateOrderStatus")
    @LogOperation("????????????????????????")
    @PostMapping("/updateStatus")
    public CommonResponse updateStatus(@RequestBody CommonRequest<SaleOrder> request) {
        service.updateStatus(request.getParam().getStatus(), request.getParam().getId());
        return CommonResponse.build(Result.success());
    }

}
