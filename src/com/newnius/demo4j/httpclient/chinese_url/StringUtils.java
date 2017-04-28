package com.newnius.demo4j.httpclient.chinese_url;

/**
 * Created by newnius on 4/28/17.
 *
 */
public class StringUtils {


    /*
    *
    * handle with white space and Chinese characters
    * From: http://www.jianshu.com/p/9be694c8fee2
    * */
    public static String encodeUrl(String url, String charset){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes(charset);
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (byte aB : b) {
                    int k = aB;
                    if (k < 0)
                        k += 256;
                    sb.append("%").append(Integer.toHexString(k).toUpperCase());
                }
            }
        }
        url = sb.toString();
        //fix url encode bug
        url = url.replaceAll(" ", "%20");
        return url;
    }
}
