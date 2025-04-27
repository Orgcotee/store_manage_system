package com.example.produce.service.impl;

import com.example.produce.domain.vo.ProductMaterialVO;
import com.example.produce.mapper.ProductMaterialMapper;
import com.example.produce.service.ProductMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-18 23:41
 **/
@Service
public class ProductMaterialServiceImpl implements ProductMaterialService {
    @Autowired
    private ProductMaterialMapper productMaterialMapper;

    @Override
    public List<ProductMaterialVO> getProductMaterial() {
        return productMaterialMapper.selectProductMaterial();
    }
}