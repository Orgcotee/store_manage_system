package com.example.produce.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:21
 **/

@Data
@ApiModel(description = "产品数据传输对象")
public class ProductDTO {
    @ApiModelProperty(value = "产品ID", example = "1")
    private Integer id;

    @NotBlank(message = "产品名称不能为空")
    @ApiModelProperty(value = "产品名称", required = true, example = "原味蛋糕")
    private String name;

    @ApiModelProperty(value = "产品分类", example = "蛋糕类")
    private String type;

    @ApiModelProperty(value = "规格描述", example = "直径20cm，圆形，1磅")
    private String specification;

    @ApiModelProperty(value = "产品图片URL", example = "/products/plain_cake.jpg")
    private String image;

    @NotNull(message = "成本价不能为空")
    @ApiModelProperty(value = "成本价", required = true, example = "35.00")
    private BigDecimal cost;

    @NotNull(message = "销售价格不能为空")
    @ApiModelProperty(value = "销售价格", required = true, example = "98.00")
    private BigDecimal price;

    @ApiModelProperty(value = "条码编号", example = "8801234567891")
    private String code;

    @ApiModelProperty(value = "状态(0:未上架,1:已上架)", example = "1")
    private Integer state;

    @ApiModelProperty(value = "排序权重", example = "1")
    private Integer sort;

}