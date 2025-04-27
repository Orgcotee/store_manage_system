package com.example.produce.service;

import com.example.produce.domain.dto.ProductBomDTO;
import com.example.produce.domain.vo.ProductBomVO;

import java.util.List;

public interface ProductBomService {
    public List<ProductBomVO> getProductBomByPId(Integer pid);

    void deleteProductBomByPBId(Integer pbid);

    void addProductBom(ProductBomDTO productBomDTO);

    void updateProductBom(ProductBomDTO productBomDTO);
}
