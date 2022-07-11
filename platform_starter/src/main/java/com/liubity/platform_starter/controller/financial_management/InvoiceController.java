package com.liubity.platform_starter.controller.financial_management;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.financial_management.Invoice;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseContract;
import com.liubity.platform_starter.service.financial_management.InvoiceService;
import com.liubity.platform_starter.service.raw_materials_purchase.PurchaseContractService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.DateUtil;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-12-15
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService service;
    
    @LogOperation("新增发票信息")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<Invoice> request, HttpServletRequest httpServletRequest){
        Invoice param=request.getParam();
        Invoice byInvoiceName=service.findByInvoiceName(param.getInvoiceName());
        if(Objects.nonNull(byInvoiceName)){
            return CommonResponse.build(ReturnCode.ERROR,"{}已存在",param.getInvoiceName());
        }
        param.setOperator(AccountInfo.getAccount(httpServletRequest).getName());
        param.setUploadTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        Invoice save=service.save(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("更新发票信息")
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<Invoice> request){
        Invoice param=request.getParam();
        Invoice save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("删除发票信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<Invoice> request){
        Invoice param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<Invoice> all=service.findAll((Specification<Invoice>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            //合同称
            if (StringUtil.isNotEmpty(param.getInvoiceName())) {
                predicates.add(criteriaBuilder.like(root.get("invoiceName"), "%" + param.getInvoiceName() + "%"));
            }
            //上传时间
            if (Objects.nonNull(param.getUploadStartTime())&&Objects.nonNull(param.getUploadEndTime())){
                predicates.add(criteriaBuilder.between(root.get("uploadTime"),param.getUploadStartTime(), param.getUploadEndTime()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
