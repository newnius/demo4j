package com.newnius.demo4j.httpclient.redirect;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URL;
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
			HttpClient httpClient = HttpClientBuilder.create()
					.disableRedirectHandling()
					.build();
			HttpResponse response = httpClient.execute(httpGet);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if(entity==null){
				errMsg = "Entity is null";
				return;
			}
			parseHeaders(entity);

			if(statusCode==301 || statusCode ==302){
				String newUrl = response.getFirstHeader("Location").getValue();
				url = new URL(new URL(url), newUrl).toString();
				url = new String(url.getBytes("ISO-8859-1"), "utf-8");
				doGet(url);
				return;
			}
         	parseBody(entity);
		}catch (Exception ex) {
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
        if(charset==null){
            charset = "UTF-8";
        }
		try {
			byte[] bytes = EntityUtils.toByteArray(entity);
			html = new String(bytes, charset);
		}catch (Exception ex){
            errMsg = ex.getClass().getSimpleName()+":"+ex.getMessage();
		}
	}
}
