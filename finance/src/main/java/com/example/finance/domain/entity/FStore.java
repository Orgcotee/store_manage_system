package com.example.finance.domain.entity;

import lombok.Data;
import lombok.Generated;

@Data
public class FStore {
    /**
     * 门店编号
     */
    @Generated
    private Integer id;
    /**
     * 管理员
     */
    private String manager;
    /**
     * 门店地址
     */
    private String address;
    /**
     * 电话
     */
    private String phone;
}
