#include "Akibot.h"

int main(int argc, char** argv) {
    EchoLocator echoLocator;

    echoLocator.initialize(25, 27, 500000, 50000, 23, 24, 4, 24, 1, 14, 400000, 35000, 1);
    
    float** result =  echoLocator.scanDistance();
    
    printf("sizeof = %d - %d - %d - %d\n", sizeof *result, sizeof result[1], sizeof result[1][1], sizeof(float) );
    for (int i = 0 ; i<21 ; i++ )  {
        printf("Distance %f = %f\n", result[0][i], result[1][i]);
   }

    return 0;
}

