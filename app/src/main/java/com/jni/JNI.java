package com.jni;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

/**
 * c调java的方法
 */

public class JNI {

    private Context context;

    public JNI(Context context) {
        this.context = context;
    }

    static {
        System.loadLibrary("hello");

    }


    //  native申明 暴露给Java层使用，在c里面实现
    //java调用C 然后C调用Java空方法
    public native void callbackmethod();

    //java调用C 然后C调用Java中的带两个int参数的方法
    public native void callbackIntmethod();

    //java调用C 然后C调用Java中参数为string的方法
    public native void callbackStringmethod();

    //java调用C 然后C调用Java中静态方法
    public native void callStaticmethod();

    //java调用C中有返回值方法
    public native String stringTest();

    public native int addJia(int x ,int y);
    //使用数组图片处理
   public native int[] getImgToGray(int[] data, int w, int h);

   //模糊
   protected static native void blurPixels(int[] img, int w, int h, int r);



    protected static native void blurBitmap(Bitmap bitmap, int r);



    /*------------------------------------------------------------------------------------------------*/


    //C调用java空方法
    public void helloFromJava() {
        Toast.makeText(context, "C调用了java的空方法", Toast.LENGTH_SHORT).show();
    }

    //C调用java中的带两个int参数的方法
    public int add(int x, int y) {
        Toast.makeText(context, "C调用了java的add--" + x + "---" + y, Toast.LENGTH_SHORT).show();
        return x + y;
    }


    //C调用java中参数为string的方法
    public void printString(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    //C调用java中静态方法
    public static void staticmethod(String s) {
        Log.w("王伟", s + ",我是被C调用的静态方法");
    }

}
