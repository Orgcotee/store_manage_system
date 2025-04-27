package com.example.produce.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-26 17:21
 **/
@Data
@ApiModel(description = "采购原材料视图对象")
public class PurchaseProductVO {
    @ApiModelProperty(value = "产品ID", example = "1")
    private Integer productId;
    @ApiModelProperty(value = "原材料ID", example = "1")
    private Integer materialId;
    @ApiModelProperty(value = "原材料名称", example = "面粉")
    private String materialName;
    @ApiModelProperty(value = "计量单位", example = "kg")
    private String unit;
    @ApiModelProperty(value = "采购数量", example = "100")
    private BigDecimal totalQuantity;
}