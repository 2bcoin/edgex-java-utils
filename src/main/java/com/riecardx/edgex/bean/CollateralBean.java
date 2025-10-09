package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * "collateralList": [
 *     {
 *       "userId": "665921128379711747",                     // 用户ID
 *       "accountId": "667210802901549329",                  // 账户ID
 *       "coinId": "1000",                                   // 币种ID（1000可能代表USDT等稳定币）
 *       "amount": "335.868157",                             // 当前抵押品净额
 *       "legacyAmount": "201.413257",                       // 传统余额（用于显示目的）
 *       "cumDepositAmount": "0",                            // 累计充值金额：0（未直接充值）
 *       "cumWithdrawAmount": "0",                           // 累计提现金额：0（未提现）
 *       "cumTransferInAmount": "200.000000",                // 累计转入金额：200（从其他账户转入）
 *       "cumTransferOutAmount": "0",                        // 累计转出金额：0（未转出到其他账户）
 *       "cumPositionBuyAmount": "-348.7420",                // 累计仓位买入支出：-348.742（开仓消耗）
 *       "cumPositionSellAmount": "484.8585",                // 累计仓位卖出收入：484.8585（平仓回收）
 *       "cumFillFeeAmount": "-0.256172",                    // 累计交易手续费：-0.256172（支出）
 *       "cumFundingFeeAmount": "0.007829",                  // 累计资金费用：0.007829（收入）
 *       "cumFillFeeIncomeAmount": "0",                      // 累计做市商手续费收入：0
 *       "createdTime": "1759075451598",                     // 创建时间戳
 *       "updatedTime": "1759469131386"                      // 更新时间戳
 *     }
 *   ]
 */
@Data
public class CollateralBean {
    @JsonProperty("userId")
    private String userId;                                    // 用户ID

    @JsonProperty("accountId")
    private String accountId;                                 // 账户ID

    @JsonProperty("coinId")
    private String coinId;                                    // 币种ID

    @JsonProperty("amount")
    private BigDecimal amount;                                // 当前抵押品净额

    @JsonProperty("legacyAmount")
    private BigDecimal legacyAmount;                          // 传统余额（用于显示目的）

    @JsonProperty("cumDepositAmount")
    private BigDecimal cumDepositAmount;                      // 累计充值金额

    @JsonProperty("cumWithdrawAmount")
    private BigDecimal cumWithdrawAmount;                     // 累计提现金额

    @JsonProperty("cumTransferInAmount")
    private BigDecimal cumTransferInAmount;                   // 累计转入金额

    @JsonProperty("cumTransferOutAmount")
    private BigDecimal cumTransferOutAmount;                  // 累计转出金额

    @JsonProperty("cumPositionBuyAmount")
    private BigDecimal cumPositionBuyAmount;                  // 累计仓位买入支出

    @JsonProperty("cumPositionSellAmount")
    private BigDecimal cumPositionSellAmount;                 // 累计仓位卖出收入

    @JsonProperty("cumFillFeeAmount")
    private BigDecimal cumFillFeeAmount;                      // 累计交易手续费

    @JsonProperty("cumFundingFeeAmount")
    private BigDecimal cumFundingFeeAmount;                   // 累计资金费用

    @JsonProperty("cumFillFeeIncomeAmount")
    private BigDecimal cumFillFeeIncomeAmount;                // 累计做市商手续费收入

    @JsonProperty("createdTime")
    private Long createdTime;                               // 创建时间戳

    @JsonProperty("updatedTime")
    private Long updatedTime;
}
