package com.liubity.platform_starter.controller.sale;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.OrderNoType;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.sale.Receivable;
import com.liubity.platform_starter.service.sale.ReceivableService;
import com.liubity.platform_starter.utils.OrderNoGenerator;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
@RestController
@RequestMapping("/receivable")
public class ReceivableController {
    
    @Autowired
    private ReceivableService service;
    
    @LogOperation("新增应收单")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<Receivable> request) {
        Receivable param=request.getParam();
        param.setOrderNo(OrderNoGenerator.orderNo(OrderNoType.YS.type));
        if(Objects.isNull(param.getStatus())){
            param.setStatus(0);
        }
        Receivable save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @LogOperation("更新应收单")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<Receivable> request) {
        Receivable param=request.getParam();
        Receivable update=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), update);
    }
    
    @LogOperation("删除应收单")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<Receivable> request) {
        Receivable param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Receivable> all=service.findAll((Specification<Receivable>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if(Objects.nonNull(param.getType())){
                predicates.add(criteriaBuilder.equal(root.get("type"), param.getType()));
            }
            if(Objects.nonNull(param.getStatus())){
                predicates.add(criteriaBuilder.equal(root.get("status"), param.getStatus()));
            }
            //单号
            if (StringUtil.isNotEmpty(param.getOrderNo())) {
                predicates.add(criteriaBuilder.like(root.get("orderNo"), "%" + param.getOrderNo() + "%"));
            }
            //应收类别
            if (Objects.nonNull(param.getReceiveMoneyType())) {
                predicates.add(criteriaBuilder.equal(root.get("receiveMoneyType"), param.getReceiveMoneyType()));
            }
            //责任人
            if (StringUtil.isNotEmpty(param.getDutyMan())) {
                predicates.add(criteriaBuilder.like(root.get("dutyMan"), "%" + param.getDutyMan() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    @LogOperation("更改委外单状态信息")
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResponse updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        service.updateStatus(id, status);
        return CommonResponse.build(Result.success());
    }
    
}
