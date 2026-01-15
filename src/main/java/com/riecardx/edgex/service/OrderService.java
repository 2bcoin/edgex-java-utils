package com.riecardx.edgex.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.edgex.exchange.api.Auth;
import com.edgex.exchange.api.CreateOrder2;
import com.edgex.exchange.model.CoinModel;
import com.edgex.exchange.model.ContractModel;
import com.edgex.exchange.model.order.CreateOrderParam;
import com.edgex.exchange.utils.CustomResponseErrorHandler;
import com.edgex.exchange.utils.ecdsa.PrivateKey;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.riecardx.edgex.bean.CancleAllOrderRequest;
import com.riecardx.edgex.bean.CancleOrderRequest;
import com.riecardx.edgex.bean.CreateOrderResult;
import com.riecardx.edgex.bean.OrderGetBean;
import com.riecardx.edgex.enums.OrderTypeEnum;
import com.riecardx.edgex.frame.mFrame;
import com.riecardx.edgex.utils.HttpUtils;
import com.riecardx.edgex.utils.SignUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    public static final String base_url = "https://pro.edgex.exchange";
    public static final String url_createOrder = "/api/v1/private/order/createOrder";
    public static final String url_cancelAllOrder = "/api/v1/private/order/cancelAllOrder";
    public static final String url_cancelOrderById = "/api/v1/private/order/cancelOrderById";
    public static final String url_getActiveOrderPage = "/api/v1/private/order/getActiveOrderPage";

    private MetaDataService metaDataService;

    public OrderService() {
        metaDataService = new MetaDataService();
    }

    /**
     * GET Get Active Orders (Paginated)
     * GET /api/v1/private/order/getActiveOrderPage
     */
    public OrderGetBean getActiveOrderPage(String privateKeyHex, String accountId) {
        try {
            long timestamp = System.currentTimeMillis();
            String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId+"&size=100", url_getActiveOrderPage);
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("X-edgeX-Api-Timestamp", ""+timestamp);
            headerMap.put("X-edgeX-Api-Signature", sign);
            String resStr =  HttpUtils.get(base_url + url_getActiveOrderPage+"?accountId="+accountId+"&size=100", headerMap);
            logger.info(resStr);
            return JSONUtil.toBean(resStr, OrderGetBean.class);
//            JSONObject jsonObject = JSONUtil.parseObj(resStr);
//            if ("SUCCESS".equals(jsonObject.getStr("code"))) {
//                JSONArray dataList = jsonObject.getJSONObject("data").getJSONArray("dataList");
//                System.out.println(dataList.size());
//                return dataList.size();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * POST Cancel All Orders under Account
     * POST /api/v1/private/order/cancelAllOrder
     */
    public boolean cancelAllOrder(String accountId, String privateKeyHex) {
        CancleAllOrderRequest cancleAllOrderRequest = new CancleAllOrderRequest();
        cancleAllOrderRequest.setAccountId(accountId);
        long timestamp = System.currentTimeMillis();
        BigInteger mySecretKey = new BigInteger(privateKeyHex, 16);
        PrivateKey privateKey = PrivateKey.create(mySecretKey);
        String sign = Auth.getPostAuthSignature(privateKey, timestamp, cancleAllOrderRequest, url_cancelAllOrder);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-edgeX-Api-Timestamp", "" + timestamp);
        headers.set("X-edgeX-Api-Signature", sign);
        HttpEntity<CreateOrderParam> entity = new HttpEntity(cancleAllOrderRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(base_url + url_cancelAllOrder, entity, String.class, new Object[0]);
        logger.info(response.getStatusCode());
        String resStr = (String)response.getBody();
        logger.info("Response: " + resStr);
        if (resStr!=null && resStr.indexOf("SUCCESS")>0) {
            return true;
        }
        return false;
    }

    /**
     * POST Cancel Order by Order ID
     * POST /api/v1/private/order/cancelOrderById
     */
    public boolean cancelOrders(String accountId, String privateKeyHex, List<String> orderIds) {
        CancleOrderRequest cancleOrderRequest = new CancleOrderRequest();
        cancleOrderRequest.setAccountId(accountId);
        cancleOrderRequest.setOrderIdList(orderIds);
        long timestamp = System.currentTimeMillis();
        BigInteger mySecretKey = new BigInteger(privateKeyHex, 16);
        PrivateKey privateKey = PrivateKey.create(mySecretKey);
        String sign = Auth.getPostAuthSignature(privateKey, timestamp, cancleOrderRequest, url_cancelOrderById);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-edgeX-Api-Timestamp", "" + timestamp);
        headers.set("X-edgeX-Api-Signature", sign);
        HttpEntity<CreateOrderParam> entity = new HttpEntity(cancleOrderRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(base_url + url_cancelOrderById, entity, String.class, new Object[0]);
        logger.info(response.getStatusCode());
        String resStr = (String)response.getBody();
        logger.info("Response: " + resStr);
        if (resStr!=null && resStr.indexOf("SUCCESS")>0) {
            return true;
        }
        return false;
    }


    /**
     * POST Create Order
     * POST /api/v1/private/order/createOrder
     * @param coinId
     * @param constractId
     * @param privateKeyHex
     * @param accountId
     * @param typeEnum
     * @param price
     * @param size
     * @param slPrice  // 止损价格
     * @param tpPrice  // 止盈价格
     * @return
     */
    public CreateOrderResult createOrder(String coinId, String constractId, String privateKeyHex, String accountId, OrderTypeEnum typeEnum, Double price, Double size, Double slPrice, Double tpPrice,Double shijiaPrice) {
        System.out.println("constractId:"+constractId+";typeEnum:"+typeEnum.getVal()+";price:"+price+";size:"+size);
        // TODO 目前只交易BTHUSD，以后有其他交易下单时，要修改
        CoinModel coinModel = metaDataService.getCoinModel(coinId);
        ContractModel contractModel = metaDataService.getContractModel(constractId);

        BigInteger mySecretKey = new BigInteger(privateKeyHex, 16);
        PrivateKey privateKey = PrivateKey.create(mySecretKey);

        Double l2valueXi = 1D;
        if (typeEnum.getVal().equals(OrderTypeEnum.BUY.getVal())) {
            l2valueXi = 1.5D;
        } else {
            l2valueXi = 0.5D;
        }

        com.edgex.exchange.model.order.CreateOrderParam orderParam = new com.edgex.exchange.model.order.CreateOrderParam();
        orderParam.setAccountId(accountId);
        orderParam.setContractId(constractId);
        orderParam.setSide(typeEnum.getVal());
        orderParam.setSize(String.format("%.2f",size));
        if (Math.abs(price - 0) < 0.000001) {
            orderParam.setPrice("0");
            orderParam.setType("MARKET");
        } else {
            orderParam.setPrice(String.format("%.2f",price));
            orderParam.setType("LIMIT");
        }
        orderParam.setClientOrderId(UUID.randomUUID().toString());
        Long l2Nonce = Long.parseLong(Hashing.sha256().hashString(orderParam.getClientOrderId(), Charsets.UTF_8).toString().substring(0, 8), 16);
        orderParam.setTimeInForce("GOOD_TIL_CANCEL");
        orderParam.setReduceOnly(false);
        orderParam.setTriggerPrice(null);
        orderParam.setTriggerPriceType("LAST_PRICE");

        long currentTimeMillis = System.currentTimeMillis();
        long expireTime = currentTimeMillis + 3600000*24; // 当前时间加1天
        long l2ExpireTime = currentTimeMillis + 3600000*24*10; // 当前时间加10天
        orderParam.setExpireTime(""+expireTime);
        orderParam.setSourceKey("");
        orderParam.setIsPositionTpsl(false);
        orderParam.setOpenTpslParentOrderId(null);

        orderParam.setL2Nonce(""+l2Nonce);
        orderParam.setL2Value(String.format("%.6f",shijiaPrice*size*l2valueXi));
        orderParam.setL2Size(orderParam.getSize());
        orderParam.setL2LimitFee(String.format("%.6f",shijiaPrice*size*l2valueXi*0.001));  // orderParam.getL2Value().multiply(new BigDecimal(0.0004))
        orderParam.setL2ExpireTime(""+l2ExpireTime);

        // 止损止盈订单 开始 start
        orderParam.setIsSetOpenTp(false);
        orderParam.setIsSetOpenSl(false);
        String sltpSide = typeEnum.getVal().equals(OrderTypeEnum.BUY.getVal())?OrderTypeEnum.SELL.getVal():OrderTypeEnum.BUY.getVal();

        if (slPrice!=null && slPrice>0) {
            orderParam.setIsSetOpenSl(true);
            String slPriceStr = String.format("%.2f",slPrice);  // TODO 正常应该设置为“0” 市价单  目前接口可能有BUG，改为设置为触发价格+-1%
            l2valueXi = 1D;
            if (sltpSide.equals(OrderTypeEnum.BUY.getVal())) {
                slPriceStr = String.format("%.2f",slPrice*0.99);
                l2valueXi = 2D;
            } else {
                slPriceStr = String.format("%.2f",slPrice*1.01);
                l2valueXi = 0.1D;
            }
            // 止损订单
            com.edgex.exchange.model.order.OpenTpSlParam slParam = new com.edgex.exchange.model.order.OpenTpSlParam();
            slParam.setSide(sltpSide);
            slParam.setPrice("0");
            slParam.setSize(String.format("%.2f",size));
            slParam.setClientOrderId("sl-"+orderParam.getClientOrderId());
            slParam.setTriggerPrice(String.format("%.2f",slPrice));
            slParam.setTriggerPriceType("LAST_PRICE");
            slParam.setExpireTime(""+expireTime);
            slParam.setL2Nonce(""+Long.parseLong(Hashing.sha256().hashString(slParam.getClientOrderId(), Charsets.UTF_8).toString().substring(0, 8), 16));
            slParam.setL2Value(String.format("%.6f",Double.valueOf(slParam.getTriggerPrice())*size*l2valueXi));
            slParam.setL2Size(slParam.getSize());
            slParam.setL2LimitFee(String.format("%.6f",Double.valueOf(slParam.getTriggerPrice())*size*0.005));
            slParam.setL2ExpireTime(orderParam.getL2ExpireTime());
            slParam.setL2Signature(SignUtils.signOpenTpSlParam(slParam, contractModel, coinModel, privateKey, accountId));
            orderParam.setOpenSl(slParam);

//            System.out.println("止损订单");
//            System.out.println(JSONUtil.toJsonStr(slParam));
        }
        if (tpPrice!=null && tpPrice>0) {
            orderParam.setIsSetOpenTp(true);
            String tpPriceStr = String.format("%.2f", tpPrice);  // TODO 正常应该设置为“0” 市价单  目前接口可能有BUG，改为设置为触发价格+-1%
            l2valueXi = 1D;
            if (sltpSide.equals(OrderTypeEnum.BUY.getVal())) {
                tpPriceStr = String.format("%.2f", tpPrice * 0.99);
                l2valueXi = 2D;
            } else {
                tpPriceStr = String.format("%.2f", tpPrice * 1.01);
                l2valueXi = 0.1D;
            }
            // 止盈订单
            com.edgex.exchange.model.order.OpenTpSlParam tpParam = new com.edgex.exchange.model.order.OpenTpSlParam();
            tpParam.setSide(sltpSide);
            tpParam.setPrice("0");
            tpParam.setSize(String.format("%.2f",size));
            tpParam.setClientOrderId("tp-"+orderParam.getClientOrderId());
            tpParam.setTriggerPrice(String.format("%.2f",tpPrice));
            tpParam.setTriggerPriceType("LAST_PRICE");
            tpParam.setExpireTime(""+expireTime);
            tpParam.setL2Nonce(""+Long.parseLong(Hashing.sha256().hashString(tpParam.getClientOrderId(), Charsets.UTF_8).toString().substring(0, 8), 16));
            tpParam.setL2Value(String.format("%.6f",Double.valueOf(tpParam.getTriggerPrice())*size*l2valueXi));
            tpParam.setL2Size(tpParam.getSize());
            tpParam.setL2LimitFee(String.format("%.6f",Double.valueOf(tpParam.getTriggerPrice())*size*0.005));
            tpParam.setL2ExpireTime(orderParam.getL2ExpireTime());
            tpParam.setL2Signature(SignUtils.signOpenTpSlParam(tpParam, contractModel, coinModel, privateKey, accountId));

//            System.out.println("止盈订单");
//            System.out.println(JSONUtil.toJsonStr(tpParam));

            orderParam.setOpenTp(tpParam);
        }
        orderParam.setL2Signature(SignUtils.signOrder(orderParam, contractModel, coinModel, privateKey));
        logger.info(JSONUtil.toJsonStr(orderParam));

        long timestamp = System.currentTimeMillis();
        String sign = Auth.getPostAuthSignature(privateKey, timestamp, orderParam, url_createOrder);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-edgeX-Api-Timestamp", "" + timestamp);
        headers.set("X-edgeX-Api-Signature", sign);
        HttpEntity<CreateOrderParam> entity = new HttpEntity(orderParam, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(base_url + url_createOrder, entity, String.class, new Object[0]);
        logger.info(response.getStatusCode());
        String resStr = (String)response.getBody();
        logger.info("Response: " + resStr);
        System.out.println("Response: " + resStr);
        return JSONUtil.toBean(resStr, CreateOrderResult.class);
    }

}
