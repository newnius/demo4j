package com.newnius.demo4j.httpclient.redirect;

import java.net.MalformedURLException;

/**
 * Created by newnius on 4/25/17.
 *
 */
public class Main3 {
    public static void main(String[] args) throws MalformedURLException {

        String url;
        CRSpider spider = new CRSpider();

        url = "http://tzb.hit.edu.cn/全国政协办公厅";
        url = "http://s.newnius.com/5xbMH";

        spider.doGet(url);
        System.out.println(spider.getStatusCode());
        System.out.println(spider.getErrMsg());
        System.out.println(spider.getHtml());
    }
}
