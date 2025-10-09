package com.riecardx.edgex.utils;

import java.util.HashMap;
import java.util.Map;

public class PublicUtils {
    public static final String base_url = "https://pro.edgex.exchange";
    public static final String url_getDepth = "/api/v1/public/quote/getDepth";
    public static final String url_getMetaData = "/api/v1/public/meta/getMetaData";

    /**
     * GET Meta Data
     * GET /api/v1/public/meta/getMetaData
     */
    public static String getMetaData() {
        return HttpUtils.get(base_url + url_getMetaData, null);
    }

    /**
     * GET Query Order Book Depth
     * GET /api/v1/public/quote/getDepth
     */
    public static String getDepth(String contractId) {
        return HttpUtils.get(base_url + url_getDepth+"?level=15&contractId="+contractId, null);
    }


}
