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
    bool done;
    long micros;
    gettimeofday(&start, NULL);
    micros = 0;
    done = false;
    while (!done) {
        gettimeofday(&now, NULL);
        if (now.tv_sec > start.tv_sec)
            micros = 1000000L;
        else micros = 0;

        micros = micros + (now.tv_usec - start.tv_usec);
        if (micros > timeout) done = true;
        if (digitalRead(pin) == level) done = true;
    }
    return micros;
}

int pulseIn(int pin, int level) {
    timeval t1, t2;
    double microseconds;

    while (digitalRead(pin) != level) {
    }
    gettimeofday(&t1, NULL);

    while (digitalRead(pin) == level) {
    }
    gettimeofday(&t2, NULL);

    microseconds = (t2.tv_sec - t1.tv_sec) * 1000000.0;
    microseconds += (t2.tv_usec - t1.tv_usec);

    return microseconds;
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

        digitalWrite(triggerPin, LOW);
        printf("echoPin=%d\n", digitalRead(echoPin));
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

    double pulseMicroseconds = pulseIn(echoPin, HIGH);
    double soundSpeed = 340.29f;
    double mm = pulseMicroseconds * soundSpeed / (2 * 1000);

    return mm;
}

DistanceMeter::~DistanceMeter() {
    initialized = false;
}

