package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * {
 *   "account": {
 *     "id": "667210802901549329",                                 // 账户ID
 *     "userId": "665921128379711747",                             // 用户ID
 *     "ethAddress": "0x398F4DE688a929a115d4085A063Fe5e2", // 以太坊地址
 *     "l2Key": "0x2696bd0e94e81984d74225f4953b5d7800ac5b2269d4", // L2密钥
 *     "l2KeyYCoordinate": "0x3ae8cab6b57a5674a38c6d252020c71", // L2密钥Y坐标
 *     "clientAccountId": "Sub-Account-1",                         // 客户端账户标识（子账户1）
 *     "isSystemAccount": false,                                   // 是否为系统账户
 *     "defaultTradeSetting": {                                    // 默认交易设置
 *       "isSetFeeRate": true,                                     // 是否设置自定义费率
 *       "takerFeeRate": "0.000360",                               // Taker费率：0.036%
 *       "makerFeeRate": "0.000100",                               // Maker费率：0.01%
 *       "isSetFeeDiscount": false,                                // 是否设置费率折扣
 *       "takerFeeDiscount": "0",                                  // Taker折扣率
 *       "makerFeeDiscount": "0",                                  // Maker折扣率
 *       "isSetMaxLeverage": false,                                // 是否设置最大杠杆
 *       "maxLeverage": "0"                                        // 最大杠杆限制
 *     },
 *     "contractIdToTradeSetting": {                               // 合约特定交易设置
 *       "10000002": {                                             // 合约ID 10000002 的设置
 *         "isSetFeeRate": false,                                  // 不覆盖默认费率设置
 *         "takerFeeRate": "0",                                    // Taker费率（使用默认）
 *         "makerFeeRate": "0",                                    // Maker费率（使用默认）
 *         "isSetFeeDiscount": false,                              // 不覆盖折扣设置
 *         "takerFeeDiscount": "0",                                // Taker折扣（使用默认）
 *         "makerFeeDiscount": "0",                                // Maker折扣（使用默认）
 *         "isSetMaxLeverage": true,                               // 设置特定杠杆限制
 *         "maxLeverage": "5"                                      // 该合约最大杠杆：5倍
 *       }
 *     },
 *     "maxLeverageLimit": "0",                                    // 全局最大杠杆限制
 *     "createOrderPerMinuteLimit": 0,                             // 每分钟下单限制
 *     "createOrderDelayMillis": 0,                                // 下单延迟（毫秒）
 *     "extraType": "",                                            // 扩展类型
 *     "extraDataJson": "",                                        // 扩展数据（JSON格式）
 *     "status": "NORMAL",                                         // 账户状态：正常
 *     "isLiquidating": false,                                     // 是否正在被强平
 *     "createdTime": "1759075451598",                             // 创建时间戳
 *     "updatedTime": "1759214276000"                              // 更新时间戳
 *   }
 * }
 */
@Data
public class AccountMainInfo {
    @JsonProperty("id")
    private String id;                                          // 账户ID

    @JsonProperty("userId")
    private String userId;                                      // 用户ID

    @JsonProperty("ethAddress")
    private String ethAddress;                                  // 以太坊地址

    @JsonProperty("l2Key")
    private String l2Key;                                       // L2密钥

    @JsonProperty("l2KeyYCoordinate")
    private String l2KeyYCoordinate;                            // L2密钥Y坐标

    @JsonProperty("clientAccountId")
    private String clientAccountId;                             // 客户端账户标识

    @JsonProperty("isSystemAccount")
    private Boolean isSystemAccount;                            // 是否为系统账户

    @JsonProperty("defaultTradeSetting")
    private TradeSetting defaultTradeSetting;                   // 默认交易设置

    @JsonProperty("contractIdToTradeSetting")
    private Map<String, TradeSetting> contractIdToTradeSetting; // 合约特定交易设置

    @JsonProperty("maxLeverageLimit")
    private BigDecimal maxLeverageLimit;                        // 全局最大杠杆限制

    @JsonProperty("createOrderPerMinuteLimit")
    private Integer createOrderPerMinuteLimit;                  // 每分钟下单限制

    @JsonProperty("createOrderDelayMillis")
    private Integer createOrderDelayMillis;                     // 下单延迟（毫秒）

    @JsonProperty("extraType")
    private String extraType;                                   // 扩展类型

    @JsonProperty("extraDataJson")
    private String extraDataJson;                               // 扩展数据（JSON格式）

    @JsonProperty("status")
    private String status;                                      // 账户状态

    @JsonProperty("isLiquidating")
    private Boolean isLiquidating;                              // 是否正在被强平

    @JsonProperty("createdTime")
    private Long createdTime;                                 // 创建时间戳

    @JsonProperty("updatedTime")
    private Long updatedTime;
}
