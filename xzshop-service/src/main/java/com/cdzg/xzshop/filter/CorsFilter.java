package com.cdzg.xzshop.filter;

/**
 * Created by xiaoyao on 17-6-10.
 */

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"}, filterName = "corsFilter")
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authorization,keeplogintoken,keepLoginToken,matchid,version,sgin,tokenId,token,Access-Control-Allow-Origin," +
                "Access-Control-Allow-Methods,Access-Control-Max-Age,authorization,Content-Type");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }

}