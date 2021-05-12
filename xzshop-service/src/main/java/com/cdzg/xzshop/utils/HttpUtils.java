/**************************************************************
* Copyright©2017-2017 by hentica All Rights Reserved
* 创建日期：2017年8月25日 下午2:23:30
* 作       者：bbliu
* 功能描述：http请求
**************************************************************/

package com.cdzg.xzshop.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bbliu
 *
 */
public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/** 请求header中的contentType */
	/** 表单 */
	public static final String CONTENT_TYPE_FORM_DATA = "application/x-www-form-urlencoded";
	/** JSON */
	public static final String CONTENT_TYPE_JSON = "application/json";

	/** 请求方式 */
	/** POST */
	public static final String METHOD_POST = "POST";
	/** GET */
	public static final String METHOD_GET = "GET";
	/** PATCH */
	public static final String METHOD_PATCH = "PATCH";
	/** PUT */
	public static final String METHOD_PUT = "PUT";

	/**
	 * http的GET请求
	 *
	 * @param requestUrl
	 * @param param
	 * @return
	 */
	public static String get(String requestUrl, String param) {

		return httpSync(requestUrl, param, "GET");
	}

	/**
	 * http的POST请求
	 *
	 * @param requestUrl
	 * @param param
	 * @return
	 */
	public static String post(String requestUrl, String param) {

		return httpSync(requestUrl, param, "POST");
	}

	/**
	 * http的POST请求
	 *
	 * @param requestUrl
	 * @param param
	 * @return
	 */
	public static String post(String requestUrl, Map<String, Object> param) {

		return httpSync(requestUrl, CONTENT_TYPE_JSON, "POST", null, param);
	}

	/**
	 * http请求
	 *
	 * @param requestUrl
	 * @param param
	 * @param method
	 * @return
	 */
	public static String httpSync(String requestUrl, String param, String method) {

		logger.debug(requestUrl + "==>" + param + "==>" + method);

		try {
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(method);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (!StringUtils.isBlank(param)) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(param.getBytes("utf-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();

			return buffer.toString();
		} catch (ConnectException ce) {
			logger.error("连接超时：", ce);
		} catch (Exception e) {
			logger.error("http请求异常：", e);
		}

		return null;
	}

	/**
	 *
	 * @param requestUrl
	 * @param contentType
	 * @param method
	 * @param headerMap
	 * @param paramMap
	 * @return
	 */
	public static String put(String requestUrl, String method, String contentType, Map<String, String> headerMap,
			Map<String, Object> paramMap) {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpEntityEnclosingRequestBase request = null;

		if (METHOD_PATCH.equals(method)) {
			request = new HttpPatch(requestUrl);
		} else {
			request = new HttpPut(requestUrl);
		}
		request.setHeader("Content-Type", contentType);
		request.setHeader("Charset", "utf-8");
		request.setHeader("Accept-Charset", "utf-8");

		if (headerMap != null && headerMap.size() > 0) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			if (paramMap != null && paramMap.size() > 0) {
				String param = "";

				if (CONTENT_TYPE_JSON.equalsIgnoreCase(contentType)) {
					param = JsonMapper.toJsonString(paramMap);
				} else {
					for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
						if (StringUtils.isBlank(param)) {
							param = entry.getKey() + "=" + String.valueOf(entry.getValue());
						} else {
							param += "&" + entry.getKey() + "=" + String.valueOf(entry.getValue());
						}
					}
				}

				if (!StringUtils.isBlank(param)) {
					request.setEntity(new StringEntity(param, "utf-8"));
				}
			}
			HttpResponse response = httpClient.execute(request);

			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 *
	 * @param requestUrl
	 * @param contentType
	 * @param
	 * @param headerMap
	 * @param paramMap
	 * @return
	 */
	public static String post(String requestUrl, String contentType, Map<String, String> headerMap,
			Map<String, Object> paramMap, String responseHeaderKey) {

		HttpPost request = new HttpPost(requestUrl);
		request.setHeader("Content-Type", contentType);
		request.setHeader("Charset", "utf-8");
		request.setHeader("Accept-Charset", "utf-8");

		if (headerMap != null && headerMap.size() > 0) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			if (paramMap != null && paramMap.size() > 0) {
				String param = "";

				if (CONTENT_TYPE_JSON.equalsIgnoreCase(contentType)) {
					param = JsonMapper.toJsonString(paramMap);
				} else {
					for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
						if (StringUtils.isBlank(param)) {
							param = entry.getKey() + "=" + String.valueOf(entry.getValue());
						} else {
							param += "&" + entry.getKey() + "=" + String.valueOf(entry.getValue());
						}
					}
				}

				if (!StringUtils.isBlank(param)) {
					request.setEntity(new StringEntity(param, "utf-8"));
				}
			}
			CookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

			HttpResponse response = httpClient.execute(request);

			if (StringUtils.isBlank(responseHeaderKey)) {
				return EntityUtils.toString(response.getEntity());
			} else {
				List<Cookie> cookies = cookieStore.getCookies();

				for (int i = 0; i < cookies.size(); i++) {
					Cookie item = cookies.get(i);

					if (item == null) {
						return null;
					}
					if (responseHeaderKey.equalsIgnoreCase(item.getName())) {
						return item.getValue();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 *
	 * @param requestUrl
	 * @param contentType
	 * @param
	 * @param headerMap
	 * @param paramMap
	 * @return
	 */
	public static String get(String requestUrl, String contentType, Map<String, String> headerMap,
			Map<String, Object> paramMap) {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			if (paramMap != null && paramMap.size() > 0) {
				String param = "";
				for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
					if (StringUtils.isBlank(param)) {
						param = entry.getKey() + "=" + String.valueOf(entry.getValue());
					} else {
						param += "&" + entry.getKey() + "=" + String.valueOf(entry.getValue());
					}
				}

				if (!StringUtils.isBlank(param)) {
					requestUrl += "?" + param;
				}
			}
			HttpGet request = new HttpGet(requestUrl);

			request.setHeader("Content-Type", contentType);
			request.setHeader("Charset", "utf-8");
			request.setHeader("Accept-Charset", "utf-8");

			if (headerMap != null && headerMap.size() > 0) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					request.setHeader(entry.getKey(), entry.getValue());
				}
			}

			HttpResponse response = httpClient.execute(request);

			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * http请求
	 *
	 * @param requestUrl
	 * @param
	 * @param method
	 * @return
	 */
	public static String httpSync(String requestUrl, String contentType, String method, Map<String, String> headerMap,
			Map<String, Object> paramMap) {

		return httpSync(requestUrl, contentType, method, headerMap, paramMap, "");
	}

	/**
	 * http请求
	 *
	 * @param requestUrl
	 * @param
	 * @param method
	 * @return
	 */
	public static String httpSync(String requestUrl, String contentType, String method, Map<String, String> headerMap,
			Map<String, Object> paramMap, String responseHeaderKey) {

		if (Lists.newArrayList(METHOD_PATCH, METHOD_PUT).contains(method)) {
			return put(requestUrl, method, contentType, headerMap, paramMap);
		} else if (METHOD_POST.equals(method)) {
			return post(requestUrl, contentType, headerMap, paramMap, responseHeaderKey);
		} else if (METHOD_GET.equals(method)) {
			return get(requestUrl, contentType, headerMap, paramMap);
		}

		return null;
	}

	/**
	 * http异步请求(POST请求)
	 *
	 * @param requestUrl
	 * @param paramMap
	 * @param callback
	 */
//	public static void httpAsync(String requestUrl, Map<String, String> paramMap, final AsyncInterface callback) {
//
//		logger.debug(requestUrl + "==>" + paramMap.toString());
//
//		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
//		httpclient.start();
//
//		final CountDownLatch latch = new CountDownLatch(1);
//		final HttpPost request = new HttpPost(requestUrl);
//
//		if (paramMap != null && paramMap.size() > 0) {
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
//				params.add(new BasicNameValuePair(entry.getMchKey(), entry.getValue()));
//			}
//			try {
//				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		httpclient.execute(request, new FutureCallback<HttpResponse>() {
//
//			@Override
//			public void completed(final HttpResponse response) {
//				latch.countDown();
//				try {
//					String content = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//					if (callback != null) {
//						callback.completed(content);
//					}
//					return;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				if (callback != null) {
//					callback.failed(new Exception("请求数据失败！"));
//				}
//			}
//
//			@Override
//			public void failed(final Exception ex) {
//				latch.countDown();
//
//				if (callback != null) {
//					callback.failed(ex);
//				}
//			}
//
//			@Override
//			public void cancelled() {
//				latch.countDown();
//				if (callback != null) {
//					callback.cancelled();
//				}
//			}
//
//		});
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			httpclient.close();
//		} catch (IOException ignore) {
//
//		}
//	}

	/**
	 * 下载文件
	 *
	 * @param downloadUrl
	 * @param savePath
	 * @return
	 */
	public static String downloadFile(String downloadUrl, String savePath) {
		// 下载网络文件
		int bytesum = 0;
		int byteread = 0;

		try {
			URL url = new URL(downloadUrl);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(savePath);

			byte[] buffer = new byte[1204];
			// int length;

			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				logger.debug("下载数目：" + bytesum);
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();

			return savePath;
		} catch (Exception e) {
			logger.error("", e);
		}

		return "";
	}
}
