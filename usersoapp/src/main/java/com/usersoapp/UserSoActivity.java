package com.usersoapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jni.JNI;
import com.t.myapplication.MyJni;

/**
 * Created by wangwei on 2019/5/24.
 */

public class UserSoActivity extends AppCompatActivity {
    private Button btn;
    private TextView text;
    private ImageView image;
    JNI jni;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user);
        btn = findViewById(R.id.btn);
        text = findViewById(R.id.text);
        image = findViewById(R.id.image);
        jni = new JNI(getApplicationContext());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(MyJni.stringFromJNI());
                Log.w("TAG", "調用so的結果==" + MyJni.stringFromJNI());
                Bitmap mBitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.srcimage)).getBitmap();
                // Pixels JNI Native
                Bitmap newBitmap3 = blurNativelyPixels(mBitmap3, 20, false);
                image.setImageBitmap(newBitmap3);
            }
        });
    }





    /**
     * "使用Pixels JNI Native实现"
     */
    public  Bitmap blurNativelyPixels(Bitmap original, int radius, boolean canReuseInBitmap) {
        if (radius < 1) {
            return null;
        }
        Bitmap bitmap = buildBitmap(original, canReuseInBitmap);
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
    public static Bitmap buildBitmap(Bitmap original, boolean canReuseInBitmap) {
        // First we should check the original
        if (original == null)
            throw new NullPointerException("Blur bitmap original isn't null");

        Bitmap.Config config = original.getConfig();
        if (config != Bitmap.Config.ARGB_8888 && config != Bitmap.Config.RGB_565) {
            throw new RuntimeException("Blur bitmap only supported Bitmap.Config.ARGB_8888 and Bitmap.Config.RGB_565.");
        }
        // If can reuse in bitmap return this or copy
        Bitmap rBitmap;
        if (canReuseInBitmap) {
            rBitmap = original;
        } else {
            rBitmap = original.copy(config, true);
        }
        return (rBitmap);
    }

}
