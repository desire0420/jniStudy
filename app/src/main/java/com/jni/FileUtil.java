package com.jni;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by wangwei on 2019/5/22.
 */

public class FileUtil {

    public static Bitmap convertGrayImg(Context context, int resID) {
        Bitmap img1 = ((BitmapDrawable) context.getResources().getDrawable(resID)).getBitmap();
        int w = img1.getWidth(), h = img1.getHeight();
        int[] pix = new int[w * h];
        img1.getPixels(pix, 0, w, 0, 0, w, h);
        int alpha = 0xFF << 24;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // 获得像素的颜色
                int color = pix[w * i + j];
                int red = ((color & 0x00FF0000) >> 16);
                int green = ((color & 0x0000FF00) >> 8);
                int blue = color & 0x000000FF;
                color = (red + green + blue) / 3;
                color = alpha | (color << 16) | (color << 8) | color;
                pix[w * i + j] = color;
            }
        }
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        result.setPixels(pix, 0, w, 0, 0, w, h);
        return result;
    }





}
