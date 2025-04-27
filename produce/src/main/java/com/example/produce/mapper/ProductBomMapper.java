package com.example.produce.mapper;


import com.example.produce.domain.dto.ProductBomDTO;
import com.example.produce.domain.vo.ProductBomVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductBomMapper {
    List<ProductBomVO> selectProductBomByPId(Integer pid);

    Integer deleteProductBomByPBId(Integer pbid);

    Integer addProductBom(ProductBomDTO productBomDTO);

    Integer updateProductBom(ProductBomDTO productBomDTO);
}
