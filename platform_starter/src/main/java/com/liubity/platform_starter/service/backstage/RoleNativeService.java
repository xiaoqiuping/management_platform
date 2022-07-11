package com.liubity.platform_starter.service.backstage;


import com.liubity.platform_starter.model.backstage.Role;
import com.liubity.platform_starter.model.common.CommonRequest;

import java.util.List;

/**
 * @Author: Liubity
 * @Date: 2020/11/14 10:48
 */
public interface RoleNativeService{

    
    List<Role> listWithDepartment(CommonRequest<Role> request);
    
}
