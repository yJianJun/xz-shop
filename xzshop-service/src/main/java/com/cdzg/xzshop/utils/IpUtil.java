package com.cdzg.xzshop.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author liqiang
 */
public final class IpUtil {
    /**
     * 获取客户端访问地址
     *
     * @return
     */
    public static String getClientIp() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        if (request == null) {
            return "";
        }
        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();

            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                /** 根据网卡取本机配置的IP */
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        /** 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割 */
        if (ipAddress != null && ipAddress.length() > 15) {
            /** "***.***.***.***".length() = 15 */
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }

        return ipAddress;
    }

    /**
     * 获取域名
     * http://96be1db6.ngrok.io/
     * @return
     */
    public static String getContextUrl(){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        if (request == null) {
            return "";
        }
        StringBuffer url = request.getRequestURL();
        if (url == null) {
            return "";
        }
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        return  tempContextUrl;
    }

    public static String getContextUrlNoSuffix(){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        if (request == null) {
            return "";
        }
        StringBuffer url = request.getRequestURL();
        if (url == null) {
            return "";
        }
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        return  tempContextUrl;
    }

}
