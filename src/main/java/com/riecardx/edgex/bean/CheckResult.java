package com.riecardx.edgex.bean;

/**
 * "endtime":"2025-10-05 21:19:46",
 *     --"error":0,
 *     --"id":14,
 *     --"ip":"110.188.92.4",
 *     "mac":"mkey",
 *     "secret":"riecardx",
 *     "size":1
 */
public class CheckResult {
    private Integer id;
    private Integer error;
    private Integer size;
    private String ip;
    private String mac;
    private String secret;
    private String endtime;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
