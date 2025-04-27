package com.example.produce.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-16 17:23
 **/

@Data
@ApiModel(description = "产品原材料持久化对象")
public class ProductMaterialPO {
    @ApiModelProperty(value = "原材料ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "原材料名称", required = true, example = "面粉")
    private String name;

    @ApiModelProperty(value = "原材料分类", example = "粮食类")
    private String type;

    @ApiModelProperty(value = "计量单位", required = true, example = "kg")
    private String unit;

    @ApiModelProperty(value = "条码编号", example = "6901234567891")
    private String barcode;

    @ApiModelProperty(value = "图片URL", example = "/materials/flour.jpg")
    private String image;

    @ApiModelProperty(value = "最小库存量", example = "100")
    private Integer minStock;

    @ApiModelProperty(value = "创建时间", example = "2023-01-01 10:00:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2023-01-01 10:00:00")
    private Date updateTime;

    @ApiModelProperty(value = "扩展字段(JSON格式)", example = "{\"supplier\":\"金龙鱼\",\"shelf_life\":180}")
    private String data;
}