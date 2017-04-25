package com.newnius.demo4j.httpclient.charset;

/**
 * Created by newnius on 4/25/17.
 *
 */
public class Main {
    public static void main(String[] args){
        String url;
        CRSpider spider = new CRSpider();

        url = "http://www.arch.tsinghua.edu.cn/chs/"; //set charset in http-equiv (gbk)
        spider.doGet(url);
        System.out.println(spider.getStatusCode());
        System.out.println(spider.getErrMsg());
        System.out.println(spider.getHtml());

        url = "http://jt.jlu.edu.cn/default.php";// set charset in header (gbk)
        spider.doGet(url);
        System.out.println(spider.getStatusCode());
        System.out.println(spider.getErrMsg());
        System.out.println(spider.getHtml());

        url = "http://www.civil.tsinghua.edu.cn/"; //set charset in meta charset (utf-8)
        spider.doGet(url);
        System.out.println(spider.getStatusCode());
        System.out.println(spider.getErrMsg());
        System.out.println(spider.getHtml());
    }
}
