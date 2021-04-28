package com.cdzg.xzshop.common;

public enum ResultCode {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    FAILED(400, "操作失败"),
    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(405, "参数检验失败"),
    /**
     * 请重新登录
     */
    UNAUTHORIZED(401, "请重新登录！"),
    /**
     * 您已在另一台设备登录，本次登录已下线!
     */
    EXPIRE(402, "您已在另一台设备登录，本次登录已下线!"),
    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),
    /**
     * 数据问题
     */
    DATA_ERROR(405, "系统未找到请求的数据！"),
    /**
     * 当前用户未设置交易密码
     */
    PASSWORD_NOT_SET(410, "当前用户未设置交易密码!"),
    /**
     * 系统错误
     */
    FAILURE(500, "系统错误"),
    /**
     * 非法操作
     */
    Illegal(501, "非法操作！"),
    /**
     * 账户余额不足
     */
    INSUFFICIENT_FUNDS(510, "账户余额不足"),
    /**
     * 支付密码错误
     */
    PAY_PWD_ERROR(511, "支付密码错误"),
    /**
     * 您任务时间还没到哦
     */
    TASK_TIME_NOT_UP(512, "您任务时间还没到哦"),
    /**
     * 您已经领取过该动态的红包了
     */
    RECEIVED_RED_ENVELOPES(513, "您已经领取过该动态的红包了"),
    /**
     * 红包被抢完了
     */
    ROBBERY_OVER(515, "红包被抢完了"),
    /**
     * 领取失败
     */
    ACQUISITION_FAILED(516, "领取失败"),
    /**
     * 动态不存在
     */
    DYNAMIC_DOES_NOT_EXIST(521, "动态不存在"),
    /**
     * 参数有误
     */
    PARAMETER_ERROR(522, "参数有误"),
    /**
     * 未抢到红包，请稍后重试
     */
    PLEASE_AGAIN_LATER(523, "未抢到红包，请稍后重试"),
    ;

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
