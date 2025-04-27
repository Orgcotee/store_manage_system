package com.example.produce.controller;

import com.example.produce.commmon.R;
import com.example.produce.domain.dto.ProductBomDTO;
import com.example.produce.domain.vo.ProductBomVO;
import com.example.produce.service.ProductBomService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-17 16:31
 **/

@RestController
@RequestMapping("/api/bom")
@Api(tags = "产品码表管理")
public class ProductBomController {
    @Autowired
    private ProductBomService productBomService;

    @GetMapping("/getOneProductBom/{pid}")
    public R<List<ProductBomVO>> getProductBomByPId(@PathVariable Integer pid) {
        System.out.println(pid);
        List<ProductBomVO> productBomByPId = productBomService.getProductBomByPId(pid);
            return R.ok(productBomByPId);
    }

    @GetMapping("/deleteByBomId/{pbid}")
    public R<Void> deleteProductBomByPBId(@PathVariable Integer pbid) {
        productBomService.deleteProductBomByPBId(pbid);
        return R.ok();
    }

    @PostMapping("/add")
    public R<Void> addProductBom(@RequestBody ProductBomDTO productBomDTO) {
        productBomService.addProductBom(productBomDTO);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> updateProductBom(@RequestBody ProductBomDTO productBomDTO) {

        productBomService.updateProductBom(productBomDTO);
        return R.ok();
    }
}