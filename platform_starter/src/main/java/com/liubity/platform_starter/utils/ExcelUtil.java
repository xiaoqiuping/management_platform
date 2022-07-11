package com.liubity.platform_starter.utils;

import org.apache.poi.hssf.usermodel.*;

import java.util.Arrays;

/**
 * @Author Liubity
 * @Date 2020-12-14
 */
public class ExcelUtil {
    
    /**
     * @param sheetName
     *         工作表名
     * @param title
     *         表头
     * @param values
     *         表内容
     * @param wb
     *         Excel表
     * @return HSSFWorkbook
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {
        //1.创建一个HSSFWorkbook,对应一个Excel文件
        if (wb == null) {
            wb=new HSSFWorkbook();
        }
        //2.在wb中添加一个sheet
        HSSFSheet sheet=wb.createSheet();
        wb.setSheetName(0, sheetName);
        //3.在sheet里面添加第0行
        HSSFRow row=sheet.createRow(0);
        //设置表头居中
        HSSFCellStyle cellStyle=wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //4.声明列对象
        HSSFCell cell;
        
        
        //添加表头标题
        for (int i=0; i < title.length; i++) {
            //第0行第i列
            cell=row.createCell(i);
            //列的样式
            cell.setCellStyle(cellStyle);
            //表中的编码
            cell.setCellValue(HSSFCell.ENCODING_UTF_16);
            //列中的数据
            cell.setCellValue(title[i]);
            
        }
        //5.创建内容
        for (int i=0; i < values.length; i++) {
            sheet.setDefaultRowHeight((short) 32.7);
            row=sheet.createRow(i + 1);
            for (int j=0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }
}
