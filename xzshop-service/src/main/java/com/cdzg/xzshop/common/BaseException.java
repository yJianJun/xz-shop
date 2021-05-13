package com.cdzg.xzshop.common;

/**
 * Created by xqlu on 2019/3/21.
 *
 * @author xqlu
 * @description 异常基类
 * @modify
 */
public class BaseException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected Integer errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BaseException() {
        super();
    }

    public BaseException(ResultCode errorInfoInterface) {
        this(errorInfoInterface,null);
    }

    public BaseException(String errorMsg) {
        this(ResultCode.FAILED.getCode(),errorMsg);
    }

    public BaseException(Integer errorCode, String errorMsg) {
        this(errorCode,errorMsg,null);
    }

    public BaseException(ResultCode errorInfoInterface, Throwable cause) {
        this(errorInfoInterface.getCode(),errorInfoInterface.getMessage(),cause);
    }

    public BaseException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
