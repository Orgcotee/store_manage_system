//package com.example.finance.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayApiException;
//import com.example.finance.service.FOrderService;
//import com.example.finance.utils.PayUtil;
//import com.second.hand.trading.server.enums.ErrorMsg;
//import com.second.hand.trading.server.model.OrderModel;
//import com.second.hand.trading.server.service.OrderService;
//import com.second.hand.trading.server.utils.JwtHelper;
//import com.second.hand.trading.server.utils.PayUtil;
//import com.second.hand.trading.server.vo.ResultVo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.UUID;
//
//@Controller
//@RequestMapping("/api/alipay")
//public class AliPayController {
//    @Autowired
//    private PayUtil payUtil;
//    @Autowired
//    private FOrderService orderService;
//
//    private OrderModel orderModels = null;
//    private String tokens = "";
//    @Autowired
//    private JwtHelper jwtHelper;
//
//    @ResponseBody
//    @PostMapping("/pay")
//    public String alipay(@RequestHeader String token, @RequestBody OrderModel orderModel) throws AlipayApiException {
//        orderModels = orderModel;
//        tokens = token;
//        //生成订单号（支付宝的要求？）
//        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        String user = UUID.randomUUID().toString().replace("-", "").toUpperCase();
//        OrderModel order = orderService.getOrder(orderModel.getId());
//        String OrderNum = time + user;
//        float oderValue = order.getOrderPrice().floatValue();
//        System.out.println("商品名字是" + orderModel.getIdleItem().getIdleName());
//        //调用封装好的方法（给支付宝接口发送请求）
//        return payUtil.sendRequestToAlipay(OrderNum, oderValue, orderModel.getIdleItem().getIdleName());
//
//    }
//
//    //    当我们支付完成之后跳转这个请求并携带参数，我们将里面的订单id接收到，通过订单id查询订单信息，信息包括支付是否成功等
//    @GetMapping("/toSuccess")
//    public String returns(String out_trade_no) throws ParseException {
//        String query = payUtil.query(out_trade_no);
//        System.out.println("==>" + query);
//        JSONObject jsonObject = JSONObject.parseObject(query);
//        Object o = jsonObject.get("alipay_trade_query_response");
//        Map map = (Map) o;
//        System.out.println(map);
//        Object s = map.get("trade_status");
//        if (s.equals("TRADE_SUCCESS")) {
//            //当支付成功之后要执行的操作
//            System.out.println("订单支付成功");
//            Long userId = jwtHelper.getUserId(tokens);
//            orderModels.setUserId(userId);
//            boolean result = orderService.updateOrder(orderModels);
//
//
//            System.out.println(s);
//            return "redirect:http://localhost:8081/#/paysuccess";
//        } else {
////            支付失败要执行的操作
//            System.out.println("订单支付失败");
//            return "index";
//        }
//    }
//
//    /*
//参数1：订单号
//参数2：订单金额
//参数3：订单名称
// */
//
//}