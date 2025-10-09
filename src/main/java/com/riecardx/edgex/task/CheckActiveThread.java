package com.riecardx.edgex.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.riecardx.edgex.bean.CheckResult;
import com.riecardx.edgex.frame.mFrame;
import com.riecardx.edgex.utils.HttpUtils;
import com.riecardx.edgex.utils.SignUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class CheckActiveThread implements Runnable{

    private volatile boolean running = true; // 使用volatile确保多线程中的可见性
    public CheckActiveThread(mFrame frame) {
        this.frame = frame;
    }

    private mFrame frame;

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(60000L);
                try {
                    frame.checkActiveStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void stopRunning() {
        running = false; // 设置标志位为false来停止循环
    }
}
