package com.example.produce.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:24
 **/

@Data
@ApiModel(description = "产品原材料数据传输对象")
public class ProductMaterialDTO {
    @ApiModelProperty(value = "原材料ID", example = "1")
    private Integer id;

    @NotBlank(message = "原材料名称不能为空")
    @ApiModelProperty(value = "原材料名称", required = true, example = "面粉")
    private String name;

    @ApiModelProperty(value = "原材料分类", example = "粮食类")
    private String type;

    @NotBlank(message = "计量单位不能为空")
    @ApiModelProperty(value = "计量单位", required = true, example = "kg")
    private String unit;

    @ApiModelProperty(value = "条码编号", example = "6901234567891")
    private String barcode;

    @ApiModelProperty(value = "图片URL", example = "/materials/flour.jpg")
    private String image;

    @NotNull(message = "最小库存量不能为空")
    @ApiModelProperty(value = "最小库存量", required = true, example = "100")
    private Integer minStock;

}