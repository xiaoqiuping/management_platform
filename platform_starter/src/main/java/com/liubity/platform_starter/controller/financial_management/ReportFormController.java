package com.liubity.platform_starter.controller.financial_management;

import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.dto.Statistic;
import com.liubity.platform_starter.service.raw_materials_purchase.PayableOrderService;
import com.liubity.platform_starter.service.sale.ReceivableService;
import com.liubity.platform_starter.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Liubity
 * @Date 2020-12-20
 */
@RestController
@RequestMapping("report_form")
public class ReportFormController {
    
    @Autowired
    private PayableOrderService payableOrderService;
    @Autowired
    private ReceivableService receivableService;
    
    
    @PostMapping("/statistic")
    public CommonResponse payableAndReceivableReport() {
        
        LocalDate today=LocalDate.now();
        String todayS=DateUtil.dateToStr(today);
        LocalDate yesterday=LocalDate.now().minusDays(1);
        String yesterdayS=DateUtil.dateToStr(yesterday);
        Date selectStart=DateUtil.localDate2Date(today.minusDays(6));
        Date selectEnd=DateUtil.localDate2Date(today.plusDays(1));
        List<Object[]> objects=receivableService.statisticByCreateTime(selectStart, selectEnd);
        List<Object[]> objects1=payableOrderService.statisticByCreateTime(selectStart, selectEnd);
        List<Statistic> receivableList=objects.stream().map(o -> {
            Statistic statistic=new Statistic();
            statistic.setDate((String) o[0])
                    .setTotalAmount((BigDecimal) o[1]);
            return statistic;
        }).collect(Collectors.toList());
        List<Statistic> payableList=objects1.stream().map(o -> {
            Statistic statistic=new Statistic();
            statistic.setDate((String) o[0])
                    .setTotalAmount((BigDecimal) o[1]);
            return statistic;
        }).collect(Collectors.toList());
        Optional<Statistic> rToday=receivableList.stream().filter(r -> r.getDate().equals(todayS)).findFirst();
        Optional<Statistic> pToday=payableList.stream().filter(r -> r.getDate().equals(todayS)).findFirst();
        Optional<Statistic> rYesterday=receivableList.stream().filter(r -> r.getDate().equals(yesterdayS)).findFirst();
        Optional<Statistic> pYesterday=payableList.stream().filter(r -> r.getDate().equals(yesterdayS)).findFirst();
        //????????????/??????
        BigDecimal rbt=rToday.orElseGet(Statistic::new).getTotalAmount();
        BigDecimal pbt=pToday.orElseGet(Statistic::new).getTotalAmount();
        //??????????????????
        BigDecimal rby=rYesterday.orElseGet(Statistic::new).getTotalAmount();
        BigDecimal pby=pYesterday.orElseGet(Statistic::new).getTotalAmount();
        //???7???????????????
        BigDecimal rba=BigDecimal.ZERO;
        for (Statistic statistic : receivableList) {
            rba=rba.add(statistic.getTotalAmount());
        }
        BigDecimal pba=BigDecimal.ZERO;
        for (Statistic statistic : payableList) {
            pba=pba.add(statistic.getTotalAmount());
        }
        HashMap<String, Object> res=new HashMap<>();
        res.put("pay_today", pbt);
        res.put("pay_yesterday", pby);
        res.put("pay_seven", pba);
        res.put("receivable_today", rbt);
        res.put("receivable_yesterday", rby);
        res.put("receivable_seven", rba);
        return CommonResponse.build(Result.success(), res);
    }
    
    
    @PostMapping("/statisticByDate")
    public CommonResponse payableAndReceivableReportByDate(@RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate) {
        
        LocalDate start=DateUtil.parseLocalDate(startDate, "yyyy-MM-dd");
        LocalDate end=DateUtil.parseLocalDate(endDate, "yyyy-MM-dd");
    
    
        Date selectStart=DateUtil.localDate2Date(start);
        Date selectEnd=DateUtil.localDate2Date(end.plusDays(1));
        List<Object[]> reAll=receivableService.statisticByCreateTimeGroupType(selectStart, selectEnd);
        List<Object[]> payAll=payableOrderService.statisticByCreateTimeGroupType(selectStart, selectEnd);
        List<Statistic> receivableAllList=reAll.stream().map(o -> {
            Statistic statistic=new Statistic();
            statistic.setType((Integer) o[0])
                    .setTotalAmount((BigDecimal) o[1]);
            return statistic;
        }).collect(Collectors.toList());
        List<Statistic> payableAllList=payAll.stream().map(o -> {
            Statistic statistic=new Statistic();
            statistic.setType((Integer) o[0])
                    .setTotalAmount((BigDecimal) o[1]);
            return statistic;
        }).collect(Collectors.toList());
        //1:?????????2?????????
        Optional<Statistic> rSale=receivableAllList.stream().filter(r -> r.getType().equals(1)).findFirst();
        BigDecimal rSaleAmount = rSale.orElseGet(Statistic::new).getTotalAmount();
        Optional<Statistic> rSaleAfter=receivableAllList.stream().filter(r -> r.getType().equals(2)).findFirst();
        BigDecimal rSaleAfterAmount = rSaleAfter.orElseGet(Statistic::new).getTotalAmount();
        //1??????????????????2???????????????
        Optional<Statistic> pRm=payableAllList.stream().filter(r -> r.getType().equals(1)).findFirst();
        BigDecimal pRmAmount = pRm.orElseGet(Statistic::new).getTotalAmount();
        Optional<Statistic> pZ=payableAllList.stream().filter(r -> r.getType().equals(2)).findFirst();
        BigDecimal pMmAmount = pZ.orElseGet(Statistic::new).getTotalAmount();
        
        HashMap<String, Object> res=new HashMap<>();
        res.put("receivable_total",rSaleAmount.add(rSaleAfterAmount));
        res.put("receivable_sale",rSaleAmount);
        res.put("receivable_sale_after",rSaleAfterAmount);
        res.put("payable_total",pRmAmount.add(pMmAmount));
        res.put("payable_raw_material",pRmAmount);
        res.put("payable_miscellaneous_material",pMmAmount);
    
    
        List<Object[]> reDate=receivableService.statisticByCreateTime(selectStart, selectEnd);
        List<Object[]> payDate=payableOrderService.statisticByCreateTime(selectStart, selectEnd);
        List<Statistic> reDateList=reDate.stream().map(o -> {
            Statistic statistic=new Statistic();
            statistic.setDate((String) o[0])
                    .setTotalAmount((BigDecimal) o[1]);
            return statistic;
        }).collect(Collectors.toList());
        List<Statistic> payDateList=payDate.stream().map(o -> {
            Statistic statistic=new Statistic();
            statistic.setDate((String) o[0])
                    .setTotalAmount((BigDecimal) o[1]);
            return statistic;
        }).collect(Collectors.toList());
    
        List<Statistic> list = new ArrayList<>();
        long between=ChronoUnit.DAYS.between(start, end)+1;
        for(int i=0;i<between;i++ ){
            LocalDate plus=start.plusDays(i);
            Statistic statistic = new Statistic();
            statistic.setDate(DateUtil.dateToStr(plus));
            list.add(statistic);
        }
    
        list.forEach(sta->{
            Optional<Statistic> r=reDateList.stream().filter(re -> re.getDate().equals(sta.getDate())).findFirst();
            r.ifPresent(statistic -> sta.setReceivableTotalAmount(statistic.getTotalAmount()));
            Optional<Statistic> p=payDateList.stream().filter(pay -> pay.getDate().equals(sta.getDate())).findFirst();
            p.ifPresent(statistic -> sta.setPayableTotalAmount(statistic.getTotalAmount()));
        });
        res.put("list",list);
        return CommonResponse.build(Result.success(), res);
    }
}
