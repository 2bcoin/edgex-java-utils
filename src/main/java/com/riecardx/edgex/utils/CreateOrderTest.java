package com.riecardx.edgex.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.edgex.exchange.api.Auth;
import com.edgex.exchange.api.CreateOrder;
import com.edgex.exchange.model.CoinModel;
import com.edgex.exchange.model.ContractModel;
import com.edgex.exchange.model.order.CreateOrderParam;
import com.edgex.exchange.utils.CustomResponseErrorHandler;
import com.edgex.exchange.utils.ecdsa.PrivateKey;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.riecardx.edgex.bean.OpenTpSlParam;
import com.riecardx.edgex.enums.OrderTypeEnum;
import com.riecardx.edgex.service.MetaDataService;
import com.riecardx.edgex.service.OrderService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.HexUtil.hexToLong;
import static cn.hutool.crypto.SecureUtil.sha256;

public class CreateOrderTest {

    public static final String base_url = "https://pro.edgex.exchange";

    static String constractId = "10000002";
    static String coinId = "1000";
    static String privateKeyHex = "";
    static String accountId = "";

    public static void main(String[] args) {
        CreateOrderTest createOrderTest = new CreateOrderTest();
        createOrderTest.getActiveOrders();
    }

    private void getActiveOrders() {
        OrderService orderService = new OrderService();
        orderService.getActiveOrderPage(privateKeyHex,accountId);
    }

    private void cancleAllOrder() {
        OrderService orderService = new OrderService();
        orderService.cancelAllOrder(accountId,privateKeyHex);
    }

    private void cancleOrder() {
        OrderService orderService = new OrderService();
        List<String> orderIdList = new ArrayList<>();
        orderIdList.add("669386174179050001");
        orderService.cancelOrders(accountId,privateKeyHex, orderIdList);
    }

    private void pingcang() {
        OrderService orderService = new OrderService();
        orderService.createOrder(coinId,constractId,privateKeyHex,accountId, OrderTypeEnum.SELL,4444D,0.02,null,null, 4555D);
    }

    private void createOrder() {
        MetaDataService metaDataService = new MetaDataService();
        CoinModel coinModel = metaDataService.getCoinModel(coinId);
        ContractModel contractModel = metaDataService.getContractModel(constractId);
        BigInteger mySecretKey = new BigInteger(privateKeyHex, 16);
        PrivateKey privateKey = PrivateKey.create(mySecretKey);

        Double price = 4468D;
        Double size = 0.02D;

        com.edgex.exchange.model.order.CreateOrderParam orderParam = new com.edgex.exchange.model.order.CreateOrderParam();
        orderParam.setAccountId(accountId);
        orderParam.setContractId("10000002");
        orderParam.setSide("BUY");
        orderParam.setSize(String.format("%.2f",size));
        orderParam.setPrice(String.format("%.2f",price));
        orderParam.setClientOrderId(UUID.randomUUID().toString());
        Long l2Nonce = Long.parseLong(Hashing.sha256().hashString(orderParam.getClientOrderId(), Charsets.UTF_8).toString().substring(0, 8), 16);
        orderParam.setType("LIMIT");
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
        orderParam.setL2Value(String.format("%.4f",price*size));
        orderParam.setL2Size(orderParam.getSize());
        orderParam.setL2LimitFee(String.format("%.6f",price*size*0.0004));  // orderParam.getL2Value().multiply(new BigDecimal(0.0004))
        orderParam.setL2ExpireTime(""+l2ExpireTime);

        orderParam.setIsSetOpenTp(true);
        orderParam.setIsSetOpenSl(true);

        // 止损订单
        com.edgex.exchange.model.order.OpenTpSlParam slParam = new com.edgex.exchange.model.order.OpenTpSlParam();
        slParam.setSide("SELL");
        slParam.setPrice("0");
        slParam.setSize(String.format("%.2f",size));
        slParam.setClientOrderId("sl-"+orderParam.getClientOrderId());
//        slParam.setClientOrderId(orderParam.getClientOrderId());
        slParam.setTriggerPrice(String.format("%.2f",price-1));
        slParam.setTriggerPriceType("LAST_PRICE");
        slParam.setExpireTime(""+expireTime);
        slParam.setL2Nonce(""+Long.parseLong(Hashing.sha256().hashString(slParam.getClientOrderId(), Charsets.UTF_8).toString().substring(0, 8), 16));
        slParam.setL2Value(String.format("%.6f",Double.valueOf(slParam.getTriggerPrice())*size*0.5));
        slParam.setL2Size(slParam.getSize());
        slParam.setL2LimitFee(String.format("%.6f",Double.valueOf(slParam.getTriggerPrice())*size*0.0004));
        slParam.setL2ExpireTime(orderParam.getL2ExpireTime());
        slParam.setL2Signature(SignUtils.signOpenTpSlParam(slParam, contractModel, coinModel, privateKey, accountId));

        System.out.println("止损订单");
        System.out.println(JSONUtil.toJsonStr(slParam));

        // 止盈订单
        com.edgex.exchange.model.order.OpenTpSlParam tpParam = new com.edgex.exchange.model.order.OpenTpSlParam();
        tpParam.setSide("SELL");
        tpParam.setPrice("0");
        tpParam.setSize(String.format("%.2f",size));
        tpParam.setClientOrderId("tp-"+orderParam.getClientOrderId());
//        tpParam.setClientOrderId(orderParam.getClientOrderId());
        tpParam.setTriggerPrice(String.format("%.2f",price+2));
        tpParam.setTriggerPriceType("LAST_PRICE");
        tpParam.setExpireTime(""+expireTime);
        tpParam.setL2Nonce(""+Long.parseLong(Hashing.sha256().hashString(tpParam.getClientOrderId(), Charsets.UTF_8).toString().substring(0, 8), 16));
        tpParam.setL2Value(String.format("%.6f",Double.valueOf(tpParam.getTriggerPrice())*size*0.5));
        tpParam.setL2Size(tpParam.getSize());
        tpParam.setL2LimitFee(String.format("%.6f",Double.valueOf(tpParam.getTriggerPrice())*size*0.0004));
        tpParam.setL2ExpireTime(orderParam.getL2ExpireTime());
        tpParam.setL2Signature(SignUtils.signOpenTpSlParam(tpParam, contractModel, coinModel, privateKey, accountId));

        System.out.println("止盈订单");
        System.out.println(JSONUtil.toJsonStr(tpParam));

        orderParam.setOpenTp(tpParam);
        orderParam.setOpenSl(slParam);
        orderParam.setL2Signature(SignUtils.signOrder(orderParam, contractModel, coinModel, privateKey));




        String requestPath = "/api/v1/private/order/createOrder";
        long timestamp = System.currentTimeMillis();
        String sign = Auth.getPostAuthSignature(privateKey, timestamp, orderParam, requestPath);
//        Map<String, String> headers = new HashMap<>();
//        headers.put("X-edgeX-Api-Timestamp", "" + timestamp);
//        headers.put("X-edgeX-Api-Signature", sign);
//        String jsonStr = HttpUtils.post(base_url+requestPath, headers,JSONUtil.toJsonStr(orderParam));
//        System.out.println(jsonStr);


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-edgeX-Api-Timestamp", "" + timestamp);
        headers.set("X-edgeX-Api-Signature", sign);
        HttpEntity<CreateOrderParam> entity = new HttpEntity(orderParam, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://pro.edgex.exchange" + requestPath, entity, String.class, new Object[0]);
        System.out.println(response.getStatusCode());
        System.out.println("Response: " + (String)response.getBody());


    }

}
