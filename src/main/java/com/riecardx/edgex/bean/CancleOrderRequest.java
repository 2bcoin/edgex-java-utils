package com.riecardx.edgex.bean;

import lombok.Data;

import java.util.List;

@Data
public class CancleOrderRequest {
    private String accountId;
    private List<String> orderIdList;
}
