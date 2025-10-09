package com.riecardx.edgex.utils;

public class HelloTest {
    static native String aardio(String code);
    public static String main(String[] args) {
        System.out.println("Hello this is a simply test"); //输出字符串,字符串用引号括起来
        System.out.println( args[0]  );
        System.out.println( args[1]  );
        return "aardio,你好,这是给你的返回值";
    }
    public int testadd(int a) {
        return a + 100;
    }
}
