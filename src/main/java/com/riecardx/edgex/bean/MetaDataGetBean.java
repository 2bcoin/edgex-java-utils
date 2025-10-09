package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetaDataGetBean {

    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private MetaDataGetBeanData data;

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
