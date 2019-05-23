package com.jni;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn, java_test, jni_test;
    private TextView tv;
    private JNI jni;
    private ImageView java_image, jni_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        jni_test = findViewById(R.id.jni_test);
        java_test = findViewById(R.id.java_test);
        tv = findViewById(R.id.sample_text);
        jni = new JNI(getApplicationContext());
        java_image = findViewById(R.id.image);
        jni_image = findViewById(R.id.jni_image);
        findViewById(R.id.bt_javaInt).setOnClickListener(this);
        findViewById(R.id.bt_javanull).setOnClickListener(this);
        findViewById(R.id.bt_javaString).setOnClickListener(this);
        findViewById(R.id.bt_static).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        jni_test.setOnClickListener(this);
        java_test.setOnClickListener(this);
        //Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.image)).getBitmap();
        // java_image.setImageBitmap(FileUtil.blur(bitmap, 30));
        //设置高斯模糊

    }


    public Bitmap getJniBitmap() {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.image)).getBitmap();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        //调用JNI 通过ImgToGray.so把彩色像素转为灰度像素
        int[] resultInt = jni.getImgToGray(pix, w, h);
        Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        resultImg.setPixels(resultInt, 0, w, 0, 0, w, h);
        return resultImg;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.java_test:
                Glide.with(this).load("https://upload-images.jianshu.io/upload_images/944365-cf5c1a9d2dddaaca.png?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/240")
                        .asBitmap().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Log.w("TAG", "secd------->" + resource.hashCode());
                        long current = System.currentTimeMillis();
                        java_image.setImageBitmap(FileUtil.convertGrayImg(resource));
                        long performance = System.currentTimeMillis() - current;
                        java_test.setText("耗时" + performance);
                    }
                }); //方法中设置asBitmap可以设置回调类型


                break;
            case R.id.jni_test:
                long jnicurrent = System.currentTimeMillis();
                jni_image.setImageBitmap(getJniBitmap());
                long jniperformance = System.currentTimeMillis() - jnicurrent;
                jni_test.setText("耗时" + jniperformance);
                break;
            case R.id.btn:
                tv.setText(jni.stringTest());
                break;
            case R.id.btn1:
                Toast.makeText(this, "---" + jni.addJia(3, 4), Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_javanull:
                jni.callbackmethod();
                break;
            case R.id.bt_javaInt:
                jni.callbackIntmethod();
                break;
            case R.id.bt_javaString:
                jni.callbackStringmethod();
                break;
            case R.id.bt_static:
                jni.callStaticmethod();
                break;
        }
    }
}
