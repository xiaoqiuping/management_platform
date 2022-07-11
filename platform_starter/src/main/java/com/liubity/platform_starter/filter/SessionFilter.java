package com.liubity.platform_starter.filter;

import com.liubity.platform_starter.model.backstage.Account;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.utils.CacheUtil;
import com.liubity.platform_starter.utils.JsonUtils;
import com.liubity.platform_starter.utils.StringUtil;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author Liubity
 * @Date 2019/1/10
 */
@Log4j2
public class SessionFilter implements Filter {
    
    /**
     * 白名单列表
     */
    private final String[] urls={"/login"};
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("SessionFilter init...");
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        String url=request.getRequestURI();
        String contextPath=request.getContextPath();
        String authorization=request.getHeader("Authorization");
    
        log.info("authorization => " + authorization);
    
        List<String> whiteLists=Arrays.stream(urls).map(contextPath::concat).collect(Collectors.toList());
        if (whiteLists.contains(url)) {
            filterChain.doFilter(request, servletResponse);
            return;
        }
        if (StringUtil.isEmpty(authorization)) {
            CommonResponse no_login=CommonResponse.build(new Result(401, "no_login"));
            PrintWriter writer=servletResponse.getWriter();
            writer.write(Objects.requireNonNull(JsonUtils.objectToJson(no_login)));
            writer.flush();
            writer.close();
            return;
        }else {
            Account account= (Account) CacheUtil.get(authorization);
            log.info("user => " + account);
            //长时间未操作
            if(Objects.isNull(account)) {
                CommonResponse no_login=CommonResponse.build(new Result(401, "no_login"));
                PrintWriter writer=servletResponse.getWriter();
                writer.write(Objects.requireNonNull(JsonUtils.objectToJson(no_login)));
                writer.flush();
                writer.close();
                return;
            }
            filterChain.doFilter(request, servletResponse);
        }
    }
    
    @Override
    public void destroy() {
        log.info("SessionFilter destroy...");
    }
}
