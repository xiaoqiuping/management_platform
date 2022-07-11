package com.liubity.platform_starter.model.backstage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liubity.platform_starter.model.common.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 工艺流程
 * @Author Liubity
 * @Date 2020-11-29
 */
@EqualsAndHashCode(callSuper=true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_backstage_process")
public class Process extends BaseObject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 工序名称
     */
    @Column(name = "process_name", nullable = false, length = 50)
    @JsonProperty("process_name")
    private String processName;
    
    /**
     * 备注
     */
    @Column(name = "remarks")
    @JsonProperty("remarks")
    private String remarks;
}
