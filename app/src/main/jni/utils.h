#include<com_example_hmyd_jni_mathkit.h>
JNIEXPORT jint JNICALL Java_net_qiujuer_ndkdemo_jni_MathKit_square
        (JNIEnv *env, jclass cls, jint num)
        {
            return num*num;
        }