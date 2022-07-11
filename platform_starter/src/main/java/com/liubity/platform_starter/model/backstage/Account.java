package com.liubity.platform_starter.model.backstage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author Liubity
 * @Date 2020-11-12
 */


@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain=true)
@Entity
@Table(name="t_backstage_account")
public class Account extends BaseObject {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 账号
     */
    @Column(name="account", nullable=false,length=50)
    @JsonProperty("account")
    private String account;
    
    
    /**
     * 密码
     */
    @Column(name="password", nullable=false,length=50)
    @JsonProperty("password")
    private String password;
    
    
    /**
     * 工号
     */
    @Column(name="job_number")
    @JsonProperty("job_number")
    private String jobNumber;
    
    /**
     * 姓名
     */
    @Column(name="name")
    @JsonProperty("name")
    private String name;
    
    /**
     * 电话
     */
    @Column(name="phone")
    @JsonProperty("phone")
    private String phone;
    
    /**
     * 性别 1：男 0：女 -1：未知
     */
    @Column(name="sex",length=1)
    @JsonProperty("sex")
    private Integer sex;
    
    /**
     * 年龄
     */
    @Column(name="age",length=3)
    @JsonProperty("age")
    private Integer age;
    
    /**
     * 生日
     */
    @Column(name="birthday")
    @JsonProperty("birthday")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;
    
    /**
     * 入职日期
     */
    @Column(name="entry_date")
    @JsonProperty("entry_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date entryDate;
    
    /**
     * 离职日期
     */
    @Column(name="quit_date")
    @JsonProperty("quit_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date quitDate;
    
    /**
     * 部门
     */
    @Column(name="department")
    @JsonProperty("department")
    private String department;
    
    /**
     * 职务
     */
    @Basic
    @Column(name="duty")
    @JsonProperty("duty")
    private String duty;
    
    /**
     * 备注
     */
    @Column(name="remarks",length=2000)
    @JsonProperty("remarks")
    private String remarks;
    
    
    /**
     * 逻辑删除 0 正常， 1 删除
     */
    @Column(name="delete_flag",length=1)
    @JsonProperty("delete_flag")
    private Integer deleteFlag = 0;
}
