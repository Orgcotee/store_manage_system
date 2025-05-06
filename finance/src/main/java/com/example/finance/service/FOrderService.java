package com.example.finance.service;

import com.example.finance.domain.vo.OrderVO;
import com.example.finance.domain.vo.OrdersUpdateDTO;

import java.util.List;

public interface FOrderService {
    //更新订单状态
    void update(OrdersUpdateDTO ordersUpdateDTO);
//    List<OrderVO> getAllOrder();
    List<OrderVO> getAllOrderWithDetails();

    OrderVO getOrderByIdWithDetails(Integer orderId);
}
