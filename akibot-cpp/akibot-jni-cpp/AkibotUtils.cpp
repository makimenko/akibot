#include "Akibot.h"



int AkibotUtils::durationMicroseconds(timeval startTime, timeval endTime) {
    int microseconds;
    microseconds = (endTime.tv_sec - startTime.tv_sec) * 1000000.0;
    microseconds += (endTime.tv_usec - startTime.tv_usec);
    return microseconds;
}

/**
 * Wait for PIN level (HIGH/LOW) and then measure duration of level
 * 
 * @param pin GPIO pin number
 * @param level 1=High, 0=Low
 * @param timeoutMicroseconds Maximum microseconds to wait.
 * @return duration in microseconds
 */
int AkibotUtils::pulseIn(int pin, int level, int timeoutMicroseconds) {
    timeval timeStart, timeLevelBegin, timeLevelEnd, now;
    gettimeofday(&timeStart, NULL);
    gettimeofday(&now, NULL);
    
    while (digitalRead(pin) != level && durationMicroseconds(timeStart, now) < timeoutMicroseconds) {
        gettimeofday(&now, NULL);
    }
    gettimeofday(&timeLevelBegin, NULL);

    while (digitalRead(pin) == level && durationMicroseconds(timeStart, now) < timeoutMicroseconds) {
        gettimeofday(&now, NULL);
    }
    gettimeofday(&timeLevelEnd, NULL);
    
    //TODO: Handle timeout value differently
    return durationMicroseconds(timeLevelBegin, timeLevelEnd);
}
