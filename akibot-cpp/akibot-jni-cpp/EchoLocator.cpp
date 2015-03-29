#include "Akibot.h"

EchoLocator::EchoLocator() {
}

EchoLocator::~EchoLocator() {
}

void EchoLocator::initialize(int _distanceTriggerPin, int _distanceEchoPin, int _distanceTimeout, int _sleepBeforeDistance, int _servoBasePin, int _servoHeadPin, int _servoLongTime, int _servoStepTime, int _distanceCount) {
    initialized = false;

    distanceTriggerPin = _distanceTriggerPin;
    distanceEchoPin = _distanceEchoPin;
    distanceTimeout = _distanceTimeout;
    sleepBeforeDistance = _sleepBeforeDistance;
    servoBasePin = _servoBasePin;
    servoHeadPin = _servoHeadPin;
    servoLongTime = _servoLongTime;
    servoStepTime = _servoStepTime;
    distanceCount = _distanceCount;

    if (wiringPiSetup() == -1) {
        throw (AkibotException("Can't initialize wiringPi: %s\n"));
    }
    distanceMeter.initialize(distanceTriggerPin, distanceEchoPin, distanceTimeout);
    servoBase.initialize(servoBasePin, 0, 200, 200);
    servoHead.initialize(servoHeadPin, 0, 200, 200);

    initialized = true;
}

bool EchoLocator::isInitializedFor(int _distanceTriggerPin, int _distanceEchoPin, int _distanceTimeout, int _sleepBeforeDistance, int _servoBasePin,
        int _servoHeadPin, int _servoLongTime, int _servoStepTime, int _distanceCount) {
    return initialized
            && _distanceTriggerPin == distanceTriggerPin
            && _distanceEchoPin == distanceEchoPin
            && _distanceTimeout == distanceTimeout
            && _sleepBeforeDistance == sleepBeforeDistance
            && _servoBasePin == servoBasePin
            && _servoHeadPin == servoHeadPin
            && _servoLongTime == servoLongTime
            && _servoStepTime == servoStepTime
            && _distanceCount == distanceCount;
}

float* EchoLocator::scanDistance(int servoBaseFrom, int servoBaseTo, int servoBaseStep, int servoHeadNormal, bool trustToLastPosition) {

    int step;
    int currentPosition = servoBaseFrom;
    if (servoBaseFrom < servoBaseTo) {
        size = servoBaseTo - servoBaseFrom + 1;
        step = servoBaseStep;
    } else {
        size = servoBaseFrom - servoBaseTo + 1;
        step = -servoBaseStep;
    }

    float *result = new float[size];

    // Move to start position only if needed:
    if (!(trustToLastPosition && lastServoBasePosition == servoBaseFrom)) {
        servoBase.softPwmWriteAndWait(servoBaseFrom, servoLongTime);
        lastServoBasePosition = servoBaseFrom;
    }
    if (!(trustToLastPosition && lastServoHeadPosition == servoHeadNormal)) {
        servoHead.softPwmWriteAndWait(servoHeadNormal, servoLongTime);
        lastServoHeadPosition = servoHeadNormal;
    }

    int index = 0;
    for (int i = 1; i <= size; i++) {
        currentPosition += step;
        servoBase.softPwmWriteAndWait(currentPosition, servoStepTime * servoBaseStep);
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



