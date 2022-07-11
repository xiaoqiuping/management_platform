package com.liubity.platform_starter.model.backstage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_backstage_log")
public class LogManagement extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    /**
     * 操作人
     */
    @Column(name = "account", nullable = false, length = 50)
    @JsonProperty("account")
    private String account;
    
    
    /**
     * 操作动作
     */
    @Column(name = "log_operation", nullable = false, length = 50)
    @JsonProperty("log_operation")
    private String logOperation;
    
    /**
     * 操作时间
     */
    @Column(name = "operation_time", nullable = false, length = 50)
    @JsonProperty("operation_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;
    
    /**
     * 操作开始时间
     */
    @Transient
    @JsonProperty("operation_start_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationStartTime;
    
    /**
     * 操作结束时间
     */
    @Transient
    @JsonProperty("operation_end_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationEndTime;
}
