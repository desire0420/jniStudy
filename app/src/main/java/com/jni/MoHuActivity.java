package com.jni;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import utils.FileUtil;

/**
 * Created by wangwei on 2019/5/23.
 */

public class MoHuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button java_btn, jni_c_bitmap, jni_c_pixels, revert_btn;
    private Button java_gary_btn, jni_gary_btn;
    private TextView time;
    private ImageView image;
    private JNI jni;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mohu);
        jni = new JNI(getApplicationContext());
        java_btn = findViewById(R.id.java_btn);
        jni_c_bitmap = findViewById(R.id.jni_c_bitmap);
        jni_c_pixels = findViewById(R.id.jni_c_pixels);
        revert_btn = findViewById(R.id.revert_btn);
        java_gary_btn = findViewById(R.id.java_gary_btn);
        jni_gary_btn = findViewById(R.id.jni_gary_btn);
        time = findViewById(R.id.time);
        image = findViewById(R.id.image);
        java_btn.setOnClickListener(this);
        jni_c_bitmap.setOnClickListener(this);
        jni_c_pixels.setOnClickListener(this);
        revert_btn.setOnClickListener(this);
        java_gary_btn.setOnClickListener(this);
        jni_gary_btn.setOnClickListener(this);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Bitmap mBitmap1 = ((BitmapDrawable) getResources().getDrawable(R.mipmap.love)).getBitmap();
                // Java
                // Bitmap newBitmap1 = StackBlur.blur(mBitmap1, (int) i, false);
                //JNI
                Bitmap newBitmap1 = blurNatively(mBitmap1, (int) i, false);
                image.setImageBitmap(newBitmap1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    String msgToast = "-";

    @Override
    public void onClick(View v) {
        long jnicurrent = System.currentTimeMillis();
        switch (v.getId()) {
            case R.id.revert_btn:
                msgToast = " revert Bitmap";
                Bitmap revert = ((BitmapDrawable) getResources().getDrawable(R.mipmap.love)).getBitmap();
                image.setImageBitmap(revert);
                break;
            case R.id.java_btn:
                msgToast = " StackBlur By Java Bitmap";
                Bitmap mBitmap1 = ((BitmapDrawable) getResources().getDrawable(R.mipmap.love)).getBitmap();
                // Java
                Bitmap newBitmap1 = StackBlur.blur(mBitmap1, 20, false);
                image.setImageBitmap(newBitmap1);
                break;
            case R.id.jni_c_bitmap:
                Bitmap mBitmap2 = ((BitmapDrawable) getResources().getDrawable(R.mipmap.love)).getBitmap();
                // Bitmap JNI Native
                Bitmap newBitmap2 = blurNatively(mBitmap2, 20, false);
                image.setImageBitmap(newBitmap2);
                msgToast = "StackBlur By Jni Bitmap";
                break;
            case R.id.jni_c_pixels:
                Bitmap mBitmap3 = ((BitmapDrawable) getResources().getDrawable(R.mipmap.love)).getBitmap();
                // Pixels JNI Native
                Bitmap newBitmap3 = blurNativelyPixels(mBitmap3, 20, false);
                image.setImageBitmap(newBitmap3);
                msgToast = "StackBlur By Jni Pixels";
                break;
            case R.id.jni_gary_btn:
                image.setImageBitmap(getJniBitmap());
                break;
            case R.id.java_gary_btn:
//                Bitmap mBitmap5 = ((BitmapDrawable) getResources().getDrawable(R.mipmap.love)).getBitmap();
//                image.setImageBitmap(processBitmap(mBitmap5));
                image.setImageBitmap(FileUtil.convertGrayImg(this, R.mipmap.love));
                break;

        }
        Toast.makeText(this, msgToast, Toast.LENGTH_SHORT).show();
        long t = System.currentTimeMillis() - jnicurrent;
        Log.w("TAG", msgToast + "----" + t);
        time.setText("" + t);
    }


    /**
     * 使用Bitmap JNI Native实现"
     */
    public Bitmap blurNatively(Bitmap original, int radius, boolean canReuseInBitmap) {
        if (radius < 1) {
            return null;
        }

        Bitmap bitmap = StackBlur.buildBitmap(original, canReuseInBitmap);
        // Return this none blur
        if (radius == 1) {
            return bitmap;
        }
        //Jni BitMap Blur
        jni.blurBitmap(bitmap, radius);
        return (bitmap);
    }

    /**
     * 处理图片，此方法中会调用nativeProcessBitmap
     *
     * @param bitmap
     * @return
     */
    private Bitmap processBitmap(Bitmap bitmap) {
        Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        jni.nativeProcessBitmap(bmp);
        return bmp;
    }


    /**
     * "使用Pixels JNI Native实现"
     */
    public Bitmap blurNativelyPixels(Bitmap original, int radius, boolean canReuseInBitmap) {
        if (radius < 1) {
            return null;
        }
        Bitmap bitmap = StackBlur.buildBitmap(original, canReuseInBitmap);
        // Return this none blur
        if (radius == 1) {
            return bitmap;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        // Jni Pixels Blur
        jni.blurPixels(pix, w, h, radius);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    /**
     * 调用JNI将图片置灰
     */
    public Bitmap getJniBitmap() {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.love)).getBitmap();
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


    /**
     * 加载网络图片置灰
     */
    private void getNetImage() {
        Glide.with(this).load("https://upload-images.jianshu.io/upload_images/944365-cf5c1a9d2dddaaca.png?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/240")
                .asBitmap().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Log.w("TAG", "secd------->" + resource.hashCode());
                long current = System.currentTimeMillis();
                image.setImageBitmap(FileUtil.convertGrayImg(resource));
                long performance = System.currentTimeMillis() - current;
                time.setText("耗时" + performance);
            }
        }); //方法中设置asBitmap可以设置回调类型
    }
}
