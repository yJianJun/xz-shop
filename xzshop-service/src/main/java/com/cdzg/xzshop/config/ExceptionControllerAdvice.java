package com.cdzg.xzshop.config;



import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.framework.utils.core.api.ApiResponse;
import com.framework.utils.core.web.GlobalExceptionHandler;
import io.swagger.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;


/**
 * 参数校验 返回数据自定义json ApiResponse
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse bindExceptionHandler(MethodArgumentNotValidException ex) {
        logger.error("发生参数异常！原因是：{}", Json.pretty(ex.getStackTrace()));
        BindingResult bindingResult = ex.getBindingResult();
        StringBuffer sb = new StringBuffer(bindingResult.getFieldErrors().size() * 16);
        for (int i = 0; i < bindingResult.getFieldErrors().size(); i++) {
            if (i > 0) {
                sb.append(";");
            }
            FieldError fieldError = bindingResult.getFieldErrors().get(i);
            sb.append(fieldError.getDefaultMessage());
        }

        return ApiResponse.buildResponse(206, sb.toString());
    }

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    public ApiResponse BaseExceptionHandler(HttpServletRequest req, BaseException e) {
        logger.error("发生业务异常！原因是：{}", Json.pretty(e.getStackTrace()));
        return CommonResult.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ApiResponse exceptionHandler(HttpServletRequest req, NullPointerException e) {
        logger.error("发生空指针异常！原因是:{}", Json.pretty(e.getStackTrace()));
        return CommonResult.error(ResultCode.DATA_ERROR);
    }


    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResponse exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未知异常！原因是:{}", Json.pretty(e.getStackTrace()));
        return CommonResult.error(ResultCode.FAILURE);
    }


    //参数类型不匹配
//getPropertyName()获取数据类型不匹配参数名称
//getRequiredType()实际要求客户端传递的数据类型
    @ExceptionHandler({TypeMismatchException.class})
    public ApiResponse requestTypeMismatch(TypeMismatchException ex) {
        logger.error("参数类型不匹配！原因是:{}", Json.pretty(ex.getStackTrace()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR, "参数类型不匹配,参数" + ex.getPropertyName() + "类型应该为" + ex.getRequiredType());
    }

    //缺少参数异常
//getParameterName() 缺少的参数名称
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ApiResponse requestMissingServletRequest(MissingServletRequestParameterException ex) {
        logger.error("缺少参数！原因是:{}", Json.pretty(ex.getStackTrace()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR, "缺少必要参数,参数名称为" + ex.getParameterName());
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ApiResponse requestTypeMismatch(MethodArgumentTypeMismatchException ex){
        logger.error("参数类型不匹配！原因是:{}", Json.pretty(ex.getStackTrace()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR, "参数类型不匹配,参数" + ex.getPropertyName() + "类型应该为" + ex.getRequiredType());
    }

    /**
     * 请求method不匹配
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ApiResponse requestMissingServletRequest(HttpRequestMethodNotSupportedException ex) {
        logger.error("请求method不匹配！原因是:{}", Json.pretty(ex.getStackTrace()));
        return CommonResult.error(ResultCode.REQUEST_ERROR, "不支持"+ex.getMethod()+"方法，支持"+ StringUtils.join(ex.getSupportedMethods(), ",")+"类型");
    }

    /**
     *
     * 控制器方法中@RequestBody类型参数数据类型转换异常
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ApiResponse httpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest wq){
        logger.error("控制器方法中@RequestBody类型参数数据类型转换异常！原因是:{}", Json.pretty(e.getStackTrace()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR,"参数数据类型转换异常,参数:"+e.getHttpInputMessage());
    }

}
