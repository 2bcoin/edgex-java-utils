/*
 * Created by JFormDesigner on Tue Sep 30 16:42:25 GMT+08:00 2025
 */

package com.riecardx.edgex.frame;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.riecardx.edgex.bean.*;
import com.riecardx.edgex.enums.OrderTypeEnum;
import com.riecardx.edgex.service.OrderService;
import com.riecardx.edgex.task.CheckActiveThread;
import com.riecardx.edgex.utils.*;
import jnr.ffi.annotations.In;
import okhttp3.internal.ws.WebSocketExtensions;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.web3j.abi.datatypes.Int;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

/**
 * @author rieca
 */
public class mFrame extends JFrame {
    private static final Logger logger = LogManager.getLogger(mFrame.class);

    private OrderService orderService;
    private Boolean isTrade = false;
    private Boolean isWanggeTrade = false;

    public Map<String, Object> dataMap;
    private Document privateKeyDocument;
    private Document accountIdDocument;
    private Document chushizijinDocument;
    CheckActiveThread checkActiveThread;
    // 创建模型和表格
    DefaultTableModel positionTableModel;
    DefaultTableModel accountAssetTableModel;

    public Map<String, String> contractMap; // 交易对、品类对应map
    public static final String contractIdETHUSD = "10000002";
    public String[] contractStaticArray;
    public static final String ganggantishi = "请将【杠杆】选择成与您页面一致的数值，否则会导致【止盈】【止损】计算错误给你造成损失！切记！切记！切记！";

    private PublicWebSocketClient publicWebSocketClient;
    public static final String wss_public_base = "wss://quote.edgex.exchange/api/v1/public/ws";
    private PrivateWebSocketClient privateWebSocketClient;
    public static final String wss_private_base = "wss://quote.edgex.exchange/api/v1/private/ws";
    private AccountAssetGetBean accountAssetGetBean; // 个人钱包信息；持仓信息

    public mFrame() {
        initComponents();
        dataMap = new ConcurrentHashMap<>();
        dataMap.put("isActive", false);
        dataMap.put("errorgetcount", Integer.valueOf(0));
        dataMap.put("endtime", "");
        myInit();
        checkActiveStatus();
        checkActiveThread = new CheckActiveThread(this);
        new Thread(checkActiveThread).start();
    }

    private void myInit() {
        orderService = new OrderService();
        // 创建图标对象
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/edgex.png"));
        // 设置窗口的图标
        this.setIconImage(icon.getImage());
        contractMap = new HashMap<>();
        contractMap.put("10000001","BTCUSD");
        contractMap.put("10000002","ETHUSD");
        contractStaticArray = new String[]{"10000002:ETHUSD"};
        dataMap.put("contractId", "10000002");
        dataMap.put("coinId", "1000");
        for (String s:contractStaticArray) {
            contractComboBox.addItem(s);
        }
        contractComboBox.setSelectedItem(contractStaticArray[0]);
        contractComboBox.setEditable(true);

        for (Double d=0.02D;d<1D;d+=0.02) {
            sizeComboBox.addItem(String.format("%.2f", d));
        }
        for (Double d=1D;d<10D;d+=0.5D) {
            sizeComboBox.addItem(String.format("%.2f", d));
        }
        sizeComboBox.setSelectedItem(0.02D);
        sizeComboBox.setEditable(true);
        dataMap.put("size", 0.02D);
        sizeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataMap.put("size", Double.valueOf(e.getItem().toString()));
                }
            }
        });


        privateKeyDocument = privateKeyTextField.getDocument();
        privateKeyDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                privateKeyTextFieldInputMethodTextChanged(null);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                privateKeyTextFieldInputMethodTextChanged(null);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        accountIdDocument = accountIdTextField.getDocument();
        accountIdDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                accountIdTextChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                accountIdTextChanged();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        // 初始表格数据
        String[] columns = { "空" }; // 定义表格列名数组
        String[][] data = {   }; // 定义表格数据数组
        positionTableModel = ((DefaultTableModel)positionTable.getModel());
        positionTableModel.setDataVector(data, columns);
        accountAssetTableModel = ((DefaultTableModel)accountAssetTable.getModel());
        accountAssetTableModel.setDataVector(data, columns);

        gangganTishiLabel1.setToolTipText(ganggantishi);
        gangganComboBox.setToolTipText(ganggantishi);
        zhiyingComboBox.setToolTipText(ganggantishi);
        zhisunComboBox.setToolTipText(ganggantishi);
        for (Integer n=1;n<101;n++) {
            zhisunComboBox.addItem(n);
            zhiyingComboBox.addItem(n);
            gangganComboBox.addItem(n);
        }
        gangganComboBox.setSelectedItem(100);
        for (Integer n=50;n<1000;n+=50) {
            zhisunComboBox.addItem(n);
            zhiyingComboBox.addItem(n);
        }
        for (Integer n=1000;n<10000;n+=100) {
            zhisunComboBox.addItem(n);
            zhiyingComboBox.addItem(n);
        }
        zhisunComboBox.setSelectedItem(5);
        zhiyingComboBox.setSelectedItem(15);
        dataMap.put("ganggan", 100);
        dataMap.put("zhiying", 15);
        dataMap.put("zhisun", 5);
        gangganComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataMap.put("ganggan", Integer.valueOf(e.getItem().toString()));
                }
            }
        });
        zhisunComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataMap.put("zhisun", Integer.valueOf(e.getItem().toString()));
                }
            }
        });
        zhiyingComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataMap.put("zhiying", Integer.valueOf(e.getItem().toString()));
                }
            }
        });

        // 网格start
        chushizijinDocument = chushizijinTextField.getDocument();
        chushizijinDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                chushizijinTextChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                chushizijinTextChanged();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        for (Integer n=1;n<101;n++) {
            maxKuisunComboBox.addItem(n);
            gePriceComboBox.addItem(n);
        }
        for (Integer n=110;n<500;n+=10) {
            gePriceComboBox.addItem(n);
        }
        maxKuisunComboBox.setSelectedItem(45);
        gePriceComboBox.setSelectedItem(5);
        dataMap.put("maxKuisun", 45);
        dataMap.put("gePrice", 5);
        maxKuisunComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataMap.put("maxKuisun", Integer.valueOf(e.getItem().toString()));
                }
            }
        });
        gePriceComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataMap.put("gePrice", Integer.valueOf(e.getItem().toString()));
                }
            }
        });
        // 网格end


        // TODO 一定要删除
        // TODO 一定要删除 end
        wsPublicStart();
        wsPrivateStart();
        startMainTradeTask();
    }

    private void wsPublicStart() {
        try {
            URI serverUri = new URI(wss_public_base);
            publicWebSocketClient = new PublicWebSocketClient(serverUri, this);
            publicWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void wsPrivateStart() {
        try {
            String accountId = MapUtils.getString(dataMap, "accountId");
            URI serverUri = new URI(wss_private_base+"?accountId="+accountId);
            privateWebSocketClient = new PrivateWebSocketClient(serverUri, null);
            long timestamp = System.currentTimeMillis();
            String privateKeyHex = MapUtils.getString(dataMap, "privateKey");
            String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId, "/api/v1/private/ws");
            privateWebSocketClient.addHeader("X-edgeX-Api-Timestamp", ""+timestamp);
            privateWebSocketClient.addHeader("X-edgeX-Api-Signature", sign);
            privateWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void accountIdTextChanged() {
        String text = accountIdTextField.getText();
        if (StringUtils.isNotBlank(text)) {
            text = text.trim();
            accountIdLengthLabel.setText(""+text.length());
        } else {
            accountIdLengthLabel.setText("0");
        }
        dataMap.put("accountId", text);
    }
    private void chushizijinTextChanged() {
        String text = chushizijinTextField.getText();
        Double d = 0D;
        try {
            text = text.trim();
            d = Double.valueOf(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataMap.put("chushizijin", d);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        boolean flag = false;
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            //关闭的提示选择
            int result= JOptionPane.showConfirmDialog(
                    this,
                    ("     确定要退出吗，退出后，已产生的仓位需要自己手动平掉。"),
                    ("关闭"),
                    JOptionPane.YES_NO_OPTION);

            if(result == JOptionPane.NO_OPTION){
                //不关闭，系统托盘？？？？
                flag = true;
            }else{
                //关闭的处理
                System.exit(0);
            }
        }
        if(!flag){
            //点击的了YES,那么交给上面去处理关闭的处理
            super.processWindowEvent(e);
        }
    }

    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
    }

    private void privateKeyTextFieldInputMethodTextChanged(InputMethodEvent e) {
        String text = privateKeyTextField.getText();
        if (StringUtils.isNotBlank(text)) {
            text = text.trim();
            privateKeyLengthLabel.setText(""+text.length());
        } else {
            privateKeyLengthLabel.setText("0");
        }
        dataMap.put("privateKey", text);
    }

    private void shuaxinButtonMouseClicked(MouseEvent e) {
        checkActiveStatus();
    }

    public boolean flushAccountAssetGetBean() {
        try {
            String jsonStr = AccountUtils.getAccountAsset(MapUtils.getString(dataMap, "privateKey"),MapUtils.getString(dataMap, "accountId"));
            accountAssetGetBean = JSONUtil.toBean(jsonStr, AccountAssetGetBean.class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void flushPositionTable() {
        flushAccountAssetGetBean();
        try {
            DateTime responseTime = DateUtil.date();
            responseTime.setTime(accountAssetGetBean.getResponseTime());
            String responseTimeStr = responseTime.toString(DatePattern.NORM_DATETIME_PATTERN);
            try {  // 当前持有仓位数据
                String[] columns = {"市场","数量","持仓价值","开仓价格","标记价格","清算价格","未实现盈亏","保证金","资金费率","更新时间"}; // 定义表格列名数组
                List<PositionAsset> positionAssetList = accountAssetGetBean.getData().getPositionAssetList();
                if (positionAssetList.size()>0) {
                    String[][] data = new String[positionAssetList.size()][10];
                    int n=0;
                    for (PositionAsset asset:positionAssetList) {
                        PositionInfo info = null;
                        for (PositionInfo pi:accountAssetGetBean.getData().getPositionList()) {
                            if (pi.getContractId().equals(asset.getContractId())) {
                                info = pi;
                                break;
                            }
                        }
                        String contractName = contractMap.get(asset.getContractId());
                        data[n][0] = contractName + " " + (asset.getPositionValue().signum()>0?"多":"空") +" "+asset.getMaxLeverage()+"X";
                        data[n][1] = info.getOpenSize().toString() + " " + contractName.replace("USD", "");
                        data[n][2] = "$" + asset.getPositionValue().toString();
                        data[n][3] = "$" + asset.getAvgEntryPrice().toString();
                        data[n][4] = "$"; // TODO 标记价格  从WS里取当前价格
                        data[n][5] = "$" + asset.getLiquidatePrice().toString();
                        data[n][6] = "$" + asset.getUnrealizePnl().toString();
                        data[n][7] = "$" + asset.getInitialMarginRequirement().toString();
                        data[n][8] = "$" + info.getFundingFee().toString();
                        data[n][9] = responseTimeStr;
                        n++;
                    }
                    positionTableModel.setDataVector(data, columns);
                } else {
                    String[] columns2 = { "空" }; // 定义表格列名数组
                    String[][] data = {   }; // 定义表格数据数组
                    positionTableModel.setDataVector(data, columns2);
                }
            } catch (Exception ee) {
                ee.printStackTrace();
                String[] columns2 = { "空" }; // 定义表格列名数组
                String[][] data = {   }; // 定义表格数据数组
                positionTableModel.setDataVector(data, columns2);
            }
            try {  // 当前账户数据
                String[] columns = {"总权益","总仓位价值","保证金","风险值","订单冻结金额","可用金额","更新时间"}; // 定义表格列名数组
                List<CollateralAssetModel> assetModelList = accountAssetGetBean.getData().getCollateralAssetModelList();
                String[][] data = new String[assetModelList.size()][7];
                int n=0;
                for (CollateralAssetModel assetModel:assetModelList) {
                    data[n][0] = "$"+assetModel.getTotalEquity().toString();
                    data[n][1] = "$"+assetModel.getTotalPositionValueAbs().toString();
                    data[n][2] = "$"+assetModel.getInitialMarginRequirement().toString();
                    data[n][3] = "$"+assetModel.getStarkExRiskValue().toString();
                    data[n][4] = "$"+assetModel.getOrderFrozenAmount().toString();
                    data[n][5] = "$"+assetModel.getAvailableAmount().toString();
                    data[n][6] = responseTimeStr;
                    n++;
                }
                accountAssetTableModel.setDataVector(data, columns);
            } catch (Exception ee) {

            }
        } catch (Exception e) {

        }
    }

    public CheckResult checkActiveStatus() {
        CheckResult checkResult = new CheckResult();
        String mkey = SignUtils.getDiskSerial() + SignUtils.getMacAddress();
        mkeyTextField.setText(mkey);
        dataMap.put("mkey", mkey);
        String skey = skeyTextField.getText()==null?"":skeyTextField.getText().trim();
        dataMap.put("skey", skey);





//        String url = "http://edgex.249250.xyz:9527/check.html?mac="+mkey+"&secret="+skey;
//        String rjsonstr = HttpUtils.get(url, null);
//        try {
//            checkResult = JSONUtil.toBean(rjsonstr, CheckResult.class);
//            lastGetLabel.setText(DateUtil.format(new Date(), "YYYY-MM-dd HH:mm:ss"));
//            if (StringUtils.isNotBlank(checkResult.getEndtime())) {
//                dataMap.put("endtime", checkResult.getEndtime());
//            } else {
//                dataMap.put("endtime", "");
//            }
//            dataMap.put("errorgetcount", Integer.valueOf(0));
//        } catch (Exception e) {
//            dataMap.put("errorgetcount", (Integer)dataMap.get("errorgetcount") + 1);
//        }



        dataMap.put("endtime", "2029-01-01 00:00:00");
        dataMap.put("errorgetcount", Integer.valueOf(0));



        flushIsActive();
        return checkResult;
    }

    public void flushIsActive() {
        Integer errorgetcount = (Integer)dataMap.get("errorgetcount");
        if (errorgetcount>=5) {
            dataMap.put("isActive", false);
            activeLabel.setText("网络错误...");
        } else {
            String endtime = (String)dataMap.get("endtime");
            if (endtime.length()==19) {
                activeLabel.setText("授权至"+endtime);
            } else {
                activeLabel.setText("未授权");
                dataMap.put("isActive", false);
            }
            try {
                Date endTime = DateUtil.parse(endtime, "YYYY-MM-dd HH:mm:ss");
                long min = DateUtil.between(new Date(), endTime, DateUnit.MINUTE);
                if (min>0) {
                    dataMap.put("isActive", true);
                }
            } catch (Exception e) {

            }
        }
    }

    public void startDepthTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000L);
                        getDepthFlush();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    /**
     * 主交易线程任务
     */
    public void startMainTradeTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(8000L);
                        mainTask();
                        wanggeTask();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
    private void mainTask() {
        logger.info("交易任务 mainTask : ", isTrade);
        if (!isTrade) {
            defaultTradeButton.setText("启动刷量交易");
            return;
        }
        defaultTradeButton.setText("停止刷量交易");
        try {
            flushAccountAssetGetBean(); // 更新仓位
            boolean hasPosition = false;
            if (accountAssetGetBean.getData().getPositionList()!=null && accountAssetGetBean.getData().getPositionList().size()>0) {
                hasPosition = true;
            }
            OrderGetBean orderGetBean = orderService.getActiveOrderPage(MapUtils.getString(dataMap, "privateKey"),MapUtils.getString(dataMap, "accountId"));
            Integer limitOrderCount = 0;
            Integer limitBuyOrderCount = 0;
            Integer limitSellOrderCount = 0;
//            Integer tpslOrderCount = 0;
            Integer otherOrderCount = 0;
            for (OrderResponseBean orderResponseBean:orderGetBean.getData().getDataList()) {
                if ("LIMIT".equals(orderResponseBean.getType())) {
                    limitOrderCount++;
                    if (OrderTypeEnum.BUY.getVal().equals(orderResponseBean.getSide())) {
                        limitBuyOrderCount++;
                    } else {
                        limitSellOrderCount++;
                    }
                } else {
                    otherOrderCount++;
                }
            }
            final String coinId = MapUtils.getString(dataMap,"coinId");
            final String contractId = MapUtils.getString(dataMap,"contractId");
            final String privateKeyHex = MapUtils.getString(dataMap,"privateKey");
            final String accountId = MapUtils.getString(dataMap,"accountId");
            if (!hasPosition && limitOrderCount==2 && otherOrderCount==0) { // 初始挂单状态(无仓位，两个限价单)，成交0， 什么都不做等待着

            } else if (hasPosition && limitOrderCount==1 && otherOrderCount==2) { // 一单成交状态（有仓位，一个限价单，两个条件单：止盈止损），成交1， 什么都不做等待着

            } else if (!hasPosition && limitOrderCount==1 && otherOrderCount==0) { // 一单成交且平仓状态（无仓位，一个限价单），成交1.1，  取消所有订单，等下一个轮回 执行自身
                orderService.cancelAllOrder(accountId,privateKeyHex);
            } else if (!hasPosition && limitOrderCount==0 && otherOrderCount==0) { // 未挂单状态（无仓位，无订单），成交-1， 创建订单
                Double currPrice = ask1.add(bid1).divide(new BigDecimal(2)).doubleValue();
                Double cha = ask1.subtract(bid1).doubleValue();
                if (cha>0 && cha<0.8) {
                    final Double size = MapUtils.getDouble(dataMap,"size");
                    Integer zhiying = MapUtils.getInt(dataMap,"zhiying");
                    Integer zhisun = MapUtils.getInt(dataMap,"zhisun");
                    Integer ganggan = MapUtils.getInt(dataMap,"ganggan");
                    // 为提高挂单率，按买一卖一扩大浮动万分之0.5的价格挂单
                    final Double fu = currPrice*0.00009D;
                    //卖单 SELL
                    final Double sellPrice = ask1.doubleValue() + fu;
                    final Double sellZhisunPrice = currPrice*zhisun/10000/ganggan + sellPrice;
                    final Double sellZhiyingPrice = sellPrice - currPrice*zhiying/10000/ganggan;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                    orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.SELL,sellPrice,size, sellZhisunPrice, sellZhiyingPrice, currPrice);
                                } catch (Exception e) {
                                   e.printStackTrace();
                                }
                        }
                    }).start();

                    // 买单
                    final Double buyPrice = bid1.doubleValue() - fu;
                    final  Double buyZhisunPrice = buyPrice - currPrice*zhisun/10000/ganggan;
                    final  Double buyZhiyingPrice = buyPrice + currPrice*zhiying/10000/ganggan;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                    orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.BUY,buyPrice,size, buyZhisunPrice, buyZhiyingPrice, currPrice);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    }).start();
                } else { // websocket数据异常，什么都不做等待下一次进入

                }
            } else if (hasPosition && otherOrderCount<2) { // 一单成交有仓状态（有仓位，<两个限价单），TODO 交易所异常，  取消所有订单，全部平仓
                // TODO 查看持仓数量 如果是ETHUSD 的数量小于0.02时，需要补仓0.2个以便平仓，这是交易所异常情况
                List<PositionAsset> positionAssetList = accountAssetGetBean.getData().getPositionAssetList();
                for (PositionAsset asset:positionAssetList) {
                    if (contractIdETHUSD.equals(asset.getContractId())) {
                        PositionInfo info = null;
                        for (PositionInfo pi:accountAssetGetBean.getData().getPositionList()) {
                            if (pi.getContractId().equals(asset.getContractId())) {
                                info = pi;
                                break;
                            }
                        }
                        Double size = info.getOpenSize().doubleValue();
                        if (Math.abs(size)<0.02D) {
                            orderService.createOrder(coinId,contractId,privateKeyHex,accountId, size>0?OrderTypeEnum.BUY:OrderTypeEnum.SELL,0D,0.02D,null,null, asset.getAvgEntryPrice().doubleValue());
                        }
                        break;
                    }
                }
                closeAllPosition(false);
                orderService.cancelAllOrder(accountId,privateKeyHex);
            } else if (!hasPosition && limitOrderCount>=2 && (limitSellOrderCount==limitOrderCount || limitBuyOrderCount==limitOrderCount)) {  // TODO 异常   没有持仓但有两个以上限价买单或者两个以上限价卖单 ->> 取消所有订单
                orderService.cancelAllOrder(accountId,privateKeyHex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wanggeTask() {
        logger.info("交易任务 wanggeTask : ", isWanggeTrade);
        if (!isWanggeTrade) {
            wanggeTradeButton.setText("启动网格交易");
            return;
        }
        wanggeTradeButton.setText("停止网格交易");
        try {
            flushAccountAssetGetBean(); // 更新仓位
            boolean hasPosition = false;
            if (accountAssetGetBean.getData().getPositionList()!=null && accountAssetGetBean.getData().getPositionList().size()>0) {
                hasPosition = true;
            }
            OrderGetBean orderGetBean = orderService.getActiveOrderPage(MapUtils.getString(dataMap, "privateKey"),MapUtils.getString(dataMap, "accountId"));
            Integer limitOrderCount = 0;
            Integer limitBuyOrderCount = 0;
            Integer limitSellOrderCount = 0;
//            Integer tpslOrderCount = 0;
            Integer otherOrderCount = 0;
            for (OrderResponseBean orderResponseBean:orderGetBean.getData().getDataList()) {
                if ("LIMIT".equals(orderResponseBean.getType())) {
                    limitOrderCount++;
                    if (OrderTypeEnum.BUY.getVal().equals(orderResponseBean.getSide())) {
                        limitBuyOrderCount++;
                    } else {
                        limitSellOrderCount++;
                    }
                } else {
                    otherOrderCount++;
                }
            }
            final String coinId = MapUtils.getString(dataMap,"coinId");
            final String contractId = MapUtils.getString(dataMap,"contractId");
            final String privateKeyHex = MapUtils.getString(dataMap,"privateKey");
            final String accountId = MapUtils.getString(dataMap,"accountId");
            Double chushizijin = MapUtils.getDouble(dataMap, "chushizijin");
            Integer maxKuisun = MapUtils.getInt(dataMap, "maxKuisun");
            Integer gePrice = MapUtils.getInt(dataMap, "gePrice");
            Double zongquanyi = 0D;
            try {  // 当前账户数据
                List<CollateralAssetModel> assetModelList = accountAssetGetBean.getData().getCollateralAssetModelList();
                for (CollateralAssetModel assetModel:assetModelList) {
                    zongquanyi += assetModel.getTotalEquity().doubleValue();
                }
            } catch (Exception ee) {

            }
            currZijinLabel.setText("$ "+ String.format("%.2f", zongquanyi)+" ("+(chushizijin*(100-maxKuisun)/100)+")");
            if (zongquanyi.intValue() > chushizijin.intValue()) {
                chushizijinTextField.setText(""+zongquanyi.intValue());
                dataMap.put("chushizijin", Double.valueOf(zongquanyi.intValue()));
            } else if (zongquanyi <= (chushizijin*(100-maxKuisun)/100)) { // 达到最大亏损，平仓、取消订单
                // TODO 查看持仓数量 如果是ETHUSD 的数量小于0.02时，需要补仓0.2个以便平仓，这是交易所异常情况
                List<PositionAsset> positionAssetList = accountAssetGetBean.getData().getPositionAssetList();
                for (PositionAsset asset:positionAssetList) {
                    if (contractIdETHUSD.equals(asset.getContractId())) {
                        PositionInfo info = null;
                        for (PositionInfo pi:accountAssetGetBean.getData().getPositionList()) {
                            if (pi.getContractId().equals(asset.getContractId())) {
                                info = pi;
                                break;
                            }
                        }
                        Double size = info.getOpenSize().doubleValue();
                        if (Math.abs(size)<0.02D) {
                            orderService.createOrder(coinId,contractId,privateKeyHex,accountId, size>0?OrderTypeEnum.BUY:OrderTypeEnum.SELL,0D,0.02D,null,null, asset.getAvgEntryPrice().doubleValue());
                        }
                        break;
                    }
                }
                closeAllPosition(false);
                orderService.cancelAllOrder(accountId,privateKeyHex);
            } else {
                Double currPrice = ask1.add(bid1).divide(new BigDecimal(2)).doubleValue();
                Double cha = ask1.subtract(bid1).doubleValue();
                if (cha>0 && cha<0.8 && limitOrderCount==0 && !hasPosition) { // 没有仓位、没有挂单  以当前价格上下各持三单
                    orderService.cancelAllOrder(accountId,privateKeyHex);
                    final Double size = MapUtils.getDouble(dataMap,"size");
                    final Double fu = currPrice*gePrice/10000;
                    for (int n=1;n<=3;n++) {
                        final Double sellPrice = ask1.doubleValue() + fu*n;
                        final Double buySellSize  = size*n;
                        try {
                            orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.SELL,sellPrice,buySellSize, null, null, currPrice);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final Double buyPrice = bid1.doubleValue() - fu*n;
                        try {
                            orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.BUY,buyPrice,buySellSize, null, null, currPrice);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {  // 订单限制要两秒两个 {"code":"CREATE_ORDER_RATE_LIMIT_EXCEED","data":null,"msg":"You create order exceed rate limit, please retry after 0 seconds. Current limits: 2 ops per 2 seconds."
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (cha>0 && cha<0.8 && limitOrderCount>0 && limitOrderCount<6 && !hasPosition) { // 没有仓位、有挂单1-5单时  取消所有订单
                    orderService.cancelAllOrder(accountId,privateKeyHex);
                } else if (hasPosition) {  // 有仓位
                    if (limitOrderCount >= 5) {  // 有仓位,有5个或6个挂单；； 什么都不做

                    } else {  // 有仓位,挂单小于等于4个(已成交两个)；；取消所有订单，重新挂单
                        orderService.cancelAllOrder(accountId,privateKeyHex);
                        // 取仓位数量，在这里分正负对应多空
                        List<PositionAsset> positionAssetList = accountAssetGetBean.getData().getPositionAssetList();
                        Double openSize = 0D;                                // 当前持仓数量 分正负
                        Double positionOpenPrice = 0D;
                        if (positionAssetList.size()>0) {
                            String[][] data = new String[positionAssetList.size()][10];
                            int n=0;
                            for (PositionAsset asset:positionAssetList) {
                                PositionInfo info = null;
                                for (PositionInfo pi:accountAssetGetBean.getData().getPositionList()) {
                                    if (pi.getContractId().equals(asset.getContractId())) {
                                        info = pi;
                                        break;
                                    }
                                }
                                openSize = info.getOpenSize().doubleValue();
                                positionOpenPrice = asset.getAvgEntryPrice().doubleValue();
                            }
                        }
                        Double positionSize = Math.abs(openSize);
                        if (positionSize>0 && positionOpenPrice>0) {
                            final Double size = MapUtils.getDouble(dataMap,"size");
                            final Double fu = currPrice*gePrice/10000;
                            Integer xi = Double.valueOf(positionSize/(size*6)).intValue() +1;
                            Double yipingSellSize = 0D;
                            Double yipingBuySize = 0D;
                            for (int n=1;n<=3;n++) {
                                if (openSize>0) { // 持有多仓；；开多时加权系数，开空(平仓)时不加权系数且一单或两单平完仓
                                    // 开空仓时，卖一价格和持仓价格以高的为基准价上加
                                    Double sellPrice = positionOpenPrice + fu*n;
                                    if (ask1.doubleValue()>positionOpenPrice) {
                                        sellPrice = ask1.doubleValue() + fu*n;
                                    }
                                    Double sellSize = 0D;
                                    if (positionSize>=(size*6)) {
                                        sellSize = positionSize/2;
                                    } else if (positionSize>=size) {
                                        sellSize = positionSize;
                                    } else {
                                        sellSize = size*n;
                                    }
                                    if (yipingSellSize>=positionSize) {
                                        sellSize = size*n;
                                    }
                                    yipingSellSize += sellSize;
                                    try {
                                        orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.SELL,sellPrice,sellSize, null, null, currPrice);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    // TODO 开多仓时
                                    final Double buyPrice = bid1.doubleValue() - fu*n*xi;
                                    final Double buySize = size*(xi*1.5+n);
                                    try {
                                        orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.BUY,buyPrice,buySize, null, null, currPrice);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else { // 持有空仓；；开多(平仓)时不加权系数且一单或两单平完仓，开空时加权系数
                                    final Double sellPrice = ask1.doubleValue() + fu*n*xi;
                                    final Double sellSize = size*(xi*1.5+n);
                                    try {
                                        orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.SELL,sellPrice,sellSize, null, null, currPrice);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    // 开多仓时，买一价格和持仓价格以低的为基准价上减
                                    Double buyPrice = positionOpenPrice - fu*n;
                                    if (bid1.doubleValue()<positionOpenPrice) {
                                        buyPrice = bid1.doubleValue() - fu*n;
                                    }
                                    Double buySize = 0D;
                                    if (positionSize>=(size*6)) {
                                        buySize = positionSize/2;
                                    } else if (positionSize>=size) {
                                        buySize = positionSize;
                                    } else {
                                        buySize = size*n;
                                    }
                                    if (yipingBuySize>=positionSize) {
                                        buySize = size*n;
                                    }
                                    yipingBuySize += buySize;
                                    try {
                                        orderService.createOrder(coinId,contractId,privateKeyHex,accountId,OrderTypeEnum.BUY,buyPrice,buySize, null, null, currPrice);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {  // 订单限制要两秒两个 {"code":"CREATE_ORDER_RATE_LIMIT_EXCEED","data":null,"msg":"You create order exceed rate limit, please retry after 0 seconds. Current limits: 2 ops per 2 seconds."
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            logger.error("仓位异常");
                            logger.error(JSONUtil.toJsonStr(accountAssetGetBean));
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DateTime responseTime = DateUtil.date();
    DecimalFormat df2 = new DecimalFormat("#.00");
    public void getDepthFlush() {
        try {
            String jsonStr = PublicUtils.getDepth(MapUtils.getString(dataMap, "contractId"));
            DepthGetBean getBean = JSONUtil.toBean(jsonStr, DepthGetBean.class);
            responseTime.setTime(getBean.getResponseTime());
            String responseTimeStr = responseTime.toString(DatePattern.NORM_DATETIME_PATTERN);
            depthLastTimeLabel.setText(responseTimeStr);
            BigDecimal ask1 = getBean.getData().get(0).getAsks().get(0).getPrice();
            BigDecimal bid1 = getBean.getData().get(0).getBids().get(0).getPrice();
            BigDecimal currPrice = ask1.add(bid1).divide(new BigDecimal(2));
            currPriceLabel.setText("$"+currPrice.setScale(2, RoundingMode.HALF_UP));
            BigDecimal cha = ask1.subtract(bid1);
            askbidPriceLabel.setText(ask1.setScale(2, RoundingMode.HALF_UP).toString()+"  "+ cha.setScale(2, RoundingMode.HALF_UP).toString()+"  "+ bid1.setScale(2, RoundingMode.HALF_UP).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigDecimal ask1;
    private BigDecimal bid1;
    public String processWebSocketMessage(String message) {
        try {
            if (message.indexOf("depth.10000002.15")>0) {
                DepthQuoteBean quoteBean = JSONUtil.toBean(message, DepthQuoteBean.class);
                List<PriceSizeBean> askList = quoteBean.getContent().getData().get(0).getAsks();
                List<PriceSizeBean> bidList = quoteBean.getContent().getData().get(0).getBids();
                if (askList!=null && askList.size()>0) {
                    ask1 = askList.get(0).getPrice();
                }
                if (bidList!=null && bidList.size()>0) {
                    bid1 = bidList.get(0).getPrice();
                }
                if (ask1!=null && bid1!=null) {
                    responseTime = DateUtil.date();
                    String responseTimeStr = responseTime.toString(DatePattern.NORM_DATETIME_PATTERN);
                    depthLastTimeLabel.setText(responseTimeStr);
                    BigDecimal currPrice = ask1.add(bid1).divide(new BigDecimal(2));
                    currPriceLabel.setText("$"+currPrice.setScale(2, RoundingMode.HALF_UP));
                    BigDecimal cha = ask1.subtract(bid1);
                    askbidPriceLabel.setText(ask1.setScale(2, RoundingMode.HALF_UP).toString()+"  "+ cha.setScale(2, RoundingMode.HALF_UP).toString()+"  "+ bid1.setScale(2, RoundingMode.HALF_UP).toString());
                }
            } else if (message.indexOf("ping")>0 && message.length()<44) {
                PingPong pp = JSONUtil.toBean(message, PingPong.class);
                pp.setType("pong");
                String json = JSONUtil.toJsonStr(pp);
                publicWebSocketClient.send(json);
            } else {
                logger.info("收到PUBLIC消息: " + message);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String processPrivateWebSocketMessage(String message) {
        try {
            if (message.indexOf("ping")>0 && message.length()<44) {
                PingPong pp = JSONUtil.toBean(message, PingPong.class);
                pp.setType("pong");
                String json = JSONUtil.toJsonStr(pp);
                privateWebSocketClient.send(json);
            } else if (message.indexOf("ORDER_UPDATE")>0) {
                logger.info(message);
            } else {
                logger.info(message);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return "";
    }




    class PublicWebSocketClient extends WebSocketClient {

        private mFrame frame;

        public PublicWebSocketClient(URI serverUri, mFrame f) {
            super(serverUri);
            this.frame = f;
        }

        @Override
        public void onOpen(ServerHandshake handShake) {
            logger.info("PublicWebSocketClient 连接已建立");
            // 连接成功后可以发送初始消息
            send("{\"type\": \"subscribe\", \"channel\": \"depth.10000002.15\"}");
        }

        @Override
        public void onMessage(String message) {
            processWebSocketMessage(message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            logger.info("PublicWebSocketClient 连接已关闭，代码: " + code + ", 原因: " + reason);
            wsPublicStart();
        }

        @Override
        public void onError(Exception ex) {
            logger.info("PublicWebSocketClient 发生错误: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    class PrivateWebSocketClient extends WebSocketClient {

        private mFrame frame;

        public PrivateWebSocketClient(URI serverUri, mFrame f) {
            super(serverUri);
            this.frame = f;
        }

        @Override
        public void onOpen(ServerHandshake handShake) {
            logger.info("PrivateWebSocketClient 连接已建立");
        }

        @Override
        public void onMessage(String message) {
            processPrivateWebSocketMessage(message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            logger.info("PrivateWebSocketClient 连接已关闭，代码: " + code + ", 原因: " + reason);
            wsPrivateStart();
        }

        @Override
        public void onError(Exception ex) {
            logger.info("PrivateWebSocketClient 发生错误: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private void flushPositionButtonMouseClicked(MouseEvent e) {
        flushPositionTable();
    }

    private void gangganTishiLabel1MouseClicked(MouseEvent e) {
        // 弹出信息对话框
        JOptionPane.showMessageDialog(this, ganggantishi, "警示", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 平仓
     */
    private void closeAllPosition(Boolean isShowMessage) {
        if (flushAccountAssetGetBean()) {
            try {
                List<PositionAsset> positionAssetList = accountAssetGetBean.getData().getPositionAssetList();
                String[][] data = new String[positionAssetList.size()][10];
                int n=0;
                for (PositionAsset asset:positionAssetList) {
                    PositionInfo info = null;
                    for (PositionInfo pi:accountAssetGetBean.getData().getPositionList()) {
                        if (pi.getContractId().equals(asset.getContractId())) {
                            info = pi;
                            break;
                        }
                    }
                    Double size = info.getOpenSize().doubleValue();
                    orderService.createOrder(MapUtils.getString(dataMap, "coinId"),asset.getContractId(),MapUtils.getString(dataMap, "privateKey"),MapUtils.getString(dataMap, "accountId"), size>0?OrderTypeEnum.SELL:OrderTypeEnum.BUY,0D,Math.abs(size),null,null, asset.getAvgEntryPrice().doubleValue());
                }
                if (isShowMessage) {
                    JOptionPane.showMessageDialog(this,"操作成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                flushPositionTable();
            }catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (isShowMessage) {
                JOptionPane.showMessageDialog(this,"操作失败", "提示", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void closeAllPositionButtonMouseClicked(MouseEvent event) {
        closeAllPosition(true);
    }

    private void defaultTradeButtonMouseClicked(MouseEvent e) {
        isTrade = !isTrade;
    }

    private void wanggeTradeButtonMouseClicked(MouseEvent e) {
        isWanggeTrade = !isWanggeTrade;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        privateKeyTextField = new JTextField();
        label1 = new JLabel();
        privateKeyLengthLabel = new JLabel();
        label2 = new JLabel();
        accountIdTextField = new JTextField();
        accountIdLengthLabel = new JLabel();
        label3 = new JLabel();
        skeyTextField = new JTextField();
        label4 = new JLabel();
        mkeyTextField = new JTextField();
        shuaxinButton = new JButton();
        activeLabel = new JLabel();
        lastGetLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        positionTable = new JTable();
        flushPositionButton = new JButton();
        closeAllPositionButton = new JButton();
        scrollPane2 = new JScrollPane();
        accountAssetTable = new JTable();
        separator1 = new JSeparator();
        contractComboBox = new JComboBox();
        label5 = new JLabel();
        label6 = new JLabel();
        sizeComboBox = new JComboBox();
        sizeValueLabel = new JLabel();
        label7 = new JLabel();
        zhiyingComboBox = new JComboBox();
        label8 = new JLabel();
        label9 = new JLabel();
        zhisunComboBox = new JComboBox();
        label10 = new JLabel();
        gangganTishiLabel1 = new JLabel();
        gangganComboBox = new JComboBox();
        label12 = new JLabel();
        currPriceLabel = new JLabel();
        depthLastTimeLabel = new JLabel();
        askbidPriceLabel = new JLabel();
        defaultTradeButton = new JButton();
        separator2 = new JSeparator();
        separator3 = new JSeparator();
        label11 = new JLabel();
        wanggeTradeButton = new JButton();
        label13 = new JLabel();
        chushizijinTextField = new JTextField();
        label14 = new JLabel();
        maxKuisunComboBox = new JComboBox();
        label15 = new JLabel();
        label16 = new JLabel();
        gePriceComboBox = new JComboBox();
        label17 = new JLabel();
        currZijinLabel = new JLabel();

        //======== this ========
        setTitle("edgexTrade");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
        });
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- privateKeyTextField ----
        privateKeyTextField.addInputMethodListener(new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent e) {}
            @Override
            public void inputMethodTextChanged(InputMethodEvent e) {
                privateKeyTextFieldInputMethodTextChanged(e);
            }
        });
        contentPane.add(privateKeyTextField);
        privateKeyTextField.setBounds(100, 10, 145, privateKeyTextField.getPreferredSize().height);

        //---- label1 ----
        label1.setText("privateKey");
        contentPane.add(label1);
        label1.setBounds(25, 12, 70, label1.getPreferredSize().height);

        //---- privateKeyLengthLabel ----
        privateKeyLengthLabel.setText("0");
        contentPane.add(privateKeyLengthLabel);
        privateKeyLengthLabel.setBounds(248, 13, 30, privateKeyLengthLabel.getPreferredSize().height);

        //---- label2 ----
        label2.setText("AccountId");
        contentPane.add(label2);
        label2.setBounds(275, 13, 70, 17);

        //---- accountIdTextField ----
        accountIdTextField.addInputMethodListener(new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent e) {}
            @Override
            public void inputMethodTextChanged(InputMethodEvent e) {
                privateKeyTextFieldInputMethodTextChanged(e);
            }
        });
        contentPane.add(accountIdTextField);
        accountIdTextField.setBounds(340, 10, 145, 23);

        //---- accountIdLengthLabel ----
        accountIdLengthLabel.setText("0");
        contentPane.add(accountIdLengthLabel);
        accountIdLengthLabel.setBounds(488, 13, 30, 17);

        //---- label3 ----
        label3.setText("skey");
        contentPane.add(label3);
        label3.setBounds(505, 13, 35, 17);

        //---- skeyTextField ----
        skeyTextField.addInputMethodListener(new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent e) {}
            @Override
            public void inputMethodTextChanged(InputMethodEvent e) {
                privateKeyTextFieldInputMethodTextChanged(e);
            }
        });
        contentPane.add(skeyTextField);
        skeyTextField.setBounds(535, 10, 145, 23);

        //---- label4 ----
        label4.setText("mkey");
        contentPane.add(label4);
        label4.setBounds(688, 15, 32, 15);

        //---- mkeyTextField ----
        mkeyTextField.setEditable(false);
        mkeyTextField.addInputMethodListener(new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent e) {}
            @Override
            public void inputMethodTextChanged(InputMethodEvent e) {
                privateKeyTextFieldInputMethodTextChanged(e);
            }
        });
        contentPane.add(mkeyTextField);
        mkeyTextField.setBounds(724, 10, 156, 23);

        //---- shuaxinButton ----
        shuaxinButton.setText("\u5237\u65b0");
        shuaxinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shuaxinButtonMouseClicked(e);
            }
        });
        contentPane.add(shuaxinButton);
        shuaxinButton.setBounds(880, 10, 60, shuaxinButton.getPreferredSize().height);

        //---- activeLabel ----
        activeLabel.setText("\u6388\u6743\u81f32025-12-12 12:12:12");
        contentPane.add(activeLabel);
        activeLabel.setBounds(945, 15, 175, 17);
        contentPane.add(lastGetLabel);
        lastGetLabel.setBounds(995, 0, 130, 17);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(positionTable);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(15, 445, 1095, 135);

        //---- flushPositionButton ----
        flushPositionButton.setText("\u5237\u65b0\u4ed3\u4f4d");
        flushPositionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                flushPositionButtonMouseClicked(e);
            }
        });
        contentPane.add(flushPositionButton);
        flushPositionButton.setBounds(921, 420, 88, 23);

        //---- closeAllPositionButton ----
        closeAllPositionButton.setText("\u5168\u90e8\u5e73\u4ed3");
        closeAllPositionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                flushPositionButtonMouseClicked(e);
                closeAllPositionButtonMouseClicked(e);
                closeAllPositionButtonMouseClicked(e);
            }
        });
        contentPane.add(closeAllPositionButton);
        closeAllPositionButton.setBounds(1015, 420, 88, 23);

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(accountAssetTable);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(15, 370, 715, 72);
        contentPane.add(separator1);
        separator1.setBounds(10, 37, 1100, 5);
        contentPane.add(contractComboBox);
        contractComboBox.setBounds(76, 47, 144, 23);

        //---- label5 ----
        label5.setText("contract");
        contentPane.add(label5);
        label5.setBounds(20, 50, 60, 17);

        //---- label6 ----
        label6.setText("\u4e0b\u5355\u6570\u91cf");
        contentPane.add(label6);
        label6.setBounds(425, 50, 55, 17);
        contentPane.add(sizeComboBox);
        sizeComboBox.setBounds(482, 46, 52, 23);

        //---- sizeValueLabel ----
        sizeValueLabel.setText("ETH");
        contentPane.add(sizeValueLabel);
        sizeValueLabel.setBounds(536, 50, 75, 17);

        //---- label7 ----
        label7.setText("\u6b62\u76c8");
        contentPane.add(label7);
        label7.setBounds(606, 50, 35, 17);
        contentPane.add(zhiyingComboBox);
        zhiyingComboBox.setBounds(636, 47, 55, 23);

        //---- label8 ----
        label8.setText("\u2031");
        contentPane.add(label8);
        label8.setBounds(693, 51, 25, 17);

        //---- label9 ----
        label9.setText("\u6b62\u635f");
        contentPane.add(label9);
        label9.setBounds(721, 49, 35, 17);
        contentPane.add(zhisunComboBox);
        zhisunComboBox.setBounds(751, 49, 55, 23);

        //---- label10 ----
        label10.setText("\u2031");
        contentPane.add(label10);
        label10.setBounds(808, 50, 25, 17);

        //---- gangganTishiLabel1 ----
        gangganTishiLabel1.setText(" \uff01");
        gangganTishiLabel1.setForeground(new Color(0xf00000));
        gangganTishiLabel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gangganTishiLabel1MouseClicked(e);
            }
        });
        contentPane.add(gangganTishiLabel1);
        gangganTishiLabel1.setBounds(926, 50, 20, gangganTishiLabel1.getPreferredSize().height);
        contentPane.add(gangganComboBox);
        gangganComboBox.setBounds(864, 48, 55, 23);

        //---- label12 ----
        label12.setText("\u6760\u6746");
        contentPane.add(label12);
        label12.setBounds(836, 50, 35, 17);

        //---- currPriceLabel ----
        currPriceLabel.setText("$");
        contentPane.add(currPriceLabel);
        currPriceLabel.setBounds(225, 50, 70, 17);

        //---- depthLastTimeLabel ----
        depthLastTimeLabel.setText("2025-12-12 12:12:12");
        contentPane.add(depthLastTimeLabel);
        depthLastTimeLabel.setBounds(295, 50, 130, 17);
        contentPane.add(askbidPriceLabel);
        askbidPriceLabel.setBounds(70, 75, 155, 17);

        //---- defaultTradeButton ----
        defaultTradeButton.setText("\u542f\u52a8\u4ea4\u6613");
        defaultTradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                defaultTradeButtonMouseClicked(e);
            }
        });
        contentPane.add(defaultTradeButton);
        defaultTradeButton.setBounds(955, 50, 145, defaultTradeButton.getPreferredSize().height);
        contentPane.add(separator2);
        separator2.setBounds(225, 80, 885, 5);

        //---- separator3 ----
        separator3.setOrientation(SwingConstants.VERTICAL);
        contentPane.add(separator3);
        separator3.setBounds(224, 82, 5, 32);

        //---- label11 ----
        label11.setText("\u7f51\u683c:");
        contentPane.add(label11);
        label11.setBounds(230, 90, 30, label11.getPreferredSize().height);

        //---- wanggeTradeButton ----
        wanggeTradeButton.setText("\u542f\u52a8\u7f51\u683c\u4ea4\u6613");
        wanggeTradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                wanggeTradeButtonMouseClicked(e);
            }
        });
        contentPane.add(wanggeTradeButton);
        wanggeTradeButton.setBounds(960, 90, 140, wanggeTradeButton.getPreferredSize().height);

        //---- label13 ----
        label13.setText("\u521d\u59cb\u8d44\u91d1$");
        contentPane.add(label13);
        label13.setBounds(265, 90, 60, label13.getPreferredSize().height);
        contentPane.add(chushizijinTextField);
        chushizijinTextField.setBounds(326, 90, 69, chushizijinTextField.getPreferredSize().height);

        //---- label14 ----
        label14.setText("\u6700\u5927\u4e8f\u635f");
        contentPane.add(label14);
        label14.setBounds(new Rectangle(new Point(551, 92), label14.getPreferredSize()));
        contentPane.add(maxKuisunComboBox);
        maxKuisunComboBox.setBounds(605, 90, 60, maxKuisunComboBox.getPreferredSize().height);

        //---- label15 ----
        label15.setText("%");
        contentPane.add(label15);
        label15.setBounds(new Rectangle(new Point(670, 93), label15.getPreferredSize()));

        //---- label16 ----
        label16.setText("\u95f4\u9694\u4ef7\u683c");
        contentPane.add(label16);
        label16.setBounds(new Rectangle(new Point(690, 95), label16.getPreferredSize()));
        contentPane.add(gePriceComboBox);
        gePriceComboBox.setBounds(740, 92, 50, gePriceComboBox.getPreferredSize().height);

        //---- label17 ----
        label17.setText("\u2031");
        contentPane.add(label17);
        label17.setBounds(795, 95, 25, 17);

        //---- currZijinLabel ----
        currZijinLabel.setText("$");
        currZijinLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
        currZijinLabel.setForeground(new Color(0xff3333));
        contentPane.add(currZijinLabel);
        currZijinLabel.setBounds(397, 89, 143, currZijinLabel.getPreferredSize().height);

        contentPane.setPreferredSize(new Dimension(1130, 630));
        setSize(1130, 630);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JTextField privateKeyTextField;
    private JLabel label1;
    private JLabel privateKeyLengthLabel;
    private JLabel label2;
    private JTextField accountIdTextField;
    private JLabel accountIdLengthLabel;
    private JLabel label3;
    private JTextField skeyTextField;
    private JLabel label4;
    private JTextField mkeyTextField;
    private JButton shuaxinButton;
    private JLabel activeLabel;
    private JLabel lastGetLabel;
    private JScrollPane scrollPane1;
    private JTable positionTable;
    private JButton flushPositionButton;
    private JButton closeAllPositionButton;
    private JScrollPane scrollPane2;
    private JTable accountAssetTable;
    private JSeparator separator1;
    private JComboBox contractComboBox;
    private JLabel label5;
    private JLabel label6;
    private JComboBox sizeComboBox;
    private JLabel sizeValueLabel;
    private JLabel label7;
    private JComboBox zhiyingComboBox;
    private JLabel label8;
    private JLabel label9;
    private JComboBox zhisunComboBox;
    private JLabel label10;
    private JLabel gangganTishiLabel1;
    private JComboBox gangganComboBox;
    private JLabel label12;
    private JLabel currPriceLabel;
    private JLabel depthLastTimeLabel;
    private JLabel askbidPriceLabel;
    private JButton defaultTradeButton;
    private JSeparator separator2;
    private JSeparator separator3;
    private JLabel label11;
    private JButton wanggeTradeButton;
    private JLabel label13;
    private JTextField chushizijinTextField;
    private JLabel label14;
    private JComboBox maxKuisunComboBox;
    private JLabel label15;
    private JLabel label16;
    private JComboBox gePriceComboBox;
    private JLabel label17;
    private JLabel currZijinLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
