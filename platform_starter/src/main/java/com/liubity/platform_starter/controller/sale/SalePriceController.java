package com.liubity.platform_starter.controller.sale;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.sale.SalePrice;
import com.liubity.platform_starter.service.sale.SalePriceService;
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
@RequestMapping("/sale_price")
public class SalePriceController {
    
    @Autowired
    private SalePriceService service;
    
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<SalePrice> request) {
        SalePrice param=request.getParam();
        SalePrice byGoodsNameAndGoodsType=service.findByGoodsNameAndGoodsType(param.getGoodsName(), param.getGoodsType());
        if (Objects.nonNull(byGoodsNameAndGoodsType)) {
            return CommonResponse.build(ReturnCode.ERROR, "该型号的商品已存在");
        }
        SalePrice save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<SalePrice> request) {
        SalePrice param=request.getParam();
        SalePrice byGoodsNameAndGoodsType=service.findByGoodsNameAndGoodsType(param.getGoodsName(), param.getGoodsType());
        if (Objects.nonNull(byGoodsNameAndGoodsType)) {
            return CommonResponse.build(ReturnCode.ERROR, "该型号的商品已存在");
        }
        SalePrice update=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), update);
    }
    
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<SalePrice> request) {
        SalePrice param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SalePrice> all=service.findAll((Specification<SalePrice>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            //商品名称
            if (StringUtil.isNotEmpty(param.getGoodsName())) {
                predicates.add(criteriaBuilder.like(root.get("goodsName"), "%" + param.getGoodsName() + "%"));
            }
            //型号
            if (StringUtil.isNotEmpty(param.getGoodsType())) {
                predicates.add(criteriaBuilder.like(root.get("goodsType"), "%" + param.getGoodsType() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
