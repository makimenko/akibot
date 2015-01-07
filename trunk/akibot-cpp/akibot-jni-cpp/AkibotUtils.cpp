#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  
#include "AkibotUtils.h"

int AkibotUtils::pulseIn(int pin, int level) {
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
