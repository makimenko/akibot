#include "Akibot.h"

int main(int argc, char** argv) {
    printf("Starting:...\n");

    if (wiringPiSetup() == -1) {
        throw (AkibotException("Can't initialize wiringPi: %s\n"));
    }

    DistanceMeterSRF05 distanceMeter;
    Servo servoBase;
    Servo servoHead;

    int baseFrom = 4;
    int baseTo = 24;
    int headNormal = 14;
    int longTime = 500000;
    int shortTime = 30000;

    distanceMeter.initialize(25, 27, 500000);
    servoBase.initialize(23, 0, 200, 200);
    servoHead.initialize(24, 0, 200, 200);

    servoBase.softPwmWriteAndWait(baseFrom, longTime);
    servoHead.softPwmWriteAndWait(headNormal, longTime);

    for (int i = baseFrom; i <= baseTo; i++) {
        servoBase.softPwmWriteAndWait(i, shortTime);
        printf("Distance = %f\n", distanceMeter.getDistance());
        //printf("Distance = %f\n", distanceMeter.getDistance());
        //printf("Distance = %f\n", distanceMeter.getDistance());
        //printf("---\n");
        
    }


    printf("END.\n");
    return 0;
}

