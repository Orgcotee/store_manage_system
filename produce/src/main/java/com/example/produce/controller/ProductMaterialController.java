package com.example.produce.controller;

import com.example.produce.commmon.R;
import com.example.produce.domain.vo.ProductMaterialVO;
import com.example.produce.service.ProductMaterialService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-18 23:38
 **/
@RestController
@RequestMapping("/api/materials")
@Api(tags = "产品原材料管理")
public class ProductMaterialController {
    @Autowired
    private ProductMaterialService productMaterialService;
    @GetMapping
    public R<List<ProductMaterialVO>> getProductMaterial(){
        List<ProductMaterialVO> list = productMaterialService.getProductMaterial();
        return R.ok(list);
    }
}