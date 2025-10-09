package com.riecardx.edgex.utils;

import cn.hutool.Hutool;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import okhttp3.OkHttpClient;

import java.util.Map;

public class HttpUtils {

    public static String get(String url, Map<String, String> headers) {
        HttpRequest httpRequest = HttpUtil.createGet(url).setProxy(null);
        if (headers!=null && headers.size()>0) {
            for (String key:headers.keySet()) {
                httpRequest.header(key, headers.get(key));
            }
        }
        return httpRequest.execute().body();
    }

    public static String get2(String url, Map<String, String> headers) {
        HttpRequest httpRequest = HttpUtil.createGet(url);
        if (headers!=null && headers.size()>0) {
            for (String key:headers.keySet()) {
                httpRequest.header(key, headers.get(key));
            }
        }
        return httpRequest.execute().body();
    }

    public static String post(String url, Map<String, String> headers, Map<String, Object> forms) {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        if (headers!=null && headers.size()>0) {
            for (String key:headers.keySet()) {
                httpRequest.header(key, headers.get(key));
            }
        }
        if (forms!=null && forms.size()>0) {
            httpRequest.form(forms);

        }
        return httpRequest.execute().body();
    }

    public static String post(String url, Map<String, String> headers, String bodyJson) {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        if (headers!=null && headers.size()>0) {
            for (String key:headers.keySet()) {
                httpRequest.header(key, headers.get(key));
            }
        }
        if (bodyJson!=null && bodyJson.length()>0) {
            httpRequest.body(bodyJson);

        }
        return httpRequest.execute().body();
    }

}
