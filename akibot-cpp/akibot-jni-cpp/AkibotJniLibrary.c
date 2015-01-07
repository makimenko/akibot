#include <jni.h>
#include <stdio.h>
#include "DistanceSR05.h"
JNIEXPORT jint JNICALL Java_akibot_jni_java_AkibotJniLibrary_getDistance
  (JNIEnv *env, jobject obj, jint triggerPin, jint echoPin, jint timeout) {
    return triggerPin+echoPin+timeout;
}
  
