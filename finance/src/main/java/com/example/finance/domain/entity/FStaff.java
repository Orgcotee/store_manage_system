package com.example.finance.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Generated;

import java.util.Date;

@Data
public class FStaff {
    /**
     * 员工编号
     */
    @Generated
    private Integer id;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 所属部门
     */
    private String department;
    /**
     * 职位（实习/正式员工/经理/离职）
     */
    private String position;
    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date serviceTime;
    /**
     * 电话
     */
    private String phone;
    /**
     * 银行卡号
     */
    private String bank;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 基础工资
     */
    private Double basicSalary;
    /**
     * 奖金
     */
    private Double bonus;
    /**
     * 工资发放状态
     */
    private Integer distributionStatus;
    /**
     * 社保
     */
    private Double socialSecurity;
    /**
     * 补贴
     */
    private Double subsidy;
    /**
     * 考勤
     */
    private Double attendance;
    /**
     * 所属门店
     */
    private Integer store;

    @TableField(exist = false)
    private String status;
    // 增加非数据库字段的传输对象
    @TableField(exist = false)
    private Boolean paid;

    // 自动转换状态
    public String getStatus() {
        return "离职".equals(position) ? "离职" : "在职";
    }

    public Boolean getPaid() {
        return distributionStatus != null && distributionStatus == 1;
    }
}
