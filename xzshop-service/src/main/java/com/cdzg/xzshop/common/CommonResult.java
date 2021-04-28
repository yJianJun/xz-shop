package com.cdzg.xzshop.common;

import com.alibaba.fastjson.JSONObject;
import com.framework.utils.core.api.ApiConst;
import com.framework.utils.core.api.ApiResponse;
import org.apache.poi.ss.formula.functions.T;

public class CommonResult extends ApiResponse<T> {


    public CommonResult(ResultCode errorInfo) {
        super();
        setCode(errorInfo.getCode());
        setMsg(errorInfo.getMessage());
    }

    public CommonResult() {
        super();
    }

    public static CommonResult buildSuccessResponse() {
        CommonResult response = new CommonResult();
        response.setCode(ApiConst.Code.CODE_SUCCESS.code());
        response.setData(null);
        return response;
    }

    /**
     * 失败
     */
    public static CommonResult error(ResultCode errorInfo) {
        return error(errorInfo.getCode(),errorInfo.getMessage());
    }

    /**
     * 失败
     */
    public static CommonResult error(int code, String message) {
        CommonResult rb = new CommonResult();
        rb.setCode(code);
        rb.setMsg(message);
        rb.setData(null);
        return rb;
    }

    public static CommonResult error(ResultCode resultCode, String message) {
        CommonResult rb = new CommonResult();
        rb.setCode(resultCode.getCode());
        rb.setMsg(message);
        rb.setData(null);
        return rb;
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
