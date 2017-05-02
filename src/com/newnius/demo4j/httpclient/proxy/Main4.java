package com.newnius.demo4j.httpclient.proxy;

import java.net.MalformedURLException;
import java.net.Proxy;

/**
 * Created by newnius on 4/25/17.
 *
 */
public class Main4 {
    public static void main(String[] args) throws MalformedURLException {

        String url;
        CRSpider spider = new CRSpider();

        url = "http://ip.chinaz.com/getip.aspx";
        //spider.setProxy(Proxy.Type.SOCKS, "localhost", 1080);
        spider.setProxy(Proxy.Type.HTTP, "120.52.72.58", 80);
        //spider.setProxy(Proxy.Type.DIRECT, null, 1080);
        spider.doGet(url);
        System.out.println(spider.getStatusCode());
        System.out.println(spider.getErrMsg());
        System.out.println(spider.getHtml());
    }
}
