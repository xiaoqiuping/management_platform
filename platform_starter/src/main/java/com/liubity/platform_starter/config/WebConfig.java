package com.liubity.platform_starter.config;

import com.liubity.platform_starter.filter.SessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @Author Liubity
 * @Date 2020-11-22
 */
@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter {
    
    
    /**
     * 创建一个 repeatRequestFilter bean
     * @return
     */
    @Bean(name = "sessionFilter")
    public SessionFilter sessionFilter() {
        return new SessionFilter();
    }
    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilterRegistration() {
        FilterRegistrationBean<SessionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(sessionFilter());
        registration.addUrlPatterns("/*");
        registration.setName("sessionFilter");
        registration.setOrder( 1 );
        return registration;
    }
}
