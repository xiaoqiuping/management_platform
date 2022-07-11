package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.OperationType;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.backstage.Account;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.sale.SaleOrder;
import com.liubity.platform_starter.model.warehouse_storage.Manufacture;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.sale.SaleOrderService;
import com.liubity.platform_starter.service.warehouse_storage.ManufactureService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.CacheUtil;
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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 成品管理
 *
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/manufacture")
public class ManufactureController {
    @Autowired
    private ManufactureService service;
    @Autowired
    private WSMaterialService materialService;
    @Autowired
    private SaleOrderService saleOrderService;
    
    @PostMapping("/save")
    @LogOperation("新增成品信息")
    @Transactional
    public CommonResponse save(@RequestBody CommonRequest<Manufacture> request, HttpServletRequest httpServletRequest) {
        Manufacture param=request.getParam();
        param.setOperationMan(AccountInfo.getAccount(httpServletRequest).getName());
        param.setOperationTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        Manufacture save=service.save(param);
    
        Optional<WSMaterial> byId=materialService.findById(param.getMaterialId());
        WSMaterial wsMaterial = byId.get();
        if(OperationType.PUT.status.equals(param.getOperationType())){
            wsMaterial.setTotalNum(wsMaterial.getTotalNum()+param.getOperationNum());
        }else {
            int i=wsMaterial.getTotalNum() - param.getOperationNum();
            if(i<0){
                throw new BizException("{}/{}库存不足",param.getManufactureName(),param.getManufactureModel());
            }
            wsMaterial.setTotalNum(i);
        }
        materialService.save(wsMaterial);
    
        if(!StringUtil.isEmpty(param.getSaleOrderNo())){
            SaleOrder byOrderNo=saleOrderService.findByOrderNo(param.getSaleOrderNo());
            if(Objects.isNull(byOrderNo)){
                throw new BizException("该生产订单号{}不存在",param.getSaleOrderNo());
            }
        }
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/update")
    @LogOperation("修改成品信息")
    @Transactional
    public CommonResponse update(@RequestBody CommonRequest<Manufacture> request, HttpServletRequest httpServletRequest) {
        Manufacture param=request.getParam();
        String authorization=httpServletRequest.getHeader("Authorization");
        Account account=(Account) CacheUtil.get(authorization);
        param.setOperationMan(account.getName());
        param.setOperationTime(DateUtil.localDateTime2Date(LocalDateTime.now()));
        Manufacture save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除成品信息")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<Manufacture> request) {
        Manufacture param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Manufacture> all=service.findAll((Specification<Manufacture>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (StringUtil.isNotEmpty(param.getManufactureName())) {
                predicates.add(criteriaBuilder.like(root.get("manufactureName"), "%" + param.getManufactureName() + "%"));
            }
            if (StringUtil.isNotEmpty(param.getManufactureModel())) {
                predicates.add(criteriaBuilder.like(root.get("manufactureModel"), "%" + param.getManufactureModel() + "%"));
            }
            if (Objects.nonNull(param.getOperationType())) {
                predicates.add(criteriaBuilder.equal(root.get("operationType"), param.getOperationType()));
            }
            if(Objects.nonNull(param.getType())){
                predicates.add(criteriaBuilder.equal(root.get("type"),  param.getType() ));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
