
#ifndef ECHOLOCATOR_H
#define	ECHOLOCATOR_H

class EchoLocator {
    int distanceTriggerPin;
    int distanceEchoPin;
    int distanceTimeout;
    int sleepBeforeDistance;
    int servoI2CBus;
    int servoI2CAddress;
    int servoI2CFrequency;
    int servoBasePin;
    int servoHeadPin;
    int servoLongTime;
    int servoStepTime;
    int distanceCount;
    bool initialized;
    
    int lastServoBasePosition;
    int lastServoHeadPosition;
        
    DistanceMeterSRF05 distanceMeter;
    PCA9685 pwm;

public:
    int size;        
    EchoLocator();
    virtual ~EchoLocator();
    void initialize(int _distanceTriggerPin, int _distanceEchoPin, int _distanceTimeout, int _sleepBeforeDistance, int _servoI2CBus, int _servoI2CAddress, int _servoI2CFrequency, int _servoBasePin, int _servoHeadPin, int _servoLongTime, int _servoStepTime, int _distanceCount);
    float* scanDistance(int _servoBaseFrom, int _servoBaseTo, int _servoBaseStep, int _servoHeadNormal, bool trustToLastPosition);
    
private:
};

#endif	/* ECHOLOCATOR_H */

