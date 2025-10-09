package com.riecardx.edgex.utils;

import cn.hutool.core.util.ZipUtil;
import com.edgex.exchange.model.CoinModel;
import com.edgex.exchange.model.ContractModel;
import com.edgex.exchange.model.order.CreateOrderParam;
import com.edgex.exchange.model.order.OpenTpSlParam;
import com.edgex.exchange.utils.BigIntUtil;
import com.edgex.exchange.utils.L2SignUtil;
import com.edgex.exchange.utils.ecdsa.Ecdsa;
import com.edgex.exchange.utils.ecdsa.PrivateKey;
import com.edgex.exchange.utils.ecdsa.Signature;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import static com.edgex.exchange.api.Auth.K_MODULUS;

public class SignUtils {

    private static final String privateKeyHex  = "len=64";

    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis();
        System.out.println("timestamp:"+timestamp);
        getGetAuthSignature(privateKeyHex,timestamp,"accountId=len18","/api/v1/private/account/getPositionTermPage");
    }

    public static String getGetAuthSignature(String _privateKeyHex, long timestamp, String requestStr, String requestPath) {
        BigInteger mySecretKey = new BigInteger(_privateKeyHex, 16);
        PrivateKey privateKey = PrivateKey.create(mySecretKey);
        String message = timestamp + "GET" + requestPath + requestStr;
        String msg = TypeEncoder.encodePacked(new Utf8String(message));
        BigInteger msgHash = Numeric.toBigInt(Hash.sha3(Numeric.hexStringToByteArray(msg)));
        msgHash = msgHash.mod(K_MODULUS);
        Signature signature = Ecdsa.sign(msgHash, privateKey);
        String var10000 = TypeEncoder.encodePacked(new Uint256(signature.r));
        String starkSignature = var10000 + TypeEncoder.encodePacked(new Uint256(signature.s)) + TypeEncoder.encodePacked(new Uint256(privateKey.publicKey().point.y));
        return starkSignature;
    }

    public static String signOrder(CreateOrderParam request, ContractModel contract, CoinModel quotelCoin, PrivateKey privateKey) {
        BigInteger msgHash = L2SignUtil.hashLimitOrder(request.getSide() == "BUY", BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId()), BigIntUtil.toBigInt(contract.getStarkExSyntheticAssetId()), BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId()), UnsignedLong.valueOf((new BigDecimal(request.getL2Value())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf((new BigDecimal(request.getL2Size())).multiply(new BigDecimal(BigIntUtil.toBigInt(contract.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf((new BigDecimal(request.getL2LimitFee())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf(request.getAccountId()), UnsignedInteger.valueOf(request.getL2Nonce()), UnsignedInteger.valueOf(Long.valueOf(request.getL2ExpireTime()) / 3600000L));
        Signature signature = Ecdsa.sign(msgHash, privateKey);
        String var10000 = TypeEncoder.encodePacked(new Uint256(signature.r));
        String sig = var10000 + TypeEncoder.encodePacked(new Uint256(signature.s));
        return sig;
    }

    public static String signOpenTpSlParam(OpenTpSlParam request, ContractModel contract, CoinModel quotelCoin, PrivateKey privateKey, String accountId) {
        Object obj = BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId());
        obj = BigIntUtil.toBigInt(contract.getStarkExSyntheticAssetId());
        obj = BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId());
        obj = UnsignedLong.valueOf((new BigDecimal(request.getL2Value())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact());
        obj = UnsignedLong.valueOf((new BigDecimal(request.getL2Size())).multiply(new BigDecimal(BigIntUtil.toBigInt(contract.getStarkExResolution()))).toBigIntegerExact());
        System.out.println(new BigDecimal(request.getL2LimitFee()));
        System.out.println(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()));
        System.out.println((new BigDecimal(request.getL2LimitFee())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))));
        System.out.println();
        System.out.println();
        obj = UnsignedLong.valueOf((new BigDecimal(request.getL2LimitFee())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact());
        obj = UnsignedLong.valueOf(accountId);
        obj = UnsignedInteger.valueOf(request.getL2Nonce());
        obj = UnsignedInteger.valueOf(Long.valueOf(request.getL2ExpireTime()) / 3600000L);
        BigInteger msgHash = L2SignUtil.hashLimitOrder(request.getSide() == "BUY", BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId()), BigIntUtil.toBigInt(contract.getStarkExSyntheticAssetId()), BigIntUtil.toBigInt(quotelCoin.getStarkExAssetId()), UnsignedLong.valueOf((new BigDecimal(request.getL2Value())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf((new BigDecimal(request.getL2Size())).multiply(new BigDecimal(BigIntUtil.toBigInt(contract.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf((new BigDecimal(request.getL2LimitFee())).multiply(new BigDecimal(BigIntUtil.toBigInt(quotelCoin.getStarkExResolution()))).toBigIntegerExact()), UnsignedLong.valueOf(accountId), UnsignedInteger.valueOf(request.getL2Nonce()), UnsignedInteger.valueOf(Long.valueOf(request.getL2ExpireTime()) / 3600000L));
        Signature signature = Ecdsa.sign(msgHash, privateKey);
        String var10000 = TypeEncoder.encodePacked(new Uint256(signature.r));
        String sig = var10000 + TypeEncoder.encodePacked(new Uint256(signature.s));
        return sig;
    }

    public static String getMacAddress(){
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                }
            }
            String s = sb.toString().replaceAll("\\d", "").replaceAll("_", "").replaceAll("-", "");
            if (s.length()>9) {
                s = s.substring(0,9);
            }
            System.out.println(s);
            return s;
        } catch (Exception e) {
            return "BEANSOFT";
        }

    }

    public static String getDiskSerial() {
        // 通过运行系统命令获取硬盘序列号
        // 这里仅提供一个例子，具体实现依操作系统而异
        String os = System.getProperty("os.name").toLowerCase();
        String command = "";
        if (os.contains("win")) {
            command = "wmic diskdrive get serialnumber";
        } else if (os.contains("nix") || os.contains("nux")) {
            command = "sudo hdparm -I /dev/sda | grep 'Serial Number'";
        }
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder serialNumber = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                serialNumber.append(line.trim());
            }
            String s = serialNumber.toString().replaceAll("SerialNumber", "").replaceAll("\\d", "").replaceAll("\\.", "").replaceAll("_", "").replaceAll("-", "");
            if (s.length()>9) {
                s = s.substring(0,9);
            }
            System.out.println(s);
            return s;
        } catch (Exception e) {
            return "RIECARDX";
        }
    }

}
