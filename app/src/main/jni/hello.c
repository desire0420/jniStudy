#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_jni_JniTest_stringHello(JNIEnv *env, jobject instance) {
    // TODO
    return (*env)->NewStringUTF(env, "helloJNi");
}


JNIEXPORT jstring JNICALL
Java_com_jni_JniTest_stringTest(JNIEnv *env, jobject instance) {

    // TODO

    return (*env)->NewStringUTF(env, "hello_new_eeeeetest");
}