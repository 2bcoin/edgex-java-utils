package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderGetBeanData {
    @JsonProperty("dataList")
    private List<OrderResponseBean> dataList;

    @JsonProperty("nextPageOffsetData")
    private String nextPageOffsetData;
}
