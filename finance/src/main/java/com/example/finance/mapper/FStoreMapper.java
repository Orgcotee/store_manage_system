package com.example.finance.mapper;

import com.example.finance.domain.entity.FStore;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FStoreMapper {
    List<FStore> selectAll();
    int insert(FStore store);
    int updateById(FStore store);
    int deleteById(Integer id);
}
