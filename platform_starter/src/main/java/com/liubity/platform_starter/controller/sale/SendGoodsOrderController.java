package com.liubity.platform_starter.controller.sale;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.enums.Departments;
import com.liubity.platform_starter.enums.OrderNoType;
import com.liubity.platform_starter.enums.SaleOrderStatus;
import com.liubity.platform_starter.enums.SendGoodsOrderStatus;
import com.liubity.platform_starter.exception.BizException;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.sale.SaleOrder;
import com.liubity.platform_starter.model.sale.SendGoodsOrder;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.sale.SaleOrderService;
import com.liubity.platform_starter.service.sale.SendGoodsOrderService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.utils.OrderNoGenerator;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-12-18
 */
@RestController
@RequestMapping("/send_goods_order")
public class SendGoodsOrderController {
    
    @Autowired
    private SendGoodsOrderService service;
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private WSMaterialService wsMaterialService;
    
    @PostMapping("/save")
    @LogOperation("新增发货单状态信息")
    public CommonResponse save(@RequestBody CommonRequest<SendGoodsOrder> request) {
        SendGoodsOrder param=request.getParam();
        param.setOrderNo(OrderNoGenerator.orderNo(OrderNoType.FH.type));
        param.setStatus(SendGoodsOrderStatus.NEW.status);
        if(StringUtil.isNotEmpty(param.getSaleOrderNo())){
            SaleOrder byOrderNo=saleOrderService.findByOrderNo(param.getSaleOrderNo());
            if(Objects.isNull(byOrderNo)){
                throw new BizException("销售订单号{}不存在",param.getSaleOrderNo());
            }
        }
        SendGoodsOrder save=service.save(param);
        return CommonResponse.build(Result.success(), save);
    }
    
    @Transactional
    @PostMapping("/update")
    @LogOperation("更改发货单状态信息")
    public CommonResponse update(@RequestBody CommonRequest<SendGoodsOrder> request) {
        SendGoodsOrder param=request.getParam();
        //出库扣库存
        if(param.getStatus().equals(SendGoodsOrderStatus.WAREHOUSE_OUT.status)){
            WSMaterial byMaterialNameAndMaterialModel=wsMaterialService.findByMaterialNameAndMaterialModel(param.getProductionName(), param.getProductionType());
            if(Objects.isNull(byMaterialNameAndMaterialModel)){
                throw new BizException("物料{}/{}不存在，请联系管理员",param.getProductionName(),param.getProductionType());
            }
            int i=byMaterialNameAndMaterialModel.getTotalNum() - param.getTotalNum();
            if(i<0){
                throw new BizException("物料{}/{}库存不足，请联系仓库管理员",param.getProductionName(),param.getProductionType());
            }
            wsMaterialService.updateTotalNum((- param.getTotalNum()),byMaterialNameAndMaterialModel.getId());
        }
        //审核不通过返回库存
        if(param.getStatus().equals(SendGoodsOrderStatus.EXAMINE_FAIL.status)){
            WSMaterial byMaterialNameAndMaterialModel=wsMaterialService.findByMaterialNameAndMaterialModel(param.getProductionName(), param.getProductionType());
            if(Objects.isNull(byMaterialNameAndMaterialModel)){
                throw new BizException("物料{}/{}不存在，请联系管理员",param.getProductionName(),param.getProductionType());
            }
            wsMaterialService.updateTotalNum(param.getTotalNum(),byMaterialNameAndMaterialModel.getId());
        }
        
        if(param.getStatus().equals(SendGoodsOrderStatus.EXAMINE_SUCCESS.status)){
            param.setStatus(SendGoodsOrderStatus.FINISHED.status);
            //更新销售订单为发货状态
            if(StringUtil.isNotEmpty(param.getSaleOrderNo())){
                saleOrderService.updateStatusByOrderNo(SaleOrderStatus.DELIVER_GOODS.status,param.getSaleOrderNo());
            }
        }
        SendGoodsOrder update=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(), update);
    }
    
    @PostMapping("/delete/{id}")
    @LogOperation("删除发货单状态信息")
    public CommonResponse delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<SendGoodsOrder> request) {
        SendGoodsOrder param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SendGoodsOrder> all=service.findAll((Specification<SendGoodsOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if(Objects.nonNull(param.getFrom())){
                if(param.getFrom().equals(Departments.PZJY.desc)){
                    predicates.add(criteriaBuilder.ge(root.get("status"),SendGoodsOrderStatus.NEW.status));
                }
            }
            //运单号
            if (StringUtil.isNotEmpty(param.getExpressNo())) {
                predicates.add(criteriaBuilder.like(root.get("expressNo"), "%" + param.getExpressNo() + "%"));
            }
            //产品名称
            if (StringUtil.isNotEmpty(param.getProductionName())) {
                predicates.add(criteriaBuilder.like(root.get("productionName"), "%" + param.getProductionName() + "%"));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    @LogOperation("更改发货单状态信息")
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResponse updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        service.updateStatus(id, status);
        return CommonResponse.build(Result.success());
    }
}
