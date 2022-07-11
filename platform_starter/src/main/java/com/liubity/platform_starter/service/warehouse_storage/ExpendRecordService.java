package com.liubity.platform_starter.service.warehouse_storage;

import com.liubity.platform_starter.model.warehouse_storage.ExpendRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author Liubity
 * @Date 2021-02-28
 */
public interface ExpendRecordService extends JpaRepository<ExpendRecord,Long>, JpaSpecificationExecutor<ExpendRecord> {
}
