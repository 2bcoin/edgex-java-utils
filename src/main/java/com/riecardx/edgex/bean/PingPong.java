package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PingPong {
    @JsonProperty("type")
    private String type;
    @JsonProperty("time")
    private String time;
}
