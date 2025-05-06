package com.example.finance.mapper;

import com.example.finance.domain.entity.FOrder;
import com.example.finance.domain.entity.FOrderDetail;
import com.example.finance.domain.vo.OrderVO;
import com.example.finance.domain.vo.OrdersUpdateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FOrderMapper {
    //更新订单状态
    Integer updateOrders(@Param("dto") OrdersUpdateDTO dto);
    // 查询所有订单
    List<OrderVO> selectOrder();
    // 根据订单ID查询详情
    List<FOrderDetail> selectDetailsByOrderId(@Param("orderId") Integer orderId);

    // 根据ID查询单个订单
    OrderVO selectOrderById(@Param("orderId") Integer orderId);

}
