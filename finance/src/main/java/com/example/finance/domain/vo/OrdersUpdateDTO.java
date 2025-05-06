package com.example.finance.domain.vo;

import lombok.Data;

@Data
public class OrdersUpdateDTO {
    /**
     * 订单主键
     */
    private Integer id;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 审批流程状态
     */
    private Integer approvalProcess;
    /**
     * 驳回原因
     */
    private String reason;
}
