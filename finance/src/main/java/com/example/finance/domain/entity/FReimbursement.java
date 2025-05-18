package com.example.finance.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.Generated;

import java.util.Date;

@Data
public class FReimbursement {
    /**
     * 报账编号
     */
    @Generated
    private Integer id;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 报账类别（日用品/零件/报损）
     */
    private String category;
    /**
     * 收入/支出
     */
    private Integer inOut;
    /**
     * 金额
     */
    private Double amount;
    /**
     * 明细
     */
    private String details;
    /**
     * 附件证明
     */
    private String img;
    /**
     * 审批状态
     */
    private Integer approvalStatus;
    /**
     * 审批意见
     */
    private String approvalOpinion;
    /**
     * 审批人
     */
    private String approver;
    /**
     * 申请时间
     */
    private Date applicationTime;
    /**
     * 审批时间
     */
    private Date dealTime;
    /**
     * 申请人ID
     */
    private Integer applicantId;

    // 添加状态枚举映射
    @TableField(exist = false)
    private String status;

    public String getStatus() {
        switch (this.approvalStatus) {
            case 0: return "pending";
            case 1: return "approved";
            case 2: return "rejected";
            default: return "unknown";
        }
    }

    // 添加金额类型转换
    @JsonSerialize(using = ToStringSerializer.class)
    public Double getAmount() {
        return amount;
    }
}
