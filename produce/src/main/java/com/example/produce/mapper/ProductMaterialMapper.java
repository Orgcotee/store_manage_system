package com.example.produce.mapper;

import com.example.produce.domain.vo.ProductMaterialVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMaterialMapper {
    List<ProductMaterialVO> selectProductMaterial();
}
