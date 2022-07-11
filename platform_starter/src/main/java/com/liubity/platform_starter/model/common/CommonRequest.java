package com.liubity.platform_starter.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author Liubity
 * @Date 2020-11-12
 */
@Data
public class CommonRequest<T> {
    
    @JsonProperty("param")
    private T param;

    @JsonProperty("page")
    private PageHelper page;
}
