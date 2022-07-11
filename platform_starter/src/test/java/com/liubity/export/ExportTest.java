package com.liubity.export;

import com.liubity.platform_starter.model.sale.SaleOffPrice;
import com.liubity.platform_starter.model.sale.SaleOrder;
import com.liubity.platform_starter.model.warehouse_storage.WSMaterial;
import com.liubity.platform_starter.service.sale.SaleOffPriceService;
import com.liubity.platform_starter.service.sale.SaleOrderService;
import com.liubity.platform_starter.service.warehouse_storage.WSMaterialService;
import com.liubity.platform_starter.utils.DateUtil;
import com.liubity.platform_starter.utils.ExcelUtil;
import com.liubity.platform_starter.utils.ParseExcel;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @Author Liubity
 * @Date 2020-12-13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@SpringBootConfiguration
@Log4j2
@ComponentScan("com.liubity")
public class ExportTest {
    
    @Autowired
    private SaleOffPriceService saleOffPriceService;
    
    @Test
    public void test() throws IOException {
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet("消息中心");
        List<SaleOffPrice> list=saleOffPriceService.findAll();
        if (list.size() > 0 && list.size() <= 10000) {
            //设置要导出的文件的名字
            String fileName="消息中心";
            //新增数据行，并且设置单元格数据
            int rowNum=1;
            //headers表示excel表中第一行的表头
            String[] headers={"卖家", "商品名称", "价格"};
            // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
            for (int i=0; i <= headers.length; i++) {
                sheet.setColumnWidth((short) i, (short) (35.7 * 200));
            }
            HSSFRow row=sheet.createRow(0);
            //在excel表中添加表头
            for (int i=0; i < headers.length; i++) {
                HSSFCell cell=row.createCell(i);
                HSSFRichTextString text=new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            for (SaleOffPrice saleOffPrice : list) {
                HSSFRow row1=sheet.createRow(rowNum);
                row1.createCell(0).setCellValue(saleOffPrice.getBuyer());
                row1.createCell(1).setCellValue(saleOffPrice.getGoodsName());
                row1.createCell(2).setCellValue(saleOffPrice.getPrice().toString());
                rowNum++;
            }
            FileOutputStream out=new FileOutputStream("W:\\workspace\\idea\\management_platform\\platform_starter\\src\\test\\java\\com\\liubity\\export\\test2.xls");
            workbook.write(out);
            out.close();
            //下面三行是关键代码，处理乱码问题
//            response.setContentType("application/x-download");
//            response.setCharacterEncoding("utf-8");
//            //设置浏览器响应头对应的Content-disposition
//            response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1")+".xls");
//            response.flushBuffer();
//            workbook.write(response.getOutputStream());
        }
    }
    
    
    //获得Excel对象
    @Test
    public void testExcel1() {
        try {
            //获取系统文档
            POIFSFileSystem fspoi=new POIFSFileSystem(new FileInputStream("W:\\workspace\\idea\\management_platform\\platform_starter\\src\\test\\java\\com\\liubity\\export\\test.xls"));
            //创建工作薄对象
            HSSFWorkbook workbook=new HSSFWorkbook(fspoi);
            //创建工作表对象
            HSSFSheet sheet=workbook.getSheet("sheet1");
            //得到Excel表格
            HSSFRow row=sheet.getRow(1);
            //得到Excel工作表指定行的单元格
            HSSFCell cell=row.getCell(1);
            System.out.println(cell);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //创建Excel对象
    @Test
    public void testExcel2() throws IOException {
        //创建工作薄对象
        HSSFWorkbook workbook=new HSSFWorkbook();//这里也可以设置sheet的Name
        //创建工作表对象
        HSSFSheet sheet=workbook.createSheet();
        //创建工作表的行
        HSSFRow row=sheet.createRow(0);//设置第一行，从零开始
        row.createCell(2).setCellValue("aaaaaaaaaaaa");//第一行第三列为aaaaaaaaaaaa
        row.createCell(0).setCellValue(new Date());//第一行第一列为日期
        workbook.setSheetName(0, "sheet的Name");//设置sheet的Name
        
        //文档输出
        FileOutputStream out=new FileOutputStream("W:\\workspace\\idea\\management_platform\\platform_starter\\src\\test\\java\\com\\liubity\\export\\test1.xls");
        workbook.write(out);
        out.close();
    }
    
    @Autowired
    private SaleOrderService saleOrderService;
    
    @Test
    public void testExcel3() throws IOException {
        SaleOrder param=new SaleOrder();
        List<SaleOrder> all=saleOrderService.findAll((Specification<SaleOrder>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (Objects.nonNull(param.getStatusList()) && param.getStatusList().size() > 0) {
                CriteriaBuilder.In<Integer> in=criteriaBuilder.in(root.get("status"));
                param.getStatusList().forEach(in::value);
                predicates.add(in);
            }
            //销售时间
            if (Objects.nonNull(param.getSaleStartDate()) && Objects.nonNull(param.getSaleEndDate())) {
                predicates.add(criteriaBuilder.between(root.get("saleDate"), param.getSaleStartDate(), param.getSaleEndDate()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        String[] title={"手表名称", "手表型号", "销售数量", "成交价格", "购买商", "销售人", "销售日期"};
        String format=DateUtil.getNowLocalTime_full();
        String fileName="销售订单表" + format + ".xls";
        //sheet名
        String sheetName="销售订单表";
        //表内容
        String[][] count=new String[all.size()][title.length];
        //循环得到表里面的内容
        for (int i=0; i < all.size(); i++) {
            count[i]=new String[title.length];
            SaleOrder order=all.get(i);
            count[i][0]=order.getWatchName();
            count[i][1]=order.getWatchType();
            count[i][2]=order.getSaleNum().toString();
            count[i][3]=order.getTranPrice().toString();
            count[i][4]=order.getBuyer();
            count[i][5]=order.getSalesman();
            count[i][6]=order.getSaleDate().toString();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb=ExcelUtil.getHSSFWorkbook(sheetName, title, count, null);
        FileOutputStream out=new FileOutputStream("W:\\workspace\\idea\\management_platform\\platform_starter\\src\\test\\java\\com\\liubity\\export\\test5.xls");
        wb.write(out);
        out.close();
    }
    
    
    @Autowired
    private WSMaterialService service;
    @Test
    public void insertData() throws Exception {
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
            for (int j=3; j <= lastRowNum; j++) {
                List<String> data=ParseExcel.getRowFromExcel(workbook,i,j);
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
                    case "成品壳":
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
                wsMaterial.setTuHao(data.get(4));
                service.saveAndFlush(wsMaterial);
            }
        }
    }
}
