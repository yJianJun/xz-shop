package com.cdzg.xzshop.filter;

import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.xzshop.filter.auth.*;
import com.cdzg.xzshop.utils.OAuthIgnoreUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.utils.core.api.ApiConst;
import com.framework.utils.core.api.ApiResponse;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 登录拦截器，名称为filter2，filter1预留给加密解密及签名校验
 *
 * @version 1.0.0
 */
@WebFilter(urlPatterns = {"/app/*"}, filterName = "filter2Login")
@Order(-1)
public class Filter2LoginApp implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(Filter2LoginApp.class);

    @Autowired
    private RedisTemplate redisTemplate;  //使用RedisTemplate操作redis

    @Value("#{'${customer.login.nofilters: }'.split(',')}")
    private List<String> notFilterUrls;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Set<String> stringSet = Sets.newHashSet();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter2LoginApp.stringSet = OAuthIgnoreUtils.getMobileOAuthod("com.cdzg");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if ("OPTIONS".equalsIgnoreCase(SpringSessionUtils.getRequest().getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        String serverPth = req.getServletPath();
        if (CollectionUtils.isNotEmpty(notFilterUrls) && (notFilterUrls.stream().anyMatch(
                filterUrl -> PatternMatchUtils.simpleMatch(filterUrl, SpringSessionUtils.getRequest().getRequestURI()))
                || FilterConstants.INTERNEL_IGNORE_URLS.stream().anyMatch(filterUrl -> PatternMatchUtils
                .simpleMatch(filterUrl, SpringSessionUtils.getRequest().getRequestURI())))) {
            setSession(request);
            chain.doFilter(request, response);
            return;
        }
//        //需要验证的数据
        if (serverPth.startsWith("/")) {
            serverPth = serverPth.substring(1);
        }
        if (!contains(serverPth)) {
            setSession(request);
            chain.doFilter(request, response);
            return;
        }
        //对token验证
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String InterfaceToken = httpRequest.getHeader("token");
        Object o = redisTemplate.opsForValue().get("appToken:" + InterfaceToken);
        if (o == null) {
            HttpServletResponse responses = SpringSessionUtils.getResponse();
            RequestUtil.writeJsonToResponseCos(ApiResponse.buildResponse(ApiConst.Code.CODE_NO_SESSION, "登录已过期！请重新登录"),
                    SpringSessionUtils.getRequest().getHeader("origin"), responses);
            return;
        }
        CustomerLoginResponse customerLoginResponse = objectMapper.convertValue(o, CustomerLoginResponse.class);
        customerLoginResponse.setTicketString(InterfaceToken);
        SpringSessionUtils.setSession(LoginConstants.Session.WEBAPI_SESSION_USER, customerLoginResponse);
        chain.doFilter(request, response);
    }

    private void setSession(ServletRequest request){
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String InterfaceToken = httpRequest.getHeader("token");
        Object o = redisTemplate.opsForValue().get("appToken:" + InterfaceToken);
        if (o != null) {
            CustomerLoginResponse customerLoginResponse = objectMapper.convertValue(o, CustomerLoginResponse.class);
            customerLoginResponse.setTicketString(InterfaceToken);
            SpringSessionUtils.setSession(LoginConstants.Session.WEBAPI_SESSION_USER, customerLoginResponse);
        }
    }
    @Override
    public void destroy() {

    }

    /**
     * @param serverPath 请求的路径
     * @param path       reset 风格的数据
     * @return
     */
    private static boolean maxServerPath(String serverPath, String path) {
        char[] serverPathCharts = serverPath.toCharArray(), pathCharts = path.toCharArray();
        int pointA = -1, pointB = -1;
        char cL = "{".toCharArray()[0], x = "/".toCharArray()[0], rL = "}".toCharArray()[0];
        int length = Math.max(serverPath.length(), path.length());
        for (int i = 0; i < length; i++) {
            pointA += 1;
            pointB += 1;
            if (pointB < pathCharts.length && cL == pathCharts[pointB]) {
                for (int j = pointB; j < pathCharts.length; j++) {
                    //  {xxx}/这种情况
                    pointB += 1;
                    if (rL == pathCharts[j]) {
                        break;
                    }
                }
                for (int j = pointA; j < serverPathCharts.length; j++) {
                    pointA += 1;
                    if (x == serverPathCharts[j]) {
                        pointA -= 1;
                        break;
                    }
                }
            }
            if (pointA < serverPathCharts.length && pointB < pathCharts.length) {
                if (serverPathCharts[pointA] != pathCharts[pointB]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param serverPath
     * @param path
     * @return
     */
    public static boolean contains(String serverPath, String path) {
        if (serverPath.equals(path)) {
            return true;
        }
        return serverPath.length() > path.length() ? maxServerPath(serverPath, path) : maxServerPath(serverPath, path);
    }

    /**
     * 是否是包含
     *
     * @param serverPth
     * @return
     */
    public static boolean contains(String serverPth) {
        for (String s : stringSet) {
            boolean contains = contains(serverPth, s);
            if (contains) {
                return true;
            }
        }
        return false;
    }


}
