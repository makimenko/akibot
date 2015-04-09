#include "Akibot.h"

int main(int argc, char** argv) {
    EchoLocator echoLocator;

    echoLocator.initialize(13, 12, 500000, 50000, 0, 7, 400000, 35000, 1);


    float* result;
    printf("===============\n");
    result = echoLocator.scanDistance(4, 24, 1, 14, true);
    for (int i = 0; i < echoLocator.size; i++) {
        printf("Distance %f\n", result[i]);
    }
    printf("===============\n");
    result = echoLocator.scanDistance(24, 4, 1, 14, true);
    for (int i = 0; i < echoLocator.size; i++) {
        printf("Distance %f\n", result[i]);
    }
    
    return 0;
}

