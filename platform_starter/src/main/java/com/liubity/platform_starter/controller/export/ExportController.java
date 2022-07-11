package com.liubity.platform_starter.controller.export;

import com.liubity.platform_starter.controller.after_sale_repairing.RepairOrderController;
import com.liubity.platform_starter.controller.production_plan.OutSourceOrderController;
import com.liubity.platform_starter.controller.raw_materials_purchase.PurchaseOrderController;
import com.liubity.platform_starter.controller.sale.SaleOrderController;
import com.liubity.platform_starter.controller.warehouse_storage.ExpendRecordController;
import com.liubity.platform_starter.controller.warehouse_storage.MaterialController;
import com.liubity.platform_starter.controller.warehouse_storage.ProductionOrderController;
import com.liubity.platform_starter.controller.warehouse_storage.WarehouseOrderController;
import com.liubity.platform_starter.enums.OutSourceOrderStatus;
import com.liubity.platform_starter.enums.PurchaseOrderStatus;
import com.liubity.platform_starter.enums.SCOrderStatus;
import com.liubity.platform_starter.enums.SaleOrderStatus;
import com.liubity.platform_starter.model.after_sale_repairing.RepairOrder;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.dto.ExpendRecordDto;
import com.liubity.platform_starter.model.production_plan.OutSourceOrder;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseOrder;
import com.liubity.platform_starter.model.raw_materials_purchase.PurchaseRawMaterial;
import com.liubity.platform_starter.model.sale.SaleOrder;
import com.liubity.platform_starter.model.warehouse_storage.*;
import com.liubity.platform_starter.service.after_sale_repairing.RepairOrderService;
import com.liubity.platform_starter.service.production_plan.OutSourceOrderService;
import com.liubity.platform_starter.service.raw_materials_purchase.PurchaseOrderService;
import com.liubity.platform_starter.service.sale.SaleOrderService;
import com.liubity.platform_starter.service.warehouse_storage.ExpendRecordService;
import com.liubity.platform_starter.service.warehouse_storage.ProductionOrderService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.service.warehouse_storage.WarehouseOrderService;
import com.liubity.platform_starter.utils.DateUtil;
import com.liubity.platform_starter.utils.ExcelUtil;
import com.liubity.platform_starter.utils.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Liubity
 * @Date 2020-12-13
 */
@Log4j2
@Controller
@RequestMapping("/exportXml")
public class ExportController {
    
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private ProductionOrderService productionOrderService;
    @Autowired
    private OutSourceOrderService outSourceOrderService;
    @Autowired
    private WarehouseOrderService warehouseOrderService;
    @Autowired
    private RepairOrderService repairOrderService;
    @Autowired
    private ExpendRecordService expendRecordService;
    @Autowired
    private WSMaterialService wsMaterialService;
    
    
    
    /**
     * 销售订单导出
     *
     * @param request
     * @param response
     */
    @PostMapping("/saleOrder")
    public void exportSaleOrder(@RequestBody CommonRequest<SaleOrder> request, HttpServletResponse response) {
        List<SaleOrder> all=saleOrderService.findAll(SaleOrderController.filter(request.getParam()));
        String[] title={"销售订单号", "手表名称", "手表型号", "销售数量", "成交价格", "购买商", "销售人", "销售日期", "销售状态"};
        String format=DateUtil.getNowDate_CN();
        String fileName="销售订单表_" + format + ".xls";
        //sheet名
        String sheetName="销售订单表";
        //表内容
        String[][] count=new String[all.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < all.size(); i++) {
            count[i]=new String[title.length];
            SaleOrder order=all.get(i);
            int k=0;
            count[i][k++]=order.getOrderNo();
            count[i][k++]=order.getWatchName();
            count[i][k++]=order.getWatchType();
            count[i][k++]=Objects.nonNull(order.getSaleNum()) ? order.getSaleNum().toString() : "";
            count[i][k++]=Objects.nonNull(order.getTranPrice()) ? order.getTranPrice().toString() : "";
            count[i][k++]=order.getBuyer();
            count[i][k++]=order.getSalesman();
            count[i][k++]=Objects.nonNull(order.getSaleDate()) ? DateUtil.dateToStr(order.getSaleDate()) : "";
            count[i][k]=SaleOrderStatus.getSaleOrderDesc(order.getStatus());
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=ExcelUtil.getHSSFWorkbook(sheetName, title, count, null);
        try {
            OutputStream output=response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
    
    /**
     * 生产订单导出
     *
     * @param request
     * @param response
     */
    @PostMapping("/productionOrder")
    public void exportProductionOrder(@RequestBody CommonRequest<ProductionOrder> request, HttpServletResponse response) {
        
        List<ProductionOrder> all=productionOrderService.findAll(ProductionOrderController.filter(request.getParam()));
        
        String[] title={"生产订单号", "产品名称", "产品型号", "类型", "生产总数量", "单价", "总金额", "订单周期", "完成日期", "发货形式",
                "订单要求", "订单状态", "工序流程", "订单生成时间", "销售订单号", "备注", "审核状态", "审核人", "审核意见", "质检人", "质检意见"};
        String format=DateUtil.getNowDate_CN();
        String fileName="生产订单表_" + format + ".xls";
        //sheet名
        String sheetName="生产订单表";
        //表内容
        String[][] count=new String[all.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < all.size(); i++) {
            count[i]=new String[title.length];
            ProductionOrder order=all.get(i);
            int k=0;
            count[i][k++]=order.getOrderNo();
            count[i][k++]=order.getProductionName();
            count[i][k++]=order.getProductionType();
            count[i][k++]=order.getType().equals(1) ? "成品" : "零件";
            count[i][k++]=Objects.nonNull(order.getTotalNum()) ? order.getTotalNum().toString() : "";
            count[i][k++]=Objects.nonNull(order.getPrice()) ? order.getPrice().toString() : "";
            count[i][k++]=Objects.nonNull(order.getTotalPrice()) ? order.getTotalPrice().toString() : "";
            count[i][k++]=Objects.nonNull(order.getOrderCycle()) ? order.getOrderCycle().toString() : "";
            count[i][k++]=Objects.nonNull(order.getProductionFinishedDate()) ? DateUtil.dateToStr(order.getProductionFinishedDate()) : "";
            count[i][k++]=order.getSendGoodsType();
            count[i][k++]=order.getOrderRequirement();
            count[i][k++]=SCOrderStatus.getSCOrderDesc(order.getStatus());
            
            StringJoiner sj=new StringJoiner(",");
            List<ProductProcess> processList=order.getProcessList();
            processList.stream().map(ProductProcess::getProcessName).collect(Collectors.toList()).forEach(sj::add);
            count[i][k++]=sj.toString();
            
            count[i][k++]=Objects.nonNull(order.getOrderCreateTime()) ? DateUtil.dateTimeToStr(order.getOrderCreateTime()) : "";
            count[i][k++]=order.getSaleOrderNo();
            count[i][k++]=order.getRemarks();
            count[i][k++]=SCOrderStatus.getSCOrderDesc(order.getVerifyStatus());
            count[i][k++]=order.getVerifyMan();
            count[i][k++]=order.getVerifyRemarks();
            count[i][k++]=order.getExamineMan();
            count[i][k]=order.getExamineRemarks();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=ExcelUtil.getHSSFWorkbook(sheetName, title, count, null);
        try {
            OutputStream output=response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
    
    
    /**
     * 原料采购单导出
     *
     * @param request
     * @param response
     */
    @PostMapping("/purchaseOrder")
    public void exportPurchaseOrder(@RequestBody CommonRequest<PurchaseOrder> request, HttpServletResponse response) {
        List<PurchaseOrder> all=purchaseOrderService.findAll(PurchaseOrderController.filter(request.getParam()));
        String[] title={"采购单单号", "创建人", "采购人", "采购时间", "订单状态", "备注", "原料详情"};
        String format=DateUtil.getNowDate_CN();
        String fileName="原料采购单表_" + format + ".xls";
        //sheet名
        String sheetName="原料采购单表";
        //表内容
        String[][] count=new String[all.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < all.size(); i++) {
            count[i]=new String[title.length];
            PurchaseOrder order=all.get(i);
            int k=0;
            count[i][k++]=order.getOrderNo();
            count[i][k++]=order.getDutyMan();
            count[i][k++]=order.getPurchaseMan();
            count[i][k++]=Objects.nonNull(order.getPurchaseDate()) ? DateUtil.dateToStr(order.getPurchaseDate()) : "";
            count[i][k++]=PurchaseOrderStatus.getPurchaseOrderDesc(order.getStatus());
            count[i][k++]=order.getRemarks();
            List<PurchaseRawMaterial> rawMaterialList=order.getRawMaterialList();
            List<String> collect=rawMaterialList.stream().map(m -> "原料名称：" + m.getRawMaterialName() + ",价格：" + m.getPrice() + ",总数量：" + m.getTotalNum()).collect(Collectors.toList());
            StringJoiner sj=new StringJoiner("  ");
            collect.forEach(sj::add);
            count[i][k]=sj.toString();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=ExcelUtil.getHSSFWorkbook(sheetName, title, count, null);
        try {
            OutputStream output=response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
    
    
    /**
     * 委外订单导出
     *
     * @param request
     * @param response
     */
    @PostMapping("/outSourceOrder")
    public void exportOutSourceOrder(@RequestBody CommonRequest<OutSourceOrder> request, HttpServletResponse response) {
        List<OutSourceOrder> all=outSourceOrderService.findAll(OutSourceOrderController.filter(request.getParam()));
        String[] title={"委外订单号", "产品名称", "产品数量", "支付费用", "完工日期", "委外厂家", "委外厂家联系人", "联系人电话", "负责人", "订单状态", "备注"};
        String format=DateUtil.getNowDate_CN();
        String fileName="委外订单表_" + format + ".xls";
        //sheet名
        String sheetName="委外订单表";
        //表内容
        String[][] count=new String[all.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < all.size(); i++) {
            count[i]=new String[title.length];
            OutSourceOrder order=all.get(i);
            int k=0;
            count[i][k++]=order.getOrderNo();
            count[i][k++]=order.getProductionName();
            count[i][k++]=Objects.nonNull(order.getProductionNum()) ? order.getProductionNum().toString() : "";
            count[i][k++]=order.getCost().toString();
            count[i][k++]=Objects.nonNull(order.getFinishedDate()) ? DateUtil.dateToStr(order.getFinishedDate()) : "";
            count[i][k++]=order.getOutSourceUnit();
            count[i][k++]=order.getContact();
            count[i][k++]=order.getPhone();
            count[i][k++]=order.getDutyMan();
            count[i][k++]=OutSourceOrderStatus.getOutSourceOrderDesc(order.getStatus());
            count[i][k]=order.getRemarks();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=ExcelUtil.getHSSFWorkbook(sheetName, title, count, null);
        try {
            OutputStream output=response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
    
    /**
     * 采购入库单导出
     *
     * @param request
     * @param response
     */
    @PostMapping("/warehouseOrder")
    public void exportWarehouseOrder(@RequestBody CommonRequest<WarehouseOrder> request, HttpServletResponse response) {
        List<WarehouseOrder> all=warehouseOrderService.findAll(WarehouseOrderController.filter(request.getParam()));
        String[] title={"入库单单号", "采购名称", "采购单位", "联系人", "联系人电话", "采购日期", "采购价格", "采购总价", "备注", "采购单单号"};
        String format=DateUtil.getNowDate_CN();
        String fileName="采购入库单表_" + format + ".xls";
        //sheet名
        String sheetName="采购入库单表";
        //表内容
        String[][] count=new String[all.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < all.size(); i++) {
            count[i]=new String[title.length];
            WarehouseOrder order=all.get(i);
            int k=0;
            count[i][k++]=order.getOrderNo();
            count[i][k++]=order.getPurchasingName();
            count[i][k++]=order.getPurchasingUnit();
            count[i][k++]=order.getContact();
            count[i][k++]=order.getPhone();
            count[i][k++]=Objects.nonNull(order.getPurchaseDate()) ? DateUtil.dateToStr(order.getPurchaseDate()) : "";
            count[i][k++]=order.getPurchasePrice().toString();
            count[i][k++]=Objects.nonNull(order.getPurchaseTotalPrice()) ? order.getPurchaseTotalPrice().toString() : "";
            count[i][k++]=order.getRemarks();
            count[i][k]=order.getCgOrderNo();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=ExcelUtil.getHSSFWorkbook(sheetName, title, count, null);
        try {
            OutputStream output=response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
    
    /**
     * 维修单导出
     *
     * @param request
     * @param response
     */
    @PostMapping("/repairOrder")
    public void exportRepairOrder(@RequestBody CommonRequest<RepairOrder> request, HttpServletResponse response) {
        List<RepairOrder> all=repairOrderService.findAll(RepairOrderController.filter(request.getParam()));
        String[] title={"维修单单号", "手表名称", "手表型号", "购买日期", "保修截止日期", "客户名称", "客户联系电话", "客户地址", "故障描述",
                "维修金额", "维修人", "维修日期", "维修分析", "送回日期", "备注"};
        String format=DateUtil.getNowDate_CN();
        String fileName="维修单表_" + format + ".xls";
        //sheet名
        String sheetName="维修单表";
        //表内容
        String[][] count=new String[all.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < all.size(); i++) {
            count[i]=new String[title.length];
            RepairOrder order=all.get(i);
            int k=0;
            count[i][k++]=order.getOrderNo();
            count[i][k++]=order.getWatchName();
            count[i][k++]=order.getWatchType();
            count[i][k++]=Objects.nonNull(order.getBuyDate()) ? DateUtil.dateToStr(order.getBuyDate()) : "";
            count[i][k++]=Objects.nonNull(order.getGuaranteeDate()) ? DateUtil.dateToStr(order.getGuaranteeDate()) : "";
            count[i][k++]=order.getBuyerName();
            count[i][k++]=order.getBuyerName();
            count[i][k++]=order.getBuyerAddress();
            count[i][k++]=order.getFaultDescription();
            count[i][k++]=Objects.nonNull(order.getRepairAmount()) ? order.getRepairAmount().toString() : "";
            count[i][k++]=order.getRepairMan();
            count[i][k++]=Objects.nonNull(order.getRepairDate()) ? DateUtil.dateToStr(order.getRepairDate()) : "";
            count[i][k++]=order.getRepairAnalysis();
            count[i][k++]=Objects.nonNull(order.getSendBackDate()) ? DateUtil.dateToStr(order.getSendBackDate()) : "";
            count[i][k]=order.getRemarks();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=ExcelUtil.getHSSFWorkbook(sheetName, title, count, null);
        try {
            OutputStream output=response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
    
    
    /**
     * 维修物料汇总信息
     *
     * @param request
     * @param response
     */
    @PostMapping("/materialSummary")
    public void exportMaterialSummary(@RequestBody CommonRequest<ExpendRecord> request, HttpServletResponse response) {
        ExpendRecord param=request.getParam();
        List<ExpendRecord> allExpendRecord=expendRecordService.findAll(ExpendRecordController.filter(param));
    
        WSMaterial wsMaterial = new WSMaterial();
        wsMaterial.setMaterialName(param.getMaterialName())
                .setMaterialModel(param.getMaterialModel())
                .setMaterialType(param.getMaterialType())
                .setChildrenType(param.getChildrenType())
                .setTuHao(param.getTuHao());
        List<WSMaterial> allMaterial=wsMaterialService.findAll(MaterialController.filter(wsMaterial));
    
        List<ExpendRecordDto> exportData = new ArrayList<>(allMaterial.size());
        
        allMaterial.forEach(m->{
            ExpendRecordDto dto = new ExpendRecordDto();
            dto.setMaterialName(m.getMaterialName());
            dto.setMaterialModle(m.getMaterialModel());
            dto.setTuHao(StringUtil.isEmpty(m.getTuHao())?"":m.getTuHao());
            double price=Objects.nonNull(m.getPrice()) ? m.getPrice().doubleValue() : 0;
            dto.setPrice(String.valueOf(price));
            dto.setTotalNum(m.getTotalNum());
            dto.setTotalPrice(m.getTotalNum()*price);
            List<ExpendRecord> collect=allExpendRecord.stream().filter(e -> e.getMaterialId().equals(m.getId())).collect(Collectors.toList());
            Integer assembleNum =0;
            Integer lossNum =0;
            Integer scrapNum =0;
            for(ExpendRecord e1:collect){
                assembleNum += e1.getAssembleNum();
                lossNum += e1.getLossNum();
                scrapNum += e1.getScrapNum();
            }
            dto.setAssembleNum(assembleNum);
            dto.setAssemblePrice(assembleNum*price);
            dto.setLossNum(lossNum);
            dto.setLossPrice(lossNum*price);
            dto.setScrapNum(scrapNum);
            dto.setScrapPrice(scrapNum*price);
            Integer expendNum = assembleNum+lossNum+scrapNum;
            dto.setExpendNum(expendNum);
            dto.setExpendPrice(expendNum*price);
            exportData.add(dto);
        });
    
        String[] title={"名称", "型号", "图号", "价格","数量", "金额", "装配", "金额", "耗损", "金额",
                "报废", "金额", "合计数量", "合计金额"};
        String format=DateUtil.getNowDate_CN();
        String fileName="零部件资产报表_" + format + ".xls";
        //sheet名
        String sheetName="零部件资产报表";
        //表内容
        String[][] count=new String[exportData.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < exportData.size(); i++) {
            count[i]=new String[title.length];
            ExpendRecordDto order=exportData.get(i);
            int k=0;
            count[i][k++]=order.getMaterialName();
            count[i][k++]=order.getMaterialModle();
            count[i][k++]=order.getTuHao();
            count[i][k++]=order.getPrice();
            count[i][k++]=String.valueOf(order.getTotalNum());
            count[i][k++]=String.valueOf(order.getTotalPrice());;
            count[i][k++]=String.valueOf(order.getAssembleNum());
            count[i][k++]=String.valueOf(order.getAssemblePrice());
            count[i][k++]=String.valueOf(order.getLossNum());
            count[i][k++]=String.valueOf(order.getLossPrice());
            count[i][k++]=String.valueOf(order.getScrapNum());
            count[i][k++]=String.valueOf(order.getScrapPrice());
            count[i][k++]=String.valueOf(order.getExpendNum());
            count[i][k]=String.valueOf(order.getExpendPrice());
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=new HSSFWorkbook();
        //2.在wb中添加一个sheet
        HSSFSheet sheet=wb.createSheet();
        wb.setSheetName(0, sheetName);
    
        //居中样式
        HSSFCellStyle centerStyle=wb.createCellStyle();
        centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        
        HSSFRow row1=sheet.createRow(0);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 13);
        sheet.addMergedRegion(region);
        HSSFCell cell1=row1.createCell(0);
        cell1.setCellStyle(centerStyle);
        cell1.setCellValue(HSSFCell.ENCODING_UTF_16);
        cell1.setCellValue("零件资产报表");
    
        HSSFRow row2=sheet.createRow(1);
        CellRangeAddress region2_1 = new CellRangeAddress(1, 1, 0, 3);
        sheet.addMergedRegion(region2_1);
        HSSFCell cell2=row2.createCell(0);
        cell2.setCellStyle(centerStyle);
        cell2.setCellValue(HSSFCell.ENCODING_UTF_16);
        cell2.setCellValue("");
    
        CellRangeAddress region2_2 = new CellRangeAddress(1, 1, 4, 5);
        sheet.addMergedRegion(region2_2);
        HSSFCell cell22=row2.createCell(4);
        cell22.setCellStyle(centerStyle);
        cell22.setCellValue(HSSFCell.ENCODING_UTF_16);
        cell22.setCellValue("库存剩余");
    
        CellRangeAddress region2_3= new CellRangeAddress(1, 1, 6, 11);
        sheet.addMergedRegion(region2_3);
        HSSFCell cell23=row2.createCell(6);
        cell23.setCellStyle(centerStyle);
        cell23.setCellValue(HSSFCell.ENCODING_UTF_16);
        cell23.setCellValue("付出详情");
    
        CellRangeAddress region2_4= new CellRangeAddress(1, 1, 12, 13);
        sheet.addMergedRegion(region2_4);
        HSSFCell cell24=row2.createCell(12);
        cell24.setCellStyle(centerStyle);
        cell24.setCellValue(HSSFCell.ENCODING_UTF_16);
        cell24.setCellValue("付出汇总");
        
        //3.在sheet里面添加第1行
        HSSFRow row=sheet.createRow(2);
        HSSFCell cell;
        for (int i=0; i < title.length; i++) {
            cell=row.createCell(i);
            cell.setCellStyle(centerStyle);
            cell.setCellValue(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(title[i]);
        }
        //5.创建内容
        for (int i=0; i < count.length; i++) {
            sheet.setDefaultRowHeight((short) 32.7);
            row=sheet.createRow(i + 3);
            for (int j=0; j < count[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(count[i][j]);
            }
        }
        try {
            OutputStream output=response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
    
}
