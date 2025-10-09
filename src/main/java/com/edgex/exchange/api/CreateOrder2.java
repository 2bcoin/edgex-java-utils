package com.edgex.exchange.api;

import cn.hutool.json.JSONUtil;
import com.edgex.exchange.model.CoinModel;
import com.edgex.exchange.model.ContractModel;
import com.edgex.exchange.model.order.CreateOrderParam;
import com.edgex.exchange.utils.BigIntUtil;
import com.edgex.exchange.utils.CustomResponseErrorHandler;
import com.edgex.exchange.utils.DecimalUtil;
import com.edgex.exchange.utils.L2SignUtil;
import com.edgex.exchange.utils.ecdsa.Ecdsa;
import com.edgex.exchange.utils.ecdsa.PrivateKey;
import com.edgex.exchange.utils.ecdsa.Signature;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.riecardx.edgex.utils.HttpUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CreateOrder2 {
    public static final String baseUrl = "https://pro.edgex.exchange";

    public CreateOrder2() {
    }

    public static void main(String[] args) {
        doCreateLimitOrder();
    }

    public static void doCreateLimitOrder() {
        String privateKeyHex = "";
        String accountId = "";
        String orderSide = "BUY";
        BigDecimal orderSize = new BigDecimal("0.02");
        BigDecimal orderPrice = new BigDecimal("4444.5");
        if (privateKeyHex.startsWith("0x")) {
            privateKeyHex = privateKeyHex.substring(2);
        }
        BigInteger mySecretKey = new BigInteger(privateKeyHex, 16);
        PrivateKey privateKey = PrivateKey.create(mySecretKey);
        CreateOrderParam orderRequest = createLimitOrderRequest(accountId, getBTCUSDTcontract(), getUsdtQuteCoin(), orderSide, orderSize, orderPrice, privateKey);
        String requestPath = "/api/v1/private/order/createOrder";
        long timestamp = System.currentTimeMillis();
        String sign = Auth.getPostAuthSignature(privateKey, timestamp, orderRequest, requestPath);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-edgeX-Api-Timestamp", "" + timestamp);
        headers.set("X-edgeX-Api-Signature", sign);
        HttpEntity<CreateOrderParam> entity = new HttpEntity(orderRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://pro.edgex.exchange" + requestPath, entity, String.class, new Object[0]);
        System.out.println(response.getStatusCode());
        System.out.println("Response: " + (String)response.getBody());


    }

    public static CreateOrderParam createLimitOrderRequest(String accountId, ContractModel contract, CoinModel quotelCoin, String orderSide, BigDecimal orderSize, BigDecimal orderPrice, PrivateKey privateKey) {
        BigDecimal l2LimitFee = DecimalUtil.roundByStepSize(orderSize.multiply(orderPrice).multiply((new BigDecimal(contract.getDefaultTakerFeeRate())).max(new BigDecimal(contract.getDefaultMakerFeeRate()))), new BigDecimal(quotelCoin.getStepSize()), RoundingMode.CEILING);
        String clientOrderId = UUID.randomUUID().toString();
        long expireTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(28L);
        Long l2Nonce = Long.parseLong(Hashing.sha256().hashString(clientOrderId, Charsets.UTF_8).toString().substring(0, 8), 16);
        CreateOrderParam order = new CreateOrderParam();
        order.setAccountId(accountId);
        order.setContractId(contract.getContractId());
        order.setPrice(orderPrice.toPlainString());
        order.setSize(orderSize.toPlainString());
        order.setType("LIMIT");
        order.setTimeInForce("GOOD_TIL_CANCEL");
        order.setReduceOnly(false);
        order.setIsPositionTpsl(false);
        order.setIsSetOpenTp(false);
        order.setIsSetOpenSl(false);
        order.setSide(orderSide);
        order.setTriggerPrice("");
        order.setTriggerPriceType("LAST_PRICE");
        order.setClientOrderId(clientOrderId);
        order.setExpireTime("" + expireTime);
        order.setL2Nonce(l2Nonce.toString());
        order.setL2Value(orderSize.multiply(orderPrice).toPlainString());
        order.setL2Size(orderSize.toPlainString());
        order.setL2LimitFee(l2LimitFee.toPlainString());
        order.setL2ExpireTime("" + (expireTime + TimeUnit.DAYS.toMillis(10L)));
        order.setL2Signature(signOrder(order, contract, quotelCoin, privateKey));
        return order;
    }

    public static String signOrder(CreateOrderParam request, ContractModel contract, CoinModel quotelCoin, PrivateKey privateKey) {
        BigInteger msgHash = L2SignUtil.hashLimitOrder(request.getSide() == "BUY", BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId()), BigIntUtil.toBigInt(contract.getStarkExSyntheticAssetId()), BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId()), UnsignedLong.valueOf((new BigDecimal(request.getL2Value())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf((new BigDecimal(request.getL2Size())).multiply(new BigDecimal(BigIntUtil.toBigInt(contract.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf((new BigDecimal(request.getL2LimitFee())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf(request.getAccountId()), UnsignedInteger.valueOf(request.getL2Nonce()), UnsignedInteger.valueOf(Long.valueOf(request.getL2ExpireTime()) / 3600000L));
        Signature signature = Ecdsa.sign(msgHash, privateKey);
        String var10000 = TypeEncoder.encodePacked(new Uint256(signature.r));
        String sig = var10000 + TypeEncoder.encodePacked(new Uint256(signature.s));
        return sig;
    }

    public static CoinModel getUsdtQuteCoin() {
        CoinModel coin = new CoinModel();
        coin.setCoinId("1000");
        coin.setCoinName("USDT");
        coin.setStepSize("0.000001");
        coin.setShowStepSize("0.0001");
        coin.setIconUrl("https://static.edgex.exchange/icons/coin/USDT.svg");
        coin.setStarkExAssetId("0x33bda5c923bae4e84825b74762d5482889b9512465fbffc50d1ae4b82e345c3");
        coin.setStarkExResolution("0xf4240");
        return coin;
    }

    public static ContractModel getBTCUSDTcontract() {
        ContractModel contract = new ContractModel();
        contract.setContractId("10000001");
        contract.setContractName("BTCUSDT");
        contract.setBaseCoinId("1001");
        contract.setQuoteCoinId("1000");
        contract.setTickSize("0.1");
        contract.setStepSize("0.001");
        contract.setMinOrderSize("0.001");
        contract.setMaxOrderSize("50.000");
        contract.setMaxOrderBuyPriceRatio("0.05");
        contract.setMinOrderSellPriceRatio("0.05");
        contract.setMaxPositionSize("60.000");
        contract.setDefaultTakerFeeRate("0.00055");
        contract.setDefaultMakerFeeRate("0.0002");
        contract.setDefaultLeverage("50");
        contract.setLiquidateFeeRate("0.01");
        contract.setEnableTrade(true);
        contract.setEnableDisplay(true);
        contract.setEnableOpenPosition(true);
        contract.setFundingInterestRate("0.0003");
        contract.setFundingImpactMarginNotional("10");
        contract.setFundingMaxRate("0.000234");
        contract.setFundingMinRate("-0.000234");
        contract.setFundingRateIntervalMin("240");
        contract.setDisplayDigitMerge("0.1,0.5,1,2,5");
        contract.setDisplayMaxLeverage("50");
        contract.setDisplayMinLeverage("1");
        contract.setDisplayNewIcon(false);
        contract.setDisplayHotIcon(true);
        contract.setMatchServerName("edgex-match-server");
        contract.setStarkExSyntheticAssetId("0x425443322d31300000000000000000");
        contract.setStarkExResolution("0x2540be400");
        contract.setStarkExOraclePriceQuorum("0x1");
        return contract;
    }

    public static ContractModel getETHUSDTcontract() {
        ContractModel contract = new ContractModel();
        contract.setContractId("10000002");
        contract.setContractName("ETHUSDT");
        contract.setBaseCoinId("1002");
        contract.setQuoteCoinId("1000");
        contract.setTickSize("0.01");
        contract.setStepSize("0.01");
        contract.setMinOrderSize("0.01");
        contract.setMaxOrderSize("500.00");
        contract.setMaxOrderBuyPriceRatio("0.05");
        contract.setMinOrderSellPriceRatio("0.05");
        contract.setMaxPositionSize("800.00");
        contract.setDefaultTakerFeeRate("0.00055");
        contract.setDefaultMakerFeeRate("0.0002");
        contract.setDefaultLeverage("50");
        contract.setLiquidateFeeRate("0.01");
        contract.setEnableTrade(true);
        contract.setEnableDisplay(true);
        contract.setEnableOpenPosition(true);
        contract.setFundingInterestRate("0.0003");
        contract.setFundingImpactMarginNotional("100");
        contract.setFundingMaxRate("0.000234");
        contract.setFundingMinRate("-0.000234");
        contract.setFundingRateIntervalMin("240");
        contract.setDisplayDigitMerge("0.01,0.02,0.04,0.1,0.2");
        contract.setDisplayMaxLeverage("50");
        contract.setDisplayMinLeverage("1");
        contract.setDisplayNewIcon(true);
        contract.setDisplayHotIcon(false);
        contract.setMatchServerName("edgex-match-server");
        contract.setStarkExSyntheticAssetId("0x4554482d3900000000000000000000");
        contract.setStarkExResolution("0x3b9aca00");
        contract.setStarkExOraclePriceQuorum("0x1");
        return contract;
    }
}
