package com.riecardx.edgex.service;

import cn.hutool.json.JSONUtil;
import com.edgex.exchange.model.CoinModel;
import com.edgex.exchange.model.ContractModel;
import com.riecardx.edgex.bean.MetaDataGetBean;
import com.riecardx.edgex.utils.PublicUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaDataService {

    private Map<String, CoinModel> coinModelMap;
    private Map<String, ContractModel> contractModelMap;

    public MetaDataService() {
        coinModelMap = new ConcurrentHashMap<>();
        contractModelMap = new ConcurrentHashMap<>();
    }

    public void reGetData() {
        try {
            String jsonStr = PublicUtils.getMetaData();
            MetaDataGetBean getBean = JSONUtil.toBean(jsonStr, MetaDataGetBean.class);
            for (CoinModel coinModel:getBean.getData().getCoinList()) {
                coinModelMap.put(coinModel.getCoinId(), coinModel);
            }
            for(ContractModel contractModel:getBean.getData().getContractList()) {
                contractModelMap.put(contractModel.getContractId(), contractModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ContractModel getContractModel(String contractId) {
        ContractModel contractModel = contractModelMap.get(contractId);
        if (contractModel == null) {
            reGetData();
            contractModel = contractModelMap.get(contractId);
        }
        return contractModel;
    }

    public CoinModel getCoinModel(String coinId) {
        CoinModel coinModel = coinModelMap.get(coinId);
        if (coinModel == null) {
            reGetData();
            coinModel = coinModelMap.get(coinId);
        }
        return  coinModel;
    }

}
