package com.example.finance.mapper;

import com.example.finance.domain.entity.FStaff;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FStaffMapper {
    List<FStaff> selectAll();
    int insert(FStaff staff);

    int update(FStaff staff);

    int delete(Integer id);
}
