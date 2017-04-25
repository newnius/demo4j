package com.newnius.demo4j.httpclient.charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
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

	private String charset = null;

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

	/*
	* doGet
	* auto fill cookie, referer, convert charset, choose UA
	* */
	public void doGet(String url) {
        logger.info(url);
		try {
			statusCode = 0;
			errMsg = null;
			charset = null;
			HttpGet httpGet = new HttpGet();
			httpGet.setURI(new URI(url));
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(httpGet);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if(entity==null){
				errMsg = "Entity is null";
				return;
			}
			parseHeaders(entity);
         	parseBody(entity);
		}catch (Exception ex){
			errMsg = ex.getMessage();
		}
	}

	/*
	 *
	 * filter out unwanted Mime Type and get page charset
	 */
	private void parseHeaders(HttpEntity entity){
		ContentType contentType = ContentType.get(entity);
		Charset cs = contentType.getCharset();
		if (cs != null) {
			charset = cs.toString();
			logger.info("Detect charset in header:"+charset);
		}
	}


	/*
	* convert encoding to utf-8
	* Ref: http://justdo2008.iteye.com/blog/463753
	* */
	private void parseBody(HttpEntity entity){
		try {
			InputStream is = entity.getContent();
			ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
			byte[] temp = new byte[1024];
			int size;
			while ((size = is.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			if (charset == null) {
				Document doc = Jsoup.parse(os.toString());
				Elements metaTags = doc.getElementsByTag("meta");
				for (Element metaTag : metaTags) {
					String content = metaTag.attr("content");
					String http_equiv = metaTag.attr("http-equiv");
					charset = metaTag.attr("charset");
					if (!charset.isEmpty()) {
                        logger.info("Detect charset in meta:"+charset);
						break;
					}
					if (http_equiv.toLowerCase().equals("content-type")) {
						charset = content.substring(content.toLowerCase().indexOf("charset") + "charset=".length());
                        logger.info("Detect charset in http-equiv:"+charset);
						break;
					}
				}
				if (charset == null || charset.isEmpty())
					charset = "utf-8";
			}
			html = new String(os.toByteArray(), charset);
		}catch (Exception ex){
			errMsg = ex.getMessage();
		}
	}
}
