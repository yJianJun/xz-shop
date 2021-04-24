package com.cdzg.xzshop.filter.auth;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * 请求工具类
 * 
 * @version 1.0.0
 */
public class RequestUtil {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 是否为Ajax请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
	}

	/**
	 * 是否为React的fetch请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isReactRequest(HttpServletRequest request) {
		return request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("react");
	}

	/**
	 * 获取访问客户端IP
	 * 
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.contains(",")) {
			// 多个IP，仅获取第一个
			ip = ip.split(",")[0];
		}
		return ip;
	}
	
	/**
	 * 通过HttpServletRequest获取参数的json值
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getJsonFromRequest(HttpServletRequest request) throws IOException {
		String contentType = request.getContentType();
		if (StringUtils.isEmpty(contentType)
				|| StringUtils.substringMatch(contentType, 0, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
			return convertFormToString(request);
		} else if (StringUtils.substringMatch(contentType, 0, MediaType.APPLICATION_JSON_VALUE)) {
			return convertInputStreamToString(request.getInputStream());
		}
		return null;
	}
	
	private static String convertFormToString(HttpServletRequest request) {
		Map<String, String> result = new TreeMap<String, String>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			result.put(name, request.getParameter(name));
		}
		try {
		    if(CollectionUtils.isEmpty(result)) {
				return "";
			}
			return objectMapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		return StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
	}

	/**
	 * 输出json数据
	 * 
	 * @param content
	 * @param response
	 */
	public static void writeJsonToResponse(Object content, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		try {
			if (content instanceof String) {
				response.getWriter().write((String) content);
			} else {
				response.getWriter().write(JSONObject.toJSONString(content));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 跨域输出JSON数据
	 * 
	 * @param content
	 * @param response
	 * @param origin   request请求头origin
	 */
	public static void writeJsonToResponseCos(Object content, String origin, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", StringUtils.isEmpty(origin) ? "*" : origin);
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE");
		try {
			if (content instanceof String) {
				response.getWriter().write((String) content);
			} else {
				response.getWriter().write(JSONObject.toJSONString(content));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
