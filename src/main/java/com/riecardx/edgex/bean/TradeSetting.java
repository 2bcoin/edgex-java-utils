package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * "defaultTradeSetting": {
 *     "isSetFeeRate": true,                   // 是否设置费用率，true表示已设置
 *     "takerFeeRate": "0.000360",             // 卖方费用率，表示交易时卖方需要支付的费用比例
 *     "makerFeeRate": "0.000100",             // 买方费用率，表示交易时买方需要支付的费用比例
 *     "isSetFeeDiscount": false,               // 是否设置费用折扣，false表示没有设置
 *     "takerFeeDiscount": "0",                 // 卖方费用折扣，0表示没有折扣
 *     "makerFeeDiscount": "0",                 // 买方费用折扣，0表示没有折扣
 *     "isSetMaxLeverage": false,               // 是否设置最大杠杆，false表示没有设置
 *     "maxLeverage": "0"                       // 最大杠杆值，0表示无最大杠杆限制
 * }
 */
@Data
public class TradeSetting {
    /**
     * 是否设置了自定义费率（true表示使用下方自定义费率）
     */
    @JsonProperty("isSetFeeRate")
    private Boolean isSetFeeRate;

    /**
     * Taker（吃单）手续费率
     */
    @JsonProperty("takerFeeRate")
    private BigDecimal takerFeeRate;

    /**
     * Maker（挂单）手续费率
     */
    @JsonProperty("makerFeeRate")
    private BigDecimal makerFeeRate;

    /**
     * 是否设置了费率折扣（false表示不使用折扣）
     */
    @JsonProperty("isSetFeeDiscount")
    private Boolean isSetFeeDiscount;

    /**
     * Taker费率折扣
     */
    @JsonProperty("takerFeeDiscount")
    private BigDecimal takerFeeDiscount;

    /**
     * Maker费率折扣
     */
    @JsonProperty("makerFeeDiscount")
    private BigDecimal makerFeeDiscount;

    /**
     * 是否设置了最大杠杆限制（false表示使用合约默认杠杆）
     */
    @JsonProperty("isSetMaxLeverage")
    private Boolean isSetMaxLeverage;

    /**
     * 最大杠杆倍数（0表示不限制或使用系统默认）
     */
    @JsonProperty("maxLeverage")
    private BigDecimal maxLeverage;

}
