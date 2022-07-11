package com.liubity.platform_starter.service.backstage;

import com.liubity.platform_starter.model.backstage.Material;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Liubity
 * @Date 2020-11-19
 */
public interface MaterialService extends JpaRepository<Material,Long> {



}
