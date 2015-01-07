#include "Akibot.h"

DistanceMeterSRF05::DistanceMeterSRF05() {

}

void DistanceMeterSRF05::initialize(int trigger, int echo) {
    initialized = false;
    triggerPin = trigger;
    echoPin = echo;

    //TODO: Initialize once!
    printf("Init DistanceMeter: trigger=%d, echo=%d\n", triggerPin, echoPin);
    pinMode(triggerPin, OUTPUT);
    pinMode(echoPin, INPUT);
    digitalWrite(triggerPin, LOW);
    initialized = true;
}

float DistanceMeterSRF05::getDistance() {
    if (!initialized) {
        printf("DistanceMeter is not initialized");
        return -1;
    }

    digitalWrite(triggerPin, LOW);
    usleep(2);
    digitalWrite(triggerPin, HIGH);
    usleep(10);
    digitalWrite(triggerPin, LOW);

    double pulseMicroseconds = AkibotUtils::pulseIn(echoPin, HIGH);
    double soundSpeed = 340.29f;
    double mm = pulseMicroseconds * soundSpeed / (2 * 1000);

    return mm;
}

bool DistanceMeterSRF05::isInitializedFor(int checkTriggerPin, int checkEchoPin) {
    return initialized && triggerPin == checkTriggerPin && echoPin == checkEchoPin;
}

DistanceMeterSRF05::~DistanceMeterSRF05() {
    initialized = false;
}

