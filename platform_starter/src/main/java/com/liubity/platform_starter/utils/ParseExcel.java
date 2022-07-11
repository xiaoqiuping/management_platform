package com.liubity.platform_starter.utils;

import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Liubity
 * @Date 2021-01-26
 */
@Log4j2
public class ParseExcel {
    
    public static void main(String[] args) throws Exception {
        String xlsPath="W:\\workspace\\idea\\management_platform\\platform_starter\\src\\test\\java\\jsmeterail.xlsx";
        Workbook workbook=WorkbookFactory.create(new FileInputStream(xlsPath));
        int numberOfSheets=workbook.getNumberOfSheets();
        log.info("总共{}个sheet", numberOfSheets);
        for (int i=0; i < numberOfSheets; i++) {
            Sheet sheet=workbook.getSheetAt(i);
            String sheetName=sheet.getSheetName();
            log.info("第{}个sheet名称为{}", (i + 1), sheetName);
            int lastRowNum=sheet.getLastRowNum();
            log.info("【{}】共有{}条数据",sheetName,lastRowNum-2);
            insertData(3,lastRowNum,workbook,i,sheetName);
        }
    }
    
    private static void insertData(int startLine,int endLine,Workbook workbook,int sheet,String sheetName) throws Exception {
        for (int i=startLine; i <= endLine; i++) {
            List<String> data=getRowFromExcel(workbook, sheet, i);
            WSMaterial wsMaterial = new WSMaterial();
            switch (sheetName){
                case "成品零件":
                    wsMaterial.setMaterialType(3);
                    wsMaterial.setChildrenType(1);
                    break;
                case "成品机芯":
                    wsMaterial.setMaterialType(3);
                    wsMaterial.setChildrenType(2);
                    break;
                case "产品壳":
                    wsMaterial.setMaterialType(3);
                    wsMaterial.setChildrenType(3);
                    break;
                case "半成品零件":
                    wsMaterial.setMaterialType(2);
                    wsMaterial.setChildrenType(1);
                    break;
                case "原材料":
                    wsMaterial.setMaterialType(1);
                    wsMaterial.setChildrenType(null);
                    break;
                default:break;
            }
            wsMaterial.setMaterialName(data.get(5));
            wsMaterial.setMaterialModel(data.get(3));
            wsMaterial.setTuHao(data.get(3));
        }
    }
    
    
    
    
    
    public static String getCellFromExcel(Workbook workbook, int sheet, String row, String col) throws Exception {
        return getCellFromExcel(workbook, sheet, Integer.parseInt(row), Integer.parseInt(col));
    }
    
    public static List<String> getRowFromExcel(Workbook workbook, int sheet, String row) throws Exception {
        return getRowFromExcel(workbook, sheet, Integer.parseInt(row));
    }
    
    public static List<String> getRowFromExcel(Workbook workbook, int sheet, int row) throws Exception {
        Sheet sheet1=workbook.getSheetAt(sheet);
        Row row1=sheet1.getRow(row);
        List<String> list=new ArrayList<>();
        int rowNum=row1.getLastCellNum();
        for (int i=0; i < rowNum; i++) {
            list.add(getCellValueByCell(row1.getCell(i)));
        }
        log.info("sheet：{}，row：{}，取值：{}", sheet, row, list.toString());
        return list;
    }
    
    
    public static String getCellFromExcel(Workbook workbook, int sheet, int row, int col) throws Exception {
        Sheet sheet1=workbook.getSheetAt(sheet);
        Row row1=sheet1.getRow(row);
        String cell=getCellValueByCell(row1.getCell(col));
        log.info("sheet：{}，row：{}，col：{}，取值：{}", sheet, row, col, cell);
        return cell;
    }
    
    //获取单元格各类型值，返回字符串类型
    private static String getCellValueByCell(Cell cell) {
        //判断是否为null或空串
        if (cell == null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue="";
        int cellType=cell.getCellType();
        // 以下是判断数据的类型
        switch (cellType) {
            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                if (0 == cell.getCellType()) {//判断单元格的类型是否则NUMERIC类型
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {// 判断是否为日期类型
                        Date date=cell.getDateCellValue();
                        DateFormat formater=new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm");
                        cellValue=formater.format(date);
                    } else {
                        cellValue=cell.getNumericCellValue() + "";
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_STRING: // 字符串
                cellValue=cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue=cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                cellValue=cell.getCellFormula() + "";
                break;
            case HSSFCell.CELL_TYPE_BLANK: // 空值
                cellValue="";
                break;
            case HSSFCell.CELL_TYPE_ERROR: // 故障
                cellValue="非法字符";
                break;
            default:
                cellValue="未知类型";
                break;
        }
        return cellValue;
    }
}
