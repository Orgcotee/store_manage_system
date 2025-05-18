package com.example.finance.domain.entity;

import lombok.Data;
import lombok.Generated;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class FStoreRecord {
    /**
     * 记录编号
     */
    @Generated
    private Integer id;
    /**
     * 收入
     */
    private Double income;
    /**
     * 支出
     */
    private Double outcome;
    /**
     * 收入明细
     */
    private String inDetail;
    /**
     * 支出明细
     */
    private String outDetail;
    /**
     * 毛利润
     */
    private Double profit;
    /**
     * 报损
     */
    private Double frmloss;
    /**
     * 报损明细
     */
    private String frmDetail;
    /**
     * 所属门店
     */
    private Integer store;
    /**
     * 记录员（门店老板）
     */
    private String recorder;
    /**
     * 记录日期
     */
    private LocalDateTime recordTime;
    /**
     * 年
     */
    private String year;
    /**
     * 月
     */
    private String month;
    /**
     * 日
     */
    private String day;
}
