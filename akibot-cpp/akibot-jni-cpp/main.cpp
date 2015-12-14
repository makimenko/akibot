#include "Akibot.h"
#include <string>
#include <iostream>
#include <thread>

#include "PCA9685.h"
PCA9685 pwm;

using namespace std;


// result = echoLocatorBack.scanDistance(4, 24, 1, 14, true);
int printResult(float* resultPrint) {
    printf("===============\n");
    for (int i = 0; i < 21; i++) {
        printf("Distance %f\n", resultPrint[i]);
    }
}
int init(int i2c_bus, int i2c_address, int freq) {
    printf("init...\n");
    pwm.init(i2c_bus, i2c_address);
    pwm.setPWMFreq(freq);
    printf("initialized.\n");
    return 0;
}

int test(void) {
    printf("Testing testing\n");
    init(0, 0x40, 61);
    
    int pin = 5;
    pwm.setPWM(pin, 380);
    usleep(1000 * 1000);
    
    //pwm.setPWM(0, 0, 600);
    //usleep(1000 * 1000); 

    return 0;
}

int main(int argc, char** argv) {
    printf("Start\n");
    test();
    printf("End\n");
    return 0;
}
