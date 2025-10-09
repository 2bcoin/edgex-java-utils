package com.riecardx.edgex.bean;

import com.edgex.exchange.model.CoinModel;
import com.edgex.exchange.model.ContractModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MetaDataGetBeanData {

    @JsonProperty("coinList")
    private List<CoinModel> coinList;

    @JsonProperty("contractList")
    private List<ContractModel> contractList;

}
