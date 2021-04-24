package com.cdzg.xzshop.filter.auth;

import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.universal.vo.response.user.UserLoginResponse;

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
        return (CustomerLoginResponse) SpringSessionUtils.getSession(LoginConstants.Session.WEBAPI_SESSION_USER);
    }

    /**
     * 获取session中的用户信息
     * @return
     */
    public static UserLoginResponse getAdminUser() {
        return (UserLoginResponse) SpringSessionUtils.getSession(LoginConstants.Session.WEBAPI_SESSION_ADMIN);
    }
}
