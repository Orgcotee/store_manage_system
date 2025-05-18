package com.example.finance.service.impl;

import com.example.finance.domain.entity.FStoreRecord;
import com.example.finance.mapper.FStoreRecordMapper;
import com.example.finance.service.FStoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FStoreRecordServiceImpl implements FStoreRecordService {
    @Autowired
    private FStoreRecordMapper fStoreRecordMapper;
    @Override
    public void createDailyRecord(FStoreRecord record) {
        // 自动计算利润
        record.setProfit((record.getIncome() != null ? record.getIncome() : 0)
                - (record.getOutcome() != null ? record.getOutcome() : 0));

        // 设置时间信息
        LocalDateTime now = LocalDateTime.now();
        record.setRecordTime(now);
        record.setYear(String.valueOf(now.getYear()));
        record.setMonth(String.format("%02d", now.getMonthValue()));
        record.setDay(String.format("%02d", now.getDayOfMonth()));

        // 数据校验
        validateRecord(record);

        fStoreRecordMapper.insert(record);
    }

    private void validateRecord(FStoreRecord record) {
        if (record.getStore() == null) {
            throw new IllegalArgumentException("必须选择所属门店");
        }
        if (record.getOutcome() == null && record.getIncome() == null && record.getFrmloss() == null) {
            throw new IllegalArgumentException("至少需要填写一项财务数据");
        }
    }
}
