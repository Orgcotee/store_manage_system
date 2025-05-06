package com.example.finance.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "后台返回信息", description = "对后台返回信息进行封装")
public class R<T> {
    /*状态码*/
    @NotBlank(message = "状态码")
    @ApiModelProperty(value = "状态码", name = "code", example = "20000", required = true)
    private int code;
    /*信息提示*/
    @NotBlank(message = "信息提示")
    @ApiModelProperty(value = "信息提示", name = "message", example = "ok", required = true)
    private String message;
    /*携带的数据*/
    @ApiModelProperty(value = "携带的数据", name = "data", example = "ok")
    private T data;

    /**
     * 操作成功，并且携带数据
     * @param data 数据
     * @return
     * @param <T>
     */
    public static <T> R<T> ok(T data){
        R<T> r = new R<T>();
        r.setCode(ServiceCode.OK.getValue());
        r.setData(data);
        r.message="ok";
        return r;
    }

    /**
     * 操作成功，不携带数据
     * @return
     */
    public static R<Void> ok(){
        return ok(null);
    }



    /**
     * 操作失败
     * @param serviceCode
     * @param message
     * @return
     * @param <Void>
     */
    public static<Void> R<Void> fail(ServiceCode serviceCode,String message){
        R<Void> r = new R<>();
        r.setCode(serviceCode.getValue());
        r.setMessage(message);
        return r;
    }

    /**
     * 由于业务异常，操作失败
     * @param e
     * @return
     */
    public static R<Void> fail(ServiceException e){
        return fail(e.getServiceCode(),e.getMessage());
    }

}
