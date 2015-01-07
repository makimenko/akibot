#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  
#include "DistanceMeter.h"
#include "AkibotUtils.h"



DistanceMeter::DistanceMeter(int trigger, int echo) {
    initialized = false;
    triggerPin = trigger;
    echoPin = echo;

    //TODO: Initialize once!
    //printf("Init DistanceMeter: trigger=%d, echo=%d\n", triggerPin, echoPin);

    if (wiringPiSetup() == -1) {
        fprintf(stderr, "Can't initialize wiringPi: %s\n", strerror(errno));
        initialized = false;
    } else {
        pinMode(triggerPin, OUTPUT);
        pinMode(echoPin, INPUT);

        digitalWrite(triggerPin, LOW);
       // printf("echoPin=%d\n", digitalRead(echoPin));
        initialized = true;
    }
}

float DistanceMeter::getDistance() {
    if (!initialized) {
        printf("DistanceMeter is not initialized");
        return -1;
    }

    digitalWrite(triggerPin, LOW);
    usleep(2);
    digitalWrite(triggerPin, HIGH);
    usleep(10);
    digitalWrite(triggerPin, LOW);

    double pulseMicroseconds = AkibotUtils::pulseIn(echoPin, HIGH);
    double soundSpeed = 340.29f;
    double mm = pulseMicroseconds * soundSpeed / (2 * 1000);

    return mm;
}

DistanceMeter::~DistanceMeter() {
    initialized = false;
}

