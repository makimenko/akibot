#include "Akibot.h"
#include <string>
#include <iostream>
#include <thread>

using namespace std;

int printResult(float* resultPrint) {
    printf("=============== result: \n");
    for (int i = 0; i < 15; i++) {
        printf("Distance %f\n", resultPrint[i]);
    }
}

int testEcholocator(void) {
    EchoLocator echoLocator;
    int _distanceTriggerPin = 13;
    int _distanceEchoPin = 12;
    int _distanceTimeout = 50000;
    int _sleepBeforeDistance = 5000;
    int _servoI2CBus = 0;
    int _servoI2CAddress = 0x40;
    int _servoI2CFrequency = 61;
    int _servoBasePin = 1;
    int _servoHeadPin = 0;
    int _servoLongTime = 400000;
    int _servoStepTime = 3000;
    int _distanceCount = 1;

    echoLocator.initialize(_distanceTriggerPin, _distanceEchoPin, _distanceTimeout, _sleepBeforeDistance, /**/ _servoI2CBus, _servoI2CAddress, _servoI2CFrequency/**/, _servoBasePin, _servoHeadPin, _servoLongTime, _servoStepTime, _distanceCount);

    float* result;
    printf("=============== START: \n");
    int servoBaseFrom = 150;
    int servoBaseTo = 650;
    int servoBaseStep = 50;
    int servoHeadNormal = 370;
    bool trustToLastPosition = true;
    result = echoLocator.scanDistance(servoBaseFrom, servoBaseTo, servoBaseStep, servoHeadNormal, trustToLastPosition);
    printResult(result);
}

int testDistanceMeter(void) {
    DistanceMeterSRF05 distanceMeter;
    int triggerPin = 13;
    int echoPin = 12;
    distanceMeter.initialize(triggerPin, echoPin, 500000);
    //distanceMeter.initialize(21, 14, 1000*1000);
    for (int i = 0; i < 30; i++) {
        float distance = distanceMeter.getDistance();
        printf("Distance = %f \n", distance);
        usleep(500 * 1000);
    }
}

int testServoPCA9685(void) {
    PCA9685 pwm;
    pwm.init(0, 0x40);
    pwm.setPWMFreq(61);
    printf("initialized.\n");
        
    int pin = 1;
    pwm.setPWM(pin, 400);
    usleep(1000 * 1000);
 }

int main(int argc, char** argv) {
    testServoPCA9685();
    return 0;
}
