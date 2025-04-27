package com.example.produce.controller;

import com.example.produce.commmon.R;
import com.example.produce.domain.vo.ProductVO;
import com.example.produce.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:50
 **/

@RestController
@RequestMapping("/api/products")
@Api(tags = "产品管理")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @ApiOperation("获取所有产品列表")
    public R<List<ProductVO>> getAllProducts() {
        List<ProductVO> allProducts = productService.getAllProducts();
        return R.ok(allProducts);
    }
}