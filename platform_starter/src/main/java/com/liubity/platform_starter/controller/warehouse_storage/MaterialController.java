package com.liubity.platform_starter.controller.warehouse_storage;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.warehouse_storage.ExpendRecord;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.utils.DateUtil;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 物料管理
 *
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private WSMaterialService service;
    
    
    @PostMapping("/save")
    @LogOperation("新增物料信息")
    @Transactional
    public CommonResponse save(@RequestBody CommonRequest<WSMaterial> request) {
        WSMaterial param=request.getParam();
        if (param.getMaterialType() == 1) {
            List<WSMaterial> byWSMaterialName=service.findByMaterialName(param.getMaterialName());
            if (Objects.nonNull(byWSMaterialName) && byWSMaterialName.size()>0) {
                return CommonResponse.build(ReturnCode.ERROR, "该原料名称【" + param.getMaterialName() + "】已存在");
            }
        } else {
            WSMaterial byWSMaterialName=service.findByMaterialNameAndMaterialModel(param.getMaterialName(), param.getMaterialModel());
            if (Objects.nonNull(byWSMaterialName)) {
                return CommonResponse.build(ReturnCode.ERROR, "该物料名称【" + param.getMaterialName()+"/"+param.getMaterialModel() + "】已存在");
            }
        }
        WSMaterial save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/update")
    @LogOperation("修改物料信息")
    @Transactional
    public CommonResponse update(@RequestBody CommonRequest<WSMaterial> request) {
        WSMaterial param=request.getParam();
        Optional<WSMaterial> byId=service.findById(param.getId());
        if(param.getMaterialType()==1){
            if(!byId.get().getMaterialName().equals(param.getMaterialName())){
                List<WSMaterial> byWSMaterialName=service.findByMaterialName(param.getMaterialName());
                if (Objects.nonNull(byWSMaterialName) && byWSMaterialName.size()>0) {
                    return CommonResponse.build(ReturnCode.ERROR, "该原料名称{}已存在",param.getMaterialName());
                }
            }
        }else {
            if(!(byId.get().getMaterialName().equals(param.getMaterialName())&&byId.get().getMaterialModel().equals(param.getMaterialModel()))){
                WSMaterial byWSMaterialName=service.findByMaterialNameAndMaterialModel(param.getMaterialName(), param.getMaterialModel());
                if (Objects.nonNull(byWSMaterialName)) {
                    return CommonResponse.build(ReturnCode.ERROR, "该物料名称{}/{}已存在",param.getMaterialName(),param.getMaterialModel());
                }
            }
        }
        WSMaterial save=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除物料信息")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<WSMaterial> request) {
        WSMaterial param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "updateTime"));
        Page<WSMaterial> all=service.findAll(filter(param), pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    public static Specification<WSMaterial> filter(WSMaterial param) {
        return (Specification<WSMaterial>) (root, criteriaQuery, criteriaBuilder) -> {
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
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
    
    @PostMapping("/listAll")
    public CommonResponse listAll() {
        List<WSMaterial> all=service.findAll();
        return CommonResponse.build(Result.success(), all);
    }
    
    @PostMapping("/listAllByMaterialName")
    public CommonResponse listAllByMaterialName(@RequestBody CommonRequest<WSMaterial> request){
        WSMaterial param=request.getParam();
        if(StringUtil.isEmpty(param.getMaterialName())){
            return CommonResponse.build(Result.success());
        }
        List<WSMaterial> all=service.findAll((Specification<WSMaterial>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (StringUtil.isNotEmpty(param.getMaterialName())) {
                predicates.add(criteriaBuilder.like(root.get("materialName"), "%" + param.getMaterialName() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        return CommonResponse.build(Result.success(), all);
    }
    
    @PostMapping("/listAllByType")
    public CommonResponse listAllByType(@RequestParam("materialType") Integer materialType, @RequestParam(value="childrenType", required=false) Integer childrenType) {
        List<WSMaterial> all=service.findAll((Specification<WSMaterial>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("materialType"), materialType));
            if (Objects.nonNull(childrenType)) {
                predicates.add(criteriaBuilder.equal(root.get("childrenType"), childrenType));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        return CommonResponse.build(Result.success(), all);
    }
    
    @PostMapping("/listAllWithOutRawMaterial")
    public CommonResponse listAllWithOutRawMaterial(@RequestBody CommonRequest<WSMaterial> request) {
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<WSMaterial> all=service.findAll((Specification<WSMaterial>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            predicates.add(criteriaBuilder.notEqual(root.get("materialType"), 1));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
