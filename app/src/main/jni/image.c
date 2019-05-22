#include <jni.h>
#include <time.h>
#include <stdio.h>
#include <android/bitmap.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

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
