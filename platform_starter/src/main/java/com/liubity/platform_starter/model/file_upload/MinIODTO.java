package com.liubity.platform_starter.model.file_upload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Liubity
 * @Date 2020-11-28
 */
@Data
@Accessors(chain=true)
public class MinIODTO {
    
    /**
     * 文件名
     */
    @JsonProperty("name")
    private String name;
    
    /**
     * 实际存储名
     */
    @JsonProperty("file_name")
    private String fileName;
    
    /**
     * 访问路径
     */
    @JsonProperty("url")
    private String url;
    
    /**
     * 文件大小
     */
    @JsonProperty("size")
    private Long size;
    
    /**
     * 文件类型
     */
    @JsonProperty("type")
    private String type;
    
}
