package com.example.produce.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:35
 **/

@Data
@ApiModel(description = "产品BOM视图对象")
public class ProductBomVO {
    @ApiModelProperty(value = "BOM ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "产品ID", example = "1")
    private Integer productId;

    @ApiModelProperty(value = "原材料ID", example = "1")
    private Integer materialId;

    @ApiModelProperty(value = "原材料名称", example = "面粉")
    private String materialName;

    @ApiModelProperty(value = "原材料单位", example = "kg")
    private String materialUnit;

    @ApiModelProperty(value = "单产品原材料用量", example = "0.500")
    private BigDecimal quantity;

    @ApiModelProperty(value = "最大损耗率", example = "0.050")
    private BigDecimal maxAttritionRatio;

    @ApiModelProperty(value = "产出产品波动上下值", example = "0.020")
    private BigDecimal productFluctuation;

    @ApiModelProperty(value = "扩展字段", example = "{\"note\":\"高筋面粉\"}")
    private Object data;
}