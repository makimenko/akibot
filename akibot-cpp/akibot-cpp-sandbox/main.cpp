
#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  
#include <softPwm.h>

#include "DistanceMeter.h"

int main(int argc, char** argv) {
    printf("Starting:...\n");
    /**
     DistanceMeter distanceMeter(25,27);
        
    while (true) {
        printf("Current Distance = %f\n", distanceMeter.getDistance());
        usleep(100000);
    }
     */
    if (wiringPiSetup() == -1) {
        fprintf(stderr, "Can't initialize wiringPi: %s\n", strerror(errno));
    } else {
        printf("Initialized\n");
    }
    // 23 - ground (4 - 14! - 24)
    // 24 - head (14! - 24)
    
    int servoPin = 23;
    pinMode(servoPin, OUTPUT);
    digitalWrite(servoPin, LOW);
    pwmSetClock(200);
    softPwmCreate(servoPin, 0, 200);

    float min = 4;
    float max = 24;
    float avg = min + ((max - min) / 2);

    printf("value: %lf\n", min);
    softPwmWrite(servoPin, min);
    usleep(500000);
    //sleep(1);

    printf("value: %lf\n", avg);
    softPwmWrite(servoPin, avg);
    //sleep(1);
    usleep(500000);

    printf("value: %lf\n", max);
    softPwmWrite(servoPin, max);
    //sleep(1);
    usleep(500000);
    
    softPwmWrite(servoPin, 0);
    sleep(100000);
    

    printf("END.\n");
    return 0;
}

