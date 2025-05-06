package com.example.finance.domain.vo;

import com.example.finance.domain.entity.FOrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderVO {
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

    // 总金额字段（BigDecimal类型避免精度问题）
//    private BigDecimal totalAmount;
    // 新增订单详情列表
    private List<FOrderDetail> orderDetails;
}
