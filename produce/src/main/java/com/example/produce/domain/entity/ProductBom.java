package com.example.produce.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:15
 **/

@Data
@ApiModel(description = "产品BOM实体")
public class ProductBom {
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

    @ApiModelProperty(value = "扩展字段", example = "{\"note\": \"高筋面粉\"}")
    private Map<String, Object> data;

    // 可以添加关联对象，方便前端展示
    @ApiModelProperty(value = "关联的原材料信息")
    private ProductMaterial material;
}