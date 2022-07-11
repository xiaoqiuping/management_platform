package com.liubity.platform_starter.controller.sale;

import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.sale.SaleOffPrice;
import com.liubity.platform_starter.service.sale.SaleOffPriceService;
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
@RequestMapping("/saleoff_price")
public class SaleOffPriceController {
    
    @Autowired
    private SaleOffPriceService service;
    
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<SaleOffPrice> request){
        SaleOffPrice param=request.getParam();
        SaleOffPrice saleOffPrice = null;
        if(Objects.nonNull(param.getGoodsName())){
            saleOffPrice = service.findByGoodsName(param.getGoodsName());
        }else if(Objects.nonNull(param.getBuyer())) {
            saleOffPrice = service.findByBuyer(param.getBuyer());
        }
        if(Objects.nonNull(saleOffPrice)){
            return CommonResponse.build(ReturnCode.ERROR,"该商品或者该购买商已存在");
        }
        SaleOffPrice save=service.save(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<SaleOffPrice> request){
        SaleOffPrice param=request.getParam();
        SaleOffPrice saleOffPrice = null;
        if(Objects.nonNull(param.getGoodsName())){
            saleOffPrice = service.findByGoodsName(param.getGoodsName());
        }else if(Objects.nonNull(param.getBuyer())) {
            saleOffPrice = service.findByBuyer(param.getBuyer());
        }
        if(Objects.nonNull(saleOffPrice)){
            return CommonResponse.build(ReturnCode.ERROR,"该商品或者该购买商已存在");
        }
        SaleOffPrice update=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),update);
    }
    
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<SaleOffPrice> request){
        SaleOffPrice param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<SaleOffPrice> all=service.findAll((Specification<SaleOffPrice>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            //商品类型
            if (StringUtil.isNotEmpty(param.getGoodsName())) {
                predicates.add(criteriaBuilder.like(root.get("goodsName"), "%" + param.getGoodsName() + "%"));
            }
            //购买商
            if (StringUtil.isNotEmpty(param.getBuyer())) {
                predicates.add(criteriaBuilder.like(root.get("buyer"), "%" + param.getBuyer() + "%"));
            }
            //类型
            if (Objects.nonNull(param.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"),  param.getType() ));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
}
