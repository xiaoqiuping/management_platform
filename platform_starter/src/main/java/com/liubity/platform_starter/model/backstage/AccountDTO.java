package com.liubity.platform_starter.model.backstage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Liubity
 * @Date 2020-11-22
 */
@Data
public class AccountDTO {
    
    @JsonProperty("account")
    private String account;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("sex")
    private Integer sex;
    
    @JsonProperty("top_menus")
    List<Menu> topMenus;
    
    @JsonProperty("sider_menus")
    List<Menu> siderMenus;
    //Map<String,List<MenuDTO>> siderMenus;
}
