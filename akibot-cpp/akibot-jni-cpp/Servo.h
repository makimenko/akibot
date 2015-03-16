/* 
 * File:   Servo.h
 * Author: dm
 *
 * Created on March 16, 2015, 8:56 PM
 */

#ifndef SERVO_H
#define	SERVO_H

class Servo {
    int servoPin, initialValue, pwmRange, divisor;    
    bool initialized;
    
public:
    Servo();
    virtual ~Servo();
    void initialize(int, int, int, int);
    bool isInitializedFor(int, int, int, int);
    void softPwmWriteAndWait(int, int);    
private:

};

#endif	/* SERVO_H */

