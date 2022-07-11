package com.liubity.platform_starter.model.backstage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-11-22
 */
@Getter
@Setter
public class MenuDTO extends Menu {
    boolean checked = false;
    List<MenuDTO> childs;
}
