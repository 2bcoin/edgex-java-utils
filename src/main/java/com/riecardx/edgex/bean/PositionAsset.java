package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * {
 *   "positionAssetList": [
 *     {
 *       "userId": "665921128379711747",                                                                   // 用户ID
 *       "accountId": "667210802901549329",                                                                // 账户ID
 *       "coinId": "1000",                                                                                 // 抵押币种ID
 *       "contractId": "10000002",                                                                         // 合约ID
 *       "positionValue": "-134.5122000016272068023681640625",                                             // 仓位价值：-134.5122（负值表示空仓）
 *       "maxLeverage": "5",                                                                               // 最大允许杠杆：5倍
 *       "initialMarginRequirement": "26.9024400003254413604736328125000000",                              // 初始保证金要求：26.90244
 *       "starkExRiskRate": "0.00500000012107193470001220703125",                                          // StarkEx风险率：0.5%
 *       "starkExRiskValue": "0.672561016293788328963831890661140278098173439502716064453125",             // StarkEx风险价值：0.672561
 *       "avgEntryPrice": "4481.83",                                                                       // 平均开仓价格：4481.83
 *       "liquidatePrice": "11139.91",                                                                     // 强平价格：11139.91
 *       "bankruptPrice": "11195.61",                                                                      // 破产价格：11195.61
 *       "worstClosePrice": "11191.57",                                                                    // 最差平仓价格：11191.57
 *       "unrealizePnl": "-0.0573000016272068023681640625",                                                // 未实现盈亏：-0.0573（亏损）
 *       "termRealizePnl": "0.000000",                                                                     // 当前周期已实现盈亏：0
 *       "totalRealizePnl": "-0.069800"                                                                    // 总已实现盈亏：-0.0698（亏损）
 *     }
 *   ]
 * }
 */
@Data
public class PositionAsset {
    @JsonProperty("userId")
    private String userId;                                          // 用户ID

    @JsonProperty("accountId")
    private String accountId;                                       // 账户ID

    @JsonProperty("coinId")
    private String coinId;                                          // 抵押币种ID

    @JsonProperty("contractId")
    private String contractId;                                      // 合约ID

    @JsonProperty("positionValue")
    private BigDecimal positionValue;                               // 仓位价值

    @JsonProperty("maxLeverage")
    private BigDecimal maxLeverage;                                 // 最大允许杠杆

    @JsonProperty("initialMarginRequirement")
    private BigDecimal initialMarginRequirement;                    // 初始保证金要求

    @JsonProperty("starkExRiskRate")
    private BigDecimal starkExRiskRate;                             // StarkEx风险率

    @JsonProperty("starkExRiskValue")
    private BigDecimal starkExRiskValue;                            // StarkEx风险价值

    @JsonProperty("avgEntryPrice")
    private BigDecimal avgEntryPrice;                               // 平均开仓价格

    @JsonProperty("liquidatePrice")
    private BigDecimal liquidatePrice;                              // 强平价格

    @JsonProperty("bankruptPrice")
    private BigDecimal bankruptPrice;                               // 破产价格

    @JsonProperty("worstClosePrice")
    private BigDecimal worstClosePrice;                             // 最差平仓价格

    @JsonProperty("unrealizePnl")
    private BigDecimal unrealizePnl;                                // 未实现盈亏

    @JsonProperty("termRealizePnl")
    private BigDecimal termRealizePnl;                              // 当前周期已实现盈亏

    @JsonProperty("totalRealizePnl")
    private BigDecimal totalRealizePnl;                             // 总已实现盈亏
}
