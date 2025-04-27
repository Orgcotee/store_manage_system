package com.example.produce.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-26 16:40
 **/
@Data
@ApiModel(description = "所需购买产品以及数量数据传输对象")
public class PurchaseProductDTO {
    @ApiModelProperty(value = "产品ID", example = "1")
    private Integer productId;
    @ApiModelProperty(value = "所需产品数量", example = "1")
    private Integer quantity;
}