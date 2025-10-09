package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateOrderResultData {
    @JsonProperty("orderId")
    private String orderId;
}
