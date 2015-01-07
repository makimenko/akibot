#include <jni.h>
#include <stdio.h>
#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  
#include "AkibotJniLibrary.h"
#include "DistanceMeter.h"
#include "AkibotUtils.h"

JNIEXPORT jint JNICALL Java_akibot_jni_java_AkibotJniLibrary_getDistance
(JNIEnv *env, jobject obj, jint triggerPin, jint echoPin, jint timeout) {
    DistanceMeter distanceMeter(triggerPin, echoPin);
    return distanceMeter.getDistance();
}

JNIEXPORT jint JNICALL Java_akibot_jni_java_AkibotJniLibrary_getDistance__II
(JNIEnv *env, jobject obj, jint pin, jint level) {
    return AkibotUtils::pulseIn(pin, level);
}


