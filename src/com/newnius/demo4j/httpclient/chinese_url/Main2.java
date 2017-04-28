package com.newnius.demo4j.httpclient.chinese_url;

/**
 * Created by newnius on 4/25/17.
 *
 */
public class Main2 {
    public static void main(String[] args){

        String url;
        CRSpider spider = new CRSpider();

        url = "http://tzb.hit.edu.cn/全国政协办公厅/main.psp";
        url = StringUtils.encodeUrl(url, "utf-8");

        //url = "http://oa2.jlu.edu.cn/intro.asp?s=电子地图&ss=前卫校区南区";
        //url = StringUtils.encodeUrl(url, "gb2312");
        spider.doGet(url);
        System.out.println(spider.getStatusCode());
        System.out.println(spider.getErrMsg());
        System.out.println(spider.getHtml());
    }
}
