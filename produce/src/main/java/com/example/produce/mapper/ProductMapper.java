package com.example.produce.mapper;

import com.example.produce.domain.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductVO> selectAllProducts();
}
