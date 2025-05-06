package com.example.finance.domain.vo;

import com.example.finance.domain.entity.FOrderDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDetailVO {
    private Integer id;
    private Date createTime;
    private Integer status;
    private String applicant;
    private String department;
    private Integer period;
    private Date deadline;
    private Integer collectPay;
    private Integer approvalProcess;
    private String reason;
    private Integer inOut;
    private Integer forSupplier;
    private String forSupplierName; // 供应商名称

    private List<FOrderDetail> orderDetails;
    private Double totalAmount; // 前端需要的总金额
}