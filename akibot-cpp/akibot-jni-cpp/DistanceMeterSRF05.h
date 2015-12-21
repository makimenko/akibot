#ifndef DISTANCEMETERSRF05_H
#define	DISTANCEMETERSRF05_H

class DistanceMeterSRF05 {
    int triggerPin, echoPin, timeoutMicroseconds;
    bool initialized;

public:
    DistanceMeterSRF05();
    virtual ~DistanceMeterSRF05();
    void initialize(int, int, int);
    float getDistance();
    bool isInitializedFor(int, int, int);
private:
};

#endif	/* DISTANCEMETERSRF05_H */

