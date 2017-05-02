package com.newnius.demo4j.httpclient.proxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

/**
 * 
 * get or post to url to get resources many customized options
 * 
 * @author Newnius
 * @version 0.1.0
 * 	Dependencies:
 * 		com.newnius.util.CRLogger
 * 		com.newnius.util.CRErrorCode
 */
public class CRSpider {
	private static final String TAG = "CRSpider";
	private Logger logger;

	private String html;

	private int statusCode;

	private String errMsg;

	public CRSpider() {
		logger = Logger.getLogger(TAG);
	}

	public String getHtml(){
		return html;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	private Proxy.Type proxy_type = Proxy.Type.DIRECT;

	private String proxy_host;

	private int proxy_port;

	private HttpClientContext context;

	public CRSpider setProxy(Proxy.Type type, String host, int port) {
		proxy_type = type;
		proxy_host = host;
		proxy_port = port;
		return this;
	}

	/*
	* doGet
	* auto fill cookie, referer, convert charset, choose UA
	* */
	public void doGet(String url) {
        logger.info(url);
		try {
			statusCode = 0;
			errMsg = null;
            context = HttpClientContext.create();
			HttpGet httpGet = new HttpGet();
			httpGet.setURI(new URI(url));
			HttpClient httpClient = buildHttpClient();
			HttpResponse response = httpClient.execute(httpGet, context);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if(entity==null){
				errMsg = "Entity is null";
				return;
			}
            byte[] bytes = EntityUtils.toByteArray(entity);
            html = new String(bytes);
		}catch (Exception ex) {
			errMsg = ex.getMessage();
		}
	}

	/*
    * build HttpClient by different proxies
    * disable Follow redirects cause They can not handle Url properly witch having Chinese and in other charsets
    * Ref: https://my.oschina.net/SmilePlus/blog/682198
    * */
	private HttpClient buildHttpClient(){
		HttpClient httpClient;
		RequestConfig config = RequestConfig.custom().build();
		if(proxy_type == Proxy.Type.SOCKS){
			Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", new PlainConnectionSocketFactory(){
						@Override
						public Socket createSocket(HttpContext context) throws IOException {
							InetSocketAddress socksAddr = (InetSocketAddress) context.getAttribute("socks.address");
							Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksAddr);
							return new Socket(proxy);
						}
						@Override
						public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
							// Convert address to unresolved
							InetSocketAddress unresolvedRemote = InetSocketAddress
									.createUnresolved(host.getHostName(), remoteAddress.getPort());
							return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
						}
					})
					.register("https", new SSLConnectionSocketFactory(SSLContexts.createSystemDefault()){
						@Override
						public Socket createSocket(HttpContext context) throws IOException {
							InetSocketAddress socksAddr = (InetSocketAddress) context.getAttribute("socks.address");
							Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksAddr);
							return new Socket(proxy);
						}
					})
					.build();

			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
			httpClient = HttpClients.custom()
					.setDefaultRequestConfig(config)
					.setConnectionManager(cm)
					.build();
			InetSocketAddress socksAddr = new InetSocketAddress(proxy_host, proxy_port);
			context.setAttribute("socks.address", socksAddr);

		}else if(proxy_type == Proxy.Type.HTTP){
			HttpHost proxy = new HttpHost(proxy_host, proxy_port, null);
			httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config)
					.setProxy(proxy)
					.build();
		}else{ //direct
			httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		}
		return httpClient;
	}

}
