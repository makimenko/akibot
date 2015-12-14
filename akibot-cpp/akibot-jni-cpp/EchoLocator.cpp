#include "Akibot.h"

EchoLocator::EchoLocator() {
}

EchoLocator::~EchoLocator() {
}

void EchoLocator::initialize(int _distanceTriggerPin, int _distanceEchoPin, int _distanceTimeout, int _sleepBeforeDistance, int _servoI2CBus, int _servoI2CAddress, int _servoI2CFrequency, int _servoBasePin, int _servoHeadPin, int _servoLongTime, int _servoStepTime, int _distanceCount) {
    initialized = false;

    distanceTriggerPin = _distanceTriggerPin;
    distanceEchoPin = _distanceEchoPin;
    distanceTimeout = _distanceTimeout;
    sleepBeforeDistance = _sleepBeforeDistance;

    servoI2CBus = _servoI2CBus;
    servoI2CAddress = _servoI2CAddress;
    servoI2CFrequency = _servoI2CFrequency;
    servoBasePin = _servoBasePin;
    servoHeadPin = _servoHeadPin;
    servoLongTime = _servoLongTime;
    servoStepTime = _servoStepTime;
    distanceCount = _distanceCount;

    pwm.init(servoI2CBus, servoI2CAddress);
    pwm.setPWMFreq(servoI2CFrequency);

    distanceMeter.initialize(distanceTriggerPin, distanceEchoPin, distanceTimeout);

    initialized = true;
}

float* EchoLocator::scanDistance(int servoBaseFrom, int servoBaseTo, int servoBaseStep, int servoHeadNormal, bool trustToLastPosition) {
    if (!initialized) {
        throw (AkibotException("Not initialized\n"));
    }

    int step;
    int currentPosition = servoBaseFrom;
    if (servoBaseFrom < servoBaseTo) {
        size = (servoBaseTo - servoBaseFrom) / servoBaseStep + 1;
        step = servoBaseStep;
    } else {
        size = (servoBaseFrom - servoBaseTo) / servoBaseStep + 1;
        step = -servoBaseStep;
    }
    printf("servoBaseFrom=%d\n", servoBaseFrom);
    printf("servoBaseTo=%d\n", servoBaseTo);
    printf("servoBaseStep=%d\n", servoBaseStep);

    printf("size=%d\n", size);

    float *result = new float[size];

    // Move to start position only if needed:
    if (!(trustToLastPosition && lastServoBasePosition == servoBaseFrom)) {
        pwm.setPWM(servoBasePin, servoBaseFrom);
        pwm.setPWM(servoHeadPin, servoHeadNormal);
        usleep(servoLongTime);
        lastServoBasePosition = servoBaseFrom;
        lastServoHeadPosition = servoHeadNormal;
    }

    int index = 0;
    for (int i = 1; i <= size; i++) {
        currentPosition += step;
        pwm.setPWM(servoBasePin, currentPosition);
        usleep(servoStepTime * servoBaseStep);

        lastServoBasePosition = currentPosition;
        if (sleepBeforeDistance > 0) {
            usleep(sleepBeforeDistance);
        }
        float totalDistance = 0;
        float avgDistance = 0;
        for (int j = 1; j <= distanceCount; j++) {
            float dist = distanceMeter.getDistance();
            totalDistance += dist;
        }
        avgDistance = totalDistance / distanceCount;
        result[index] = avgDistance;
        index++;
    }

    return result;
}



