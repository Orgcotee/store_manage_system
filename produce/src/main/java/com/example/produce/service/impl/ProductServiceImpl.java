package com.example.produce.service.impl;

import com.example.produce.domain.vo.ProductVO;
import com.example.produce.mapper.ProductMapper;
import com.example.produce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:54
 **/


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductVO> getAllProducts() {
        return productMapper.selectAllProducts();
    }
}