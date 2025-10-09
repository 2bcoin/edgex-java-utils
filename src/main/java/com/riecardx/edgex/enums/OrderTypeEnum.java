package com.riecardx.edgex.enums;

public enum OrderTypeEnum {

    BUY("BUY"),
    SELL("SELL");

    private OrderTypeEnum(String val) {
        this.val = val;
    }

    public static OrderTypeEnum getEnum(String val) {
        for (OrderTypeEnum e:OrderTypeEnum.values()) {
            if (e.getVal() == val) {
                return e;
            }
        }
        return null;
    }

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
