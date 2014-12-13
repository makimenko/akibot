#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  
#include "DistanceMeter.h"


int waitforpin(int pin, int level, int timeout) {
    struct timeval now, start;
    int done;
    long micros;
    gettimeofday(&start, NULL);
    micros = 0;
    done = 0;
    while (!done) {
        gettimeofday(&now, NULL);
        if (now.tv_sec > start.tv_sec) micros = 1000000L;
        else micros = 0;
        micros = micros + (now.tv_usec - start.tv_usec);
        if (micros > timeout) done = 1;
        if (digitalRead(pin) == level) done = 1;
    }
    return micros;
}

DistanceMeter::DistanceMeter(int trigger, int echo) {
    initialized = false;
    triggerPin = trigger;
    echoPin = echo;

    printf("Init DistanceMeter: trigger=%d, echo=%d\n", triggerPin, echoPin);

    if (wiringPiSetup() == -1) {
        fprintf(stderr, "Can't initialize wiringPi: %s\n", strerror(errno));
        initialized = false;
    } else {
        pinMode(triggerPin, OUTPUT);
        pinMode(echoPin, INPUT);
        initialized = true;
    }

}

float DistanceMeter::getDistance() {
    if (!initialized) {
        printf("DistanceMeter is not initialized");
        return -1;
    }
    int pulsewidth;

    digitalWrite(triggerPin, LOW);
    usleep(2);
    digitalWrite(triggerPin, HIGH);
    usleep(10);
    digitalWrite(triggerPin, LOW);

    waitforpin(echoPin, HIGH, 5000); /* 5 ms timeout */

    if (digitalRead(echoPin) == HIGH) {
        pulsewidth = waitforpin(echoPin, LOW, 60000L); /* 60 ms timeout */
        if (digitalRead(echoPin) == LOW) {
            /* valid reading code */
            
            //float soundSpeed = 340.29;
             
            // 
            return pulsewidth;
        } else {
            return -1;
        }
    } else {
        /* sensor not firing code */
        return -1;
    }

}

DistanceMeter::~DistanceMeter() {
    initialized = false;
}

