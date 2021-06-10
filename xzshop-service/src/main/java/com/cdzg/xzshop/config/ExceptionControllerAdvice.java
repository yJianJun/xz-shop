package com.cdzg.xzshop.config;



import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.framework.utils.core.api.ApiResponse;
import com.framework.utils.core.web.GlobalExceptionHandler;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 参数校验 返回数据自定义json ApiResponse
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse bindExceptionHandler(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        logger.error("发生参数异常！原因是：{}", Json.pretty(ex.getSuppressed()));
        log.error("发生参数异常！原因是：{}", Json.pretty(ex.getSuppressed()));
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

    /**
     * 拦截捕捉 BindException 异常
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse handleBindException(BindException ex) {
        List<FieldError> bindingResult = ex.getBindingResult().getFieldErrors();
        List<String> msgList = new ArrayList<String>();
        for (FieldError fieldError : bindingResult) {
            System.err.println(fieldError.getField() + " " + fieldError.getDefaultMessage());
            msgList.add(fieldError.getDefaultMessage());
        }
        String firstMsg = msgList.get(0);
        return CommonResult.error(ResultCode.PARAMETER_ERROR,firstMsg);
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
        logger.error("发生业务异常！原因是：{}", Json.pretty(e.getSuppressed()));
        log.error("发生业务异常！原因是：{}", Json.pretty(e.getSuppressed()));
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
        logger.error("发生空指针异常！原因是:{}", Json.pretty(e.getSuppressed()));
        log.error("发生空指针异常！原因是:{}", e);
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
        e.printStackTrace();
        logger.error("未知异常！原因是:{}", Json.pretty("cause:"+e.getCause()+",suppressed:"+ Arrays.toString(e.getSuppressed())));
        log.error("未知异常！原因是:{}", Json.pretty("cause:"+e.getCause()+",suppressed:"+ Arrays.toString(e.getSuppressed())));
        return CommonResult.error(ResultCode.FAILURE,e.getMessage());
    }


    //参数类型不匹配
//getPropertyName()获取数据类型不匹配参数名称
//getRequiredType()实际要求客户端传递的数据类型
    @ExceptionHandler({TypeMismatchException.class})
    public ApiResponse requestTypeMismatch(TypeMismatchException ex) {
        logger.error("参数类型不匹配！原因是:{}", Json.pretty(ex.getSuppressed()));
        log.error("参数类型不匹配！原因是:{}", Json.pretty(ex.getSuppressed()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR, "参数类型不匹配,参数" + ex.getPropertyName() + "类型应该为" + ex.getRequiredType());
    }

    //缺少参数异常
//getParameterName() 缺少的参数名称
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ApiResponse requestMissingServletRequest(MissingServletRequestParameterException ex) {
        ex.printStackTrace();
        logger.error("缺少参数！原因是:{}", Json.pretty(ex.getSuppressed()));
        log.error("缺少参数！原因是:{}", Json.pretty(ex.getSuppressed()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR, "缺少必要参数,参数名称为" + ex.getParameterName());
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ApiResponse requestTypeMismatch(MethodArgumentTypeMismatchException ex){
        logger.error("参数类型不匹配！原因是:{}", Json.pretty(ex.getSuppressed()));
        log.error("参数类型不匹配！原因是:{}", Json.pretty(ex.getSuppressed()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR, "参数类型不匹配,参数" + ex.getPropertyName() + "类型应该为" + ex.getRequiredType());
    }

    /**
     * 请求method不匹配
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ApiResponse requestMissingServletRequest(HttpRequestMethodNotSupportedException ex) {
        logger.error("请求method不匹配！原因是:{}", Json.pretty(ex.getSuppressed()));
        log.error("请求method不匹配！原因是:{}", Json.pretty(ex.getSuppressed()));
        return CommonResult.error(ResultCode.REQUEST_ERROR, "不支持"+ex.getMethod()+"方法，支持"+ StringUtils.join(ex.getSupportedMethods(), ",")+"类型");
    }

    /**
     *
     * 控制器方法中@RequestBody类型参数数据类型转换异常
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ApiResponse httpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest wq){
        e.printStackTrace();
        logger.error("控制器方法中@RequestBody类型参数数据类型转换异常！原因是:{}", Json.pretty(e.getSuppressed()));
        log.error("控制器方法中@RequestBody类型参数数据类型转换异常！原因是:{}", Json.pretty(e.getSuppressed()));
        return CommonResult.error(ResultCode.PARAMETER_ERROR,"参数数据类型转换异常,参数:"+e.getHttpInputMessage());
    }

    /***
     * 参数异常 -- ConstraintViolationException()
     * 用于处理类似http://localhost:8080/user/getUser?age=30&name=yoyo请求中age和name的校验引发的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ApiResponse urlParametersExceptionHandle(ConstraintViolationException e) {
        e.printStackTrace();
        logger.error("【请求参数异常】:{}", Json.pretty(e.getSuppressed()));
        log.error("【请求参数异常】:{}", Json.pretty(e.getSuppressed()));
        //收集所有错误信息
        List<String> errorMsg = e.getConstraintViolations()
                .stream().map(s -> s.getMessage()).collect(Collectors.toList());

        StringBuffer sb = new StringBuffer(errorMsg.size() * 16);
        for (int i = 0; i < errorMsg.size(); i++) {

            if (i > 0) {
                sb.append(";");
            }
            String s = errorMsg.get(i);
            sb.append(s);
        }
        return CommonResult.error(ResultCode.PARAMETER_ERROR,sb.toString());
    }

}
