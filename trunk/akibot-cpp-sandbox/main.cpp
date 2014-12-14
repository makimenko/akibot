
#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  

#include "DistanceMeter.h"

int main(int argc, char** argv) {
    printf("Starting:...\n");
    DistanceMeter distanceMeter(25,27);
        
    while (true) {
        printf("Current Distance = %f\n", distanceMeter.getDistance());
        usleep(100000);
    }
       
    printf("END.\n");
    return 0;
}

