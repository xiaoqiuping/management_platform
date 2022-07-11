package com.liubity.platform_starter.service.backstage;

import com.liubity.platform_starter.model.backstage.Role;
import com.liubity.platform_starter.model.common.CommonRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-11-16
 */
@Service
public class RoleNativeServiceImpl implements RoleNativeService {
    @Override
    public List<Role> listWithDepartment(CommonRequest<Role> request) {
        return null;
    }

//    @PersistenceContext
//    private EntityManager entityManager;
//
//    //后面再研究
//    @Override
//    public List<Role> listWithDepartment(CommonRequest<Role> request) {
//        String sql = "select r.id,r.name,r.remarks,r.enable_flag,r.create_time,d.name from t_backstage_role r left join t_backstage_department d on d.id=r.department_id";
//        String count = "select count(1) from t_backstage_role r left join t_backstage_department d on d.id=r.department_id";
//        StringBuffer sqlSB = new StringBuffer(sql);
//        StringBuffer countSB = new StringBuffer(count);
//        Role param=request.getParam();
//        if(Objects.nonNull(param)) {
//            sqlSB.append(" where 1=1");
//            countSB.append(" where 1=1");
//            if (param.getName() != null) {
//                sqlSB.append(" and r.name like %").append(param.getName()).append("%");
//                countSB.append(" and r.name like %").append(param.getName()).append("%");
//            }
//            //有修改 原来是department_id
//            if (Objects.nonNull(param.getDepartmentName())){
//                sqlSB.append(" and r.department_id =").append(param.getDepartmentName());
//                countSB.append(" and r.name like %").append(param.getName()).append("%");
//            }
//            if(Objects.nonNull(param.getEnableFlag())){
//                sqlSB.append(" and r.enable_flag =").append(param.getEnableFlag());
//                countSB.append(" and r.name like %").append(param.getName()).append("%");
//            }
//        }
//        if(Objects.nonNull(request.getPage())){
//            sqlSB.append(" limit ").append(request.getPage().getPage()*request.getPage().getSize()).append(",").append(request.getPage().getSize());
//            countSB.append(" limit ").append(request.getPage().getPage()*request.getPage().getSize()).append(",").append(request.getPage().getSize());
//        }
//        Query listQuery=entityManager.createNativeQuery(sqlSB.toString());
//        Query countQuery=entityManager.createNativeQuery(count.toLowerCase());
//        BigDecimal singleResult= (BigDecimal)countQuery.getSingleResult();
//
//        List resultList=listQuery.getResultList();
//
//        if(Objects.nonNull(resultList)){
//            List<Role> roleList = new ArrayList<>(resultList.size());
//
//            resultList.forEach(map->{
//                Role role = new Role();
//                role.setId(1l);
//            });
//        }
//        return  null;
//    }
}
