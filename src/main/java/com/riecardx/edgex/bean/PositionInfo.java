package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *{
 *   "positionList": [
 *     {
 *       "userId": "665921128379711747",                           // 用户ID
 *       "accountId": "667210802901549329",                        // 账户ID
 *       "coinId": "1000",                                         // 抵押币种ID
 *       "contractId": "10000002",                                 // 合约ID
 *       "openSize": "-0.03",                                      // 当前持仓数量：-0.03（空仓）
 *       "openValue": "-134.454900",                               // 当前持仓价值：-134.4549（负值表示空仓）
 *       "openFee": "-0.048403",                                   // 当前持仓手续费：-0.048403（支出）
 *       "fundingFee": "0.000000",                                 // 当前资金费用：0
 *       "longTermCount": 3,                                       // 多仓交易周期数：3次
 *       "longTermStat": {                                         // 当前多仓周期统计
 *         "cumOpenSize": "0.02",                                  // 累计开多仓数量
 *         "cumOpenValue": "88.015800",                            // 累计开多仓价值
 *         "cumOpenFee": "-0.031685",                              // 累计开多仓手续费
 *         "cumCloseSize": "-0.02",                                // 累计平多仓数量
 *         "cumCloseValue": "-88.669200",                          // 累计平多仓价值
 *         "cumCloseFee": "-0.031920",                             // 累计平多仓手续费
 *         "cumFundingFee": "0.003971",                            // 累计资金费用（收入）
 *         "cumLiquidateFee": "0"                                  // 累计强平费用
 *       },
 *       "longTermCreatedTime": "1759419345600",                   // 当前多仓周期创建时间
 *       "longTermUpdatedTime": "1759420834014",                   // 当前多仓周期更新时间
 *       "shortTermCount": 5,                                      // 空仓交易周期数：5次
 *       "shortTermStat": {                                        // 当前空仓周期统计
 *         "cumOpenSize": "-0.03",                                 // 累计开空仓数量
 *         "cumOpenValue": "-134.454900",                          // 累计开空仓价值
 *         "cumOpenFee": "-0.048403",                              // 累计开空仓手续费
 *         "cumCloseSize": "0",                                    // 累计平空仓数量（未平仓）
 *         "cumCloseValue": "0",                                   // 累计平空仓价值（未平仓）
 *         "cumCloseFee": "0",                                     // 累计平空仓手续费（未平仓）
 *         "cumFundingFee": "0",                                   // 累计资金费用
 *         "cumLiquidateFee": "0"                                  // 累计强平费用
 *       },
 *       "shortTermCreatedTime": "1759469131386",                  // 当前空仓周期创建时间
 *       "shortTermUpdatedTime": "1759469131386",                  // 当前空仓周期更新时间
 *       "longTotalStat": {                                        // 多仓历史总统计
 *         "cumOpenSize": "0.04",                                  // 历史累计开多仓数量
 *         "cumOpenValue": "171.937800",                           // 历史累计开多仓价值
 *         "cumOpenFee": "-0.040077",                              // 历史累计开多仓手续费
 *         "cumCloseSize": "-0.04",                                // 历史累计平多仓数量
 *         "cumCloseValue": "-173.669200",                         // 历史累计平多仓价值
 *         "cumCloseFee": "-0.040420",                             // 历史累计平多仓手续费
 *         "cumFundingFee": "0.007829",                            // 历史累计资金费用（收入）
 *         "cumLiquidateFee": "0"                                  // 历史累计强平费用
 *       },
 *       "shortTotalStat": {                                       // 空仓历史总统计
 *         "cumOpenSize": "-0.07",                                 // 历史累计开空仓数量
 *         "cumOpenValue": "-311.189300",                          // 历史累计开空仓价值
 *         "cumOpenFee": "-0.112026",                              // 历史累计开空仓手续费
 *         "cumCloseSize": "0.04",                                 // 历史累计平空仓数量
 *         "cumCloseValue": "176.804200",                          // 历史累计平空仓价值
 *         "cumCloseFee": "-0.063649",                             // 历史累计平空仓手续费
 *         "cumFundingFee": "0",                                   // 历史累计资金费用
 *         "cumLiquidateFee": "0"                                  // 历史累计强平费用
 *       },
 *       "createdTime": "1759214298203",                          // 仓位创建时间
 *       "updatedTime": "1759469131386"                           // 仓位更新时间
 *     }
 *   ]
 * }
 */
@Data
public class PositionInfo {
    @JsonProperty("userId")
    private String userId;                                      // 用户ID

    @JsonProperty("accountId")
    private String accountId;                                   // 账户ID

    @JsonProperty("coinId")
    private String coinId;                                      // 抵押币种ID

    @JsonProperty("contractId")
    private String contractId;                                  // 合约ID

    @JsonProperty("openSize")
    private BigDecimal openSize;                                // 当前持仓数量

    @JsonProperty("openValue")
    private BigDecimal openValue;                               // 当前持仓价值

    @JsonProperty("openFee")
    private BigDecimal openFee;                                 // 当前持仓手续费

    @JsonProperty("fundingFee")
    private BigDecimal fundingFee;                              // 当前资金费用

    @JsonProperty("longTermCount")
    private Integer longTermCount;                              // 多仓交易周期数

    @JsonProperty("longTermStat")
    private TermStat longTermStat;                          // 当前多仓周期统计

    @JsonProperty("longTermCreatedTime")
    private Long longTermCreatedTime;                         // 当前多仓周期创建时间

    @JsonProperty("longTermUpdatedTime")
    private Long longTermUpdatedTime;                         // 当前多仓周期更新时间

    @JsonProperty("shortTermCount")
    private Integer shortTermCount;                             // 空仓交易周期数

    @JsonProperty("shortTermStat")
    private TermStat shortTermStat;                         // 当前空仓周期统计

    @JsonProperty("shortTermCreatedTime")
    private Long shortTermCreatedTime;                        // 当前空仓周期创建时间

    @JsonProperty("shortTermUpdatedTime")
    private Long shortTermUpdatedTime;                        // 当前空仓周期更新时间

    @JsonProperty("longTotalStat")
    private TermStat longTotalStat;                         // 多仓历史总统计

    @JsonProperty("shortTotalStat")
    private TermStat shortTotalStat;                        // 空仓历史总统计

    @JsonProperty("createdTime")
    private Long createdTime;                                 // 仓位创建时间

    @JsonProperty("updatedTime")
    private Long updatedTime;                                 // 仓位更新时间
}
