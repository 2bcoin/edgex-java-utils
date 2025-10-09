package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DepthGetBeanData {
    @JsonProperty("startVersion")
    private String startVersion;
    @JsonProperty("endVersion")
    private String endVersion;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("contractId")
    private String contractId;
    @JsonProperty("contractName")
    private String contractName;

    @JsonProperty("asks")
    private List<PriceSizeBean> asks;

    @JsonProperty("bids")
    private List<PriceSizeBean> bids;

    @JsonProperty("depthType")
    private String depthType;
}
