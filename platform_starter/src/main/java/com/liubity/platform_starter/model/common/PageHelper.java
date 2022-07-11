package com.liubity.platform_starter.model.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


/**
 * @Author: Liubity
 * @Date: 2020/11/14 14:03
 */
@Data
@Accessors(chain = true)
public class PageHelper  {

    /**
     * 当前页
     */
    private int page;

    /**
     * 页数大小
     */
    private int size;

    public static PageRequest buildPageRequest(PageHelper helper) {
        return PageRequest.of(helper.getPage()-1,helper.getSize());
    }
    public static PageRequest buildPageRequest(PageHelper helper, Sort sort) {
        return PageRequest.of(helper.getPage()-1,helper.getSize(),sort);
    }
}
