package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * "longTermStat": {
 *     "cumOpenSize": "0.02",           // 累计开仓数量：0.02
 *     "cumOpenValue": "88.015800",     // 累计开仓价值：88.0158
 *     "cumOpenFee": "-0.031685",       // 累计开仓手续费：-0.031685（负值表示支出）
 *     "cumCloseSize": "-0.02",         // 累计平仓数量：-0.02（负值表示减少持仓）
 *     "cumCloseValue": "-88.669200",   // 累计平仓价值：-88.6692（负值表示平仓操作）
 *     "cumCloseFee": "-0.031920",      // 累计平仓手续费：-0.03192（负值表示支出）
 *     "cumFundingFee": "0.003971",     // 累计资金费用：0.003971（正值表示收入）
 *     "cumLiquidateFee": "0"           // 累计强平费用：0（表示未发生强平）
 *   }
 */
@Data
public class TermStat {
    /**
     * 累计开仓数量
     */
    @JsonProperty("cumOpenSize")
    private BigDecimal cumOpenSize;

    /**
     * 累计开仓价值
     */
    @JsonProperty("cumOpenValue")
    private BigDecimal cumOpenValue;

    /**
     * 累计开仓手续费（负值表示支出）
     */
    @JsonProperty("cumOpenFee")
    private BigDecimal cumOpenFee;

    /**
     * 累计平仓数量（负值表示减少持仓）
     */
    @JsonProperty("cumCloseSize")
    private BigDecimal cumCloseSize;

    /**
     * 累计平仓价值（负值表示平仓操作）
     */
    @JsonProperty("cumCloseValue")
    private BigDecimal cumCloseValue;

    /**
     * 累计平仓手续费（负值表示支出）
     */
    @JsonProperty("cumCloseFee")
    private BigDecimal cumCloseFee;

    /**
     * 累计资金费用（正值表示收入，负值表示支出）
     */
    @JsonProperty("cumFundingFee")
    private BigDecimal cumFundingFee;

    /**
     * 累计强平费用（0表示未发生强平）
     */
    @JsonProperty("cumLiquidateFee")
    private BigDecimal cumLiquidateFee;
}
