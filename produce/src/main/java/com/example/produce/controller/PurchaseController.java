package com.example.produce.controller;

import com.example.produce.commmon.R;
import com.example.produce.domain.dto.PurchaseProductDTO;
import com.example.produce.domain.vo.ProductMaterialVO;
import com.example.produce.domain.vo.PurchaseProductVO;
import com.example.produce.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-26 16:34
 **/
@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public R<List<PurchaseProductVO>> getMaterialListByProductIdAndQuantity(@RequestBody List<PurchaseProductDTO> purchaseProductDTOs) {
        return R.ok(purchaseService.getMaterialListByProductIdAndQuantity(purchaseProductDTOs));
    }
}