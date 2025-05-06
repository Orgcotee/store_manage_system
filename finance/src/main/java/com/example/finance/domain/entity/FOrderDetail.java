package com.example.finance.domain.entity;

import lombok.Data;

@Data
public class FOrderDetail {
    /**
     * 订单详情编号
     */
    private Integer id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品数量
     */
    private Integer num;
    /**
     * 所属订单
     */
    private Integer oid;
    /**
     * 商品单价
     */
    private Double price;
}
