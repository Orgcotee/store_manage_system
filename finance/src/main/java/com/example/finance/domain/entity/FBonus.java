package com.example.finance.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Generated;

import java.util.Date;
@Data
public class FBonus {
    /**
     * 奖金编号
     */
    @Generated
    private Integer id;
    /**
     * 所属员工
     */
    private Integer belong;
    /**
     * 金额
     */
    private Double amount;
    /**
     * 原因
     */
    private String reason;
    /**
     * 奖金类别
     */
    private String category;
    /**
     * 附件
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
     * 申请时间
     */
    private Date applicationTime;
    /**
     * 审批人
     */
    private String approver;
    /**
     * 处理时间
     */
    private Date dealTime;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 所属员工id
     */
    private Integer employeeId;

    @TableField(exist = false)
    private String statusName;

    public String getStatusName() {
        switch (this.approvalStatus) {
            case 0: return "待审批";
            case 1: return "已通过";
            case 2: return "已驳回";
            default: return "未知状态";
        }
    }
}
