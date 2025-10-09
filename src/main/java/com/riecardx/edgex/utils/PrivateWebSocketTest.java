package com.riecardx.edgex.utils;

import com.riecardx.edgex.frame.mFrame;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class PrivateWebSocketTest {

    private static PrivateWebSocketClient privateWebSocketClient;

    public static void main(String[] args) {
        try {
            URI serverUri = new URI("wss://quote.edgex.exchange/api/v1/private/ws?accountId=");
            privateWebSocketClient = new PrivateWebSocketClient(serverUri, null);
            long timestamp = System.currentTimeMillis();
            String privateKeyHex = "";
            String accountId = "";
            String sign = SignUtils.getGetAuthSignature(privateKeyHex, timestamp,"accountId="+accountId, "/api/v1/private/ws");
            privateWebSocketClient.addHeader("X-edgeX-Api-Timestamp", ""+timestamp);
            privateWebSocketClient.addHeader("X-edgeX-Api-Signature", sign);
            privateWebSocketClient.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static class PrivateWebSocketClient extends WebSocketClient {

        private mFrame frame;

        public PrivateWebSocketClient(URI serverUri, mFrame f) {
            super(serverUri);
            this.frame = f;
        }

        @Override
        public void onOpen(ServerHandshake handShake) {
            System.out.println("连接已建立");
        }

        @Override
        public void onMessage(String message) {
            System.out.println("收到消息: " + message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("连接已关闭，代码: " + code + ", 原因: " + reason);
        }

        @Override
        public void onError(Exception ex) {
            System.out.println("发生错误: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
