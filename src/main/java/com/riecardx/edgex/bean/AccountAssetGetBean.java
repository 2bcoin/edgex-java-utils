package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 接口返回数据对象
 *"/api/v1/private/account/getAccountAsset"
 */
@Data
public class AccountAssetGetBean {

    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private AccountAssetGetBeanData data;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("errorParam")
    private String errorParam;

    @JsonProperty("requestTime")
    private Long requestTime;                                 // 时间

    @JsonProperty("responseTime")
    private Long responseTime;                                 // 时间

    @JsonProperty("traceId")
    private String traceId;  // 追踪ID


}
