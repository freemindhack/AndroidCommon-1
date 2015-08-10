/** Copyright © 2015-2020 100msh.com All Rights Reserved */
package com.bmsh.common.util;

import java.io.IOException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * HTTP请求管理类
 * 
 * @author Frank
 * @date 2015年8月6日下午3:53:40
 */

public class HttpUtils {

	/**
	 * 请求超时
	 */
	private static final int RESPONSE_TIMEOUT = 30 * 1000;

	/**
	 * 响应超时
	 */
	private static final int REQUEST_TIMEOUT = 10 * 1000;

	/**
	 * 从连接池中取连接的超时时间
	 */
	private static final int CONN_MANAGER_TIMEOUT = 1000;

	/**
	 * 最大连接数
	 */
	private static final int DEFAULT_MAX_CONNECTIONS = 30;

	/**
	 * Socket超时时间
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;

	/**
	 * Socket默认buffer大小
	 */
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

	/**
	 * HTTP请求Client
	 */
	private static DefaultHttpClient sHttpClient;

	/**
	 * 请求参数
	 */
	final static HttpParams httpParams = new BasicHttpParams();

	/**
	 * 静态代码块
	 */
	static {
		ConnManagerParams.setTimeout(httpParams, CONN_MANAGER_TIMEOUT);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(10));
		ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpClientParams.setRedirecting(httpParams, true);
		HttpProtocolParams.setUserAgent(httpParams, "Android client");
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			schemeRegistry.register(new Scheme("https", sf, 443));
		} catch (Exception ex) {
		}

		BasicCookieStore cookieStore = new BasicCookieStore();
		ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
		sHttpClient = new DefaultHttpClient(manager, httpParams);
		sHttpClient.setCookieStore(cookieStore);
		sHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);
		sHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, RESPONSE_TIMEOUT);
		sHttpClient.getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, true);
	}

	/**
	 * Post请求
	 * 
	 * @param urlString
	 * @param body
	 *            正常的JSON数据Post
	 * @return
	 */
	public static HttpResponse postRequest(String urlString, String body) {
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(urlString);
			httpPost.setEntity(new StringEntity(body, HTTP.UTF_8));
			HttpResponse response = sHttpClient.execute(httpPost);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			if (httpPost != null) {
				httpPost.abort();
			}
		}
		return null;
	}

	/**
	 * Post请求
	 * 
	 * @param urlString
	 * @param paramsMap
	 *            表单数据Post
	 * @return
	 */
	public static HttpResponse postRequest(String urlString, Map<String, String> paramsMap) {
		HttpPost httpPost = null;
		try {
			// MAP转成List
			List<BasicNameValuePair> valuePairList = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				valuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			httpPost = new HttpPost(urlString);
			httpPost.setEntity(new UrlEncodedFormEntity(valuePairList, HTTP.UTF_8));
			HttpResponse response = sHttpClient.execute(httpPost);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			if (httpPost != null) {
				httpPost.abort();
			}
		}
		return null;
	}

	/**
	 * Get请求
	 * 
	 * @param urlString
	 * @param paramsMap
	 * @return
	 */
	public static HttpResponse getRequest(String urlString, Map<String, String> paramsMap) {
		HttpGet httpGet = null;
		try {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(urlString);
			if (null != paramsMap) {
				urlBuilder.append("?");
				Iterator<Entry<String, String>> iterator = paramsMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> param = iterator.next();
					urlBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8")).append('=')
							.append(URLEncoder.encode(param.getValue(), "UTF-8"));
					if (iterator.hasNext()) {
						urlBuilder.append('&');
					}
				}
			}
			httpGet = new HttpGet(urlBuilder.toString());
			HttpResponse response = sHttpClient.execute(httpGet);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			if (httpGet != null) {
				httpGet.abort();
			}
		}
		return null;
	}

	/**
	 * SSL相关
	 * 
	 * @author Frank
	 *
	 */
	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
