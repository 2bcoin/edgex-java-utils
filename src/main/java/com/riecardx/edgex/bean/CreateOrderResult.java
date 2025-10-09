package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderResult {
    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private CreateOrderResultData data;

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

    private Boolean isSuccess;

    private String thirdId;
    private String tpThirdId;
    private String slThirdId;
}
