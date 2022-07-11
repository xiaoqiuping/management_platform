package com.liubity.platform_starter.service.backstage;

import com.liubity.platform_starter.model.backstage.Menu;
import com.liubity.platform_starter.model.backstage.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2020-11-29
 */
public interface ProcessService extends JpaRepository<Process,Long>, JpaSpecificationExecutor<Menu> {
}
