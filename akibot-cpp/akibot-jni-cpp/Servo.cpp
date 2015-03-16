/* 
 * File:   Servo.cpp
 * Author: dm
 * 
 * Created on March 16, 2015, 8:56 PM
 */

#include "Akibot.h"

Servo::Servo() {
}

Servo::~Servo() {
}

void Servo::initialize(int servoPinIn, int initialValueIn, int pwmRangeIn, int divisorIn) {
    initialized = false;
    servoPin = servoPinIn;
    initialValue = initialValueIn;
    pwmRange = pwmRangeIn;
    divisor = divisorIn;
    
    pinMode(servoPin, OUTPUT);
    digitalWrite(servoPin, LOW);
    pwmSetClock(divisor);
    softPwmCreate(servoPin, initialValue, pwmRange);
    
    initialized = true;
}

bool Servo::isInitializedFor(int servoPinIn, int initialValueIn, int pwmRangeIn, int divisorIn) {
    return initialized && servoPin == servoPinIn && initialValue == initialValueIn && pwmRange == pwmRangeIn && divisor == divisorIn;
}

void Servo::softPwmWriteAndWait(int value, int microseconds) {
    softPwmWrite(servoPin, value);
    usleep(microseconds);
}


    