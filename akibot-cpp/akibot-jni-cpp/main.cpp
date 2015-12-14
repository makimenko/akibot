#include "Akibot.h"
#include <string>
#include <iostream>
#include <thread>


using namespace std;


// result = echoLocatorBack.scanDistance(4, 24, 1, 14, true);
int printResult(float* resultPrint) {
    printf("=============== result: \n");
    for (int i = 0; i < 10; i++) {
        printf("Distance %f\n", resultPrint[i]);
    }
}


int main(int argc, char** argv) {
    EchoLocator echoLocator;
    
    //int _distanceTriggerPin, int _distanceEchoPin, int _distanceTimeout, int _sleepBeforeDistance, int _servoI2CBus, int _servoI2CAddress, int _servoI2CFrequency
    //, int _servoBasePin, int _servoHeadPin, int _servoLongTime, int _servoStepTime, int _distanceCount
    echoLocator.initialize(13, 12, 500000, 50000, /**/ 0, 0x40, 61/**/, 1, 0, 400000, 1500, 1);


    float* result;
    printf("=============== START: \n");
    // int servoBaseFrom, int servoBaseTo, int servoBaseStep, int servoHeadNormal, bool trustToLastPosition
    result = echoLocator.scanDistance(150, 640, 100, 400, true);
    printResult(result);
    
    
    return 0;
}
