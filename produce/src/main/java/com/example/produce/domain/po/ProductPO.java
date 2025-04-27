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
@ApiModel(description = "产品持久化对象")
public class ProductPO {
    @ApiModelProperty(value = "产品ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "产品名称", required = true, example = "原味蛋糕")
    private String name;

    @ApiModelProperty(value = "产品分类", example = "蛋糕类")
    private String type;

    @ApiModelProperty(value = "规格描述", example = "直径20cm，圆形，1磅")
    private String specification;

    @ApiModelProperty(value = "产品图片URL", example = "/products/plain_cake.jpg")
    private String image;

    @ApiModelProperty(value = "成本价", example = "35.00")
    private BigDecimal cost;

    @ApiModelProperty(value = "销售价格", example = "98.00")
    private BigDecimal price;

    @ApiModelProperty(value = "条码编号", example = "8801234567891")
    private String code;

    @ApiModelProperty(value = "状态(0:未上架,1:已上架)", example = "1")
    private Integer state;

    @ApiModelProperty(value = "排序权重", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "创建时间", example = "2023-01-05 09:00:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2023-01-10 15:30:00")
    private Date updateTime;

    @ApiModelProperty(value = "扩展字段(JSON格式)", example = "{\"flavor\":\"原味\",\"allergens\":[\"鸡蛋\",\"牛奶\"]}")
    private String data;
}