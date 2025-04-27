package com.example.produce.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:20
 **/

@Data
@ApiModel(description = "产品BOM持久化对象")
public class ProductBomPO {
    @ApiModelProperty(value = "BOM ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "产品ID", required = true, example = "1")
    private Integer productId;

    @ApiModelProperty(value = "原材料ID", required = true, example = "1")
    private Integer materialId;

    @ApiModelProperty(value = "单产品原材料用量", required = true, example = "0.500")
    private BigDecimal quantity;

    @ApiModelProperty(value = "最大损耗率", example = "0.050")
    private BigDecimal maxAttritionRatio;

    @ApiModelProperty(value = "产出产品波动上下值", example = "0.020")
    private BigDecimal productFluctuation;

    @ApiModelProperty(value = "创建时间", example = "2023-01-05 11:00:00")
    private Date createTime;

    @ApiModelProperty(value = "扩展字段(JSON格式)", example = "{\"note\":\"高筋面粉\"}")
    private String data;
}