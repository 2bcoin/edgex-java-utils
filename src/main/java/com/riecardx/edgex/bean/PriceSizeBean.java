package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceSizeBean {
    @JsonProperty("size")
    private BigDecimal size;                                       // 订单数量

    @JsonProperty("price")
    private BigDecimal price;
}
