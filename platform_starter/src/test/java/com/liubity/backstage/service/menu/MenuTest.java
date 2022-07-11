package com.liubity.backstage.service.menu;

import com.liubity.platform_starter.model.backstage.Menu;
import com.liubity.platform_starter.model.backstage.Role;
import com.liubity.platform_starter.model.backstage.RoleMenuRef;
import com.liubity.platform_starter.model.dto.Statistic;
import com.liubity.platform_starter.service.backstage.AccountRoleRefService;
import com.liubity.platform_starter.service.backstage.AccountService;
import com.liubity.platform_starter.service.backstage.MenuService;
import com.liubity.platform_starter.service.sale.ReceivableService;
import com.liubity.platform_starter.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@SpringBootConfiguration
@ComponentScan("com.liubity")
public class MenuTest {
    
    
    @Autowired
    private MenuService menuService;
    
    @Test
    public void queryMenuWithRoleId(){
    
        Menu menu= new Menu();
        
        List<Menu> all=menuService.findAll((Specification<Menu>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            //Root<RoleMenuRef> roleRoot = criteriaQuery.from(RoleMenuRef.class);
            Join<Menu , RoleMenuRef> join = root.join("rmr", JoinType.LEFT);
//            join.on(criteriaBuilder.equal(join.get("roleId"),1));
            //predicates.add(criteriaBuilder.equal(root.get("id"),roleRoot.get("menuId")));
            //predicates.add(criteriaBuilder.equal(roleRoot.get("roleId"), 2));
//            Join<Menu,RoleMenuRef> join = root.join("roleMenuRef", JoinType.LEFT);
//            join.on(criteriaBuilder.equal(root.get("id"),join.get("menuId")));
//            predicates.add(criteriaBuilder.equal(root.get("id"), 1));
            
            
            
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        all.forEach(System.out::println);
        //List<Menu> menuByRoleId=menuService.findMenuByRoleId(1L);
    
        //menuByRoleId.forEach(System.out::println);
    }
    
    @Autowired
    private AccountRoleRefService accountRoleRefService;
    @Test
    public void queryRoleWithAccountID(){
        List<Role> roleByAccount=accountRoleRefService.getRoleByAccount(1L);
        roleByAccount.forEach(System.out::println);
    }
    
    @Autowired
    private AccountService accountService;
    @Test
    public void selectRoleIds(){
        List<Long> roleIds=accountService.selectRoleIds(1L);
        roleIds.forEach(System.out::println);
        Set<Menu> menuByRoleId=menuService.findMenuInRoleIds(roleIds);
        menuByRoleId.forEach(System.out::println);
    }
    
    
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ReceivableService receivableService;
//    @Autowired
//    private StatisticService statisticService;
    @Test
    public void selectSta(){
        LocalDate today=LocalDate.now().plusDays(1);
        Date start = DateUtil.localDate2Date(today.minusDays(7));
        Date end = DateUtil.localDate2Date(today);
        List<Object[]> objects=receivableService.statisticByCreateTime(start, end);
        List<Statistic> collect=objects.stream().map(o -> {
            Statistic statistic=new Statistic();
            statistic.setDate((String) o[0])
                    .setTotalAmount((BigDecimal) o[1]);
            return statistic;
        }).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }
    
}
