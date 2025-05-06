package com.example.finance.domain.entity;

import lombok.Data;

import java.util.Date;

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
    private Integer status;
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
    private Integer period;
    /**
     * 账期时间
     */
    private Date deadline;
    /**
     * 支出/收入状态（1/0）
     */
    private Integer collectPay;
    /**
     * 审批流程状态
     */
    private Integer approvalProcess;
    /**
     * 驳回原因
     */
    private String reason;
    /**
     * 内部订单/外部订单
     */
    private Integer inOut;
    /**
     * 所属供应商id
     */
    private Integer forSupplier;
}
