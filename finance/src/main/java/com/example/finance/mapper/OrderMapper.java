package com.example.finance.mapper;

import com.example.finance.domain.entity.FOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {


    List<FOrder> selectOrder();
}
