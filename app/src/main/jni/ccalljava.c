#include <jni.h>

/**
 * 宏定义在C中可以打印LogCat
 */
#include <android/log.h>

#define LOG_TAG "王伟"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

/**
 * java调用callbackmethod 后
 * C函数反射调用java中的helloFromJava空方法
 * JNI调用本地方法，根据类名找到类，注意类名用"/"分隔。
 */
JNIEXPORT void JNICALL
Java_com_jni_JNI_callbackmethod(JNIEnv *env, jobject object) {

    //先获取字节码对象  jclass      (*FindClass)(JNIEnv*, const char*);
    jclass jclazz = (*env)->FindClass(env, "com/jni/JNI");
    //获取method对象   jmethodID   (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);
    jmethodID methodID = (*env)->GetMethodID(env, jclazz, "helloFromJava", "()V");
    //通过字节码文件创建一个object对象（可选，方法中已经传递一个object），如果需要调用的方法与本地方法不在同一个文件夹则需要新创建object
    //jobject     (*AllocObject)(JNIEnv*, jclass);
    //通过对象调用方法 (调用java的是空方法，不用填写参数) void   (*CallVoidMethod)(JNIEnv*, jobject, jmethodID, ...);
    (*env)->CallVoidMethod(env, object, methodID);

}
/**
* 调用java中Int方法
*/
JNIEXPORT void JNICALL
Java_com_jni_JNI_callbackIntmethod(JNIEnv *env, jobject object) {

    //先获取字节码对象  jclass      (*FindClass)(JNIEnv*, const char*);
    jclass clzz = (*env)->FindClass(env, "com/jni/JNI");
    //获取method对象   jmethodID   (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);
    jmethodID methodID = (*env)->GetMethodID(env, clzz, "add", "(II)I");
    //调用add 并返回值
    int result = (*env)->CallIntMethod(env, object, methodID, 3, 4);
    //logcat 打印相加返回的结果
    LOGD("RESLUT = %d", result);

}
/**
 * 调用java中String方法
 */
JNIEXPORT void JNICALL
Java_com_jni_JNI_callbackStringmethod(JNIEnv *env, jobject object) {
//先获取字节码对象  jclass      (*FindClass)(JNIEnv*, const char*);
    jclass clzz = (*env)->FindClass(env, "com/jni/JNI");
    //获取method对象   jmethodID   (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);
    jmethodID methodID = (*env)->GetMethodID(env, clzz, "printString", "(Ljava/lang/String;)V");
    //将要传递的字符串先转换成jstring类型 ，然后在传递给java方法
    int result = (*env)->NewStringUTF(env, "hello form C/C++ ");
    (*env)->CallVoidMethod(env, object, methodID, result);

}
/**
 * 调用Java中的静态方法
 */
JNIEXPORT void JNICALL
Java_com_jni_JNI_callStaticmethod(JNIEnv *env, jobject instance) {

    //先获取字节码对象  jclass      (*FindClass)(JNIEnv*, const char*);
    jclass clzz = (*env)->FindClass(env, "com/jni/JNI");
    jmethodID methodID = (*env)->GetStaticMethodID(env, clzz, "staticmethod",
                                                   "(Ljava/lang/String;)V");

    jstring str = (*env)->NewStringUTF(env, "C调用java");
    (*env)->CallStaticVoidMethod(env, clzz, methodID, str);
}

/**
 * Java调用C
 * */
JNIEXPORT jstring JNICALL
Java_com_jni_JNI_stringTest(JNIEnv *env, jobject jobject) {
    //将要传递的字符串先转换成jstring类型 ，然后在传递给java方法
    //把UTF_8 转换成为java的Unicode编码
    int result = (*env)->NewStringUTF(env, "Java調用C成功了拉");
    return result;
}



/**Java携帶参数调用C*/
JNIEXPORT jint JNICALL
Java_com_jni_JNI_addJia(JNIEnv *env, jobject instance, jint x, jint y) {
    LOGD("RESLUT = %d", x);
    LOGD("RESLUT = %d", y);
    return x + y;

}