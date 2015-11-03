#include "Akibot.h"
#include <string>
#include <iostream>
#include <thread>

using namespace std;

int printResult(float* resultPrint) {
    printf("===============\n");
    for (int i = 0; i < 21; i++) {
        printf("Distance %f\n", resultPrint[i]);
    }
}

void front() {
    float* result;
    EchoLocator echoLocatorFront;
    echoLocatorFront.initialize(13, 12, 500000, 50000, 0, 7, 400000, 35000, 1);
    result = echoLocatorFront.scanDistance(4, 24, 1, 14, true);
    printResult(result);
}

void back() {
    float* result;
    EchoLocator echoLocatorBack;
    echoLocatorBack.initialize(21, 14, 500000, 50000, 3, 2, 400000, 35000, 1);
    result = echoLocatorBack.scanDistance(4, 24, 1, 14, true);
    printResult(result);
}

void testThreads() {
    thread t1(front);
    thread t2(back);

    t1.join();
    t2.join();
}

void init() {
    printf("Initializing...\n");
    if (wiringPiSetup() == -1) {
        throw (AkibotException("Can't initialize wiringPi: %s\n"));
    }
}

void testServo(int servoPin, int microseconds) {
    printf("testServo %f starting...\n", (float) servoPin);
    
    for (int value = 4; value <= 14; value += 2) {
        printf("value %f\n", (float) value);
        softPwmWrite(servoPin, value);
        usleep(microseconds);
        softPwmWrite(servoPin, 0);
    }

    printf("testServo %f finished.\n", (float) servoPin);
}

void testServoThreads() {
    int divisor = 200;
    int initialValue = 0;
    int pwmRange = 200;

    pwmSetClock(divisor);
        
    pinMode(0, OUTPUT);
    digitalWrite(0, LOW);
    softPwmCreate(0, initialValue, pwmRange);
    
    pinMode(3, OUTPUT);
    digitalWrite(3, LOW);
    softPwmCreate(3, initialValue, pwmRange);
    
    
    thread t2(testServo, 3, 400000);
    thread t1(testServo, 0, 200000);


    t1.join();
    t2.join();
}

int main(int argc, char** argv) {

    init();
    //front();.

    testServoThreads();

    return 0;
}
