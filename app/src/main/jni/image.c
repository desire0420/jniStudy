#include <jni.h>
#include <time.h>
#include <stdio.h>
#include <android/bitmap.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <android/log.h>
#include "stackblur.c"


/**
 * 宏定义在C中可以打印LogCat
 */
#define LOG_TAG "wangwei"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

/**c图片处理*/
JNIEXPORT jintArray JNICALL
Java_com_jni_JNI_getImgToGray(JNIEnv *env, jobject instance, jintArray data_, jint w,
                              jint h) {
    //jint *data = (*env)->GetIntArrayElements(env, data_, NULL);
    jint *data;
    data = (*env)->GetIntArrayElements(env, data_, NULL);
    if (data == NULL) {
        return 0; /* exception occurred */
    }
    int alpha = 0xFF << 24;
    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
            // 获得像素的颜色
            int color = data[w * i + j];
            int red = ((color & 0x00FF0000) >> 16);
            int green = ((color & 0x0000FF00) >> 8);
            int blue = color & 0x000000FF;
            color = (red + green + blue) / 3;
            color = alpha | (color << 16) | (color << 8) | color;
            data[w * i + j] = color;
        }
    }
    int size = w * h;
    jintArray result = (*env)->NewIntArray(env, size);
    (*env)->SetIntArrayRegion(env, result, 0, size, data);

    (*env)->ReleaseIntArrayElements(env, data_, data, 0);

    return result;
}

/**模糊,使用Pixels JNI Native实现*/
JNIEXPORT void JNICALL
Java_com_jni_JNI_blurPixels(JNIEnv *env, jclass obj, jintArray arrIn, jint w, jint h, jint r) {
    jint *pix;
    pix = (*env)->GetIntArrayElements(env, arrIn, 0);
    if (pix == NULL) {
        LOGD("Input pixels isn't null.");
        return;
    }
    //Start
    pix = blur_ARGB_8888(pix, w, h, r);
    //End
    //int size = w * h;
    //jintArray result = env->NewIntArray(size);
    //env->SetIntArrayRegion(result, 0, size, pix);
    (*env)->ReleaseIntArrayElements(env, arrIn, pix, 0);
    //return result;
}

/**------使用Bitmap JNI Native实现-------*/
JNIEXPORT void JNICALL
Java_com_jni_JNI_blurBitmap(JNIEnv *env, jclass obj, jobject bitmapIn, jint r) {
    AndroidBitmapInfo infoIn;
    void *pixels;

    // Get srcimage info
    if (AndroidBitmap_getInfo(env, bitmapIn, &infoIn) != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGD("AndroidBitmap_getInfo failed!");
        return;
    }
    // Check srcimage
    if (infoIn.format != ANDROID_BITMAP_FORMAT_RGBA_8888 &&
        infoIn.format != ANDROID_BITMAP_FORMAT_RGB_565) {
        LOGD("Only support ANDROID_BITMAP_FORMAT_RGBA_8888 and ANDROID_BITMAP_FORMAT_RGB_565");
        return;
    }


    // Lock all images
    if (AndroidBitmap_lockPixels(env, bitmapIn, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGD("AndroidBitmap_lockPixels failed!");
        return;
    }
    // height width
    int h = infoIn.height;
    int w = infoIn.width;

    // Start
    if (infoIn.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        pixels = blur_ARGB_8888((int *) pixels, w, h, r);
    } else if (infoIn.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        pixels = blur_RGB_565((short *) pixels, w, h, r);
    }

    // End
    // Unlocks everything
    AndroidBitmap_unlockPixels(env, bitmapIn);
}
