package com.riecardx.edgex.utils;

import java.util.HashMap;
import java.util.Map;

public class AccountUtils {
    public static final String base_url = "https://pro.edgex.exchange";
    public static final String url_getPositionTermPage = "/api/v1/private/account/getPositionTermPage";
    public static final String url_getPositionTransactions = "/api/v1/private/account/getPositionTransactionById";
    public static final String url_getPositionTransactionPage = "/api/v1/private/account/getPositionTransactionPage";
    public static final String url_getCollateralTransactionPage = "/api/v1/private/account/getCollateralTransactionPage";
    public static final String url_getPositionByContractId = "/api/v1/private/account/getPositionByContractId";
    public static final String url_getAccountAsset = "/api/v1/private/account/getAccountAsset";

    /**
     * GET Account Asset
     * GET /api/v1/private/account/getAccountAsset
     */
    public static String getAccountAsset(String privateKeyHex,String accountId) {
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId, url_getAccountAsset);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-edgeX-Api-Timestamp", ""+timestamp);
        headerMap.put("X-edgeX-Api-Signature", sign);
        return HttpUtils.get(base_url + url_getAccountAsset+"?accountId="+accountId, headerMap);
    }

    /**
     * GET Get Position By Account ID and Contract ID
     * GET /api/v1/private/account/getPositionByContractId
     */
    public static String getPositionByContractId(String privateKeyHex,String accountId) {
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId+"&contractIdList=[10000001,10000002,10000003]", url_getPositionByContractId);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-edgeX-Api-Timestamp", ""+timestamp);
        headerMap.put("X-edgeX-Api-Signature", sign);
        return HttpUtils.get(base_url + url_getPositionByContractId+"?accountId="+accountId+"&contractIdList=[10000001,10000002,10000003]", headerMap);
    }

    /**
     * GET Get Collateral Transaction Page by Account ID
     * GET /api/v1/private/account/getCollateralTransactionPage
     */
    public static String getCollateralTransactionPage(String privateKeyHex,String accountId) {
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId+"&size=100", url_getCollateralTransactionPage);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-edgeX-Api-Timestamp", ""+timestamp);
        headerMap.put("X-edgeX-Api-Signature", sign);
        return HttpUtils.get(base_url + url_getCollateralTransactionPage+"?accountId="+accountId+"&size=100", headerMap);
    }

    /**
     * GET Get Position Transaction Page
     * GET /api/v1/private/account/getPositionTransactionPage
     */
    public static String getPositionTransactionPage(String privateKeyHex,String accountId) {
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId+"&filterCloseOnly=false&filterOpenOnly=true&size=100", url_getPositionTransactionPage);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-edgeX-Api-Timestamp", ""+timestamp);
        headerMap.put("X-edgeX-Api-Signature", sign);
        return HttpUtils.get(base_url + url_getPositionTransactionPage+"?accountId="+accountId+"&filterCloseOnly=false&filterOpenOnly=true&size=100", headerMap);
    }

    /**
     * 查询当前持仓 GET Get Position Transactions By Account ID and Transaction ID
     * @param privateKeyHex
     * @param accountId
     * @return
     */
    public static String getPositionTransactions(String privateKeyHex,String accountId) {
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId+"&size=100", url_getPositionTransactions);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-edgeX-Api-Timestamp", ""+timestamp);
        headerMap.put("X-edgeX-Api-Signature", sign);
        return HttpUtils.get(base_url + url_getPositionTransactions+"?accountId="+accountId+"&size=100", headerMap);
    }


    public static String getPositionTermPage(String privateKeyHex,String accountId) {
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId, url_getPositionTermPage);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-edgeX-Api-Timestamp", ""+timestamp);
        headerMap.put("X-edgeX-Api-Signature", sign);
        return HttpUtils.get(base_url + url_getPositionTermPage+"?accountId="+accountId, headerMap);
    }


}
