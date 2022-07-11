package com.liubity.platform_starter.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author Liubity
 * @Date 2021-02-28
 */
@Data
public class ExpendRecordDto {
    private String materialName;
    private String materialModle;
    private String tuHao;
    private String price;
    private Integer totalNum;
    private Double totalPrice;
    private Integer assembleNum;
    private Double assemblePrice;
    private Integer lossNum;
    private Double lossPrice;
    private Integer scrapNum;
    private Double scrapPrice;
    private Integer expendNum;
    private Double expendPrice;
}
