/* 
 * File:   DistanceMeter.h
 * Author: dm
 *
 * Created on 13 декабря 2014 г., 19:29
 */

#ifndef DISTANCEMETER_H
#define	DISTANCEMETER_H

class DistanceMeter {
    int triggerPin, echoPin;
    bool initialized;

public:
    DistanceMeter();
    virtual ~DistanceMeter();
    void initialize(int, int);
    float getDistance();
    bool isInitializedFor(int, int);
private:
};

#endif	/* DISTANCEMETER_H */

