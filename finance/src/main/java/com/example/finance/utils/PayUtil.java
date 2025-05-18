//package com.example.finance.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.example.finance.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.request.AlipayTradePagePayRequest;
//import com.alipay.api.request.AlipayTradeQueryRequest;
//import com.alipay.api.response.AlipayTradeQueryResponse;
//import com.example.finance.service.FOrderService;
//import com.second.hand.trading.server.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PayUtil {
//    @Autowired
//    private FOrderService fOrderService;
//    //appid
//    private final String APP_ID = "2021000148674810";
//    //应用私钥
//    private final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVXk1kuXSMANu5ki/cqaqx24iKHWq8yXn2yEKXmOkbFK2wbuWMQS4yVnIujCgjTRcR3tIOmAJR9Gqv7OzEENZPuefxIIhJmgTLkX+VcuiixuF1RwZG/s+Mx8BM4S64mmtwLz66cnYZLKtdnoMvK750gNJHbUTAGvhkpuH+9mPLuOo1+N/6q1myjpcPEPUaXsEJ5gPmlKMP0BVnIxb9PR3TDXK4DUKY0CieJgNAjlPAXvV9a9IdGSckarf3m2uj6/X93B0TYd7yW+Egvs03PTXTE/2maAg+7bFgMS72MwjnrkGee5ZJSpgeoxEkOLt1Q918JZNCNFv6z/+CW5X0luNFAgMBAAECggEAJQET3xIi2l0Tvl1SG6z5Q5tVVM7BQjiXBGu60NveIao2bVDuUqLDRkujxdrc4zJte9XvDETksh33msc2UNKyuGT/itwfhtRFXKDBRcQGAKUJWPOWbR2oFTxXZ33uQmnVlEE9WcycGsWaesQQ8E9f2oi071pOABstVRd0Pmmw6RW5paQFMsuKY14cNxj4if2Bzfaf+xpksXJYXk+/iGvtZbTJoRrCWNbEqoAXa24/j6SnXyerzMlwr+Mu/LVub2QFvLWr6/bSdZahzK8UL/ImleBNPT6Hh+oSzWSQptOOKzCBc4DpnJshdHf0JHQZ1H/l+iPxhGt1hScpMFIK/hBjSQKBgQD3grBXmWfXvWFF0RZgT4jcIoFVXmYwSAJDJqZP4LQOglzFbiC/r/jujL8Sn2BGAU2I35X5r9X3etatgqCszWW40z+rmd95GF3NG84arAkj95zBCKfwBQZJYvZf0H21jwhhcH35GlIT3O268WwMh1xCjkYxv0ox/CXQhlV63FVzDwKBgQDcr9J93Z7s99LT/DqlTYPHXX7x+Q9TYvdbw7SjnGPPcoQYgI6C6yh1AWS8BqHkK7c304Hm6+fX7T3BwQ3GQI3IB1pYe2JzRCSP0qX9kYCXjhnI8lpoLIizuX5sVCWPMkxO/oDa0W/vEj4Voy921S4jMcJ3+0HfBf32auaiO3N0awKBgGaKNyJjhJjldXomMF4ANVP1250RecaA3qcRSQ+IqS/lpCeLA3hGoBu8JaDCdHEsMwIEly6VQVDBDFVf688LXRwgMJ2YJvQVOy8xWMbGBRdoiwGR43lVq5AVtrplNt9T/YXYw+fClsRx3+91+2yJW5ibNst/U2635raoIJvA0onTAoGBAJ1slrzOgPYNeslZXgRZHS30VgAKaJJVQJqnKo6jmtd6mxxXaFGfqkZ87fzqxkVboeEi8cFmewYe6DT0n/zSQ9Fy3VHNAlezPlmSLHKLfq2d4mrj5vX0/rcmFTb1d62EdcbL6eMuD6j83GjW9Z7RR443ir/H7h9HyPglHcyQmDVtAoGBAMG6itcelj2cR9HdWybu3KOZL5lABx45tShPFtso+lsnCVAXArjyUXODptv90ETTpOuTBPxG03mTV5Uw7/o0jHQZzD40zuAJnUXUDJVsDIijOT8bXOZp7sfRh2jNWNmkoSJt8p2iDyygNZaPeyQm4uOFWptgQksq6q3zusKT+hIP";
//
//    private final String CHARSET = "UTF-8";
//    // 支付宝公钥
//    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjmImQmGDD6y08q+QUCnozlmXygbfrGbGUTwtMzikqjiLr1WR7+/yLDgxk9pOiqOMYq55+BxE/S3HGHlZCXgmmFNGtvsmF3aP328omDW83oxQ3Rh8TZWVi7EtNOZXsxtjhE+gcq6d8I7DqNCJb0YByAF4GQsOXxIODCWVaF+GFwc2YT1pZKffjL2Q1fxoY2aAZrwvJT0neF3Bion8PeXQLGWOSUPrfx0rn718pcP4chsPM/PajVDspNaIFPIqFLzlaFhOOZk5LTh8XwMV2wbd7qchOAG6fLN9jDIWyXYT+4SR0VOuYS28T+d0abzkdoxsESQyCbioXupnoJC4o2zu/QIDAQAB";
//    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
//    private final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
//    private final String FORMAT = "JSON";
//    //签名方式
//    private final String SIGN_TYPE = "RSA2";
//    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
//    private final String NOTIFY_URL = " http://sbe673c2.natappfree.cc/api/alipay/toSuccess";
//    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
//    private final String RETURN_URL = "http://localhost:8080/api/alipay/toSuccess";
//    private AlipayClient alipayClient = null;
//    //支付宝官方提供的接口
//    public String sendRequestToAlipay(String outTradeNo, Float totalAmount, String subject) throws AlipayApiException {
//        //获得初始化的AlipayClient
//        alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
//
//        //设置请求参数
//        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
//        alipayRequest.setReturnUrl(RETURN_URL);
//        alipayRequest.setNotifyUrl(NOTIFY_URL);
//
//        //商品描述（可空）
//        String body = "";
//        alipayRequest.setBizContent("{\"out_trade_nos\":\"" + outTradeNo + "\","
//                + "\"total_amount\":\"" + totalAmount + "\","
//                + "\"subject\":\"" + subject + "\","
//                + "\"body\":\"" + body + "\","
//                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
//
//        //请求
//        String result = alipayClient.pageExecute(alipayRequest).getBody();
//        System.out.println("返回的结果是："+result );
//        return result;
//    }
//
//    //    通过订单编号查询
//    public String query(String id){
//        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
//        JSONObject bizContent = new JSONObject();
//        bizContent.put("out_trade_no", id);
//        request.setBizContent(bizContent.toString());
//        AlipayTradeQueryResponse response = null;
//        String body=null;
//        try {
//            response = alipayClient.execute(request);
//            body = response.getBody();
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//        }
//        return body;
//    }
//}
