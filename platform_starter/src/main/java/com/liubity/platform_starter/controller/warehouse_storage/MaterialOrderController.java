package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.sale.Contract;
import com.liubity.platform_starter.model.sale.ContractFile;
import com.liubity.platform_starter.model.warehouse_storage.MaterialOperation;
import com.liubity.platform_starter.model.warehouse_storage.MaterialOrder;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.warehouse_storage.MaterialOperationService;
import com.liubity.platform_starter.service.warehouse_storage.MaterialOrderService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.utils.AccountInfo;
import com.liubity.platform_starter.utils.OrderNoGenerator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 物料管理
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/material_order")
public class MaterialOrderController {
    @Autowired
    private MaterialOrderService service;
    @Autowired
    private MaterialOperationService operationService;
    @Autowired
    private WSMaterialService wsMaterialService;
    
    
    @Transactional
    @PostMapping("/save")
    @LogOperation("新增物料管理信息")
    public CommonResponse save(@RequestBody CommonRequest<MaterialOrder> request, HttpServletRequest httpServletRequest){
        MaterialOrder param=request.getParam();
        param.setOperationMan(AccountInfo.getAccount(httpServletRequest).getName());
        if(param.getOperationType()==1){
            param.setOrderNo(OrderNoGenerator.orderNo("LL"));
        }else if(param.getOperationType()==2){
            param.setOrderNo(OrderNoGenerator.orderNo("TL"));
        }else if(param.getOperationType()==3){
            param.setOrderNo(OrderNoGenerator.orderNo("BL"));
        }
        MaterialOrder save=service.save(param);
        List<MaterialOperation> operationList=param.getMaterialOperationList();
        if(Objects.nonNull(operationList) && operationList.size()>0){
            operationList.forEach(operation->{
                operation.setMaterialOrderId(save.getId());
                operationService.save(operation);
                Optional<WSMaterial> byId=wsMaterialService.findById(operation.getMaterialId());
                WSMaterial wsMaterial = byId.get();
                //退料、补料
                if(param.getOperationType()!=1){
                    wsMaterial.setTotalNum(wsMaterial.getTotalNum()+operation.getOperationNum());
                //领料
                }else {
                    int i=wsMaterial.getTotalNum() - operation.getOperationNum();
                    if(i<0){
                        throw new BizException("{}/{}库存不足",operation.getMaterialName(),operation.getMaterialModel());
                    }
                    wsMaterial.setTotalNum(i);
                }
                wsMaterialService.save(wsMaterial);
            });
        }
        return CommonResponse.build(Result.success(),save);
    }
    
    @Transactional
    @PostMapping("/update")
    @LogOperation("修改物料管理信息")
    public CommonResponse update(@RequestBody CommonRequest<MaterialOrder> request){
        MaterialOrder param=request.getParam();
        MaterialOrder save=service.saveAndFlush(param);
        //operationService.deleteAllByMaterialId(param.getId());
        List<MaterialOperation> operationList=param.getMaterialOperationList();
        if(Objects.nonNull(operationList) && operationList.size()>0){
            operationList.forEach(operation->{
                Optional<MaterialOperation> byId=operationService.findById(operation.getId());
                Optional<WSMaterial> byId1=wsMaterialService.findById(operation.getMaterialId());
                WSMaterial wsMaterial=byId1.get();
                //退料、补料
                if(param.getOperationType()!=1){
                    //退料、补料 要先减掉
                    int old =  wsMaterial.getTotalNum()-byId.get().getOperationNum();
                    if(old<0){
                        throw new BizException("{}/{}无法回退，因为库存不足",operation.getMaterialName(),operation.getMaterialModel());
                    }
                    wsMaterial.setTotalNum(old);
                    wsMaterial.setTotalNum(wsMaterial.getTotalNum()+operation.getOperationNum());
                    //领料
                }else {
                    //领料要先加上
                    int old =  wsMaterial.getTotalNum()+byId.get().getOperationNum();
                    wsMaterial.setTotalNum(old);
                    int i=wsMaterial.getTotalNum() - operation.getOperationNum();
                    if(i<0){
                        throw new BizException("{}/{}库存不足",operation.getMaterialName(),operation.getMaterialModel());
                    }
                    wsMaterial.setTotalNum(i);
                }
                wsMaterialService.save(wsMaterial);
                operation.setMaterialOrderId(save.getId());
                operationService.saveAndFlush(operation);
            });
        }
        return CommonResponse.build(Result.success(),save);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除物料管理信息")
    public CommonResponse delete(@PathVariable("id")Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @GetMapping("/get/{id}")
    public CommonResponse get(@PathVariable("id")Long id){
        Optional<MaterialOrder> byId=service.findById(id);
        return CommonResponse.build(Result.success(),byId.get());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<MaterialOrder> request){
        MaterialOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<MaterialOrder> all=service.findAll((Specification<MaterialOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            //根据类型分类
            predicates.add(criteriaBuilder.equal(root.get("operationType"), param.getOperationType()));
            if(StringUtil.isNotEmpty(param.getDutyMan())){
                predicates.add(criteriaBuilder.like(root.get("dutyMan"),"%" + param.getDutyMan() + "%"));
            }
            if(Objects.nonNull(param.getOperationStartTime()) && Objects.nonNull(param.getOperationEndTime())){
                predicates.add(criteriaBuilder.between(root.get("operationTime"),param.getOperationStartTime(),param.getOperationEndTime()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
