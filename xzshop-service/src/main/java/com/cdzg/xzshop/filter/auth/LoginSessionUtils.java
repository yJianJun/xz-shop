package com.cdzg.xzshop.filter.auth;

import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.framework.utils.core.api.ApiResponse;

import java.util.Objects;

/**
 * 登录工具类
 * @version 1.0.0
 */
public class LoginSessionUtils {

    /**
     * 获取session中的用户信息
     * @return 
     */
    public static CustomerLoginResponse getAppUser() {
        Object session = SpringSessionUtils.getSession(LoginConstants.Session.WEBAPI_SESSION_USER);
        if (Objects.isNull(session)){
            throw new BaseException(ResultCode.UNAUTHORIZED);
        }
        return (CustomerLoginResponse) session;
    }

    /**
     * 获取session中的用户信息
     * @return
     */
    public static UserLoginResponse getAdminUser() {
        Object session = SpringSessionUtils.getSession(LoginConstants.Session.WEBAPI_SESSION_ADMIN);
        if (session == null) {
            throw new BaseException(ResultCode.UNAUTHORIZED);
        }
        return (UserLoginResponse) session;
    }

    /**
     * 判断是否超管
     * @return
     */
    public static boolean isAdmin() {
        return getAdminUser().getIsAdmin();
    }


}
