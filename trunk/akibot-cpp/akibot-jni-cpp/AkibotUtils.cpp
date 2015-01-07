#include "Akibot.h"

/**
 * Wait for PIN level (HIGH/LOW) and then measure duration of level
 * 
 * @param pin GPIO pin number
 * @param level 1=High, 0=Low
 * @return duration in microseconds
 */
int AkibotUtils::pulseIn(int pin, int level) {
    timeval t1, t2;
    double microseconds;

    while (digitalRead(pin) != level) {
    }
    gettimeofday(&t1, NULL);

    while (digitalRead(pin) == level) {
    }
    gettimeofday(&t2, NULL);

    microseconds = (t2.tv_sec - t1.tv_sec) * 1000000.0;
    microseconds += (t2.tv_usec - t1.tv_usec);

    return microseconds;
}
