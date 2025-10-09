package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *{
 *   "collateralAssetModelList": [
 *     {
 *       "userId": "665921128379711747",                                                                   // 用户ID
 *       "accountId": "667210802901549329",                                                                // 账户ID
 *       "coinId": "1000",                                                                                 // 抵押币种ID
 *       "totalEquity": "201.3559569983727931976318359375",                                                // 总权益（净值）：201.355957
 *       "totalPositionValueAbs": "134.5122000016272068023681640625",                                      // 总仓位价值绝对值：134.5122
 *       "initialMarginRequirement": "26.9024400003254413604736328125000000",                              // 初始保证金要求：26.90244
 *       "starkExRiskValue": "0.672561016293788328963831890661140278098173439502716064453125",             // StarkEx风险价值：0.672561
 *       "pendingWithdrawAmount": "0",                                                                     // 待提现金额：0
 *       "pendingTransferOutAmount": "0",                                                                  // 待转出金额：0
 *       "orderFrozenAmount": "0",                                                                         // 订单冻结金额：0
 *       "availableAmount": "174.453516"                                                                   // 可用金额：174.453516
 *     }
 *   ]
 * }
 */
@Data
public class CollateralAssetModel {
    @JsonProperty("userId")
    private String userId;                                          // 用户ID

    @JsonProperty("accountId")
    private String accountId;                                       // 账户ID

    @JsonProperty("coinId")
    private String coinId;                                          // 抵押币种ID

    @JsonProperty("totalEquity")
    private BigDecimal totalEquity;                                 // 总权益（净值）

    @JsonProperty("totalPositionValueAbs")
    private BigDecimal totalPositionValueAbs;                       // 总仓位价值绝对值

    @JsonProperty("initialMarginRequirement")
    private BigDecimal initialMarginRequirement;                    // 初始保证金要求

    @JsonProperty("starkExRiskValue")
    private BigDecimal starkExRiskValue;                            // StarkEx风险价值

    @JsonProperty("pendingWithdrawAmount")
    private BigDecimal pendingWithdrawAmount;                       // 待提现金额

    @JsonProperty("pendingTransferOutAmount")
    private BigDecimal pendingTransferOutAmount;                    // 待转出金额

    @JsonProperty("orderFrozenAmount")
    private BigDecimal orderFrozenAmount;                           // 订单冻结金额

    @JsonProperty("availableAmount")
    private BigDecimal availableAmount;                             // 可用金额
}
