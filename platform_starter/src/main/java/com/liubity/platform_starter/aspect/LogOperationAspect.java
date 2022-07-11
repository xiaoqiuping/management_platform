package com.liubity.platform_starter.aspect;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.backstage.LogManagement;
import com.liubity.platform_starter.model.backstage.Account;
import com.liubity.platform_starter.service.backstage.LogManagementService;
import com.liubity.platform_starter.utils.CacheUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
@Aspect
@Component
@Log4j2
public class LogOperationAspect {
    
    @Autowired
    private LogManagementService service;
    
    
    @Pointcut("@annotation(params)")
    public void logOperation(LogOperation params) {
    }
    
    /**
     * 保存操作记录
     *
     * @param point 切点
     * @param params 注解携带的信息
     * @return object
     * @throws Throwable Throwable异常
     */
//    @Around("logOperation(params)")
//    public Object callAround(ProceedingJoinPoint point, LogOperation params) throws Throwable {
//        //拿登录人信息
//        //todo
//        String account = "Liutity";
//        String operation=params.value();
//        LogManagement logManagement = new LogManagement();
//        logManagement.setAccount(account)
//                .setLogOperation(operation)
//                .setOperationTime(new Date());
//        service.save(logManagement);
//        return point.proceed();
//    }
    
    @After("logOperation(params)")
    public void callAfter(JoinPoint point, LogOperation params){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //拿登录人信息
        String authorization=request.getHeader("Authorization");
        Account account = (Account) CacheUtil.get(authorization);
        String operation=params.value();
        LogManagement logManagement = new LogManagement();
        logManagement.setAccount(account.getName())
                .setLogOperation(operation)
                .setOperationTime(new Date());
        service.save(logManagement);
    }
}
