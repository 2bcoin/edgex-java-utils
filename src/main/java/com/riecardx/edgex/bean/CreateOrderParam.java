package com.riecardx.edgex.bean;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 创建订单参数类
 * 用于向交易平台提交各种类型的订单
 */
public class CreateOrderParam {

    @JsonProperty("accountId")
    private String accountId;                                      // 账户ID

    @JsonProperty("contractId")
    private String contractId;                                     // 合约ID

    @JsonProperty("side")
    private String side;                                           // 买卖方向

    @JsonProperty("size")
    private BigDecimal size;                                       // 订单数量

    @JsonProperty("price")
    private BigDecimal price;                                      // 订单价格

    @JsonProperty("clientOrderId")
    private String clientOrderId;                                  // 客户端订单ID

    @JsonProperty("type")
    private String type;                                           // 订单类型

    @JsonProperty("timeInForce")
    private String timeInForce;                                    // 订单执行策略  type为 LIMIT/STOP_LIMIT/TAKE_PROFIT_LIMIT 时有意义。此字段必填，市价单必为IMMEDIATE_OR_CANCEL。

    @JsonProperty("reduceOnly")
    private Boolean reduceOnly;                                    // 是否只减仓

    @JsonProperty("triggerPrice")
    private BigDecimal triggerPrice;                               // 触发价格 触发价格。type为 STOP_LIMIT/STOP_MARKET/TAKE_PROFIT_LIMIT/TAKE_PROFIT_MARKET 时有意义。如果为0代表字段为空。实际为decimal类型。当为条件单时必填。

    @JsonProperty("triggerPriceType")
    private String triggerPriceType;                               // 触发价格类型。最新市价[默认],标记价格。type为条件单时有意义。当为条件单时必填。

    @JsonProperty("expireTime")
    private String expireTime;                                     // 过期时间

    @JsonProperty("sourceKey")
    private String sourceKey;                                      // 来源密钥

    @JsonProperty("isPositionTpsl")
    private Boolean isPositionTpsl;                                // 是否为仓位止盈止损单

    @JsonProperty("openTpslParentOrderId")
    private String openTpslParentOrderId;                          // 开仓止盈止损父订单ID

    @JsonProperty("isSetOpenTp")
    private Boolean isSetOpenTp;                                   // 是否设置开仓止盈

    @JsonProperty("isSetOpenSl")
    private Boolean isSetOpenSl;                                   // 是否设置开仓止盈

    @JsonProperty("openTp")
    private OpenTpSlParam openTp;                                  // 开仓止盈参数

    @JsonProperty("openSl")
    private OpenTpSlParam openSl;                                  // 开仓止损参数

    @JsonProperty("l2Nonce")
    private String l2Nonce;                                        // L2签名随机数

    @JsonProperty("l2Value")
    private BigDecimal l2Value;                                    // L2签名订单价值

    @JsonProperty("l2Size")
    private BigDecimal l2Size;                                     // L2签名订单数量

    @JsonProperty("l2LimitFee")
    private BigDecimal l2LimitFee;                                 // L2签名最大手续费

    @JsonProperty("l2ExpireTime")
    private String l2ExpireTime;                                   // L2签名过期时间

    @JsonProperty("l2Signature")
    private String l2Signature;                                    // L2签名

    @JsonProperty("extraType")
    private String extraType;                                      // 扩展类型

    @JsonProperty("extraDataJson")
    private String extraDataJson;                                  // 扩展数据

    public CreateOrderParam() {
    }

    /**
     * 检查是否为市价单
     */
    public boolean isMarketOrder() {
        return price != null && price.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 检查是否为条件订单（止损/止盈）
     */
    public boolean isConditionalOrder() {
        return "STOP_LIMIT".equals(type) || "STOP_MARKET".equals(type) ||
                "TAKE_PROFIT_LIMIT".equals(type) || "TAKE_PROFIT_MARKET".equals(type);
    }

    /**
     * 验证基本订单参数
     */
    public boolean validateBasicParams() {
        return side != null && !side.isEmpty() &&
                size != null && size.compareTo(BigDecimal.ZERO) > 0 &&
                price != null &&
                clientOrderId != null && !clientOrderId.isEmpty() &&
                type != null && !type.isEmpty() &&
                timeInForce != null && !timeInForce.isEmpty() &&
                reduceOnly != null;
    }

    /**
     * 验证条件订单参数
     */
    public boolean validateConditionalOrderParams() {
        if (!isConditionalOrder()) {
            return true;
        }
        return triggerPrice != null && triggerPrice.compareTo(BigDecimal.ZERO) > 0 &&
                triggerPriceType != null && !triggerPriceType.isEmpty();
    }

    /**
     * 验证L2签名参数
     */
    public boolean validateL2Params() {
        if (l2Signature == null || l2Signature.isEmpty()) {
            return true; // L2签名可选
        }
        return l2Nonce != null && !l2Nonce.isEmpty() &&
                l2ExpireTime != null && !l2ExpireTime.isEmpty();
    }

    /**
     * 获取订单摘要信息
     */
    public String getOrderSummary() {
        return String.format("%s %s %s @ %s | Type: %s | TIF: %s | ReduceOnly: %s",
                side,
                size != null ? size.toPlainString() : "N/A",
                contractId != null ? contractId : "N/A",
                isMarketOrder() ? "Market" : (price != null ? price.toPlainString() : "N/A"),
                type,
                timeInForce,
                reduceOnly != null ? reduceOnly : false);
    }

    /**
     * 检查是否设置了开仓止盈止损
     */
    public boolean hasOpenTpSl() {
        return (isSetOpenTp != null && isSetOpenTp && (openTp != null || openSl != null)) || (isSetOpenSl != null && isSetOpenSl && (openTp != null || openSl != null));
    }

    /**
     * 计算L2签名订单价值
     */
    public BigDecimal calculateL2Value() {
        if (l2Value != null) {
            return l2Value;
        }
        if (price != null && size != null && price.compareTo(BigDecimal.ZERO) > 0) {
            return price.multiply(size);
        }
        return null;
    }

    /**
     * 验证市价单参数
     */
    public boolean validateMarketOrderParams() {
        if (!isMarketOrder()) {
            return true;
        }
        return "IMMEDIATE_OR_CANCEL".equals(timeInForce);
    }

    // Getter 和 Setter 方法
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public Boolean getReduceOnly() {
        return reduceOnly;
    }

    public void setReduceOnly(Boolean reduceOnly) {
        this.reduceOnly = reduceOnly;
    }

    public BigDecimal getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(BigDecimal triggerPrice) {
        this.triggerPrice = triggerPrice;
    }

    public String getTriggerPriceType() {
        return triggerPriceType;
    }

    public void setTriggerPriceType(String triggerPriceType) {
        this.triggerPriceType = triggerPriceType;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public Boolean getIsPositionTpsl() {
        return isPositionTpsl;
    }

    public void setIsPositionTpsl(Boolean isPositionTpsl) {
        this.isPositionTpsl = isPositionTpsl;
    }

    public String getOpenTpslParentOrderId() {
        return openTpslParentOrderId;
    }

    public void setOpenTpslParentOrderId(String openTpslParentOrderId) {
        this.openTpslParentOrderId = openTpslParentOrderId;
    }

    public Boolean getIsSetOpenTp() {
        return isSetOpenTp;
    }

    public void setIsSetOpenTp(Boolean isSetOpenTp) {
        this.isSetOpenTp = isSetOpenTp;
    }

    public Boolean getIsSetOpenSl() {
        return isSetOpenSl;
    }

    public void setIsSetOpenSl(Boolean isSetOpenSl) {
        this.isSetOpenSl = isSetOpenSl;
    }

    public OpenTpSlParam getOpenTp() {
        return openTp;
    }

    public void setOpenTp(OpenTpSlParam openTp) {
        this.openTp = openTp;
    }

    public OpenTpSlParam getOpenSl() {
        return openSl;
    }

    public void setOpenSl(OpenTpSlParam openSl) {
        this.openSl = openSl;
    }

    public String getL2Nonce() {
        return l2Nonce;
    }

    public void setL2Nonce(String l2Nonce) {
        this.l2Nonce = l2Nonce;
    }

    public BigDecimal getL2Value() {
        return l2Value;
    }

    public void setL2Value(BigDecimal l2Value) {
        this.l2Value = l2Value;
    }

    public BigDecimal getL2Size() {
        return l2Size;
    }

    public void setL2Size(BigDecimal l2Size) {
        this.l2Size = l2Size;
    }

    public BigDecimal getL2LimitFee() {
        return l2LimitFee;
    }

    public void setL2LimitFee(BigDecimal l2LimitFee) {
        this.l2LimitFee = l2LimitFee;
    }

    public String getL2ExpireTime() {
        return l2ExpireTime;
    }

    public void setL2ExpireTime(String l2ExpireTime) {
        this.l2ExpireTime = l2ExpireTime;
    }

    public String getL2Signature() {
        return l2Signature;
    }

    public void setL2Signature(String l2Signature) {
        this.l2Signature = l2Signature;
    }

    public String getExtraType() {
        return extraType;
    }

    public void setExtraType(String extraType) {
        this.extraType = extraType;
    }

    public String getExtraDataJson() {
        return extraDataJson;
    }

    public void setExtraDataJson(String extraDataJson) {
        this.extraDataJson = extraDataJson;
    }

    @Override
    public String toString() {
        return "CreateOrderParam{" +
                "accountId='" + accountId + '\'' +
                ", contractId='" + contractId + '\'' +
                ", side='" + side + '\'' +
                ", size=" + size +
                ", price=" + price +
                ", clientOrderId='" + clientOrderId + '\'' +
                ", type='" + type + '\'' +
                ", timeInForce='" + timeInForce + '\'' +
                ", reduceOnly=" + reduceOnly +
                ", triggerPrice=" + triggerPrice +
                ", triggerPriceType='" + triggerPriceType + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", sourceKey='" + sourceKey + '\'' +
                ", isPositionTpsl=" + isPositionTpsl +
                ", openTpslParentOrderId='" + openTpslParentOrderId + '\'' +
                ", isSetOpenTp=" + isSetOpenTp +
                ", isSetOpenSl=" + isSetOpenSl +
                ", openTp=" + openTp +
                ", openSl=" + openSl +
                ", l2Nonce='" + l2Nonce + '\'' +
                ", l2Value=" + l2Value +
                ", l2Size=" + l2Size +
                ", l2LimitFee=" + l2LimitFee +
                ", l2ExpireTime='" + l2ExpireTime + '\'' +
                ", l2Signature='" + l2Signature + '\'' +
                ", extraType='" + extraType + '\'' +
                ", extraDataJson='" + extraDataJson + '\'' +
                '}';
    }
}
