package com.example.finance.service.impl;

import com.example.finance.domain.entity.FOrder;
import com.example.finance.mapper.OrderMapper;
import com.example.finance.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-27 15:39
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public List<FOrder> getAllOrder() {
        return orderMapper.selectOrder();
    }
}