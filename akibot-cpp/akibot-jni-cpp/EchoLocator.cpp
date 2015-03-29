#include "Akibot.h"

EchoLocator::EchoLocator() {
}

EchoLocator::~EchoLocator() {
}

void EchoLocator::initialize(int _distanceTriggerPin, int _distanceEchoPin, int _distanceTimeout, int _sleepBeforeDistance, int _servoBasePin, int _servoHeadPin, int _servoBaseFrom, int _servoBaseTo, int _servoBaseStep, int _servoHeadNormal, int _servoLongTime, int _servoStepTime, int _distanceCount) {
    initialized = false;

    distanceTriggerPin = _distanceTriggerPin;
    distanceEchoPin = _distanceEchoPin;
    distanceTimeout = _distanceTimeout;
    sleepBeforeDistance = _sleepBeforeDistance;
    servoBasePin = _servoBasePin;
    servoHeadPin = _servoHeadPin;
    servoBaseFrom = _servoBaseFrom;
    servoBaseTo = _servoBaseTo;
    servoBaseStep = _servoBaseStep;
    servoHeadNormal = _servoHeadNormal;
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

void EchoLocator::run() {

    servoBase.softPwmWriteAndWait(servoBaseFrom, servoLongTime);
    servoHead.softPwmWriteAndWait(servoHeadNormal, servoLongTime);

    for (int i = servoBaseFrom; i <= servoBaseTo; i += servoBaseStep) {
        servoBase.softPwmWriteAndWait(i, servoStepTime * servoBaseStep);
        if (sleepBeforeDistance > 0) {
            usleep(sleepBeforeDistance);
        }
        printf("Distance = ");
        float totalDistance = 0;
        float avgDistance = 0;
        for (int j = 1; j <= distanceCount; j++) {
            float dist = distanceMeter.getDistance();
            totalDistance += dist;
            printf("%f, ", dist);
        }
        avgDistance = totalDistance / distanceCount;
        printf(" ==> %f\n", avgDistance);

    }

}

float** EchoLocator::scanDistance() {

    int size = servoBaseTo - servoBaseFrom + 1;
    float **result = new float*[2];
    for (int i = 0; i < 2; i++) {
        result[i] = new float[size];
    }


    servoBase.softPwmWriteAndWait(servoBaseFrom, servoLongTime);
    servoHead.softPwmWriteAndWait(servoHeadNormal, servoLongTime);

    int index = 0;
    for (int i = servoBaseFrom; i <= servoBaseTo; i += servoBaseStep) {
        servoBase.softPwmWriteAndWait(i, servoStepTime * servoBaseStep);
        if (sleepBeforeDistance > 0) {
            usleep(sleepBeforeDistance);
        }
        printf("Distance = ");
        float totalDistance = 0;
        float avgDistance = 0;
        for (int j = 1; j <= distanceCount; j++) {
            float dist = distanceMeter.getDistance();
            totalDistance += dist;
            printf("%f, ", dist);
        }
        avgDistance = totalDistance / distanceCount;
        result[0][index] = i;
        result[1][index] = avgDistance;
        printf(" ==> %f\n", avgDistance);
        index++;
    }

    return result;
}



