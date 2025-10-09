package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderResponseBean {
    /**
     * 订单ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 用户ID
     */
    @JsonProperty("userId")
    private String userId;

    /**
     * 账户ID
     */
    @JsonProperty("accountId")
    private String accountId;

    /**
     * 币种ID
     */
    @JsonProperty("coinId")
    private String coinId;

    /**
     * 合约ID
     */
    @JsonProperty("contractId")
    private String contractId;

    /**
     * 订单方向：BUY/SELL
     */
    @JsonProperty("side")
    private String side;

    /**
     * 订单价格
     */
    @JsonProperty("price")
    private String price;

    /**
     * 订单数量
     */
    @JsonProperty("size")
    private String size;

    /**
     * 客户端订单ID
     */
    @JsonProperty("clientOrderId")
    private String clientOrderId;

    /**
     * 订单类型：LIMIT/MARKET等
     */
    @JsonProperty("type")
    private String type;

    /**
     * 订单有效时间类型：GOOD_TIL_CANCEL等
     */
    @JsonProperty("timeInForce")
    private String timeInForce;

    /**
     * 是否只减仓
     */
    @JsonProperty("reduceOnly")
    private Boolean reduceOnly;

    /**
     * 触发价格
     */
    @JsonProperty("triggerPrice")
    private String triggerPrice;

    /**
     * 触发价格类型
     */
    @JsonProperty("triggerPriceType")
    private String triggerPriceType;

    /**
     * 过期时间
     */
    @JsonProperty("expireTime")
    private String expireTime;

    /**
     * 来源标识
     */
    @JsonProperty("sourceKey")
    private String sourceKey;

    /**
     * 是否为仓位止盈止损订单
     */
    @JsonProperty("isPositionTpsl")
    private Boolean isPositionTpsl;

    /**
     * 是否为强平订单
     */
    @JsonProperty("isLiquidate")
    private Boolean isLiquidate;

    /**
     * 是否为减仓订单
     */
    @JsonProperty("isDeleverage")
    private Boolean isDeleverage;

    /**
     * 开仓止盈止损父订单ID
     */
    @JsonProperty("openTpslParentOrderId")
    private String openTpslParentOrderId;

    /**
     * 是否设置开仓止盈
     */
    @JsonProperty("isSetOpenTp")
    private Boolean isSetOpenTp;
}
