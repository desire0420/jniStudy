package com.t.myapplication;

/**
 * Created by wangwei on 2019/5/23.
 * 注意使用so的時候业务函调用的java 方法的包名必须和创建so的包名一样
 * 因为是根据包名+类名+方法名去寻找c里面的方法，不然会报错
 * 一般暴露的java文件 提供so的一方会提供一个；lib  开发者只需要调用lib 就好
 *
 *
 *
 * 出现这个异常的原因是NDKtest类的包名一定要是com.example.fanenqian.ndk1，原因是JNI接口Java_com_example_fanenqian_ndk1_NDKtest_getString中，com_example_fanenqian_ndk1代表的是package name，NDKtest则是class name。所以该NDKtest包名必须是com_example_fanenqian_ndk1，类名是NDKtest，也就是说，我们.so中函数声明涉及到的package name和class name与调用它的package name和class name不符。因此我们要改变我们工程中的package name和class name。使其与.so文件中函数签名提示的一致，在这个类中加入native方法的声明。
 */

public class MyJni {
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
