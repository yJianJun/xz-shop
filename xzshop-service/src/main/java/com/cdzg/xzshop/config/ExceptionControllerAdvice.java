package com.cdzg.xzshop.config;



import com.framework.utils.core.api.ApiResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 参数校验 返回数据自定义json ApiResponse
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse bindExceptionHandler(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        StringBuffer sb = new StringBuffer(bindingResult.getFieldErrors().size()*16);
        for(int i=0;i<bindingResult.getFieldErrors().size();i++){
            if(i>0){
                sb.append(";");
            }
            FieldError fieldError = bindingResult.getFieldErrors().get(i);
            sb.append(fieldError.getDefaultMessage());
        }

        return ApiResponse.buildResponse(206,sb.toString());
    }
}
