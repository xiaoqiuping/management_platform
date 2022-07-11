package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.after_sale_repairing.RepairOrder;
import com.liubity.platform_starter.model.backstage.Account;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.warehouse_storage.ExpendRecord;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.warehouse_storage.ExpendRecordService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.utils.CacheUtil;
import com.liubity.platform_starter.utils.DateUtil;
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
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @Author Liubity
 * @Date 2021-02-28
 */
@RestController
@RequestMapping("/expend_record")
public class ExpendRecordController {
    
    
    @Autowired
    private ExpendRecordService service;
    @Autowired
    private WSMaterialService wsMaterialService;
    
    
    @PostMapping("/save")
    @LogOperation("新增耗费记录信息")
    @Transactional
    public CommonResponse save(@RequestBody CommonRequest<ExpendRecord> request, HttpServletRequest httpServletRequest) {
        ExpendRecord param=request.getParam();
        String authorization=httpServletRequest.getHeader("Authorization");
        Account account=(Account) CacheUtil.get(authorization);
    
        Optional<WSMaterial> byId=wsMaterialService.findById(param.getMaterialId());
        if(!byId.isPresent()){
            throw new BizException("未查询到物料信息");
        }
        WSMaterial wsMaterial = byId.get();
        param.setMaterialName(wsMaterial.getMaterialName());
        param.setMaterialModel(wsMaterial.getMaterialModel());
        param.setMaterialType(wsMaterial.getMaterialType());
        param.setChildrenType(wsMaterial.getChildrenType());
        param.setTuHao(wsMaterial.getTuHao());
        param.setRecordMan(account.getName());
        param.setRecordDate(new Date());
        ExpendRecord save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/update")
    @LogOperation("修改耗费记录信息")
    @Transactional
    public CommonResponse update(@RequestBody CommonRequest<ExpendRecord> request) {
        ExpendRecord param=request.getParam();
        ExpendRecord save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除耗费记录信息")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<ExpendRecord> request) {
        ExpendRecord param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ExpendRecord> all=service.findAll(filter(param), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    public static Specification<ExpendRecord> filter(ExpendRecord param) {
        return (Specification<ExpendRecord>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (StringUtil.isNotEmpty(param.getMaterialName())) {
                predicates.add(criteriaBuilder.like(root.get("materialName"), "%" + param.getMaterialName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getMaterialModel())) {
                predicates.add(criteriaBuilder.like(root.get("materialModel"), "%" + param.getMaterialModel() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getTuHao())) {
                predicates.add(criteriaBuilder.like(root.get("tuHao"), "%" + param.getTuHao() + "%"));
            }
            if (Objects.nonNull(param.getMaterialType())) {
                predicates.add(criteriaBuilder.equal(root.get("materialType"), param.getMaterialType()));
                if (Objects.nonNull(param.getChildrenType())) {
                    predicates.add(criteriaBuilder.equal(root.get("childrenType"), param.getChildrenType()));
                }
            }
            if(Objects.nonNull(param.getRecordDateList()) && param.getRecordDateList().size()>0){
                predicates.add(criteriaBuilder.between(root.get("recordDate"), DateUtil.parseDate(param.getRecordDateList().get(0),"yyyy-MM-dd"),parseDate(param.getRecordDateList().get(1),"yyyy-MM-dd")));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
    
    public static Date parseDate(String dateStr, String pattern) {
        LocalDate localDate = DateUtil.parseLocalDate(dateStr,pattern);
        localDate = localDate.plusDays(1);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }
    
}
