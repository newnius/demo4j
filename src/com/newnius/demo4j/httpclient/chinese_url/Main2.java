package com.newnius.demo4j.httpclient.chinese_url;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by newnius on 4/25/17.
 *
 */
public class Main2 {
    public static void main(String[] args) throws MalformedURLException {

        String url;
        CRSpider spider = new CRSpider();

        url = "http://tzb.hit.edu.cn/全国政协办公厅/main.psp";
        url = StringUtils.encodeUrl(new URL(url), Charset.forName("utf-8"));

        //url = "http://oa2.jlu.edu.cn/intro.asp?s=电子地图#ahahha";
        //url = StringUtils.encodeUrl(new URL(url), Charset.forName("gb2312"));

        url = "http://apache.local/中文utf8.php?key=中文";
        url = StringUtils.encodeUrl(new URL(url), Charset.forName("gb2312"));

        spider.doGet(url);
        System.out.println(spider.getStatusCode());
        System.out.println(spider.getErrMsg());
        System.out.println(spider.getHtml());
    }
}
