package com.example.finance.controller;

import com.example.finance.common.R;
import com.example.finance.domain.entity.FOrder;
import com.example.finance.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-27 15:32
 **/
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/getAllOrder")
    public R<List<FOrder>> getAllOrder() {
        List<FOrder> orders = orderService.getAllOrder();
        return R.ok(orders);
    }
}