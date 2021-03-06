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
        HSSFSheet sheet=workbook.createSheet("????????????");
        List<SaleOffPrice> list=saleOffPriceService.findAll();
        if (list.size() > 0 && list.size() <= 10000) {
            //?????????????????????????????????
            String fileName="????????????";
            //?????????????????????????????????????????????
            int rowNum=1;
            //headers??????excel????????????????????????
            String[] headers={"??????", "????????????", "??????"};
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????n????????????????????????
            for (int i=0; i <= headers.length; i++) {
                sheet.setColumnWidth((short) i, (short) (35.7 * 200));
            }
            HSSFRow row=sheet.createRow(0);
            //???excel??????????????????
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
            //????????????????????????????????????????????????
//            response.setContentType("application/x-download");
//            response.setCharacterEncoding("utf-8");
//            //?????????????????????????????????Content-disposition
//            response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1")+".xls");
//            response.flushBuffer();
//            workbook.write(response.getOutputStream());
        }
    }
    
    
    //??????Excel??????
    @Test
    public void testExcel1() {
        try {
            //??????????????????
            POIFSFileSystem fspoi=new POIFSFileSystem(new FileInputStream("W:\\workspace\\idea\\management_platform\\platform_starter\\src\\test\\java\\com\\liubity\\export\\test.xls"));
            //?????????????????????
            HSSFWorkbook workbook=new HSSFWorkbook(fspoi);
            //?????????????????????
            HSSFSheet sheet=workbook.getSheet("sheet1");
            //??????Excel??????
            HSSFRow row=sheet.getRow(1);
            //??????Excel??????????????????????????????
            HSSFCell cell=row.getCell(1);
            System.out.println(cell);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //??????Excel??????
    @Test
    public void testExcel2() throws IOException {
        //?????????????????????
        HSSFWorkbook workbook=new HSSFWorkbook();//?????????????????????sheet???Name
        //?????????????????????
        HSSFSheet sheet=workbook.createSheet();
        //?????????????????????
        HSSFRow row=sheet.createRow(0);//??????????????????????????????
        row.createCell(2).setCellValue("aaaaaaaaaaaa");//?????????????????????aaaaaaaaaaaa
        row.createCell(0).setCellValue(new Date());//???????????????????????????
        workbook.setSheetName(0, "sheet???Name");//??????sheet???Name
        
        //????????????
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
            //????????????
            if (Objects.nonNull(param.getSaleStartDate()) && Objects.nonNull(param.getSaleEndDate())) {
                predicates.add(criteriaBuilder.between(root.get("saleDate"), param.getSaleStartDate(), param.getSaleEndDate()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        String[] title={"????????????", "????????????", "????????????", "????????????", "?????????", "?????????", "????????????"};
        String format=DateUtil.getNowLocalTime_full();
        String fileName="???????????????" + format + ".xls";
        //sheet???
        String sheetName="???????????????";
        //?????????
        String[][] count=new String[all.size()][title.length];
        //??????????????????????????????
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
        //??????HSSFWorkbook
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
        log.info("??????{}???sheet", numberOfSheets);
        for (int i=0; i < numberOfSheets; i++) {
            Sheet sheet=workbook.getSheetAt(i);
            String sheetName=sheet.getSheetName();
            log.info("???{}???sheet?????????{}", (i + 1), sheetName);
            int lastRowNum=sheet.getLastRowNum();
            log.info("???{}?????????{}?????????",sheetName,lastRowNum-2);
            for (int j=3; j <= lastRowNum; j++) {
                List<String> data=ParseExcel.getRowFromExcel(workbook,i,j);
                WSMaterial wsMaterial = new WSMaterial();
                switch (sheetName){
                    case "????????????":
                        wsMaterial.setMaterialType(3);
                        wsMaterial.setChildrenType(1);
                        break;
                    case "????????????":
                        wsMaterial.setMaterialType(3);
                        wsMaterial.setChildrenType(2);
                        break;
                    case "?????????":
                        wsMaterial.setMaterialType(3);
                        wsMaterial.setChildrenType(3);
                        break;
                    case "???????????????":
                        wsMaterial.setMaterialType(2);
                        wsMaterial.setChildrenType(1);
                        break;
                    case "?????????":
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
