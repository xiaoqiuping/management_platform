package com.liubity.platform_starter.model.financial_management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 发票管理
 * @Author Liubity
 * @Date 2020-12-15
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_fm_invoice")
public class Invoice extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //发票名称
    @Column(name = "invoice_name",nullable=false)
    @JsonProperty("invoice_name")
    private String invoiceName;
    
    //开票日期
    @Column(name = "invoice_date")
    @JsonProperty("invoice_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    
    //开票金额
    @Column(name = "total_amount")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    
    //上传人
    @Column(name = "operator")
    @JsonProperty("operator")
    private String operator;
    
    //上传时间
    @Column(name = "upload_time")
    @JsonProperty("upload_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
    
    @Transient
    @JsonProperty("upload_start_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadStartTime;
    @Transient
    @JsonProperty("upload_end_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadEndTime;
    
    //备注
    @Column(name="remarks",length=500)
    @JsonProperty("remarks")
    private String remarks;
    
    @JsonProperty("invoice_file_list")
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "t_fm_ref_i_if", joinColumns = {@JoinColumn(name = "invoice_id", referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name = "if_id", referencedColumnName = "id")})
    List<InvoiceFile> invoiceFilesList;
    
    
}
