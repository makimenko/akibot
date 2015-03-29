/* 
 * File:   Servo.h
 * Author: dm
 *
 * Created on March 16, 2015, 8:56 PM
 */

#ifndef ECHOLOCATOR_H
#define	ECHOLOCATOR_H

class EchoLocator {
    int distanceTriggerPin;
    int distanceEchoPin;
    int distanceTimeout;
    int sleepBeforeDistance;
    int servoBasePin;
    int servoHeadPin;
    int servoBaseFrom;
    int servoBaseTo;
    int servoBaseStep;
    int servoHeadNormal;
    int servoLongTime;
    int servoStepTime;
    int distanceCount;
    
    bool initialized;
    
    DistanceMeterSRF05 distanceMeter;
    Servo servoBase;
    Servo servoHead;
    
public:
    EchoLocator();
    virtual ~EchoLocator();
    void initialize(int _distanceTriggerPin, int _distanceEchoPin, int _distanceTimeout, int _sleepBeforeDistance, int _servoBasePin, int _servoHeadPin, int _servoBaseFrom, 
        int _servoBaseTo, int _servoBaseStep, int _servoHeadNormal, int _servoLongTime, int _servoStepTime, int _distanceCount);
    void run();
    float** scanDistance();
    
private:
};

#endif	/* ECHOLOCATOR_H */

