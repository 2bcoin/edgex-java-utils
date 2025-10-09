package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DepthQuoteBean {
    @JsonProperty("channel")
    private String channel;

    @JsonProperty("content")
    private DepthQuoteContent content;

    @JsonProperty("dataType")
    private String dataType;
}
