package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DepthQuoteContent {
    @JsonProperty("channel")
    private String channel;

    @JsonProperty("data")
    private List<DepthGetBeanData> data;

    @JsonProperty("dataType")
    private String dataType;
}
