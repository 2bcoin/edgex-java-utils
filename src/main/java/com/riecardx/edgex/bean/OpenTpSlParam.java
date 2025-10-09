package com.riecardx.edgex.bean;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 开仓止盈止损参数类
 * 用于设置开仓订单的止盈止损条件
 */
public class OpenTpSlParam {

    @JsonProperty("side")
    private String side;                                          // 买卖方向

    @JsonProperty("price")
    private BigDecimal price;                                     // 订单价格

    @JsonProperty("size")
    private BigDecimal size;                                      // 订单数量

    @JsonProperty("clientOrderId")
    private String clientOrderId;                                 // 客户端订单ID

    @JsonProperty("triggerPrice")
    private BigDecimal triggerPrice;                              // 触发价格

    @JsonProperty("triggerPriceType")
    private String triggerPriceType;                              // 触发价格类型

    @JsonProperty("expireTime")
    private String expireTime;                                    // 过期时间

    @JsonProperty("l2Nonce")
    private String l2Nonce;                                       // L2签名随机数

    @JsonProperty("l2Value")
    private BigDecimal l2Value;                                   // L2签名订单价值

    @JsonProperty("l2Size")
    private BigDecimal l2Size;                                    // L2签名订单数量

    @JsonProperty("l2LimitFee")
    private BigDecimal l2LimitFee;                                // L2签名最大手续费

    @JsonProperty("l2ExpireTime")
    private String l2ExpireTime;                                  // L2签名过期时间

    @JsonProperty("l2Signature")
    private String l2Signature;                                   // L2签名

    public OpenTpSlParam() {
    }

    /**
     * 检查是否为市价单
     */
    public boolean isMarketOrder() {
        return price != null && price.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 验证基本参数完整性
     */
    public boolean validateBasicParams() {
        return side != null && !side.isEmpty() &&
                size != null && size.compareTo(BigDecimal.ZERO) > 0 &&
                clientOrderId != null && !clientOrderId.isEmpty() &&
                triggerPrice != null && triggerPrice.compareTo(BigDecimal.ZERO) > 0 &&
                triggerPriceType != null && !triggerPriceType.isEmpty();
    }

    /**
     * 验证L2签名参数完整性
     */
    public boolean validateL2Params() {
        return l2Nonce != null && !l2Nonce.isEmpty() &&
                l2Signature != null && !l2Signature.isEmpty() &&
                l2ExpireTime != null && !l2ExpireTime.isEmpty();
    }

    /**
     * 获取订单摘要信息
     */
    public String getOrderSummary() {
        return String.format("Side: %s | Size: %s | Trigger: %s (%s) | %s",
                side,
                size != null ? size.toPlainString() : "N/A",
                triggerPrice != null ? triggerPrice.toPlainString() : "N/A",
                triggerPriceType,
                isMarketOrder() ? "Market" : "Limit@" + (price != null ? price.toPlainString() : "N/A"));
    }

    /**
     * 计算L2签名订单价值（如果未设置l2Value，则使用price * size）
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
     * 检查订单是否使用L2签名
     */
    public boolean usesL2Signature() {
        return l2Signature != null && !l2Signature.isEmpty();
    }

    // Getter 和 Setter 方法
    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
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

    @Override
    public String toString() {
        return "OpenTpSlParam{" +
                "side='" + side + '\'' +
                ", price=" + price +
                ", size=" + size +
                ", clientOrderId='" + clientOrderId + '\'' +
                ", triggerPrice=" + triggerPrice +
                ", triggerPriceType='" + triggerPriceType + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", l2Nonce='" + l2Nonce + '\'' +
                ", l2Value=" + l2Value +
                ", l2Size=" + l2Size +
                ", l2LimitFee=" + l2LimitFee +
                ", l2ExpireTime='" + l2ExpireTime + '\'' +
                ", l2Signature='" + l2Signature + '\'' +
                '}';
    }
}