package com.jni;

/**
 * Created by wangwei on 2019/5/22.
 */

public class JniTest {
    // Used to load the 'native-lib' library on application startup.
    // 加载库
    static {
        System.loadLibrary("hello");
    }
    // native申明 在c里面实现
    public native String stringTest();

    public native String stringHello();



}
