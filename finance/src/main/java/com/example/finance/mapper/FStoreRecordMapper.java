package com.example.finance.mapper;

import com.example.finance.domain.entity.FStoreRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FStoreRecordMapper {
    int insert(FStoreRecord record);
}
