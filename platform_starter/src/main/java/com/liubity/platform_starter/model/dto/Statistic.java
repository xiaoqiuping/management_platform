package com.liubity.platform_starter.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.sale.Receivable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NamedNativeQuery;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.SqlResultSetMapping;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Liubity
 * @Date 2020-12-20
 */

@Data
@Accessors(chain=true)
public class Statistic{
    
    /**
     * 日期
     */
    @JsonProperty("date")
    private String date;
    
    /**
     * 1:销售，2，售后
     */
    @JsonProperty("type")
    private Integer type;
    
    
    /**
     * 总金额
     */
    @JsonProperty("total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @JsonProperty("payable_total_amount")
    private BigDecimal payableTotalAmount = BigDecimal.ZERO;
    
    @JsonProperty("receivable_total_amount")
    private BigDecimal receivableTotalAmount = BigDecimal.ZERO;
    
}
