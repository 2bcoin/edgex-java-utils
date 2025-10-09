package com.riecardx.edgex;

import com.riecardx.edgex.frame.mFrame;
import com.riecardx.edgex.utils.AccountUtils;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Hello, World 1!");
        System.out.println("Hello, World! 2");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mFrame frame = new mFrame();
                frame.setVisible(true); // 显示窗口
            }
        });
    }
}
