package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.model.backstage.LogManagement;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.service.backstage.LogManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
@RestController
@RequestMapping("/log")
public class LogManagementController {
    
    @Autowired
    private LogManagementService logManagementService;
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<LogManagement> request) {
        LogManagement param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<LogManagement> all=logManagementService.findAll((Specification<LogManagement>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (Objects.nonNull(param.getAccount())) {
                predicates.add(criteriaBuilder.like(root.get("account"), "%" + param.getAccount() + "%"));
            }
            if (Objects.nonNull(param.getOperationStartTime()) && Objects.nonNull(param.getOperationEndTime())) {
                predicates.add(criteriaBuilder.between(root.get("operationTime").as(Date.class), param.getOperationStartTime(),param.getOperationEndTime()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
