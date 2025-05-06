package com.example.finance.controller;

import com.example.finance.common.R;
import com.example.finance.common.ServiceCode;
import com.example.finance.domain.vo.OrderVO;
import com.example.finance.domain.vo.OrdersUpdateDTO;
import com.example.finance.service.FOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fOrder")
public class FOrderController {

    @Autowired
    private FOrderService fOrderService;

    @PostMapping("/update")
    public R updateOrder(@RequestBody OrdersUpdateDTO dto) {
        fOrderService.update(dto);
        return R.ok();
    }

    @GetMapping("/getAllOrder")
    public R<List<OrderVO>> getAllOrder() {
//        List<OrderVO> orders = fOrderService.getAllOrderWithDetails();
//        return R.ok(orders);
        try {
            List<OrderVO> orders = fOrderService.getAllOrderWithDetails();
            return R.ok(orders);
        } catch (Exception e) {
            return R.fail(ServiceCode.ERR_UNKNOWN, "订单查询失败");
        }
    }
    @GetMapping("/{orderId}")
    public R<OrderVO> getOrderWithDetails(@PathVariable Integer orderId) {
        try {
            OrderVO order = fOrderService.getOrderByIdWithDetails(orderId);
            return R.ok(order);
        } catch (Exception e) {
            return R.fail(ServiceCode.ERR_UNKNOWN, "订单详情查询失败");
        }
    }

}
