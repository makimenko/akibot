// Libraries:
#include <jni.h>
#include <stdio.h>
#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  
#include <iostream>
#include <string>
#include <softPwm.h>

// Akibot custom libraries:
#include "AkibotJniLibrary.h"
#include "DistanceMeterSRF05.h"
#include "Servo.h"
#include "AkibotUtils.h"
#include "AkibotException.h"
#include "EchoLocator.h"
