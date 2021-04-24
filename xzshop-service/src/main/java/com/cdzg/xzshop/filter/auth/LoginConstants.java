package com.cdzg.xzshop.filter.auth;

/**
 * 登录常量类
 * @version 1.0.0
 */
public class LoginConstants {
    
    /**
     * session常量
     * @version 1.0.0
     */
    public static final class Session {
        
        /**
         * app的session 用户信息key
         */
        public static final String WEBAPI_SESSION_USER = "xz_app_session_user";

        /**
         * session 用户信息key
         */
        public static final String WEBAPI_SESSION_ADMIN = "xz_admin_session_user";

        public static final String IS_SINGLETION = "is_singletion";
        
        /**
         * session ticket信息
         */
        public static final String WEBAPI_SESSION_TICKET = "webapi_session_ticket";
    }
}
