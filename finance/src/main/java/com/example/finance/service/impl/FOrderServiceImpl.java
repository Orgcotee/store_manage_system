package com.example.finance.service.impl;

import com.example.finance.domain.entity.FOrderDetail;
import com.example.finance.domain.vo.OrderVO;
import com.example.finance.domain.vo.OrdersUpdateDTO;
import com.example.finance.mapper.FOrderMapper;
import com.example.finance.service.FOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FOrderServiceImpl implements FOrderService {
    @Autowired
    private FOrderMapper fOrderMapper;

    @Override
    @Transactional
    public void update(OrdersUpdateDTO ordersUpdateDTO) {
        // 1. 查询订单是否存在
        OrderVO exist = fOrderMapper.selectOrderById(ordersUpdateDTO.getId());
        if (exist == null) {
            throw new RuntimeException("订单不存在");
        }

        // 2. 验证状态流转是否合法
        validateStatusTransition(exist.getStatus(), ordersUpdateDTO.getStatus());

        // 3. 更新订单
        int rows = fOrderMapper.updateOrders(ordersUpdateDTO);
        if (rows != 1) {
            throw new RuntimeException("更新订单失败");
        }
    }

    // 验证状态流转是否合法
    private void validateStatusTransition(Integer oldStatus, Integer newStatus) {
        // 状态流转规则：
        // 0(待审批) -> 1(已驳回) 或 2(待签订)
        // 1(已驳回) -> 0(待审批) [重新提交]
        // 2(待签订) -> 3(签订中)
        // 3(签订中) -> 4(待支付)
        // 4(待支付) -> 5(账期中) 或 6(履行中)
        // 5(账期中) -> 6(履行中)
        // 6(履行中) -> 7(已完成)

        if (oldStatus.equals(newStatus)) {
            throw new RuntimeException("订单状态未改变");
        }

        switch (oldStatus) {
//            case 0: // 待审批
//                if (!(newStatus == 1 || newStatus == 2)) {
//                    throw new RuntimeException("无效的状态变更");
//                }
//                break;
//            case 1: // 已驳回
//                if (newStatus != 0) {
//                    throw new RuntimeException("已驳回订单只能重新提交审批");
//                }
//                break;
            // 其他状态流转验证...
//            default:
//                if (newStatus <= oldStatus) {
//                    throw new RuntimeException("订单状态不能回退");
//                }
        }
    }

    @Override
    public List<OrderVO> getAllOrderWithDetails() {
        // 查询所有订单
        List<OrderVO> orders = fOrderMapper.selectOrder();

        // 为每个订单查询详情
        return orders.stream().peek(order -> {
            List<FOrderDetail> details = fOrderMapper.selectDetailsByOrderId(order.getId());
            order.setOrderDetails(details);
        }).collect(Collectors.toList());
  }

    @Override
    @Transactional(readOnly = true)  // 添加事务注解
    public OrderVO getOrderByIdWithDetails(Integer orderId) {
        // 1. 查询订单基础信息
        OrderVO order = fOrderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 2. 查询订单详情
        List<FOrderDetail> details = fOrderMapper.selectDetailsByOrderId(orderId);
        order.setOrderDetails(details);

        return order;
    }
}
