package com.riecardx.edgex.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 接口返回数据对象主体(data)
 *"/api/v1/private/account/getAccountAsset"
 */
@Data
public class AccountAssetGetBeanData {

    @JsonProperty("account")
    private AccountMainInfo account;

    @JsonProperty("collateralList")
    private List<CollateralBean> collateralList;

    @JsonProperty("positionList")
    private List<PositionInfo> positionList;

    @JsonProperty("version")
    private String version;

    @JsonProperty("positionAssetList")
    private List<PositionAsset> positionAssetList;

    @JsonProperty("collateralAssetModelList")
    private List<CollateralAssetModel> collateralAssetModelList;

    @JsonProperty("oraclePriceList")
    private List<Object> oraclePriceList;

}
