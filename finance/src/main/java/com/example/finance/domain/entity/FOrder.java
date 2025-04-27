package com.example.finance.domain.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-27 15:30
 **/
@Data
public class FOrder {
    /**
     * 订单编号
     */
    private Integer id;
    /**
     * 创立时间
     */
    private Date createTime;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 申请人部门
     */
    private String department;
    /**
     * 是否有账期
     */
    private String period;
    /**
     * 账期时间
     */
    private String deadline;
    /**
     * 支出/收入状态（1/0）
     */
    private String collectPay;
    /**
     * 审批流程状态
     */
    private String approvalProcess;
    /**
     * 驳回原因
     */
    private String reason;
    /**
     * 内部订单/外部订单
     */
    private String inOut;
}