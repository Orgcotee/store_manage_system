package com.example.finance.service;

import com.example.finance.domain.entity.FStoreRecord;

public interface FStoreRecordService {
    /**
     * 创建门店日报账记录
     * @param record 日报账实体对象
     * @throws IllegalArgumentException 当数据校验不通过时抛出
     */
    void createDailyRecord(FStoreRecord record);
}
